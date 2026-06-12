package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.exception.BusinessException;
import com.itheima.mapper.ClazzMapper;
import com.itheima.mapper.StudentMapper;
import com.itheima.pojo.Clazz;
import com.itheima.pojo.ClazzQueryParam;
import com.itheima.pojo.PageResult;
import com.itheima.service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClazzServiceImpl implements ClazzService {

    @Autowired
    private ClazzMapper clazzMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public PageResult<Clazz> page(ClazzQueryParam clazzQueryParam) {
        PageHelper.startPage(clazzQueryParam.getPage(), clazzQueryParam.getPageSize());
        List<Clazz> clazzList = clazzMapper.getClazzInfo(clazzQueryParam);
        clazzList.forEach(this::fillStatus);
        Page<Clazz> page = (Page<Clazz>) clazzList;
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        if (studentMapper.countByClazzId(id) > 0) {
            throw new BusinessException("该班级下存在学生，不能直接删除");
        }
        clazzMapper.deleteById(id);
    }

    @Override
    public void save(Clazz clazz) {
        validateDateRange(clazz);
        clazz.setCreateTime(LocalDateTime.now());
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.insert(clazz);
    }

    @Override
    public Clazz getById(Integer id) {
        Clazz clazz = clazzMapper.getById(id);
        if (clazz == null) {
            throw new BusinessException("班级不存在");
        }
        fillStatus(clazz);
        return clazz;
    }

    @Override
    public void update(Clazz clazz) {
        Clazz existing = clazzMapper.getById(clazz.getId());
        if (existing == null) {
            throw new BusinessException("班级不存在");
        }
        validateDateRange(mergeForValidation(existing, clazz));
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.update(clazz);
    }

    @Override
    public List<Clazz> allInfo() {
        List<Clazz> clazzList = clazzMapper.getAllInfo();
        clazzList.forEach(this::fillStatus);
        return clazzList;
    }

    private void fillStatus(Clazz clazz) {
        LocalDate today = LocalDate.now();
        if (clazz.getBeginDate() != null && clazz.getBeginDate().isAfter(today)) {
            clazz.setStatus("未开班");
        } else if (clazz.getEndDate() != null && clazz.getEndDate().isBefore(today)) {
            clazz.setStatus("已结课");
        } else {
            clazz.setStatus("在读");
        }
    }

    private void validateDateRange(Clazz clazz) {
        if (clazz.getBeginDate() != null
                && clazz.getEndDate() != null
                && clazz.getBeginDate().isAfter(clazz.getEndDate())) {
            throw new BusinessException("开课日期不能晚于结课日期");
        }
    }

    private Clazz mergeForValidation(Clazz existing, Clazz updated) {
        Clazz merged = new Clazz();
        merged.setBeginDate(updated.getBeginDate() == null ? existing.getBeginDate() : updated.getBeginDate());
        merged.setEndDate(updated.getEndDate() == null ? existing.getEndDate() : updated.getEndDate());
        return merged;
    }
}
