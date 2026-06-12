package com.itheima.service.impl;
import com.itheima.exception.BusinessException;
import com.itheima.mapper.DeptMapper;
import com.itheima.mapper.EmpMapper;
import com.itheima.pojo.Dept;
import com.itheima.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptMapper deptMapper;
    @Autowired
    private EmpMapper empMapper;

    @Override
    public List<Dept> findAll(){
        return deptMapper.findAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Integer id){
        if (empMapper.countByDeptId(id)>0){
            throw new BusinessException("对不起，当前部门下有员工，不能直接删除");
        }
        deptMapper.deleteById(id);
    }

    @Override
    public void add(Dept dept) {
        dept.setCreateTime(LocalDateTime.now());
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.insert(dept);
    }

    @Override
    public Dept getInfo(Integer id) {
        return deptMapper.getInfo(id);
    }

    @Override
    public void update(Dept dept) {
        //补全基础信息
        dept.setUpdateTime(LocalDateTime.now());

        //调用mapper层
        deptMapper.update(dept);
    }
}
