package com.macro.mall.convert;

import com.macro.mall.ams.model.AmsOrderBalancerefund;
import com.macro.mall.ams.model.AmsOrderNonbalancerefund;
import com.macro.mall.domain.order.OrderBalancerefund;
import com.macro.mall.domain.order.OrderNonbalancerefund;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author dongjb
 * @date 2020/11/19
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderNonBalancerefundConvert {
    OrderNonBalancerefundConvert INSTANCE = Mappers.getMapper(OrderNonBalancerefundConvert.class);

    /**
     * 非余额退款流水do转成po
     *
     * @param order 非余额退款订单流水do
     * @return 非余额退款订单流水po
     */
    AmsOrderNonbalancerefund do2po(OrderNonbalancerefund order);

    OrderNonbalancerefund po2do(AmsOrderNonbalancerefund order);
}
