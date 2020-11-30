package com.macro.mall.convert;

import com.macro.mall.dto.UmsAdminParam;
import com.macro.mall.ums.model.UmsAdmin;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author dongjb
 * @date 2020/11/19
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminConvert {
    AdminConvert INSTANCE = Mappers.getMapper(AdminConvert.class);

    /**
     * 用户参数对象转换成用户对象
     * @param umsAdminParam 用户参数对象
     * @return 用户对象
     */
    UmsAdmin umsAdmin2UmsAdminParam(UmsAdminParam umsAdminParam);

}
