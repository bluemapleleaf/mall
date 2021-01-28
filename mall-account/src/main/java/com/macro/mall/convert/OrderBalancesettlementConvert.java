package com.macro.mall.convert;

import com.macro.mall.ams.model.AmsOrderBalancesettlement;
import com.macro.mall.ams.model.AmsOrderNonbalancepay;
import com.macro.mall.domain.order.OrderBalancesettlement;
import com.macro.mall.domain.order.OrderNonbalancepay;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author dongjb
 * @date 2020/11/19
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderBalancesettlementConvert {
    OrderBalancesettlementConvert INSTANCE = Mappers.getMapper(OrderBalancesettlementConvert.class);

    /**
     * 余额结算订单流水do转成po
     *
     * @param order 余额结算订单流水do
     * @return 余额结算订单流水po
     */
    AmsOrderBalancesettlement do2po(OrderBalancesettlement order);

    OrderBalancesettlement po2do(AmsOrderBalancesettlement order);
}
