/**
 * Created by xieYF
 * 2022/5/4 17:16
 */

package com.xyf.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyf.reggie.entity.Setmeal;
import com.xyf.reggie.mapper.SetmealMapper;
import com.xyf.reggie.service.SetmealService;
import org.springframework.stereotype.Service;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
}
