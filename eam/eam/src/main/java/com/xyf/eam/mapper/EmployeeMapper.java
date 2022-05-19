/**
 * Created by xieYF
 * 2022/5/2 16:14
 */

package com.xyf.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xyf.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
