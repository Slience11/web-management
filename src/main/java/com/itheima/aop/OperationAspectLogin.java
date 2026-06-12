package com.itheima.aop;

import com.itheima.mapper.EmpLogMapper;
import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpLoginLog;
import com.itheima.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
public class OperationAspectLogin {

    @Autowired
    private EmpLogMapper empLogMapper;

    @Around("execution(* com.itheima.controller.LoginController.login(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Emp emp = getLoginParam(joinPoint.getArgs());
        Result result = null;

        try {
            result = (Result) joinPoint.proceed();
            return result;
        } finally {
            saveLoginLog(emp, result, System.currentTimeMillis() - startTime);
        }
    }

    private Emp getLoginParam(Object[] args) {
        if (args != null && args.length > 0 && args[0] instanceof Emp emp) {
            return emp;
        }
        return null;
    }

    private void saveLoginLog(Emp emp, Result result, long costTime) {
        EmpLoginLog empLoginLog = new EmpLoginLog();
        empLoginLog.setUsername(emp == null ? null : emp.getUsername());
        empLoginLog.setPassword("");
        empLoginLog.setLoginTime(LocalDateTime.now());
        empLoginLog.setIsSuccess(result != null && Integer.valueOf(1).equals(result.getCode()) ? (short) 1 : (short) 0);
        empLoginLog.setJwt("");
        empLoginLog.setCostTime(costTime);

        try {
            empLogMapper.insertLoginLog(empLoginLog);
        } catch (Exception e) {
            log.warn("Save login log failed: {}", e.getMessage());
        }
    }
}
