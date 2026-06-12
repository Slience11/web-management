package com.itheima.controller;

import com.itheima.pojo.Clazz;
import com.itheima.pojo.ClazzQueryParam;
import com.itheima.pojo.PageResult;
import com.itheima.pojo.Result;
import com.itheima.service.ClazzService;
import com.itheima.validation.ValidationGroups;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/clazzs")
public class ClazzController {

    @Autowired
    private ClazzService clazzService;

    @GetMapping
    public Result list(@Validated ClazzQueryParam clazzQueryParam) {
        log.info("Query clazz page: {}", clazzQueryParam);
        PageResult<Clazz> clazzPageList = clazzService.page(clazzQueryParam);
        return Result.success(clazzPageList);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable @NotNull(message = "班级ID不能为空") Integer id) {
        log.info("Delete clazz: {}", id);
        clazzService.delete(id);
        return Result.success();
    }

    @PostMapping
    public Result save(@RequestBody @Validated(ValidationGroups.Create.class) Clazz clazz) {
        log.info("Create clazz: {}", clazz);
        clazzService.save(clazz);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable @NotNull(message = "班级ID不能为空") Integer id) {
        log.info("Get clazz: {}", id);
        Clazz clazz = clazzService.getById(id);
        return Result.success(clazz);
    }

    @PutMapping
    public Result update(@RequestBody @Validated(ValidationGroups.Update.class) Clazz clazz) {
        log.info("Update clazz: {}", clazz);
        clazzService.update(clazz);
        return Result.success();
    }

    @GetMapping("/list")
    public Result allInfo() {
        List<Clazz> clazzList = clazzService.allInfo();
        return Result.success(clazzList);
    }
}
