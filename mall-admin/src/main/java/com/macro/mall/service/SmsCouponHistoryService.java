package com.macro.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.sms.model.SmsCouponHistory;
import com.macro.mall.sms.service.SmsCouponHistoryRepository;

import java.util.List;

/**
 * 优惠券领取记录管理Service
 * Created by macro on 2018/11/6.
 */
public interface SmsCouponHistoryService extends SmsCouponHistoryRepository {
    /**
     * 分页查询优惠券领取记录
     * @param couponId 优惠券id
     * @param useStatus 使用状态
     * @param orderSn 使用订单号码
     */
    Page<SmsCouponHistory> list(Long couponId, Integer useStatus, String orderSn, Integer pageSize, Integer pageNum);
}
