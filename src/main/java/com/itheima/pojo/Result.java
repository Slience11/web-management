package com.itheima.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {

    private static final int SUCCESS_CODE = 1;
    private static final int ERROR_CODE = 0;

    private Integer code;
    private String msg;
    private Object data;

    public static Result success() {
        Result result = new Result();
        result.code = SUCCESS_CODE;
        result.msg = "success";
        return result;
    }

    public static Result success(Object object) {
        Result result = new Result();
        result.data = object;
        result.code = SUCCESS_CODE;
        result.msg = "success";
        return result;
    }

    public static Result error(String msg) {
        Result result = new Result();
        result.msg = msg;
        result.code = ERROR_CODE;
        return result;
    }
}
