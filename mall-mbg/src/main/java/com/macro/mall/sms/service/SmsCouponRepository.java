package com.macro.mall.sms.service;

import com.macro.mall.sms.dto.SmsCouponParam;
import com.macro.mall.sms.model.SmsCoupon;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 优惠券表 服务类
 * </p>
 *
 * @author dongjb
 * @since 2020-11-29
 */
public interface SmsCouponRepository extends IService<SmsCoupon> {
    /**
     * 获取优惠券详情包括绑定关系
     */
    SmsCouponParam getItem(Long id);
}
