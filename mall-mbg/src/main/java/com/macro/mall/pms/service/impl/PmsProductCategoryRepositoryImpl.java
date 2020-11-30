package com.macro.mall.pms.service.impl;

import com.macro.mall.pms.dto.PmsProductCategoryWithChildrenItem;
import com.macro.mall.pms.model.PmsProductCategory;
import com.macro.mall.pms.mapper.PmsProductCategoryMapper;
import com.macro.mall.pms.service.PmsProductCategoryRepository;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 产品分类 服务实现类
 * </p>
 *
 * @author dongjb
 * @since 2020-11-27
 */
@Service
public class PmsProductCategoryRepositoryImpl extends ServiceImpl<PmsProductCategoryMapper, PmsProductCategory> implements PmsProductCategoryRepository {
    @Autowired
    PmsProductCategoryMapper productCategoryMapper;
    /**
     * 获取商品分类及其子分类
     */
    @Override
    public List<PmsProductCategoryWithChildrenItem> listWithChildren(){
        return productCategoryMapper.listWithChildren();
    }
}
