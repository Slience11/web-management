package com.itheima.mapper;

import com.itheima.pojo.EmpLog;
import com.itheima.pojo.EmpLoginLog;
import com.itheima.pojo.OperateLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Mapper
public interface EmpLogMapper {

    /**
     * 分页查询员工操作日志
     */
    @Select("select ol.* ,e.name  operate_emp_name from operate_log ol left join emp e on ol.operate_emp_id = e.id")
    public List<OperateLog> findLogPage(Integer page, Integer pageSize);

    /**
     * 添加员工操作日志
     */
    @Insert("insert into emp_log (operate_time, info) values (#{operateTime}, #{info})")
    public void insert(EmpLog empLog);

    /**
     * 员工登录日志
     */

    @Insert("insert into emp_login_log(username,password,login_time,is_success,jwt,cost_time)" +
            " values(#{username},#{password},#{loginTime},#{isSuccess},#{jwt},#{costTime})")
    public void insertLoginLog(EmpLoginLog empLoginLog);
}
