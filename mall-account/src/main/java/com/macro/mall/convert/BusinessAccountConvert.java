package com.macro.mall.convert;

import com.macro.mall.ams.model.AmsBusinessAccount;
import com.macro.mall.domain.BusinessAccount;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author dongjb
 * @date 2020/11/19
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BusinessAccountConvert {
    BusinessAccountConvert INSTANCE = Mappers.getMapper(BusinessAccountConvert.class);

    /**
     * B户账户po转成do
     *
     * @param amsBusinessAccount B户账户po
     * @return B户账户do
     */
    BusinessAccount amsBusinessAccount2BusinessAccount(AmsBusinessAccount amsBusinessAccount);

}
