package com.macro.mall.convert;

import com.macro.mall.ams.model.AmsOrderIntegralpay;
import com.macro.mall.ams.model.AmsOrderIntegralrefund;
import com.macro.mall.domain.order.OrderIntegralpay;
import com.macro.mall.domain.order.OrderIntegralrefund;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author dongjb
 * @date 2020/11/19
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderIntegralrefundConvert {
    OrderIntegralrefundConvert INSTANCE = Mappers.getMapper(OrderIntegralrefundConvert.class);

    /**
     * 积分支付流水do转成po
     *
     * @param order 积分支付订单流水do
     * @return 积分支付订单流水po
     */
    AmsOrderIntegralrefund do2po(OrderIntegralrefund order);

    OrderIntegralrefund po2do(AmsOrderIntegralrefund order);
}
