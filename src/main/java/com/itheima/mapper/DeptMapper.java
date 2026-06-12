package com.itheima.mapper;

import com.itheima.pojo.Dept;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DeptMapper {

    /**
     * 查询所有部门
     */
//    解决字段名与属性名不匹配问题方式一：手动结果映射
//    @Results({
//            @Result( column = "create_time",property = "createTime"),
//            @Result( column = "update_time",property = "updateTime")
//    })
//    方式二 起别名
//    @Select("select id, name, create_time createTime, update_time updateTime from dept order by update_time desc;")

    //方式三 开启驼峰命名(推荐)
    @Select("select id, name, create_time, update_time from dept order by update_time desc;")
    public List<Dept> findAll();

    /**
     * 根据id删除部门
     */
    @Delete("delete from dept where id = #{id}")
    public void deleteById(Integer id);


    /**
     * 插入部门
     */
    @Insert("insert into dept(name, create_time, update_time) value(#{name},#{createTime},#{updateTime})")
    void insert(Dept dept);

    /**
     * 根据id查询部门
     */
    @Select("select id, name, create_time, update_time from dept where id = #{id}")
    Dept getInfo(Integer id);

    /**
     * 根据id修改部门信息
     */
    @Update("update dept set name = #{name},update_time = #{updateTime} where id =#{id}" )
    void update(Dept dept);

}

