package com.macro.mall.convert;

import com.macro.mall.ams.model.AmsOrderIntegralpay;
import com.macro.mall.ams.model.AmsOrderIntegralrecharge;
import com.macro.mall.domain.order.OrderIntegralpay;
import com.macro.mall.domain.order.OrderIntegralrecharge;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author dongjb
 * @date 2020/11/19
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderIntegralrechargeConvert {
    OrderIntegralrechargeConvert INSTANCE = Mappers.getMapper(OrderIntegralrechargeConvert.class);

    /**
     * 积分充值流水do转成po
     *
     * @param order 积分充值订单流水do
     * @return 积分充值订单流水po
     */
    AmsOrderIntegralrecharge do2po(OrderIntegralrecharge order);

    OrderIntegralrecharge po2do(AmsOrderIntegralrecharge order);
}
