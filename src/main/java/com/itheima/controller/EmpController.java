package com.itheima.controller;

import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpQueryParam;
import com.itheima.pojo.PageResult;
import com.itheima.pojo.Result;
import com.itheima.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequestMapping("/emps")
@RestController
public class EmpController {

    @Autowired
    private EmpService empService;

    /**
     * 条件分页查询
     */
    @GetMapping
    public Result page(EmpQueryParam empQueryParam){
        log.info("分页查询：{}",empQueryParam);
        PageResult<Emp> pageResult =empService.page(empQueryParam);
        return Result.success(pageResult);
    }

    /**
     * 新增员工信息
     */
    @PostMapping
    //接收JOSN格式的数据 需要使用@RequestBody！！！！！！！！！！
    public Result save(@RequestBody Emp emp) throws Exception{
        log.info("添加员工信息：{}",emp);
        empService.save(emp);
        return Result.success();
    }
    /**
     * 批量删除员工信息
     */
    @DeleteMapping
    public Result delete(@RequestParam List<Integer> ids){
        log.info("批量删除员工信息：{}",ids);
        empService.delete(ids);
        return Result.success();
    }

    /**
     * 修改员工信息（查询回显）
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id){
//    public Result getInfoById(@PathVariable Integer id){
        log.info("查询员工信息：{}",id);
        Emp emp = empService.getInfo(id);
//        Emp emp = empService.getInfoById(id);
        return Result.success(emp);
    }

    /**
     * 修改员工信息(修改数据)
     */
    @PutMapping
    public Result update(@RequestBody Emp emp){
        log.info("修改员工信息：{}",emp);
        empService.update(emp);
        return Result.success();
    }

    /**
     * 查询所有员工信息
     */
    @GetMapping("/list")
    public Result list(){
        log.info("查询所有员工信息");
        List<Emp> list = empService.allInfo();
        return Result.success(list);
    }


}
