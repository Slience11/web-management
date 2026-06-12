package com.itheima.mapper;

import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpExpr;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmpExprMapper {
    /**
     * 批量保存员工工作经历信息
     */
    void insertBatch(Emp emp);

    /**
     * 根据员工id批量删除员工工作经历信息
     */
    void deleteByEmpId(List<Integer> empIds);


    List<EmpExpr> getByIdBatch(Integer id);
}
