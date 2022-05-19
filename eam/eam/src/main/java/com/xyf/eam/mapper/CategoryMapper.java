/**
 * Created by xieYF
 * 2022/5/4 16:24
 */

package com.xyf.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xyf.reggie.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
