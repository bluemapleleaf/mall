package com.macro.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.macro.mall.service.SmsHomeRecommendProductService;
import com.macro.mall.sms.model.SmsHomeRecommendProduct;
import com.macro.mall.sms.service.impl.SmsHomeRecommendProductRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 首页人气推荐管理Service实现类
 * Created by macro on 2018/11/7.
 */
@Service
public class SmsHomeRecommendProductServiceImpl extends SmsHomeRecommendProductRepositoryImpl implements SmsHomeRecommendProductService {
    @Override
    public boolean create(List<SmsHomeRecommendProduct> homeRecommendProductList) {
        for (SmsHomeRecommendProduct recommendProduct : homeRecommendProductList) {
            recommendProduct.setRecommendStatus(1);
            recommendProduct.setSort(0);
        }
        return saveBatch(homeRecommendProductList);
    }

    @Override
    public boolean updateSort(Long id, Integer sort) {
        SmsHomeRecommendProduct recommendProduct = new SmsHomeRecommendProduct();
        recommendProduct.setId(id);
        recommendProduct.setSort(sort);
        return updateById(recommendProduct);
    }

    @Override
    public boolean delete(List<Long> ids) {
        return removeByIds(ids);
    }

    @Override
    public boolean updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        LambdaUpdateWrapper<SmsHomeRecommendProduct> lambda = new LambdaUpdateWrapper<>();
        lambda.in(SmsHomeRecommendProduct::getId, ids);
        lambda.set(SmsHomeRecommendProduct::getRecommendStatus,recommendStatus);
        return update(lambda);
    }

    @Override
    public Page<SmsHomeRecommendProduct> list(String productName, Integer recommendStatus, Integer pageSize, Integer pageNum) {
        Page<SmsHomeRecommendProduct> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SmsHomeRecommendProduct> lambda = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(productName)){
            lambda.like(SmsHomeRecommendProduct::getProductName, productName);
        }
        if(recommendStatus!=null){
            lambda.eq(SmsHomeRecommendProduct::getRecommendStatus, recommendStatus);
        }
        lambda.orderByDesc(SmsHomeRecommendProduct::getSort);
        return page(page, lambda);
    }
}
