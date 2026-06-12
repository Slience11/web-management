package com.itheima.pojo;

import com.itheima.validation.ValidationGroups;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Clazz {
    @NotNull(message = "班级ID不能为空", groups = ValidationGroups.Update.class)
    private Integer id;

    @NotBlank(message = "班级名称不能为空", groups = ValidationGroups.Create.class)
    private String name;

    @NotBlank(message = "教室不能为空", groups = ValidationGroups.Create.class)
    private String room;

    @NotNull(message = "开课日期不能为空", groups = ValidationGroups.Create.class)
    private LocalDate beginDate;

    @NotNull(message = "结课日期不能为空", groups = ValidationGroups.Create.class)
    private LocalDate endDate;

    private Integer masterId;

    @NotNull(message = "学科不能为空", groups = ValidationGroups.Create.class)
    @Min(value = 1, message = "学科参数不合法", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Max(value = 20, message = "学科参数不合法", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private Integer subject;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String masterName;
    private String status;
}
