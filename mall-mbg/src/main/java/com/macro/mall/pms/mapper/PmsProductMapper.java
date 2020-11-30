package com.macro.mall.pms.mapper;

import com.macro.mall.pms.domain.CartProduct;
import com.macro.mall.pms.domain.PromotionProduct;
import com.macro.mall.pms.dto.PmsProductResult;
import com.macro.mall.pms.model.PmsProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.macro.mall.sms.model.SmsCoupon;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商品信息 Mapper 接口
 * </p>
 *
 * @author dongjb
 * @since 2020-11-27
 */
public interface PmsProductMapper extends BaseMapper<PmsProduct> {
    /**
     * 获取商品编辑信息
     */
    PmsProductResult getUpdateInfo(@Param("id") Long id);

    /**
     * 获取购物车商品信息
     */
    CartProduct getCartProduct(@Param("id") Long id);

    /**
     * 获取促销商品信息列表
     */
    List<PromotionProduct> getPromotionProductList(@Param("ids") List<Long> ids);

    /**
     * 获取可用优惠券列表
     */
    List<SmsCoupon> getAvailableCouponList(@Param("productId") Long productId, @Param("productCategoryId") Long productCategoryId);

}
