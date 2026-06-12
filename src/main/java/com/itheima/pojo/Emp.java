package com.itheima.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.itheima.validation.ValidationGroups;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Emp {
    @NotNull(message = "员工ID不能为空", groups = ValidationGroups.Update.class)
    private Integer id;

    @NotBlank(message = "用户名不能为空", groups = {ValidationGroups.Create.class, ValidationGroups.Login.class})
    private String username;

    @NotBlank(message = "密码不能为空", groups = ValidationGroups.Login.class)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ToString.Exclude
    private String password;

    @NotBlank(message = "姓名不能为空", groups = ValidationGroups.Create.class)
    private String name;

    @NotNull(message = "性别不能为空", groups = ValidationGroups.Create.class)
    @Min(value = 1, message = "性别参数不合法", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Max(value = 2, message = "性别参数不合法", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private Integer gender;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String phone;

    @Min(value = 1, message = "职位参数不合法", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Max(value = 5, message = "职位参数不合法", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private Integer job;

    @PositiveOrZero(message = "薪资不能为负数", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private Integer salary;

    private String image;
    private LocalDate entryDate;
    private Integer deptId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String deptName;

    @Valid
    private List<EmpExpr> exprList;
}
