package com.itheima.mapper;

import com.itheima.pojo.Student;
import com.itheima.pojo.StudentQueryParam;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentMapper {

    /**
     * 根据班级ID查询学生数量
     */
    @Select("select count(*) from student where clazz_id = #{clazzId}")
    long countByClazzId(Integer clazzId);

    /**
     * 条件分页查询所有学生信息
     */
    List<Student> getPageInfo(StudentQueryParam studentQueryParam);

    /**
     * 新增学生信息
     */
    void insert(Student student);

    /**
     * 根据ID查询学生信息
     */
    Student getById(Integer id);

    /**
     * 修改学生信息
     */
    void update(Student student);

    /**
     * 批量删除学生信息
     */
    void deleteById(List<Integer> ids);

    /**
     * 学生学历统计
     */
    @MapKey("")
    List<Map<String, Object>> getStudentDegreeData();
}
