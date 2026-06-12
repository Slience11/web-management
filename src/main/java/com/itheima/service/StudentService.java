package com.itheima.service;

import com.itheima.pojo.PageResult;
import com.itheima.pojo.Student;
import com.itheima.pojo.StudentQueryParam;

import java.util.List;

public interface StudentService {

    /**
     * 条件分页查询
     */
    PageResult<Student> page(StudentQueryParam studentQueryParam);

    /**
     * 添加学生信息
     */
    void save(Student student);

    /**
     * 根据ID查询学生信息
     */
    Student getById(Integer id);

    /**
     * 修改学生信息
     */
    void update(Student student);

    /**
     * 处理违纪信息
     */
    void handleViolation(Integer id, Short score);

    /**
     * 批量删除学生信息
     */
    void delete(List<Integer> ids);
}
