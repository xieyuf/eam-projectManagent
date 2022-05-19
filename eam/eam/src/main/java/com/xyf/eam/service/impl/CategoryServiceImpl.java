/**
 * Created by xieYF
 * 2022/5/4 16:26
 */

package com.xyf.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyf.reggie.common.CustomException;
import com.xyf.reggie.entity.Category;
import com.xyf.reggie.entity.Dish;
import com.xyf.reggie.entity.Setmeal;
import com.xyf.reggie.mapper.CategoryMapper;
import com.xyf.reggie.service.CategoryService;
import com.xyf.reggie.service.DishService;
import com.xyf.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类，删除之前需要进行判断
     * @param id
     */
    @Override
    public void remove(Long id) {

        //查询当前分类是否关联了菜品，若关联，抛出一个业务异常

        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<Dish>();
        //添加查询条件，根据分类id进行查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(dishLambdaQueryWrapper);

        if(count1 > 0){
            //已经关联菜品，抛出一个业务异常
            throw new CustomException("当前分类已经关联了菜品，不能删除");
        }


        //查询当前分类是否关联了套餐，若关联，抛出一个业务异常

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id进行查询
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);

        if(count2 > 0){
            //已经关联套餐，抛出一个业务异常
            throw new CustomException("当前分类已经关联了套餐，不能删除");
        }

        //正常删除分类
        super.removeById(id);

    }
}
