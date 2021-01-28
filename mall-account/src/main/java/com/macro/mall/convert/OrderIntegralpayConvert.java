package com.macro.mall.convert;

import com.macro.mall.ams.model.AmsOrderIntegralpay;
import com.macro.mall.ams.model.AmsOrderShopsettlement;
import com.macro.mall.domain.order.OrderIntegralpay;
import com.macro.mall.domain.order.OrderShopsettlement;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author dongjb
 * @date 2020/11/19
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderIntegralpayConvert {
    OrderIntegralpayConvert INSTANCE = Mappers.getMapper(OrderIntegralpayConvert.class);

    /**
     * 积分支付流水do转成po
     *
     * @param order 积分支付订单流水do
     * @return 积分支付订单流水po
     */
    AmsOrderIntegralpay do2po(OrderIntegralpay order);

    OrderIntegralpay po2do(AmsOrderIntegralpay order);
}
