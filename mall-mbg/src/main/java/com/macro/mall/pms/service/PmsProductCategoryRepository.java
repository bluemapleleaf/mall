package com.macro.mall.pms.service;

import com.macro.mall.pms.dto.PmsProductCategoryWithChildrenItem;
import com.macro.mall.pms.model.PmsProductCategory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 产品分类 服务类
 * </p>
 *
 * @author dongjb
 * @since 2020-11-27
 */
public interface PmsProductCategoryRepository extends IService<PmsProductCategory> {
    /**
     * 获取商品分类及其子分类
     */
    List<PmsProductCategoryWithChildrenItem> listWithChildren();
}
