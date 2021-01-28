package com.macro.mall.convert;

import com.macro.mall.ams.model.AmsOrderBalancetransfer;
import com.macro.mall.ams.model.AmsOrderNonbalancetransfer;
import com.macro.mall.domain.order.OrderBalancetransfer;
import com.macro.mall.domain.order.OrderNonbalancetransfer;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author dongjb
 * @date 2020/11/19
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderNonbalancetransferConvert {
    OrderNonbalancetransferConvert INSTANCE = Mappers.getMapper(OrderNonbalancetransferConvert.class);

    /**
     * 非余额转账订单流水do转成po
     *
     * @param order 非余额转账订单流水do
     * @return 非余额转账订单流水po
     */
    AmsOrderNonbalancetransfer do2po(OrderNonbalancetransfer order);

    OrderNonbalancetransfer po2do(AmsOrderNonbalancetransfer order);
}
