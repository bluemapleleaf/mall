package com.macro.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.macro.mall.ams.model.*;
import com.macro.mall.ams.service.*;
import com.macro.mall.ams.service.impl.AmsCustAccountRepositoryImpl;
import com.macro.mall.convert.CustAccountConvert;
import com.macro.mall.domain.CustAccount;
import com.macro.mall.enums.AccountTypeEnum;
import com.macro.mall.enums.StatusEnum;
import com.macro.mall.service.CustomerService;
import com.macro.mall.util.CodeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static com.macro.mall.enums.DefaultEnum.ISDEFAULT;
import static com.macro.mall.enums.DefaultEnum.NOTDEFAULT;

/**
 * C户服务
 *
 * @author dongjb
 * @date 2021/01/05
 */
@Slf4j
@Service
public class CustomerServiceImpl extends AmsCustAccountRepositoryImpl implements CustomerService {
    @Autowired
    AmsCustBindinfoRepository amsCustBindinfoRepository;

    @Autowired
    AmsCustSubaccountRepository amsCustSubaccountRepository;

    @Autowired
    AmsCustChangelogRepository amsCustChangelogRepository;

    @Autowired
    AmsUserRepository amsUserRepository;

    @Autowired
    AmsDicAccountTypeRepository amsDicAccountTypeRepository;

    @Autowired
    AmsDicCBindtypeRepository amsDicCBindtypeRepository;
    /**
     * C户用户级别
     */
    private static final int CUST_LEVELS = 100;

    @Override
    public List<AmsCustAccount> findCustAccount() {
        return list();
    }

    @Override
    public CustAccount findCustAccountByCustId(String custid) {
        AmsCustAccount amsCustAccount = getById(custid);
        CustAccount custAccount = CustAccountConvert.INSTANCE.amsCustAccount2CustAccount(amsCustAccount);
        return custAccount;
    }

    @Override
    public CustAccount findCustAccountByTelephone(String telephone) {
        LambdaQueryWrapper<AmsCustAccount> lamda = new LambdaQueryWrapper<>();
        lamda.eq(AmsCustAccount::getTelephone, telephone);
        AmsCustAccount amsCustAccount = getOne(lamda);
        CustAccount custAccount = CustAccountConvert.INSTANCE.amsCustAccount2CustAccount(amsCustAccount);
        return custAccount;
    }

    @Override
    public List<AmsCustBindinfo> findCustBindInfoByCustId(String custid) {
        LambdaQueryWrapper<AmsCustBindinfo> lamda = new LambdaQueryWrapper<>();
        lamda.eq(AmsCustBindinfo::getCustid, custid);
        return amsCustBindinfoRepository.list(lamda);
    }

    @Override
    public AmsCustBindinfo findCustBindInfoById(String id) {
        return amsCustBindinfoRepository.getById(id);
    }

    @Override
    public List<AmsCustSubaccount> findCustSubAccountByCustId(String custid) {
        LambdaQueryWrapper<AmsCustSubaccount> lamda = new LambdaQueryWrapper<>();
        lamda.eq(AmsCustSubaccount::getCustid, custid);
        return amsCustSubaccountRepository.list(lamda);
    }

    @Override
    public List<AmsCustChangelog> findCustChangeLog(String custid, String type, String yearmonth) {
        LambdaQueryWrapper<AmsCustChangelog> lamda = new LambdaQueryWrapper<>();
        lamda.eq(AmsCustChangelog::getCustid, custid)
                .eq(AmsCustChangelog::getType, type)
                .eq(AmsCustChangelog::getChangedate, yearmonth);
        return amsCustChangelogRepository.list(lamda);
    }

    @Override
    public AmsCustAccount doCreateCustAccount(AmsCustAccount custAccount) {
        //进行用户重复校验
        AmsCustAccount custAccountOld = getById(custAccount.getTelephone());
        //C户不存在需要创建
        if (custAccountOld == null) {
            String channel = custAccount.getChannel();
            String regioncode = channel.substring(0, 6);
            String custid = CodeFactory.generateCustId(regioncode);
            if (StringUtils.isEmpty(custid)) {
                return null;
            }
            custAccount.setCustid(custid);
            custAccount.setCreatedate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
            custAccount.setUserid(custAccount.getUserid());
            custAccount.setStatus(StatusEnum.ACTIVATE.getValue());
            if (save(custAccount)) {
                log.info("C户创建成功！");
                LambdaQueryWrapper<AmsCustSubaccount> lambda = new LambdaQueryWrapper<>();
                lambda.eq(AmsCustSubaccount::getCustid, custid);
                List<AmsCustSubaccount> custSubAccounts = amsCustSubaccountRepository.list(lambda);
                if (custSubAccounts.isEmpty()) {
                    log.info("正在创建子账户信息...");

                    for (AccountTypeEnum accountType : AccountTypeEnum.values()) {
                        String code = CodeFactory.generateCustSubCode(custid, accountType);
                        AmsCustSubaccount subAccount = new AmsCustSubaccount();
                        subAccount.setCustid(custid);
                        subAccount.setCode(code);
                        subAccount.setStatus(StatusEnum.ACTIVATE.getValue());
                        subAccount.setType(accountType.getType());
                        subAccount.setCreatedate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
                        subAccount.setBalance(BigDecimal.ZERO);
                        subAccount.setMoney(BigDecimal.ZERO);

                        if(amsCustSubaccountRepository.save(subAccount)) {
                            log.info("创建子账户：" + accountType.getName() + "(" + accountType.getType() + ") >> 成功!");
                        } else {
                            log.info("创建子账户：" + accountType.getName() + "(" + accountType.getType() + ") >> 失败!");
                        }
                        custSubAccounts.add(subAccount);
                    }
                }
                custAccount.setSubaccount(custSubAccounts);

                log.info("创建用户！");
                AmsUser user = new AmsUser();
                user.setId(custAccount.getCustid());
                user.setName(custAccount.getNickname());
                user.setLoginno(custAccount.getTelephone());
                user.setPassword(custAccount.getPassword());
                user.setLevels(CUST_LEVELS);
                user.setStatus(StatusEnum.ACTIVATE.getValue());
                user.setSort(CUST_LEVELS);

                if(amsUserRepository.save(user)) {
                    log.info("创建用户成功");
                }else {
                    log.info("创建用户失败");
                }

            } else {
                log.error("C户创建失败!");
                return null;
            }
        } else {
            return custAccountOld;
        }
        return custAccount;

    }

    @Override
    public boolean doModifyTelephone(AmsCustAccount custAccount) {
        AmsCustAccount custAccountOld = getById(custAccount.getCustid());
        if (custAccountOld != null) {
            if (!custAccountOld.getTelephone().equals(custAccount.getTelephone())) {
                log.error("原手机号不正确");
            }
            custAccount.setUserid(custAccountOld.getUserid());
        } else {
            log.error("客户信息不存在：" + custAccount.getCustid());
        }
        AmsUser user = amsUserRepository.getById(custAccount.getUserid());
        if (user.getPassword().equals(custAccount.getLoginpwd())) {
            user.setLoginno(custAccount.getTelephonenew());
            user.setPassword(custAccount.getLoginpwdnew());
            custAccount.setTelephone(custAccount.getTelephonenew());
            return updateById(custAccount) && amsUserRepository.updateById(user);
        } else {
            log.info("原登录密码不正确");
            return false;
        }
    }

    @Override
    public boolean doAddCustBindInfo(AmsCustBindinfo custBindInfo) {
        AmsCustAccount custAccount =getById(custBindInfo.getCustid());
        if (custAccount == null) {
            log.error("客户信息不存在：" + custBindInfo.getCustid());
        }
        return amsCustBindinfoRepository.save(custBindInfo);
    }

    @Override
    public boolean doDelCustBinfInfo(String ids) {
        String[] iDs = ids.split(",");
        return amsCustBindinfoRepository.removeByIds(Arrays.asList(iDs.clone()));
    }

    @Override
    public boolean setDefaultCustBindInfo(AmsCustBindinfo custBindinfo) {
        AmsCustAccount custAccount = getById(custBindinfo.getCustid());
        if (custAccount == null) {
            log.error("客户信息不存在：" + custBindinfo.getCustid());
            return false;
        }
        LambdaUpdateWrapper<AmsCustBindinfo> lamda = new LambdaUpdateWrapper<>();
        lamda.eq(AmsCustBindinfo::getCustid, custBindinfo.getCustid())
                .eq(AmsCustBindinfo::getType, custBindinfo.getType())
                .eq(AmsCustBindinfo::getIsdefault, ISDEFAULT.getValue())
                .set(AmsCustBindinfo::getIsdefault, NOTDEFAULT.getValue());
        amsCustBindinfoRepository.update(lamda);

        return amsCustBindinfoRepository.updateById(custBindinfo);
    }

    @Override
    public boolean doSetCustPayPassword(AmsCustAccount custAccount) {
        AmsCustAccount custAccountOld = getById(custAccount.getCustid());
        if (custAccountOld == null) {
            log.error("客户信息不存在：" + custAccount.getCustid());
            return false;
        }
        if (StringUtils.isEmpty(custAccountOld.getPassword())) {
            custAccountOld.setPassword(custAccount.getPassword());
            updateById(custAccountOld);
            return true;
        } else {
            log.warn("已设置支付密码，请使用支付密码修改!");
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean doModCustPayPassword(CustAccount custAccount) {
        AmsCustAccount custAccountOld = getById(custAccount.getCustid());
        if (custAccountOld == null) {
            log.error("客户信息不存在：" + custAccount.getCustid());
            return false;
        }
        if (doValidatePayPassword(custAccount)) {
            custAccount.setPassword(custAccount.getPasswordnew());
            if (updateById(CustAccountConvert.INSTANCE.custAccount2AmsCustAccount(custAccount))) {
                log.info("修改支付密码成功");
                return true;
            } else {
                log.error("修改支付密码失败");
                return false;
            }
        } else {
            log.error("原支付密码不正确");
            return false;
        }
    }

    @Override
    public boolean doValidatePayPassword(CustAccount custAccount) {
        AmsCustAccount custAccountOld = getById(custAccount.getCustid());
        if (custAccountOld == null) {
            log.error("客户信息不存在：" + custAccount.getCustid());
            return false;
        }
        return custAccount.getPassword().equals(custAccountOld.getPassword());
    }

    @Override
    public List<AmsDicCBindtype> getallcbindtype() {
        return amsDicCBindtypeRepository.list();
    }

    @Override
    public boolean updateCustSubAccount(AmsCustSubaccount subaccount) {
        return amsCustSubaccountRepository.updateById(subaccount);
    }

    @Override
    public boolean createCustChangelog(AmsCustChangelog changeLog) {
        return amsCustChangelogRepository.save(changeLog);
    }

    @Override
    public AmsUser findUserById(String userId) {
        return amsUserRepository.getById(userId);
    }

}
