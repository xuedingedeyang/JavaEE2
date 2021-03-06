package com.yang.mybatis.config.mappers;

import com.yang.mybatis.bean.Employee;
import org.apache.ibatis.annotations.Select;

public interface EmployeeMapperAnnotation {
    @Select("select * from tbl_employee where id = #{id}")
    Employee getEmployeeById(Integer id);
}
