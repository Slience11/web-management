package com.itheima.pojo;

import com.itheima.validation.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dept {
    @NotNull(message = "部门ID不能为空", groups = ValidationGroups.Update.class)
    private Integer id;

    @NotBlank(message = "部门名称不能为空", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String name;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
