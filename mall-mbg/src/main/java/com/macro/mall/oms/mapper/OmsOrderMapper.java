package com.macro.mall.oms.mapper;

import com.macro.mall.oms.domain.OmsOrderDetailPortal;
import com.macro.mall.oms.dto.OmsOrderDetail;
import com.macro.mall.oms.model.OmsOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author dongjb
 * @since 2020-11-28
 */
public interface OmsOrderMapper extends BaseMapper<OmsOrder> {
    /**
     * 获取订单详情
     */
    OmsOrderDetail getDetail(@Param("id") Long id);

    /**
     * 获取订单及下单商品详情
     */
    OmsOrderDetailPortal getDetailPortal(@Param("orderId") Long orderId);

    /**
     * 获取超时订单
     * @param minute 超时时间（分）
     */
    List<OmsOrderDetailPortal> getTimeOutOrders(@Param("minute") Integer minute);

}
