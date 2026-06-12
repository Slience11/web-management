package com.itheima.controller;

import com.itheima.anno.Log;
import com.itheima.pojo.OperateLog;
import com.itheima.pojo.PageResult;
import com.itheima.pojo.Result;
import com.itheima.pojo.Student;
import com.itheima.service.EmpLogService;
import com.itheima.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
public class LogController {

    @Autowired
    private EmpLogService empLogService;

    /**
     * 日志列表查询
     */
    @GetMapping("/log/page")
    public Result LogPage(Integer page, Integer pageSize){
        log.info("分页查询：{},{}",page,pageSize);
        PageResult<OperateLog> logList = empLogService.findLogPage(page, pageSize);
        return Result.success(logList);
    }



}
