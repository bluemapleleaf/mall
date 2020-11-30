package com.macro.mall.pms.service;

import com.macro.mall.pms.dto.ProductAttrInfo;
import com.macro.mall.pms.model.PmsProductAttribute;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商品属性参数表 服务类
 * </p>
 *
 * @author dongjb
 * @since 2020-11-27
 */
public interface PmsProductAttributeRepository extends IService<PmsProductAttribute> {
    /**
     * 获取商品属性信息
     */
    List<ProductAttrInfo> getProductAttrInfoRepo(Long productCategoryId);

}
