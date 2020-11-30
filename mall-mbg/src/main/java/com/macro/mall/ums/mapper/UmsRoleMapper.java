package com.macro.mall.ums.mapper;

import com.macro.mall.ums.model.UmsRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 后台用户角色表 Mapper 接口
 * </p>
 *
 * @author dongjb
 * @since 2020-11-25
 */
public interface UmsRoleMapper extends BaseMapper<UmsRole> {
    /**
     * 获取用户所有角色
     * @param adminId 用户标识
     * @return 角色列表
     */
    List<UmsRole> getRoleList(@Param("adminId") Long adminId);

}
