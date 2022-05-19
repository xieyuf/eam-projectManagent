/**
 * Created by xieYF
 * 2022/5/4 16:05
 */

package com.xyf.reggie.common;

/**
 * 基于ThreadLocal封装工具类，用于保存和获取当前登录用户的id
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new InheritableThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }

}
