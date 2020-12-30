package com.macro.mall.portal.service;

import com.macro.mall.ums.model.UmsMember;

/**
 * 会员信息缓存业务类
 *
 * @author dongjb
 * @date 2020/11/30
 */
public interface UmsMemberCacheService {

    /**
     * 删除会员用户缓存
     * @param memberId 会员id
     */
    void delMember(Long memberId);

    /**
     * 获取会员用户缓存
     * @param username 会员名称
     * @return 会员
     */
    UmsMember getMember(String username);

    /**
     * 设置会员用户缓存
     * @param member 会员
     */
    void setMember(UmsMember member);

    /**
     * 设置验证码
     * @param telephone 手机号码
     * @param authCode 验证码
     */
    void setAuthCode(String telephone, String authCode);

    /**
     * 获取验证码
     * @param telephone 手机号码
     * @return 验证码
     */
    String getAuthCode(String telephone);
}
