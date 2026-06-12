package com.itheima.controller;

import com.itheima.pojo.PageResult;
import com.itheima.pojo.Result;
import com.itheima.pojo.Student;
import com.itheima.pojo.StudentQueryParam;
import com.itheima.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * 条件分页查询学生信息
     */
    @GetMapping
    public Result list(StudentQueryParam studentQueryParam){
        log.info("分页查询：{}",studentQueryParam);
        PageResult<Student> studentList = studentService.page(studentQueryParam);
        return Result.success(studentList);
    }

    /**
     * 添加学生信息
     */
    @PostMapping
    public Result save(@RequestBody Student student){
        log.info("添加学生信息：{}",student);
        studentService.save(student);
        return Result.success();
    }

    /**
     * 根据ID查询学生信息
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id){
        log.info("根据ID查询学生信息：{}",id);
        Student student = studentService.getById(id);
        return Result.success(student);
    }

    /**
     * 修改学生信息
     */
    @PutMapping
    public Result update(@RequestBody Student student){
        log.info("修改学生信息：{}",student);
        studentService.update(student);
        return Result.success();
    }

    /**
     * 违规处理
     */
    @PutMapping("/violation/{id}/{score}")
    public Result handleViolation(@PathVariable Integer id,@PathVariable Short score){
        log.info("处理学生违规：id={},score={}",id,score);
        studentService.handleViolation(id,score);
        return Result.success();
    }

    /**
     * 批量删除学生信息
     */
    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable List<Integer> ids){
        log.info("批量删除学生信息：{}",ids);
        studentService.delete(ids);
        return Result.success();
    }
}
