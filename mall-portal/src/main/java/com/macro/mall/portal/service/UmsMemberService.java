package com.macro.mall.portal.service;

import com.macro.mall.ums.model.UmsMember;
import com.macro.mall.ums.service.UmsMemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

/**
 * 会员管理Service
 *
 * @author dongjb
 * @date 2020/11/30
 */
public interface UmsMemberService extends UmsMemberRepository {

    /**
     * 根据用户名获取会员
     * @param username 用户名称
     * @return 会员
     */
    UmsMember getByUsername(String username);

    /**
     * 根据会员编号获取会员
     * @param id 用户标识
     * @return 会员
     */
    UmsMember getItemById(Long id);

    /**
     * 用户注册
     * @param username 用户名称
     * @param password 密码
     * @param telephone 手机号码
     * @param authCode 验证码
     */
    @Transactional(rollbackFor = Exception.class)
    void register(String username, String password, String telephone, String authCode);

    /**
     * 生成验证码
     * @param telephone 手机号码
     * @return 生成验证码
     */
    String generateAuthCode(String telephone);

    /**
     * 修改密码
     * @param telephone 手机号码
     * @param password 密码
     * @param authCode 验证码
     */
    @Transactional(rollbackFor = Exception.class)
    void updatePassword(String telephone, String password, String authCode);

    /**
     * 获取当前登录会员
     * @return 会员
     */
    UmsMember getCurrentMember();

    /**
     * 根据会员id修改会员积分
     * @param id 会员标识
     * @param integration 积分
     */
    void updateIntegration(Long id,Integer integration);

    /**
     * 获取用户信息
     * @param username 会员名称
     * @return 会员对象
     */
    UserDetails loadUserByUsername(String username);

    /**
     * 登录后获取token
     * @param username 会员名称
     * @param password 密码
     * @return token
     */
    String login(String username, String password);

    /**
     * 刷新token
     * @param token 旧token
     * @return 新token
     */
    String refreshToken(String token);
}
