package com.macro.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.pms.model.PmsProductAttributeCategory;
import com.macro.mall.pms.service.impl.PmsProductAttributeCategoryRepositoryImpl;
import com.macro.mall.service.PmsProductAttributeCategoryService;
import org.springframework.stereotype.Service;

/**
 * PmsProductAttributeCategoryService实现类
 * Created by macro on 2018/4/26.
 */
@Service
public class PmsProductAttributeCategoryServiceImpl extends PmsProductAttributeCategoryRepositoryImpl implements PmsProductAttributeCategoryService {

    @Override
    public boolean create(String name) {
        PmsProductAttributeCategory productAttributeCategory = new PmsProductAttributeCategory();
        productAttributeCategory.setName(name);
        return save(productAttributeCategory);
    }

    @Override
    public boolean update(Long id, String name) {
        PmsProductAttributeCategory productAttributeCategory = new PmsProductAttributeCategory();
        productAttributeCategory.setName(name);
        productAttributeCategory.setId(id);
        return updateById(productAttributeCategory);
    }

    @Override
    public boolean delete(Long id) {
        return removeById(id);
    }

    @Override
    public PmsProductAttributeCategory getItem(Long id) {
        return getById(id);
    }

    @Override
    public Page<PmsProductAttributeCategory> getList(Integer pageSize, Integer pageNum) {
        Page<PmsProductAttributeCategory> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PmsProductAttributeCategory> lambda = new LambdaQueryWrapper<>();
        return page(page, lambda);
    }

}
