package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.exception.BusinessException;
import com.itheima.mapper.StudentMapper;
import com.itheima.pojo.PageResult;
import com.itheima.pojo.Student;
import com.itheima.pojo.StudentQueryParam;
import com.itheima.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public PageResult<Student> page(StudentQueryParam studentQueryParam) {
        PageHelper.startPage(studentQueryParam.getPage(), studentQueryParam.getPageSize());
        List<Student> studentList = studentMapper.getPageInfo(studentQueryParam);
        Page<Student> page = (Page<Student>) studentList;
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    @Override
    public void save(Student student) {
        student.setViolationCount((short) 0);
        student.setViolationScore((short) 0);
        student.setCreateTime(LocalDateTime.now());
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.insert(student);
    }

    @Override
    public Student getById(Integer id) {
        Student student = studentMapper.getById(id);
        if (student == null) {
            throw new BusinessException("学生不存在");
        }
        return student;
    }

    @Override
    public void update(Student student) {
        if (studentMapper.getById(student.getId()) == null) {
            throw new BusinessException("学生不存在");
        }
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.update(student);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void handleViolation(Integer id, Short score) {
        Student student = studentMapper.getById(id);
        if (student == null) {
            throw new BusinessException("学生不存在");
        }

        short violationCount = student.getViolationCount() == null ? 0 : student.getViolationCount();
        short violationScore = student.getViolationScore() == null ? 0 : student.getViolationScore();
        student.setViolationCount((short) (violationCount + 1));
        student.setViolationScore((short) (violationScore + score));
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.update(student);
    }

    @Override
    public void delete(List<Integer> ids) {
        studentMapper.deleteById(ids);
    }
}
