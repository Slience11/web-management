package com.itheima.service;

import com.itheima.pojo.ClazzOption;
import com.itheima.pojo.JobOption;

import java.util.List;
import java.util.Map;

public interface ReportService {

    /**
     * 统计员工职位数据
     */
    JobOption getEmpJobDate();

    /**
     * 统计员工性别数据
     */
    List<Map<String, Object>> getEmpGenderData();

    /**
     * 统计班级人数
     */
    ClazzOption getClazzData();

    /**
     * 统计学生学历数据
     */
    List<Map<String, Object>> getStudentDegreeData();
}
