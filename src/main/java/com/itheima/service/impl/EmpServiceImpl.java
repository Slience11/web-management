package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.mapper.EmpExprMapper;
import com.itheima.mapper.EmpLogMapper;
import com.itheima.mapper.EmpMapper;
import com.itheima.pojo.*;
import com.itheima.service.EmpLogService;
import com.itheima.service.EmpService;
import com.itheima.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private EmpExprMapper empExprMapper;
    @Autowired
    private EmpLogService empLogService;
    @Autowired
    private EmpLogMapper empLogMapper;

//    /**
//     * 原始分页查询
//     * @param page 页码
//     * @param pageSize 页码大小
//     */
//    @Override
//    public PageResult<Emp> page(Integer page, Integer pageSize) {
//
//        //1.查询总记录数
//        Long total = empMapper.count();
//
//        //2.查询当前页数据
//        Integer start = (page-1)*pageSize;
//        List<Emp> rows = empMapper.page(start, pageSize);
//
//        //3.封装PageResult对象
//        return new PageResult<>(total,rows);
//    }

    /**
     * PageHelper分页查询
     */
    @Override
    public PageResult<Emp> page(EmpQueryParam empQueryParam) {

        //1.调用PageHeper的startPage方法设置分页参数
        PageHelper.startPage(empQueryParam.getPage(),empQueryParam.getPageSize());//设置分页参数
        //2.查询当前页数据
        List<Emp> list =  empMapper.page(empQueryParam);
        Page<Emp> p = (Page<Emp>) list;//强转
        //3.封装PageResult对象
        return new PageResult<Emp>(p.getTotal(),p.getResult());
    }

    /**
     * 保存员工信息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(Emp emp) throws Exception {
        try {
            //1.设置默认值并保存员工基础信息
            emp.setCreateTime(LocalDateTime.now());
            emp.setUpdateTime(LocalDateTime.now());
            empMapper.insert(emp);
            //2.保存员工工作经历信息
            List<EmpExpr> empList = emp.getExprList();
            //判断List集合是否为空，如果不为空，则遍历集合，设置员工id，批量保存
            if (!CollectionUtils.isEmpty(empList)){

                empList.forEach(empExpr -> {
                    empExpr.setEmpId(emp.getId());
                });
                empExprMapper.insertBatch(emp);
            }
        } finally {
            //记录操作日志
            EmpLog empLog = new EmpLog(null,LocalDateTime.now(),"新建员工信息"+emp);
            empLogService.insertLog(empLog);

        }
    }

    /**
     * 批量删除员工信息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<Integer> ids) {
        //删除员工基础信息
        empMapper.deleteById(ids);
        //删除员工工作经历信息
        empExprMapper.deleteByEmpId(ids);
    }

    /**
     * 根据员工id查询员工详细信息（封装）
     */
    @Override
    public Emp getInfo(Integer id) {
        //封装查询参数
        Emp emp = empMapper.getById(id);
        return emp;
    }

    /**
     * 根据员工id查询员工详细信息（非封装）
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Emp getInfoById(Integer id) {
        //封装查询参数
//        Emp emp = empMapper.getById(id);
        Emp emp = empMapper.getInfoById(id);

        List<EmpExpr> exprList = empExprMapper.getByIdBatch(id);

        emp.setExprList(exprList);

        return emp;
    }

    /**
     * 根据id修改员工信息
     */
    @Override
    public void update(Emp emp) {
        //修改员工基础信息
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.updateById(emp);

        //修改员工工作经历信息
        //1.删除员工工作经历信息
        empExprMapper.deleteByEmpId(Arrays.asList(emp.getId()));
        //2.保存新员工工作经历信息
        List<EmpExpr> exprList = emp.getExprList();
        if(!CollectionUtils.isEmpty(exprList)){
            //给每个员工工作经历对象设置员工id
            exprList.forEach(empExpr -> {
                empExpr.setEmpId(emp.getId());
            });
        }
        empExprMapper.insertBatch(emp);
    }

    /**
     * 查询所有员工信息
     */
    @Override
    public List<Emp> allInfo() {
       return empMapper.getEmpInfo();
    }

    /**
     * 查询用户名或密码
     */
    @Override
    public LoginInfo login(Emp emp) {
        //1.查询信息
        Emp e  = empMapper.getInfoByUsernameAndPassword(emp);
        log.info("获取用户名和密码信息{}",e);
        //组装信息
        if (e!= null){
            //生成JWT令牌
            HashMap<String, Object> dataMap = new HashMap<>();
            dataMap.put("id",e.getId());
            dataMap.put("username",e.getUsername());

            String jwt = JwtUtils.generateJwt(dataMap);
            return new LoginInfo(e.getId(),e.getUsername(),e.getName(),jwt);
        }
        return null;
    }


}
