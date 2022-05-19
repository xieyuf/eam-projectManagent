/**
 * Created by xieYF
 * 2022/5/4 16:25
 */

package com.xyf.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xyf.reggie.entity.Category;

public interface CategoryService extends IService<Category> {
    void remove(Long id);
}
