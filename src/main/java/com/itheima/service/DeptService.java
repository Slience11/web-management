package com.itheima.service;

import com.itheima.pojo.Dept;
import java.util.List;


public interface DeptService {
    /**
     * 查询所有部门信息
     */
    List<Dept> findAll();

    /**
     *根据id删除部门信息
     */
    void deleteById(Integer id);

    /**
     * 新增部门
     */
    void add(Dept dept);

    /**
     * 根据id查询部门
     */
    Dept getInfo(Integer id);

    /**
     * 更新部门信息
     */
    void update(Dept dept);
}
