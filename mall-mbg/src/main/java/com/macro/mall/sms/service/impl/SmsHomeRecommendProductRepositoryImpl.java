package com.macro.mall.sms.service.impl;

import com.macro.mall.pms.model.PmsProduct;
import com.macro.mall.sms.model.SmsHomeRecommendProduct;
import com.macro.mall.sms.mapper.SmsHomeRecommendProductMapper;
import com.macro.mall.sms.service.SmsHomeRecommendProductRepository;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 人气推荐商品表 服务实现类
 * </p>
 *
 * @author dongjb
 * @since 2020-11-29
 */
@Service
public class SmsHomeRecommendProductRepositoryImpl extends ServiceImpl<SmsHomeRecommendProductMapper, SmsHomeRecommendProduct> implements SmsHomeRecommendProductRepository {
    /**
     * 获取人气推荐
     */
    @Autowired
    SmsHomeRecommendProductMapper homeRecommendProductMapper;
    @Override
    public List<PmsProduct> getHotProductList(Integer offset, Integer limit){
        return homeRecommendProductMapper.getHotProductList(offset, limit);
    }

}
