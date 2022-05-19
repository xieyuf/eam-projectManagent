/**
 * Created by xieYF
 * 2022/5/4 17:11
 */

package com.xyf.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xyf.reggie.dto.DishDto;
import com.xyf.reggie.entity.Dish;

public interface DishService extends IService<Dish> {
    //新增菜品，同时插入菜品对应的口味数据，需要操作两张表
    void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品信息和口味信息
    DishDto getByIdWithFlavor(Long id);

    //更新菜品
    void updateWithFlavor(DishDto dishDto);


}
