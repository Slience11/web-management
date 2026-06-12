package com.itheima.mapper;

import com.itheima.pojo.Clazz;
import com.itheima.pojo.ClazzQueryParam;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;

@Mapper
public interface ClazzMapper {
    /**
     * 查询班级信息
     */
    List<Clazz> getClazzInfo(ClazzQueryParam clazzQueryParam);

    /**
     * 通过id删除班级信息
     */
    void deleteById(Integer id);

    /**
     * 新增班级信息
     */
    void insert(Clazz clazz);

    /**
     * 通过id查询班级信息
     */
    Clazz getById(Integer id);

    /**
     * 修改班级信息
     */
    void update(Clazz clazz);

    /**
     * 查询所有班级信息
     */
    List<Clazz> getAllInfo();

    /**
     * 统计班级人数信息
     */
    @MapKey("")
    List<Map<String, Object>> getClazzData();
}
