/**
 * Created by xieYF
 * 2022/5/4 17:10
 */

package com.xyf.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xyf.reggie.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
