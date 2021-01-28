package com.macro.mall.convert;

import com.macro.mall.ams.model.AmsOrderBalancetransfer;
import com.macro.mall.ams.model.AmsOrderNonbalancepay;
import com.macro.mall.domain.order.OrderBalancetransfer;
import com.macro.mall.domain.order.OrderNonbalancepay;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author dongjb
 * @date 2020/11/19
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderBalancetransferConvert {
    OrderBalancetransferConvert INSTANCE = Mappers.getMapper(OrderBalancetransferConvert.class);

    /**
     * 余额转账订单流水do转成po
     *
     * @param order 余额转账订单流水do
     * @return 余额转账订单流水po
     */
    AmsOrderBalancetransfer do2po(OrderBalancetransfer order);

    OrderBalancetransfer po2do(AmsOrderBalancetransfer order);
}
