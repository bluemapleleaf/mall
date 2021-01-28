package com.macro.mall.convert;

import com.macro.mall.ams.model.AmsOrderNonbalancepay;
import com.macro.mall.domain.order.OrderNonbalancepay;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author dongjb
 * @date 2020/11/19
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderNonBalancepayConvert {
    OrderNonBalancepayConvert INSTANCE = Mappers.getMapper(OrderNonBalancepayConvert.class);

    /**
     * 非余额订单流水do转成po
     *
     * @param order 非余额订单流水do
     * @return 非余额订单流水po
     */
    AmsOrderNonbalancepay do2po(OrderNonbalancepay order);

    OrderNonbalancepay po2do(AmsOrderNonbalancepay order);
}
