package com.macro.mall.convert;

import com.macro.mall.dto.UmsMenuNode;
import com.macro.mall.ums.model.UmsMenu;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author dongjb
 * @date 2020/11/19
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuConvert {
    MenuConvert INSTANCE = Mappers.getMapper(MenuConvert.class);

    /**
     * 后台菜单转换成菜单节点封装
     * @param umsMenu 后台菜单
     * @return 菜单节点封装
     */
    UmsMenuNode umsMenu2UmsMenuNode(UmsMenu umsMenu);

}
