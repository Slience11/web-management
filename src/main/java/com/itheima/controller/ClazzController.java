package com.itheima.controller;

import com.itheima.pojo.Clazz;
import com.itheima.pojo.ClazzQueryParam;
import com.itheima.pojo.PageResult;
import com.itheima.pojo.Result;
import com.itheima.service.ClazzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/clazzs")
public class ClazzController {

    @Autowired
    private ClazzService clazzService;

    /**
     * 条件分页查询班级信息
     */
    @GetMapping
    public Result list(ClazzQueryParam clazzQueryParam){
        log.info("条件分页查询：{}",clazzQueryParam);
        PageResult<Clazz> clazzPageList = clazzService.page(clazzQueryParam);
        return Result.success(clazzPageList);
    }

    /**
     * 删除班级信息
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        log.info("删除班级信息：{}",id);
        clazzService.delete(id);
        return Result.success();
    }

    /**
     * 新增班级信息
     */
    @PostMapping
    public Result save(@RequestBody Clazz clazz){
        log.info("新增班级信息：{}",clazz);
        clazzService.save(clazz);
        return Result.success();
    }

    /**
     * 根据ID查询班级信息
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id){
        log.info("根据查询id:{}班级信息",id);
        Clazz clazz = clazzService.getById(id);
        return Result.success(clazz);
    }

    /**
     * 修改班级信息
     */
    @PutMapping
    public Result update(@RequestBody Clazz clazz){
        log.info("修改班级信息：{}",clazz.toString());
        clazzService.update(clazz);
        return Result.success();
    }

    /**
     * 查询所有班级班级信息
     */
    @GetMapping("/list")
    public Result allInfo(){
        List<Clazz> clazzList = clazzService.allInfo();
        return Result.success(clazzList);
    }
}
