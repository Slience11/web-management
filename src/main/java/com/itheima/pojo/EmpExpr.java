package com.itheima.pojo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmpExpr {
    private Integer id;
    private Integer empId;
    private LocalDate begin;
    private LocalDate end;

    @NotBlank(message = "工作经历公司名称不能为空")
    private String company;

    @NotBlank(message = "工作经历职位不能为空")
    private String job;
}
