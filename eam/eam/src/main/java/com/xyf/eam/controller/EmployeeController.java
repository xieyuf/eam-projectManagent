/**
 * Created by xieYF
 * 2022/5/2 16:18
 */

package com.xyf.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xyf.reggie.common.R;
import com.xyf.reggie.entity.Employee;
import com.xyf.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;


    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){

        //1.将页面提交的密码进行md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2.根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //3.如果没有查询到则返回登陆失败结果
        if(emp == null){
            return R.error("登录失败，查询不到此用户");
        }

        //4.密码对比，如果不一致则返回登陆失败结果
        if(!emp.getPassword().equals(password)){
            return R.error("登录失败，密码错误");
        }

        //5.查看员工状态，若为已禁用状态，则登陆失败
        if(emp.getStatus() == 0){
            return R.error("登陆失败，账号已禁用");
        }

        //6.登录成功，将用户id存入Session
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);

    }

    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清理Session中保存的当前登录的员工id
        request.getSession().removeAttribute("employee");

        return R.success("退出成功");

    }


    /**
     * 新增员工
     * @param request
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工，员工信息:{}",employee.toString());

        //设置一个初始化密码，并且进行加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        //这里通过MybatisPlus公共字段自动填充实现

        //初始化创建时间，修改时间，也就是都设置为当前时间
        //employee.setCreateTime(LocalDateTime.now());
       //employee.setUpdateTime(LocalDateTime.now());

        //获得当前用户的id
        //这是创建者，第一次修改者的id
        //Long empId = (Long) request.getSession().getAttribute("employee");
        //employee.setCreateUser(empId);
        //employee.setUpdateUser(empId);

        employeeService.save(employee);

        return R.success("新增员工成功");
    }


    /**
     * 员工信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page ,int pageSize ,String name){

        log.info("page={},pageSize={},name={}",page,pageSize,name);

        //构造分页构造器
        Page pageInfo = new Page(page,pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();

        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        //执行查询
        employeeService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 根据id修改员工信息
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());

        //得到修改者的id
        //Long empId = (Long) request.getSession().getAttribute("employee");

        //这里通过MybatisPlus公共字段自动填充实现

        //修改修改时间
        //employee.setUpdateTime(LocalDateTime.now());
        //修改修改者id
        //employee.setUpdateUser(empId);

        //修改用户信息
        employeeService.updateById(employee);

        return R.success("员工状态修改成功");
    }


    /**
     * 通过id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("通过id查询员工信息");

        //通过id查询员工，返回一个employee对象
        Employee employee = employeeService.getById(id);
        if(employee != null){
         //如果查出来了就返回
            return R.success(employee);
        }

        //没查出来返回错误信息
        return R.error("查询错误");
    }




}
