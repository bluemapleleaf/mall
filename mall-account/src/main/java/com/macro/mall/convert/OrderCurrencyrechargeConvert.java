package com.macro.mall.convert;

import com.macro.mall.ams.model.AmsOrderCurrencyrecharge;
import com.macro.mall.ams.model.AmsOrderCurrencyrefund;
import com.macro.mall.domain.order.OrderCurrencyrecharge;
import com.macro.mall.domain.order.OrderCurrencyrefund;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author dongjb
 * @date 2020/11/19
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderCurrencyrechargeConvert {
    OrderCurrencyrechargeConvert INSTANCE = Mappers.getMapper(OrderCurrencyrechargeConvert.class);

    /**
     * 本币充值流水do转成po
     *
     * @param order 本币充值订单流水do
     * @return 本币充值订单流水po
     */
    AmsOrderCurrencyrecharge do2po(OrderCurrencyrecharge order);

    OrderCurrencyrecharge po2do(AmsOrderCurrencyrecharge order);
}
