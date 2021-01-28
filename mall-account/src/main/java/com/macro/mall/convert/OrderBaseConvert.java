package com.macro.mall.convert;

import com.macro.mall.ams.model.AmsOrderBase;
import com.macro.mall.domain.order.OrderBase;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author dongjb
 * @date 2020/11/19
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderBaseConvert {
    OrderBaseConvert INSTANCE = Mappers.getMapper(OrderBaseConvert.class);

    /**
     * 基础订单do转成po
     *
     * @param order 基础订单do
     * @return 基础订单po
     */
    AmsOrderBase do2po(OrderBase order);

    /**
     * 基础订单po转成do
     *
     * @param order 基础订单po
     * @return 基础订单do
     */
    OrderBase po2do(AmsOrderBase order);

}
