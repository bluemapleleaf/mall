package com.macro.mall.convert;

import com.macro.mall.ams.model.AmsOrderCurrencypay;
import com.macro.mall.ams.model.AmsOrderCurrencyrefund;
import com.macro.mall.domain.order.OrderCurrencypay;
import com.macro.mall.domain.order.OrderCurrencyrefund;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author dongjb
 * @date 2020/11/19
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderCurrencyrefundConvert {
    OrderCurrencyrefundConvert INSTANCE = Mappers.getMapper(OrderCurrencyrefundConvert.class);

    /**
     * 本币退款流水do转成po
     *
     * @param order 本币退款订单流水do
     * @return 本币退款订单流水po
     */
    AmsOrderCurrencyrefund do2po(OrderCurrencyrefund order);

    OrderCurrencyrefund po2do(AmsOrderCurrencyrefund order);
}
