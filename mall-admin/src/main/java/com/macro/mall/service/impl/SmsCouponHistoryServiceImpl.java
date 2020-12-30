package com.macro.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.macro.mall.service.SmsCouponHistoryService;
import com.macro.mall.sms.model.SmsCouponHistory;
import com.macro.mall.sms.service.impl.SmsCouponHistoryRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 优惠券领取记录管理Service实现类
 * Created by macro on 2018/11/6.
 */
@Service
public class SmsCouponHistoryServiceImpl extends SmsCouponHistoryRepositoryImpl implements SmsCouponHistoryService {
    @Override
    public Page<SmsCouponHistory> list(Long couponId, Integer useStatus, String orderSn, Integer pageSize, Integer pageNum) {
        Page<SmsCouponHistory> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SmsCouponHistory> lambda = new LambdaQueryWrapper<>();
        if(couponId!=null){
            lambda.eq(SmsCouponHistory::getCouponId, couponId);
        }
        if(useStatus!=null){
            lambda.eq(SmsCouponHistory::getUseStatus, useStatus);
        }
        if(!StringUtils.isEmpty(orderSn)){
            lambda.eq(SmsCouponHistory::getOrderSn, orderSn);
        }
        return page(page, lambda);
    }
}
