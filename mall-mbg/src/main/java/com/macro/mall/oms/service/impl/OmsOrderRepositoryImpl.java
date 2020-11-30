package com.macro.mall.oms.service.impl;

import com.macro.mall.oms.domain.OmsOrderDetailPortal;
import com.macro.mall.oms.dto.OmsOrderDetail;
import com.macro.mall.oms.model.OmsOrder;
import com.macro.mall.oms.mapper.OmsOrderMapper;
import com.macro.mall.oms.service.OmsOrderRepository;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author dongjb
 * @since 2020-11-28
 */
@Service
public class OmsOrderRepositoryImpl extends ServiceImpl<OmsOrderMapper, OmsOrder> implements OmsOrderRepository {
    @Autowired
    private OmsOrderMapper orderMapper;

    @Override
    public OmsOrderDetail getDetail(Long id){
        return orderMapper.getDetail(id);
    }

    /**
     * 获取订单及下单商品详情
     */
    @Override
    public OmsOrderDetailPortal getDetailPortal(Long orderId){
        return orderMapper.getDetailPortal(orderId);
    }

    /**
     * 获取超时订单
     * @param minute 超时时间（分）
     */
    @Override
    public List<OmsOrderDetailPortal> getTimeOutOrders(Integer minute){
        return orderMapper.getTimeOutOrders(minute);
    }
}
