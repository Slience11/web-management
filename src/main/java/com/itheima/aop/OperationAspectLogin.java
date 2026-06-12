package com.itheima.aop;

import com.itheima.mapper.EmpLogMapper;
import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpLoginLog;
import com.itheima.pojo.LoginInfo;
import com.itheima.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class OperationAspectLogin {

    @Autowired
    private EmpLogMapper empLogMapper;

    @Around("execution(* com.itheima.controller.LoginController.login(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        EmpLoginLog empLoginLog = new EmpLoginLog();
        Result result;
        try{
            log.info("记录登录日志");
            // 执行方法，记录耗时
            long startTime = System.currentTimeMillis();
            result = (Result) joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            long costTime = endTime - startTime;

            // 获取参数
            Object[] args = joinPoint.getArgs();
            Emp emp = (Emp) args[0];
            log.info("登录信息：{}",emp);

            // 设置参数
            empLoginLog.setUsername(emp.getUsername());
            empLoginLog.setPassword(emp.getPassword());
            empLoginLog.setLoginTime(LocalDateTime.now());
            empLoginLog.setIsSuccess(result.getCode() == 1 ? (short)1 : (short)0);
            empLoginLog.setCostTime(costTime);
            LoginInfo loginInfo = (LoginInfo) (result.getData());
            empLoginLog.setJwt(loginInfo.getToken());

        }finally {
            empLogMapper.insertLoginLog(empLoginLog);
        }
        return result;
    }

}
