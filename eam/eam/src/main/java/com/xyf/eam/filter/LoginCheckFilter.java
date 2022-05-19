/**
 * Created by xieYF
 * 2022/5/2 17:23
 */

package com.xyf.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.xyf.reggie.common.BaseContext;
import com.xyf.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否完成用户登录
 * 采用拦截器的方式
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;

        //1.获取本次请求的URI
        //getRequestURI()方法获得请求的URI
        String requestURI = request.getRequestURI();

        log.info("拦截到请求:{}",requestURI);

        //将不需要拦截，不需要登录，过滤器直接放行
        String[] urls = new String[]{
          "/employee/login",
          "/employee/logout",
          "/backend/**",
          "/front/**"
        };


        //2.判断本次请求是否需要进行判断
        //也就是判断是否属于上面写的字符串数组里面的内容
        boolean check = check(urls, requestURI);

        //3.如果不需要处理，直接放行
        if(check){

            log.info("本次请求不需要处理:{}",requestURI);
            filterChain.doFilter(request,response);
            return;
        }

        //4.判断登录状态，如果已登录，直接放行
        if(request.getSession().getAttribute("employee") != null){

            log.info("用户已登录，id为:{}",request.getSession().getAttribute("employee"));

            //将用户的id存入线程
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request,response);
            return;
        }


        log.info("用户未登录");
        //5.如果未登录则返回未登陆结果，通过输出流的方式向客户端页面响应信息
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }



    /**
     * 路径匹配，判断本次请求是否需要放行
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls,String requestURI){
        for (String url: urls){
            boolean match = PATH_MATCHER.match(url,requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }


}
