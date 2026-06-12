package com.itheima.controller;

import com.itheima.anno.Log;
import com.itheima.pojo.Dept;
import com.itheima.pojo.Result;
import com.itheima.service.DeptService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/depts")
@RestController
public class DeptController {

    @Autowired
    private  DeptService deptService;

    /**
     * 查新全部部门信息
     */
//    @RequestMapping(value = "/depts",method = RequestMethod.GET)
//    @GetMapping("/depts")
    @GetMapping
    public Result list(){
        log.info("查询全部部门信息");
        List<Dept> deptList = deptService.findAll();
        return Result.success(deptList);
    }

    /**
     * 删除部门信息
     */
    /*//简单接收方法一 通过原始的 HttpServletRequest 对象获取请求参数
    @DeleteMapping("/depts")
    public Result delete(HttpServletRequest  request){
        String idStr = request.getParameter("id");
        Integer id = parseInt(idStr);
        System.out.println("id:"+id);
        return Result.success();
    }*/

    /*//接收方法二：通过Spring提供的 @RequestParam 注解，将请求参数绑定给方法形参
    @DeleteMapping("/depts")
    public Result delete(@RequestParam("id") Integer id){
        System.out.println("id:"+id);
        return Result.success();
    }*/

    //接收方法三：如果请求参数名与形参变量名相同，直接定义方法形参即可接收。（省略@RequestParam）
//    @DeleteMapping("/depts")
    @Log
    @DeleteMapping
    public Result delete(Integer id){
        log.info("删除部门信息 {}",id);
        deptService.deleteById(id);
        return Result.success();
    }

    /**
     * 添加部门
     */
//    @PostMapping("/depts")
    @Log
    @PostMapping
    public Result add(@RequestBody Dept dept){
        log.info("添加部门信息{}",dept);
        deptService.add(dept);
        return Result.success();
    }

    /**
     * 根据id查询部门
     */
//    @GetMapping("/depts/{id}")
//    public Result getById(@PathVariable("id") Integer getId){
//        System.out.println("getId"+getId);
//        Dept dept = deptService.getInfo(getId);
//        return Result.success(dept);
//    }
//    @GetMapping("/depts/{id}")
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id){
        log.info("根据id查询部门信息{}",id);
        Dept dept = deptService.getInfo(id);
        return Result.success(dept);
    }

    /**
     * 修改部门信息
     */
//    @PutMapping("/depts")
    @Log
    @PutMapping
    public Result update(@RequestBody Dept dept){
        deptService.update(dept);
        log.info("修改部门信息{}",dept);
        return Result.success();
    }

}
