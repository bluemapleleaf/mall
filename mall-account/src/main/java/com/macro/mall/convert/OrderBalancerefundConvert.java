package com.macro.mall.convert;

import com.macro.mall.ams.model.AmsOrderBalancerefund;
import com.macro.mall.ams.model.AmsOrderNonbalancepay;
import com.macro.mall.domain.order.OrderBalancerefund;
import com.macro.mall.domain.order.OrderNonbalancepay;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author dongjb
 * @date 2020/11/19
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderBalancerefundConvert {
    OrderBalancerefundConvert INSTANCE = Mappers.getMapper(OrderBalancerefundConvert.class);

    /**
     * 余额退款流水do转成po
     *
     * @param order 余额退款订单流水do
     * @return 余额退款订单流水po
     */
    AmsOrderBalancerefund do2po(OrderBalancerefund order);

    OrderBalancerefund po2do(AmsOrderBalancerefund order);
}
