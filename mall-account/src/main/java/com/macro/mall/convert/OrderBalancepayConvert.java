package com.macro.mall.convert;

import com.macro.mall.ams.model.AmsOrderBalancepay;
import com.macro.mall.ams.model.AmsOrderBalancerecharge;
import com.macro.mall.domain.order.OrderBalancepay;
import com.macro.mall.domain.order.OrderBalancerecharge;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author dongjb
 * @date 2020/11/19
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderBalancepayConvert {
    OrderBalancepayConvert INSTANCE = Mappers.getMapper(OrderBalancepayConvert.class);

    /**
     * 支付订单流水do转成po
     *
     * @param order 支付订单流水do
     * @return 支付订单流水po
     */
    AmsOrderBalancepay do2po(OrderBalancepay order);

}
