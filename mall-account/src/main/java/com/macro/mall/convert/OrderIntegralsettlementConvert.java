package com.macro.mall.convert;

import com.macro.mall.ams.model.AmsOrderIntegralrefund;
import com.macro.mall.ams.model.AmsOrderIntegralsettlement;
import com.macro.mall.domain.order.OrderIntegralrefund;
import com.macro.mall.domain.order.OrderIntegralsettlement;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author dongjb
 * @date 2020/11/19
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderIntegralsettlementConvert {
    OrderIntegralsettlementConvert INSTANCE = Mappers.getMapper(OrderIntegralsettlementConvert.class);

    /**
     * 积分支付流水do转成po
     *
     * @param order 积分支付订单流水do
     * @return 积分支付订单流水po
     */
    AmsOrderIntegralsettlement do2po(OrderIntegralsettlement order);

    OrderIntegralsettlement po2do(AmsOrderIntegralsettlement order);
}
