package com.itheima.service;

import com.itheima.pojo.Clazz;
import com.itheima.pojo.ClazzQueryParam;
import com.itheima.pojo.PageResult;

import java.util.List;

public interface ClazzService {
    /**
     * 条件分页查询
     */
    PageResult<Clazz> page(ClazzQueryParam clazzQueryParam);

    /**
     * 删除班级信息
     */
    void delete(Integer id);

    /**
     * 新增班级信息
     */
    void save(Clazz clazz);

    /**
     * 根据ID查询班级信息
     */
    Clazz getById(Integer id);

    /**
     * 修改班级信息
     */
    void update(Clazz clazz);

    /**
     * 查询所有班级信息
     */
    List<Clazz> allInfo();
}
