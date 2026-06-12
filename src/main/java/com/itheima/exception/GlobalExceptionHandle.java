package com.itheima.exception;

import com.itheima.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandle {

    @ExceptionHandler(BusinessException.class)
    public Result handleBusinessException(BusinessException ex) {
        log.warn("Business exception: {}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public Result handleDuplicateKeyException(DuplicateKeyException e) {
        log.warn("Duplicate key exception: {}", e.getMessage());
        return Result.error(resolveDuplicateMessage(e.getMessage()));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public Result handleValidationException(Exception e) {
        log.warn("Validation exception: {}", e.getMessage());
        return Result.error("请求参数不合法");
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        log.error("Unexpected exception", e);
        return Result.error("服务器异常，请联系管理员");
    }

    private String resolveDuplicateMessage(String message) {
        if (message == null || !message.contains("Duplicate entry")) {
            return "数据已存在";
        }
        try {
            String duplicatePart = message.substring(message.indexOf("Duplicate entry"));
            String[] arr = duplicatePart.split(" ");
            return "数据已存在: " + arr[2].replace("'", "");
        } catch (Exception ignored) {
            return "数据已存在";
        }
    }
}
