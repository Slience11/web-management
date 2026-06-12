package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.mapper.EmpExprMapper;
import com.itheima.mapper.EmpMapper;
import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpExpr;
import com.itheima.pojo.EmpLog;
import com.itheima.pojo.EmpQueryParam;
import com.itheima.pojo.LoginInfo;
import com.itheima.pojo.PageResult;
import com.itheima.service.EmpLogService;
import com.itheima.service.EmpService;
import com.itheima.utils.JwtUtils;
import com.itheima.utils.PasswordUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class EmpServiceImpl implements EmpService {

    private static final String DEFAULT_EMP_PASSWORD =
            System.getenv().getOrDefault("DEFAULT_EMP_PASSWORD", "123456");

    @Autowired
    private EmpMapper empMapper;

    @Autowired
    private EmpExprMapper empExprMapper;

    @Autowired
    private EmpLogService empLogService;

    @Override
    public PageResult<Emp> page(EmpQueryParam empQueryParam) {
        PageHelper.startPage(empQueryParam.getPage(), empQueryParam.getPageSize());
        List<Emp> list = empMapper.page(empQueryParam);
        Page<Emp> page = (Page<Emp>) list;
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(Emp emp) {
        try {
            emp.setCreateTime(LocalDateTime.now());
            emp.setUpdateTime(LocalDateTime.now());
            emp.setPassword(PasswordUtils.encode(resolveInitialPassword(emp.getPassword())));
            empMapper.insert(emp);

            List<EmpExpr> exprList = emp.getExprList();
            if (!CollectionUtils.isEmpty(exprList)) {
                exprList.forEach(empExpr -> empExpr.setEmpId(emp.getId()));
                empExprMapper.insertBatch(emp);
            }
        } finally {
            EmpLog empLog = new EmpLog(null, LocalDateTime.now(), "新增员工信息: id=" + emp.getId());
            empLogService.insertLog(empLog);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<Integer> ids) {
        empMapper.deleteById(ids);
        empExprMapper.deleteByEmpId(ids);
    }

    @Override
    public Emp getInfo(Integer id) {
        return empMapper.getById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Emp getInfoById(Integer id) {
        Emp emp = empMapper.getInfoById(id);
        if (emp != null) {
            emp.setExprList(empExprMapper.getByIdBatch(id));
        }
        return emp;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Emp emp) {
        emp.setUpdateTime(LocalDateTime.now());
        if (StringUtils.hasText(emp.getPassword()) && !PasswordUtils.isEncoded(emp.getPassword())) {
            emp.setPassword(PasswordUtils.encode(emp.getPassword()));
        }
        empMapper.updateById(emp);

        empExprMapper.deleteByEmpId(Arrays.asList(emp.getId()));
        List<EmpExpr> exprList = emp.getExprList();
        if (!CollectionUtils.isEmpty(exprList)) {
            exprList.forEach(empExpr -> empExpr.setEmpId(emp.getId()));
            empExprMapper.insertBatch(emp);
        }
    }

    @Override
    public List<Emp> allInfo() {
        return empMapper.getEmpInfo();
    }

    @Override
    public LoginInfo login(Emp emp) {
        Emp storedEmp = empMapper.getByUsername(emp.getUsername());
        if (storedEmp == null || !PasswordUtils.matches(emp.getPassword(), storedEmp.getPassword())) {
            return null;
        }

        upgradeLegacyPasswordIfNeeded(storedEmp, emp.getPassword());

        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("id", storedEmp.getId());
        dataMap.put("username", storedEmp.getUsername());

        String jwt = JwtUtils.generateJwt(dataMap);
        return new LoginInfo(storedEmp.getId(), storedEmp.getUsername(), storedEmp.getName(), jwt);
    }

    private String resolveInitialPassword(String password) {
        return StringUtils.hasText(password) ? password : DEFAULT_EMP_PASSWORD;
    }

    private void upgradeLegacyPasswordIfNeeded(Emp emp, String rawPassword) {
        if (!PasswordUtils.isEncoded(emp.getPassword())) {
            empMapper.updatePassword(emp.getId(), PasswordUtils.encode(rawPassword));
        }
    }
}
