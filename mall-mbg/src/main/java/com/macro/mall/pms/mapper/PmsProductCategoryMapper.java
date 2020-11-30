package com.macro.mall.pms.mapper;

import com.macro.mall.pms.dto.PmsProductCategoryWithChildrenItem;
import com.macro.mall.pms.model.PmsProductCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 产品分类 Mapper 接口
 * </p>
 *
 * @author dongjb
 * @since 2020-11-27
 */
public interface PmsProductCategoryMapper extends BaseMapper<PmsProductCategory> {
    /**
     * 获取商品分类及其子分类
     */
    List<PmsProductCategoryWithChildrenItem> listWithChildren();

}
