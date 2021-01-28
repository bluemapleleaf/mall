package com.macro.mall.convert;

import com.macro.mall.ams.model.AmsOrderInternaltrade;
import com.macro.mall.ams.model.AmsOrderReviewrecord;
import com.macro.mall.domain.order.OrderInternaltrade;
import com.macro.mall.domain.order.OrderReviewrecord;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author dongjb
 * @date 2020/11/19
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderReviewrecordConvert {
    OrderReviewrecordConvert INSTANCE = Mappers.getMapper(OrderReviewrecordConvert.class);

    /**
     * 订单审核流水do转成po
     *
     * @param order 订单审核单流水do
     * @return 订单审核流水po
     */
    AmsOrderReviewrecord do2po(OrderReviewrecord order);

    OrderReviewrecord po2do(AmsOrderReviewrecord order);
}
