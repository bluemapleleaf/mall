package com.macro.mall.pms.service;

import com.macro.mall.pms.dto.PmsProductAttributeCategoryItem;
import com.macro.mall.pms.model.PmsProductAttributeCategory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 产品属性分类表 服务类
 * </p>
 *
 * @author dongjb
 * @since 2020-11-27
 */
public interface PmsProductAttributeCategoryRepository extends IService<PmsProductAttributeCategory> {

    /**
     * 获取包含属性的商品属性分类
     */
    List<PmsProductAttributeCategoryItem> getListWithAttr();
}
