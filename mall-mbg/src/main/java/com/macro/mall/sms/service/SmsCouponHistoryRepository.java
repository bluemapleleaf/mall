package com.macro.mall.sms.service;

import com.macro.mall.sms.domain.SmsCouponHistoryDetail;
import com.macro.mall.sms.model.SmsCoupon;
import com.macro.mall.sms.model.SmsCouponHistory;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 优惠券使用、领取历史表 服务类
 * </p>
 *
 * @author dongjb
 * @since 2020-11-29
 */
public interface SmsCouponHistoryRepository extends IService<SmsCouponHistory> {
    /**
     * 获取优惠券历史详情
     */
    List<SmsCouponHistoryDetail> getDetailList(Long memberId);

    /**
     * 获取指定会员优惠券列表
     */
    List<SmsCoupon> getCouponList(Long memberId, Integer useStatus);

}
