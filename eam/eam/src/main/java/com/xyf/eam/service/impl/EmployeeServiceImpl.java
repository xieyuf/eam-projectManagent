/**
 * Created by xieYF
 * 2022/5/2 16:16
 */

package com.xyf.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyf.reggie.entity.Employee;
import com.xyf.reggie.mapper.EmployeeMapper;
import com.xyf.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
