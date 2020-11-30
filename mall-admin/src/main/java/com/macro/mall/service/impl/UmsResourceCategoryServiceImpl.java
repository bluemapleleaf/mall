package com.macro.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.macro.mall.ums.model.UmsResourceCategory;
import com.macro.mall.service.UmsResourceCategoryService;
import com.macro.mall.ums.service.impl.UmsResourceCategoryRepositoryImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 后台资源分类管理Service实现类
 *
 * @author dongjb
 * @date 2020/11/21
 */
@Service
public class UmsResourceCategoryServiceImpl extends UmsResourceCategoryRepositoryImpl implements UmsResourceCategoryService {

    @Override
    public List<UmsResourceCategory> listAll() {
        QueryWrapper<UmsResourceCategory> wrapper = new QueryWrapper<>();
        wrapper.lambda().orderByDesc(UmsResourceCategory::getSort);
        return list(wrapper);
    }

    @Override
    public boolean create(UmsResourceCategory umsResourceCategory) {
        umsResourceCategory.setCreateTime(new Date());
        return save(umsResourceCategory);
    }
}