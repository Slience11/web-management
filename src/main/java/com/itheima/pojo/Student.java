package com.itheima.pojo;

import com.itheima.validation.ValidationGroups;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @NotNull(message = "学生ID不能为空", groups = ValidationGroups.Update.class)
    private Integer id;

    @NotBlank(message = "学生姓名不能为空", groups = ValidationGroups.Create.class)
    private String name;

    @NotBlank(message = "学生学号不能为空", groups = ValidationGroups.Create.class)
    private String no;

    @NotNull(message = "性别不能为空", groups = ValidationGroups.Create.class)
    @Min(value = 1, message = "性别参数不合法", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Max(value = 2, message = "性别参数不合法", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private Integer gender;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String phone;

    private String idCard;

    @Min(value = 0, message = "院校来源参数不合法", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Max(value = 1, message = "院校来源参数不合法", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private Integer isCollege;

    private String address;

    @Min(value = 1, message = "学历参数不合法", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Max(value = 6, message = "学历参数不合法", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private Integer degree;

    private LocalDate graduationDate;

    @NotNull(message = "班级ID不能为空", groups = ValidationGroups.Create.class)
    private Integer clazzId;

    private Short violationCount;
    private Short violationScore;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String clazzName;
}
