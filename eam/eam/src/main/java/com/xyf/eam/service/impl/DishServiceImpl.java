/**
 * Created by xieYF
 * 2022/5/4 17:12
 */

package com.xyf.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyf.reggie.dto.DishDto;
import com.xyf.reggie.entity.Dish;
import com.xyf.reggie.entity.DishFlavor;
import com.xyf.reggie.mapper.DishMapper;
import com.xyf.reggie.service.DishFlavorService;
import com.xyf.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {


    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品同时保存口味数据
     * @param dishDto
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //因为要操作两张表，加入事务控制
        //保持数据一致性

        //保存菜品的基本信息到菜品表
        this.save(dishDto);

        Long dishId = dishDto.getId();//菜品id


        //保存菜品口味数据到菜品口味表


        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors.stream().map((item) ->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(dishDto.getFlavors());

    }


    /**
     * 根据id查询菜品信息和口味信息
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {

        //查询菜品基本信息，从dish表查询
        Dish dish = this.getById(id);

        //创建Dto对象来保存菜品信息
        DishDto dishDto = new DishDto();
        //将基本信息拷贝到Dto对象中
        BeanUtils.copyProperties(dish,dishDto);

        //查询当前菜品对应的口味信息，从dish_flavor表查询
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());

        //根据菜品的id获得口味，存到list中
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        //把口味set到Dto对象中
        dishDto.setFlavors(flavors);


        //返回Dto对象
        return dishDto;
    }

    /**
     * 更新修改菜品
     * @param dishDto
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表基本信息
        this.updateById(dishDto);

        //清理当前菜品对应口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());

        dishFlavorService.remove(queryWrapper);

        //新增更新的口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        //保存菜品口味数据到菜品口味表
        flavors = flavors.stream().map((item) ->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);


    }



}
