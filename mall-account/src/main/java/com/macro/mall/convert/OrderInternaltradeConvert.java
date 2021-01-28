package com.macro.mall.convert;

import com.macro.mall.ams.model.AmsOrderInternaltrade;
import com.macro.mall.domain.order.OrderInternaltrade;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author dongjb
 * @date 2020/11/19
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderInternaltradeConvert {
    OrderInternaltradeConvert INSTANCE = Mappers.getMapper(OrderInternaltradeConvert.class);

    /**
     * 内部交易单流水do转成po
     *
     * @param order 内部交易订单流水do
     * @return 内部交易订单流水po
     */
    AmsOrderInternaltrade do2po(OrderInternaltrade order);

    OrderInternaltrade po2do(AmsOrderInternaltrade order);
}
