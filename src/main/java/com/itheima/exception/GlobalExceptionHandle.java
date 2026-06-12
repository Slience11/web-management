package com.itheima.exception;

import com.itheima.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandle {

    @ExceptionHandler
    public Result handleException(Exception e){
        log.info("捕获异常{}",e.toString());
        return Result.error("服务器异常请联系管理员");
    }

    @ExceptionHandler
    public Result handleDuplicateKeyException(DuplicateKeyException e){
        log.info("捕获异常{}",e.toString());
        String msg = e.getMessage();
        String index = msg.substring(msg.indexOf("Duplicate entry"));//截取索引
        String[] arr = index.split(" ");
        return Result.error("已存在"+arr[2]);
    }

    @ExceptionHandler
    public Result handleBusinessException(BusinessException ex) {
        log.error("捕获异常BusinessException");
        return Result.error(ex.getMessage());
    }
}
