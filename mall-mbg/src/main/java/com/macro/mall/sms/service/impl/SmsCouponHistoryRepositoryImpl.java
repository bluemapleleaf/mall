package com.macro.mall.sms.service.impl;

import com.macro.mall.sms.domain.SmsCouponHistoryDetail;
import com.macro.mall.sms.model.SmsCoupon;
import com.macro.mall.sms.model.SmsCouponHistory;
import com.macro.mall.sms.mapper.SmsCouponHistoryMapper;
import com.macro.mall.sms.service.SmsCouponHistoryRepository;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 优惠券使用、领取历史表 服务实现类
 * </p>
 *
 * @author dongjb
 * @since 2020-11-29
 */
@Service
public class SmsCouponHistoryRepositoryImpl extends ServiceImpl<SmsCouponHistoryMapper, SmsCouponHistory> implements SmsCouponHistoryRepository {
    /**
     * 获取优惠券历史详情
     */
    @Autowired
    private SmsCouponHistoryMapper couponHistoryMapper;
    @Override
    public List<SmsCouponHistoryDetail> getDetailList(Long memberId) {
        return couponHistoryMapper.getDetailList(memberId);
    }

    /**
     * 获取指定会员优惠券列表
     */
    @Override
    public List<SmsCoupon> getCouponList(Long memberId, Integer useStatus) {
        return couponHistoryMapper.getCouponList(memberId, useStatus);
    }

}
