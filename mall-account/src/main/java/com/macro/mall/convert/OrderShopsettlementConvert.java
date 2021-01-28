package com.macro.mall.convert;

import com.macro.mall.ams.model.AmsOrderNonbalancepay;
import com.macro.mall.ams.model.AmsOrderShopsettlement;
import com.macro.mall.domain.order.OrderNonbalancepay;
import com.macro.mall.domain.order.OrderShopsettlement;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author dongjb
 * @date 2020/11/19
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderShopsettlementConvert {
    OrderShopsettlementConvert INSTANCE = Mappers.getMapper(OrderShopsettlementConvert.class);

    /**
     * 商家结算订单流水do转成po
     *
     * @param order 商家结算订单流水do
     * @return 商家结算订单流水po
     */
    AmsOrderShopsettlement do2po(OrderShopsettlement order);

    OrderShopsettlement po2do(AmsOrderShopsettlement order);
}
