package com.macro.mall.sms.mapper;

import com.macro.mall.sms.dto.SmsCouponParam;
import com.macro.mall.sms.model.SmsCoupon;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 优惠券表 Mapper 接口
 * </p>
 *
 * @author dongjb
 * @since 2020-11-29
 */
public interface SmsCouponMapper extends BaseMapper<SmsCoupon> {
    /**
     * 获取优惠券详情包括绑定关系
     */
    SmsCouponParam getItem(@Param("id") Long id);
}
