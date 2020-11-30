package com.macro.mall.oms.service;

import com.macro.mall.oms.domain.OmsOrderDetailPortal;
import com.macro.mall.oms.dto.OmsOrderDetail;
import com.macro.mall.oms.model.OmsOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author dongjb
 * @since 2020-11-28
 */
public interface OmsOrderRepository extends IService<OmsOrder> {
    /**
     * 获取订单详情
     */
    OmsOrderDetail getDetail(Long id);

    /**
     * 获取订单及下单商品详情
     */
    OmsOrderDetailPortal getDetailPortal(Long orderId);

    /**
     * 获取超时订单
     * @param minute 超时时间（分）
     */
    List<OmsOrderDetailPortal> getTimeOutOrders(Integer minute);

}
