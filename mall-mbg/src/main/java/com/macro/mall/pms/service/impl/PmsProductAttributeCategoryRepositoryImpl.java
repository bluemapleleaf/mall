package com.macro.mall.pms.service.impl;

import com.macro.mall.pms.dto.PmsProductAttributeCategoryItem;
import com.macro.mall.pms.model.PmsProductAttributeCategory;
import com.macro.mall.pms.mapper.PmsProductAttributeCategoryMapper;
import com.macro.mall.pms.service.PmsProductAttributeCategoryRepository;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 产品属性分类表 服务实现类
 * </p>
 *
 * @author dongjb
 * @since 2020-11-27
 */
@Service
public class PmsProductAttributeCategoryRepositoryImpl extends ServiceImpl<PmsProductAttributeCategoryMapper, PmsProductAttributeCategory> implements PmsProductAttributeCategoryRepository {
    @Autowired
    PmsProductAttributeCategoryMapper productAttributeCategoryMapper;
    @Override
    public List<PmsProductAttributeCategoryItem> getListWithAttr() {
        return productAttributeCategoryMapper.getListWithAttr();
    }
}
