package com.macro.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.macro.mall.service.SmsHomeNewProductService;
import com.macro.mall.sms.model.SmsHomeNewProduct;
import com.macro.mall.sms.service.impl.SmsHomeNewProductRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 首页新品推荐管理Service实现类
 * Created by macro on 2018/11/6.
 */
@Service
public class SmsHomeNewProductServiceImpl extends SmsHomeNewProductRepositoryImpl implements SmsHomeNewProductService {
    @Override
    public boolean create(List<SmsHomeNewProduct> homeNewProductList) {
        for (SmsHomeNewProduct SmsHomeNewProduct : homeNewProductList) {
            SmsHomeNewProduct.setRecommendStatus(1);
            SmsHomeNewProduct.setSort(0);
        }
        return saveBatch(homeNewProductList);
    }

    @Override
    public boolean updateSort(Long id, Integer sort) {
        SmsHomeNewProduct homeNewProduct = new SmsHomeNewProduct();
        homeNewProduct.setId(id);
        homeNewProduct.setSort(sort);
        return updateById(homeNewProduct);
    }

    @Override
    public boolean delete(List<Long> ids) {
        return removeByIds(ids);
    }

    @Override
    public boolean updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        LambdaUpdateWrapper<SmsHomeNewProduct> lambda = new LambdaUpdateWrapper<>();
        lambda.in(SmsHomeNewProduct::getId, ids);
        lambda.set(SmsHomeNewProduct::getRecommendStatus, recommendStatus);
        return update(lambda);
    }

    @Override
    public Page<SmsHomeNewProduct> list(String productName, Integer recommendStatus, Integer pageSize, Integer pageNum) {
        Page<SmsHomeNewProduct> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SmsHomeNewProduct> lambda = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(productName)){
            lambda.like(SmsHomeNewProduct::getProductName, productName);
        }
        if(recommendStatus!=null){
            lambda.eq(SmsHomeNewProduct::getRecommendStatus, recommendStatus);
        }
        lambda.orderByDesc(SmsHomeNewProduct::getSort);
        return page(page, lambda);
    }
}
