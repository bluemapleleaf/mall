package com.macro.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.service.SmsHomeBrandService;
import com.macro.mall.sms.model.SmsHomeBrand;
import com.macro.mall.sms.service.impl.SmsHomeBrandRepositoryImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 首页品牌管理Service实现类
 * Created by macro on 2018/11/6.
 */
@Service
public class SmsHomeBrandServiceImpl extends SmsHomeBrandRepositoryImpl implements SmsHomeBrandService {
    @Override
    public boolean create(List<SmsHomeBrand> homeBrandList) {
        for (SmsHomeBrand smsHomeBrand : homeBrandList) {
            smsHomeBrand.setRecommendStatus(1);
            smsHomeBrand.setSort(0);

        }
        return save(homeBrandList.get(0));
    }

    @Override
    public boolean updateSort(Long id, Integer sort) {
        SmsHomeBrand homeBrand = new SmsHomeBrand();
        homeBrand.setId(id);
        homeBrand.setSort(sort);
        return updateById(homeBrand);
    }

    @Override
    public boolean delete(List<Long> ids) {
        return removeByIds(ids);
    }

    @Override
    public boolean updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        LambdaUpdateWrapper<SmsHomeBrand> lambda = new LambdaUpdateWrapper<>();
        lambda.like(SmsHomeBrand::getId, ids);
        lambda.set(SmsHomeBrand::getRecommendStatus, recommendStatus);
        return update(lambda);
    }

    @Override
    public Page<SmsHomeBrand> list(String brandName, Integer recommendStatus, Integer pageSize, Integer pageNum) {
        Page<SmsHomeBrand> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SmsHomeBrand> lambda = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(brandName)){
            lambda.like(SmsHomeBrand::getBrandName, brandName);
        }
        if(recommendStatus!=null){
            lambda.eq(SmsHomeBrand::getRecommendStatus, recommendStatus);
        }
        lambda.orderByDesc(SmsHomeBrand::getSort);
        return page(page, lambda);
    }
}
