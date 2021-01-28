package com.macro.mall.convert;

import com.macro.mall.ams.model.AmsOrderCurrencypay;
import com.macro.mall.ams.model.AmsOrderIntegralrecharge;
import com.macro.mall.domain.order.OrderCurrencypay;
import com.macro.mall.domain.order.OrderIntegralrecharge;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author dongjb
 * @date 2020/11/19
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderCurrencypayConvert {
    OrderCurrencypayConvert INSTANCE = Mappers.getMapper(OrderCurrencypayConvert.class);

    /**
     * 本币缴费流水do转成po
     *
     * @param order 本币缴费订单流水do
     * @return 本币缴费订单流水po
     */
    AmsOrderCurrencypay do2po(OrderCurrencypay order);

    OrderCurrencypay po2do(AmsOrderCurrencypay order);
}
