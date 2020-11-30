package com.macro.mall.pms.service.impl;

import com.macro.mall.pms.domain.CartProduct;
import com.macro.mall.pms.domain.PromotionProduct;
import com.macro.mall.pms.dto.PmsProductResult;
import com.macro.mall.pms.model.PmsProduct;
import com.macro.mall.pms.mapper.PmsProductMapper;
import com.macro.mall.pms.service.PmsProductRepository;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.sms.model.SmsCoupon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品信息 服务实现类
 * </p>
 *
 * @author dongjb
 * @since 2020-11-27
 */
@Service
public class PmsProductRepositoryImpl extends ServiceImpl<PmsProductMapper, PmsProduct> implements PmsProductRepository {
    @Autowired
    PmsProductMapper productMapper;

    @Override
    public PmsProductResult getUpdateInfo(Long id) {
        return productMapper.getUpdateInfo(id);
    }

    @Override
    public CartProduct getCartProduct(Long id){
        return productMapper.getCartProduct(id);
    }

    @Override
    public List<PromotionProduct> getPromotionProductList(List<Long> ids){
        return productMapper.getPromotionProductList(ids);
    }

    @Override
    public List<SmsCoupon> getAvailableCouponList(Long productId, Long productCategoryId){
        return productMapper.getAvailableCouponList(productId, productCategoryId);
    }

}
