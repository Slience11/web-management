package com.itheima.service;

import com.itheima.pojo.EmpLog;
import com.itheima.pojo.OperateLog;
import com.itheima.pojo.PageResult;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface EmpLogService {

    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void insertLog(EmpLog empLog);

    /**
     * 日志列表查询
     */
    PageResult<OperateLog> findLogPage(Integer page, Integer pageSize);
}
