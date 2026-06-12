package com.itheima.service;

import com.itheima.pojo.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

public interface EmpService {
    /**
     * 分页查询
     */
    PageResult<Emp> page(EmpQueryParam empQueryParam);

    /**
     * 保存员工信息
     */
    void save(Emp emp) throws Exception;

    /**
     * 批量删除员工信息
     */
    void delete(List<Integer> ids);

    /**
     * 根据id查询员工信息
     */
    Emp getInfo(Integer id);

    Emp getInfoById(Integer id);

    /**
     * 修改员工信息
     */
    void update(Emp emp);

    /**
     * 查询所有员工信息
     */
    List<Emp> allInfo();


    /**
     * 根据用户名和密码查询
     */
    LoginInfo login(Emp emp);



}




/**
     * 分页查询
     */
//    PageResult<Emp> page(Integer page, Integer pageSize,
//                         String name, Integer gender,
//                         LocalDate begin, LocalDate end);
//}


