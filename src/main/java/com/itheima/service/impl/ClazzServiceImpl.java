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
        //调用PageHelper的startPage方法设置分页参数
        PageHelper.startPage(clazzQueryParam.getPage(),clazzQueryParam.getPageSize());
        //调用mapper方法查询数据
        List<Clazz> clazzList = clazzMapper.getClazzInfo(clazzQueryParam);
        clazzList.forEach(clazz -> {
            //开始时间比现在晚:未开班
            if (clazz.getBeginDate().isAfter(LocalDate.now())){
                clazz.setStatus("未开班");
            } else if (clazz.getBeginDate().isBefore(LocalDate.now())&&clazz.getEndDate().isAfter(LocalDate.now())) {
                clazz.setStatus("已开班");
            }else{
                clazz.setStatus("已结课");
            }
        });
        //获取分页结果
        Page<Clazz> p = (Page<Clazz>)clazzList;
        //封装PageResult对象
        return new PageResult<Clazz>(p.getTotal(),p.getResult());
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (studentMapper.countByClazzId(id)>0){
                throw new BusinessException("对不起, 该班级下有学生, 不能直接删除");
        }
        clazzMapper.deleteById(id);
    }

    @Override
    public void save(Clazz clazz) {
        //插入基础数据
        clazz.setCreateTime(LocalDateTime.now());
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.insert(clazz);
    }

    @Override
    public Clazz getById(Integer id) {
        return clazzMapper.getById(id);
    }

    @Override
    public void update(Clazz clazz) {
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.update(clazz);
    }

    @Override
    public List<Clazz> allInfo() {
        return clazzMapper.getAllInfo();

    }
}
