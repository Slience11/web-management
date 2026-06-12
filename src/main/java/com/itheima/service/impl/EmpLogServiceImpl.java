package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.mapper.EmpLogMapper;
import com.itheima.pojo.EmpLog;
import com.itheima.pojo.OperateLog;
import com.itheima.pojo.PageResult;
import com.itheima.service.EmpLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmpLogServiceImpl implements EmpLogService {

    @Autowired
    private EmpLogMapper empLogMapper;
    
    @Override
    public void insertLog(EmpLog empLog) {

        empLogMapper.insert(empLog);
    }

    /**
     * 日志列表查询
     */
    @Override
    public PageResult<OperateLog> findLogPage(Integer page, Integer pageSize) {

        PageHelper.startPage(page,pageSize);

        List<OperateLog> logList = empLogMapper.findLogPage(page, pageSize);

        Page<OperateLog> p = (Page<OperateLog>) logList;

        return new PageResult<>(p.getTotal(),p.getResult());
    }
}
