package com.macro.mall.convert;

import com.macro.mall.ams.model.AmsOrderBalancerecharge;
import com.macro.mall.domain.order.OrderBalancerecharge;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author dongjb
 * @date 2020/11/19
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderBalancerechargeConvert {
    OrderBalancerechargeConvert INSTANCE = Mappers.getMapper(OrderBalancerechargeConvert.class);

    /**
     * 充值订单流水do转成po
     *
     * @param order 充值订单流水do
     * @return 充值订单流水po
     */
    AmsOrderBalancerecharge do2po(OrderBalancerecharge order);

}
