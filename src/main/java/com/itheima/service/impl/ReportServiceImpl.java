package com.itheima.service.impl;

import com.itheima.mapper.ClazzMapper;
import com.itheima.mapper.EmpMapper;
import com.itheima.mapper.StudentMapper;
import com.itheima.pojo.ClazzOption;
import com.itheima.pojo.JobOption;
import com.itheima.pojo.Result;
import com.itheima.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private EmpMapper empMapper;

    @Autowired
    private ClazzMapper clazzMapper;
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public JobOption getEmpJobDate() {
        //1.查询员工工作数据 封装至Map集合中
        List<Map<String, Object>> list = empMapper.getEmpJobDate();
        //将数据分装至对应集合
        List<Object> jobList = list.stream().map(dataMap->dataMap.get("job")).toList();
        List<Object> dataList = list.stream().map(dataMap->dataMap.get("num")).toList();

        return new JobOption(jobList,dataList);
    }

    @Override
    public List<Map<String, Object>> getEmpGenderData() {
        return  empMapper.getEmpGenderDate();
    }

    @Override
    public ClazzOption getClazzData() {
        List<Map<String, Object>> list = clazzMapper.getClazzData();
        //将数据分装至对应集合
        List<Object> clazzList = list.stream().map(dataMap->dataMap.get("clazz")).toList();
        List<Object> dataList = list.stream().map(dataMap->dataMap.get("num")).toList();
        return new ClazzOption(clazzList,dataList);
    }

    /**
     * 学生学历统计
     */
    @Override
    public List<Map<String, Object>> getStudentDegreeData(){
        return studentMapper.getStudentDegreeData();
    }
}

