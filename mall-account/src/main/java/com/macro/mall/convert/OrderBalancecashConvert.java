package com.macro.mall.convert;

import com.macro.mall.ams.model.AmsOrderBalancecash;
import com.macro.mall.ams.model.AmsOrderBalancetransfer;
import com.macro.mall.domain.order.OrderBalancecash;
import com.macro.mall.domain.order.OrderBalancetransfer;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author dongjb
 * @date 2020/11/19
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderBalancecashConvert {
    OrderBalancecashConvert INSTANCE = Mappers.getMapper(OrderBalancecashConvert.class);

    /**
     * 余额提现订单流水do转成po
     *
     * @param order 余额提现订单流水do
     * @return 余额提现订单流水po
     */
    AmsOrderBalancecash do2po(OrderBalancecash order);

    OrderBalancecash po2do(AmsOrderBalancecash order);
}
