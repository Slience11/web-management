package com.itheima.pojo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentQueryParam {

    private String name;

    @Min(value = 1, message = "学历参数不合法")
    @Max(value = 6, message = "学历参数不合法")
    private Integer degree;

    private Integer clazzId;

    @Min(value = 1, message = "页码不能小于1")
    private Integer page = 1;

    @Min(value = 1, message = "每页记录数不能小于1")
    @Max(value = 100, message = "每页记录数不能大于100")
    private Integer pageSize = 10;
}
