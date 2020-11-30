package com.macro.mall.ums.mapper;

import com.macro.mall.ums.model.UmsMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 后台菜单表 Mapper 接口
 * </p>
 *
 * @author dongjb
 * @since 2020-11-25
 */
public interface UmsMenuMapper extends BaseMapper<UmsMenu> {

    /**
     * 根据后台用户ID获取菜单
     * @param adminId 用户标识
     * @return 菜单列表
     */
    List<UmsMenu> getMenuList(@Param("adminId") Long adminId);

    /**
     * 根据角色ID获取菜单
     * @param roleId 角色标识
     * @return 菜单列表
     */
    List<UmsMenu> getMenuListByRoleId(@Param("roleId") Long roleId);

}
