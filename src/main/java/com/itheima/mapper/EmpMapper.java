package com.itheima.mapper;

import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpQueryParam;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface EmpMapper {

//===============================原始分页查询==============================
//    /**
//     * 返回页数
//     */
//    @Select("select count(*) from  emp e left join dept d on e.dept_id = d.id")
//    Long count();
//
//    /**
//     * 分页查询
//     */
//    @Select("select e.*,d.name deptName from emp e left join dept d on e.dept_id = d.id order by update_time limit #{start},#{pageSize};")
//    List<Emp> page(Integer start,Integer pageSize);
//===============================PageHelper分页查询==============================
//    @Select(" select e.*,d.name deptName from emp e left join dept d on e.dept_id = d.id\n" +where e.name like concat('%',#{name},'%') and e.gender = #{gender} and e.entry_date between #{begin} and #{end}")
    List<Emp> page(EmpQueryParam empQueryParam);

    /**
     * 保存员工基础信息
     */
    @Options(useGeneratedKeys = true,keyProperty = "id")//插入成功后，将主键值回填到emp对象中
    @Insert("insert into emp(username, password, name, gender, phone, job, salary, image, entry_date, dept_id, create_time, update_time) " +
            "values (#{username},#{password},#{name},#{gender},#{phone},#{job},#{salary},#{image},#{entryDate},#{deptId},#{createTime},#{updateTime})")
    void insert(Emp emp);
    //    @Insert("insert into emp(username, name, gender, phone, job, salary, image, entry_date, dept_id, create_time, update_time) " +
//            "values (#{username},#{name},#{gender},#{phone},#{job},#{salary},#{image},#{entryDate},#{deptId},#{createTime},#{updateTime})")
//    void insert(Emp emp);

    /**
     * 批量删除员工信息
     */
    void deleteById(List<Integer> ids);

    /**
     * 根据id查询员工信息
     */
    //方式一：SQL封装ResultMap
    Emp getById(Integer id);
    //方式二：双搜索sql 赋值给emp对象
    Emp getInfoById(Integer id);

    /**
     * 根据id修改员工基础信息
     */
    void updateById(Emp emp);

    /**
     * 生成员工工作数据报表
     */
    @MapKey("")
    List<Map<String, Object>> getEmpJobDate();

    /**
     * 生成员工性别数据报表
     */
    @MapKey("")
    List<Map<String, Object>> getEmpGenderDate();

    /**
     * 根据部门id查询员工数量
     */
    @Select("select count(*) from emp where dept_id = #{id}")
    Integer countByDeptId(Integer id);

    /**
     * 查询所有员工信息
     */
    @Select("select id, username, password, name, gender, phone, job, salary, image, entry_date, dept_id, create_time, update_time from emp")
    List<Emp> getEmpInfo();

    /**
     * 查询登录信息
     */
    @Select("select id, username, password, name from emp where username = #{username}")
    Emp getByUsername(String username);

    @Update("update emp set password = #{password}, update_time = now() where id = #{id}")
    void updatePassword(@Param("id") Integer id, @Param("password") String password);
}
