package com.macro.mall.service;

import com.macro.mall.ams.model.AmsBusinessBindinfo;
import com.macro.mall.ams.model.AmsBusinessSubaccount;
import com.macro.mall.ams.model.AmsDicBBindtype;
import com.macro.mall.ams.service.AmsBusinessAccountRepository;
import com.macro.mall.domain.BusinessAccount;

import java.util.List;

/**
 * B户服务接口
 *
 * @author dongjb
 * @date 2021/01/06
 */
public interface BusinessService extends AmsBusinessAccountRepository {

    /**
     * 通过id获取B户校验key
     *
     * @param id B户id
     * @return B户校验key
     */
    String getBusinessKeyById(String id);

    /**
     * 获取B户列表
     *
     * @return B户列表
     */
    List<BusinessAccount> findBusinessAccount();

    /**
     * 通过id获取B户信息
     *
     * @param id B户id
     * @return B户账户信息
     */
    BusinessAccount findBusinessAccountById(String id);

    /**
     * 通过客户号获取B户信息
     *
     * @param businessId B户客户号
     * @return B户账户信息
     */
    BusinessAccount findBusinessAccountByBusinessId(String businessId);

    /**
     * 获取B户默认结算绑定信息
     *
     * @param businessid 账户类型编码
     * @return 结果集
     */
    AmsBusinessBindinfo findBusinessDefaultBindInfoByBusinessId(String businessid);

    /**
     * 根据绑定id获取绑定信息
     *
     * @param bindInfoId 绑定id
     * @return
     */
    AmsBusinessBindinfo findBusinessBindInfoById(String bindInfoId);

    /**
     * 获取运营B户信息
     *
     * @return B户信息
     */
    BusinessAccount findOperBusinessAccount();

    /**
     * 获取运营B户绑定类型
     *
     * @return 结果集
     */
    List<AmsDicBBindtype> findOperBusinessBindInfoType();

    /**
     * 获取运营B户绑定信息
     *
     * @param typecode 账户类型编码
     * @return 结果集
     */
    AmsBusinessBindinfo findOperBusinessBindInfo(String typecode);

    /**
     * 根据账户B账户标识获取子账户信息
     *
     * @param bussinessId B户客户号
     * @return 子账户信息
     */
    List<AmsBusinessSubaccount> findBusinessSubaccountByBusinessid(String bussinessId);


    /**
     * 更新子账户信息
     *
     * @param subaccount 子账户对象
     * @return
     */
    boolean updateBusinessSubAccount(AmsBusinessSubaccount subaccount);

}