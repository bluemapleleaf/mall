package com.macro.mall.convert;

import com.macro.mall.ams.model.AmsDicOrdertypeAmontdetailrule;
import com.macro.mall.domain.DicOrdertypeAmontdetailrule;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author dongjb
 * @date 2020/11/19
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrdertypeAmontdetailruleConvert {
    OrdertypeAmontdetailruleConvert INSTANCE = Mappers.getMapper(OrdertypeAmontdetailruleConvert.class);

    /**
     * 资金详细规则po转成do
     *
     * @param amsDicOrdertypeAmontdetailrule 资金详细规则po
     * @return 资金详细规则do
     */
    List<DicOrdertypeAmontdetailrule> amsAmontdetailrules2eAmontdetailrules(List<AmsDicOrdertypeAmontdetailrule> amsDicOrdertypeAmontdetailrule);

}
