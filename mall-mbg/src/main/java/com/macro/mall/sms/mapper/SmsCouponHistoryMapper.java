package com.macro.mall.sms.mapper;

import com.macro.mall.sms.domain.SmsCouponHistoryDetail;
import com.macro.mall.sms.model.SmsCoupon;
import com.macro.mall.sms.model.SmsCouponHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 优惠券使用、领取历史表 Mapper 接口
 * </p>
 *
 * @author dongjb
 * @since 2020-11-29
 */
public interface SmsCouponHistoryMapper extends BaseMapper<SmsCouponHistory> {
    /**
     * 获取优惠券历史详情
     */
    List<SmsCouponHistoryDetail> getDetailList(@Param("memberId") Long memberId);

    /**
     * 获取指定会员优惠券列表
     */
    List<SmsCoupon> getCouponList(@Param("memberId") Long memberId, @Param("useStatus")Integer useStatus);

}
