package com.itheima.controller;

import com.itheima.pojo.ClazzOption;
import com.itheima.pojo.JobOption;
import com.itheima.pojo.Result;
import com.itheima.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/report")
@RestController
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 员工工作报表
     */
    @GetMapping("/empJobData")
    public Result getEmpJobReport() {
        log.info("统计各个职位的员工人数");
        JobOption jobOption=reportService.getEmpJobDate();
        return Result.success(jobOption);
    }

    /**
     * 员工性别报表
     */
    @GetMapping("/empGenderData")
    public Result getEmpGenderReport(){
        log.info("统计员工性别信息");
        List<Map<String,Object>> list = reportService.getEmpGenderData();
        return Result.success(list);
    }

    /**
     * 班级人数统计报表
     */
    @GetMapping("/studentCountData")
    public Result getClazzReport(){
        log.info("统计班级人数信息");
        ClazzOption clazzOption = reportService.getClazzData();
        return Result.success(clazzOption);
    }

    /**
     * 学生学历报表
     */
    @GetMapping("/studentDegreeData")
    public Result getStudentDegreeData(){
        log.info("统计员工性别信息");
        List<Map<String,Object>> list = reportService.getStudentDegreeData();
        return Result.success(list);
    }

}
