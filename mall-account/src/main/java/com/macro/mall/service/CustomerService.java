package com.macro.mall.service;

import com.macro.mall.ams.model.*;
import com.macro.mall.ams.service.AmsCustAccountRepository;
import com.macro.mall.domain.CustAccount;

import java.util.List;

/**
 * C户服务接口
 *
 * @author dongjb
 * @date 2021/01/05
 */
public interface CustomerService extends AmsCustAccountRepository {

    /**
     * 获取C户列表
     *
     * @return C户列表
     */
    List<AmsCustAccount> findCustAccount();

    /**
     * 根据custid获取C户信息
     *
     * @param custid 客户号
     * @return C户对象
     */
    CustAccount findCustAccountByCustId(String custid);

    /**
     * 根据电话号码查询C户
     *
     * @param telephone 手机号
     * @return C户对象
     */
    CustAccount findCustAccountByTelephone(String telephone);

    /**
     * 根据C户客户号获取绑定信息
     *
     * @param custid 客户号
     * @return 绑定信息
     */

    List<AmsCustBindinfo> findCustBindInfoByCustId(String custid);

    /**
     * 根据id获取C户绑定信息
     *
     * @param id 绑定信息id
     * @return 绑定信息
     */

    AmsCustBindinfo findCustBindInfoById(String id);

    /**
     * 根据客户id获取子账户列表
     *
     * @param custid 客户号
     * @return 结果集
     */
    List<AmsCustSubaccount> findCustSubAccountByCustId(String custid);

    /**
     * 获取指定年月的虚拟账户余额变动记录
     *
     * @param custid    C户客户号
     * @param type      虚拟账户类型
     * @param yearmonth 年月
     * @return 余额变动记录
     */
    List<AmsCustChangelog> findCustChangeLog(String custid, String type, String yearmonth);

    /**
     * 创建C户
     *
     * @param custAccount 参数对象
     * @return C户对象
     */
    AmsCustAccount doCreateCustAccount(AmsCustAccount custAccount);

    /**
     * C户手机号变更
     *
     * @param custAccount 参数对象
     * @return C户对象
     */
    boolean doModifyTelephone(AmsCustAccount custAccount);

    /**
     * C户添加绑定信息
     *
     * @param custBindInfo 绑定信息
     * @return 处理结果 true or false
     */
    boolean doAddCustBindInfo(AmsCustBindinfo custBindInfo);

    /**
     * C户删除绑定信息
     *
     * @param ids 绑定信息id
     * @return 处理结果 true or false
     */
    boolean doDelCustBinfInfo(String ids);

    /**
     * 绑定信息设置为默认
     *
     * @param custBindinfo 参数对象
     * @return 处理结果 true or false
     */
    boolean setDefaultCustBindInfo(AmsCustBindinfo custBindinfo);

    /**
     * 设置C户支付密码
     *
     * @param custAccount C户对象
     * @return 处理结果 true or false
     */
    boolean doSetCustPayPassword(AmsCustAccount custAccount);

    /**
     * 修改C户支付密码
     *
     * @param custAccount C户对象
     * @return 处理结果 true|false
     */
    boolean doModCustPayPassword(CustAccount custAccount);

    /**
     * 验证C户支付密码
     *
     * @param custAccount custAccount C户对象
     * @return 处理结果 true or false
     */
    boolean doValidatePayPassword(CustAccount custAccount);

    /**
     * 获取全部绑定类型(C户)
     *
     * @return 绑定类型列表
     */
    List<AmsDicCBindtype> getallcbindtype();

    /**
     * 更新子账户信息
     *
     * @param subaccount 子账户对象
     * @return 是否更新成功
     */
    boolean updateCustSubAccount(AmsCustSubaccount subaccount);

    /**
     * 创建客户变更日志
     *
     * @param changeLog 变更日志对象
     * @return 是否成功
     */
    boolean createCustChangelog(AmsCustChangelog changeLog);

    /**
     * 根据id查询用户信息
     *
     * @param userId 用户标识
     * @return 用户对象
     */
    AmsUser findUserById(String userId);

}
