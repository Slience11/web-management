package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.mapper.StudentMapper;
import com.itheima.pojo.PageResult;
import com.itheima.pojo.Student;
import com.itheima.pojo.StudentQueryParam;
import com.itheima.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    /**
     * 条件分页查询学生信息
     */
    @Override
    public PageResult<Student> page(StudentQueryParam studentQueryParam) {
        //调用PageHelper的startPage方法设置分页参数
        PageHelper.startPage(studentQueryParam.getPage(), studentQueryParam.getPageSize());
        //调用Mapper方法查询数据
         List<Student> studentList =studentMapper.getPageInfo(studentQueryParam);
        //返回封装数据
        Page<Student> p = (Page<Student>) studentList;
        return new PageResult<Student>(p.getTotal(),p.getResult());
    }

    /**
     * 添加学生信息
     */
    @Override
    public void save(Student student) {
        //插入基础数据
        student.setCreateTime(LocalDateTime.now());
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.insert(student);
    }

    /**
     * 根据ID查询学生信息
     */
    @Override
    public Student getById(Integer id) {
        return studentMapper.getById(id);
    }

    /**
     * 修改学生信息
     */
    @Override
    public void update(Student student) {
        //插入基础数据
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.update(student);
    }

    /**
     * 处理违纪信息
     */
    @Override
    public void handleViolation(Integer id, Short score) {
        Student student = studentMapper.getById(id);
        student.setViolationCount((short) (student.getViolationCount()+1));
        student.setViolationScore((short) (student.getViolationScore()+score));
        studentMapper.update(student);
    }

    /**
     * 批量删除学生信息
     */
    @Override
    public void delete(List<Integer> ids) {
        studentMapper.deleteById(ids);
    }

}
