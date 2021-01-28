package com.macro.mall.convert;

import com.macro.mall.ams.model.AmsCustAccount;
import com.macro.mall.domain.CustAccount;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author dongjb
 * @date 2020/11/19
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustAccountConvert {
    CustAccountConvert INSTANCE = Mappers.getMapper(CustAccountConvert.class);

    /**
     * C户账户po转成do
     *
     * @param amsCustAccount C户账户po
     * @return C户账户do
     */
    CustAccount amsCustAccount2CustAccount(AmsCustAccount amsCustAccount);

    /**
     * C户账户do转成po
     *
     * @param custAccount C户账户do
     * @return C户账户po
     */
    AmsCustAccount custAccount2AmsCustAccount(CustAccount custAccount);

}
