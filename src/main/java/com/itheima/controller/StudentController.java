package com.itheima.controller;

import com.itheima.pojo.PageResult;
import com.itheima.pojo.Result;
import com.itheima.pojo.Student;
import com.itheima.pojo.StudentQueryParam;
import com.itheima.service.StudentService;
import com.itheima.validation.ValidationGroups;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
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
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public Result list(@Validated StudentQueryParam studentQueryParam) {
        log.info("Query student page: {}", studentQueryParam);
        PageResult<Student> studentList = studentService.page(studentQueryParam);
        return Result.success(studentList);
    }

    @PostMapping
    public Result save(@RequestBody @Validated(ValidationGroups.Create.class) Student student) {
        log.info("Create student: {}", student);
        studentService.save(student);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable @NotNull(message = "学生ID不能为空") Integer id) {
        log.info("Get student: {}", id);
        Student student = studentService.getById(id);
        return Result.success(student);
    }

    @PutMapping
    public Result update(@RequestBody @Validated(ValidationGroups.Update.class) Student student) {
        log.info("Update student: {}", student);
        studentService.update(student);
        return Result.success();
    }

    @PutMapping("/violation/{id}/{score}")
    public Result handleViolation(
            @PathVariable @NotNull(message = "学生ID不能为空") Integer id,
            @PathVariable @Min(value = 1, message = "扣分必须大于0") @Max(value = 100, message = "扣分不能大于100") Short score) {
        log.info("Handle student violation: id={}, score={}", id, score);
        studentService.handleViolation(id, score);
        return Result.success();
    }

    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable @NotEmpty(message = "学生ID列表不能为空") List<Integer> ids) {
        log.info("Delete students: {}", ids);
        studentService.delete(ids);
        return Result.success();
    }
}
