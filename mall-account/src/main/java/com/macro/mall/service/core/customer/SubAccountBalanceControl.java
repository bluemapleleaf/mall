package com.macro.mall.service.core.customer;

import com.macro.mall.ams.model.AmsBusinessSubaccount;
import com.macro.mall.ams.model.AmsCustChangelog;
import com.macro.mall.ams.model.AmsCustSubaccount;
import com.macro.mall.domain.result.AccountResult;
import com.macro.mall.enums.ChangeAccountTypeEnum;
import com.macro.mall.enums.ChangeBalanceDirectEnum;
import com.macro.mall.enums.ResultStatusEnum;
import com.macro.mall.service.BusinessService;
import com.macro.mall.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 余额控制
 *
 * @author dongjb
 * @date 2021/01/17
 */
@Slf4j
class SubAccountBalanceControl {

    @Autowired
    static BusinessService businessService;

    @Autowired
    static CustomerService customerService;

    static AccountResult result = new AccountResult();

    /**
     * 获取虚拟账户余额
     *
     * @param customerid            客户号
     * @param changeAccountTypeEnum 账户类型
     * @param accountTypeCode       虚拟账户类型编码
     * @return 余额
     */
    static BigDecimal getSubAccountBalance(String customerid, ChangeAccountTypeEnum changeAccountTypeEnum, String accountTypeCode) {
        BigDecimal result = BigDecimal.ZERO;
        Map<String, Object> param = new HashMap<>(8);
        switch (changeAccountTypeEnum) {
            case Account:
            case Business:
            case DisBusiness:
                Optional<AmsBusinessSubaccount> optionalBussinessSubaccount =
                        businessService.findBusinessSubaccountByBusinessid(customerid).stream()
                        .filter(subAccount -> subAccount.getType().equals(accountTypeCode))
                        .findFirst();
                if(optionalBussinessSubaccount.isPresent()) {
                    result = optionalBussinessSubaccount.get().getBalance();
                }
                break;
            case Cust:
            case DisCust:
                Optional<AmsCustSubaccount> optionalCustSubaccount =
                        customerService.findCustSubAccountByCustId(customerid).stream()
                        .filter(subAccount -> subAccount.getType().equals(accountTypeCode))
                        .findFirst();
                if(optionalCustSubaccount.isPresent()) {
                    result = optionalCustSubaccount.get().getBalance();
                }
                break;
            default:
                log.info("默认路径");
        }
        return result;
    }

    /**
     * 修改虚拟账户余额
     *
     * @param customerid              客户号
     * @param amont                   变动额
     * @param actamont                实际变动金额
     * @param changeAccountTypeEnum   账户类型
     * @param accountTypeCode         虚拟账户编码
     * @param changeBalanceDirectEnum 变动方向
     * @return 记账结果对象
     */
    static AccountResult changeBalance(String customerid, BigDecimal amont, BigDecimal actamont, ChangeAccountTypeEnum changeAccountTypeEnum, String accountTypeCode, ChangeBalanceDirectEnum changeBalanceDirectEnum) {
        AccountResult result = new AccountResult();
        switch (changeAccountTypeEnum) {
            case Account:
            case Business:
            case DisBusiness:
                Optional<AmsBusinessSubaccount> optionalBusinessSubaccount   =
                        businessService.findBusinessSubaccountByBusinessid(customerid).stream()
                        .filter(subAccount -> subAccount.getType().equals(accountTypeCode))
                        .findFirst();
                if(optionalBusinessSubaccount.isPresent()) {
                    return doBusinessChange(optionalBusinessSubaccount.get(), amont, actamont, changeBalanceDirectEnum);
                }
            case Cust:
            case DisCust:
                Optional<AmsCustSubaccount> optionalcustSubaccount  =
                        customerService.findCustSubAccountByCustId(customerid).stream()
                        .filter(subAccount -> subAccount.getType().equals(accountTypeCode))
                        .findFirst();
                if(optionalcustSubaccount.isPresent()) {
                    return doCustChange(optionalcustSubaccount.get(), amont, actamont, changeBalanceDirectEnum);
                }
            default:
                log.info("默认路径");
        }
        return null;
    }

    /**
     * 变更账户余额
     *
     * @param subAccount              虚拟账户对象
     * @param amont                   变动额
     * @param actamont                实际变动金额
     * @param changeBalanceDirectEnum 变动方向
     * @return 记账结果对象
     */
    private static AccountResult doBusinessChange(AmsBusinessSubaccount subAccount, BigDecimal amont, BigDecimal actamont, ChangeBalanceDirectEnum changeBalanceDirectEnum) {
        result.setBeforbalance(subAccount.getBalance());
        result.setBeformoney(subAccount.getMoney());
        switch (changeBalanceDirectEnum) {
            case Plus:
                subAccount.setBalance(subAccount.getBalance().add(amont));
                subAccount.setMoney(subAccount.getMoney().add(actamont));
                break;
            case Minus:
                subAccount.setBalance(subAccount.getBalance().subtract(amont));
                subAccount.setMoney(subAccount.getMoney().subtract(actamont));
                break;
            default:
                result.setStatus(ResultStatusEnum.failed);
                result.setMessage("余额操作非法！");
                return result;
        }

        if(! businessService.updateBusinessSubAccount(subAccount)) {
            log.error("修改账户余额失败：code=" + subAccount.getCode());
            result.setStatus(ResultStatusEnum.failed);
            result.setMessage("修改账户余额失败");
        } else {
            result.setAfterbalance(subAccount.getBalance());
            result.setAftermoney(subAccount.getMoney());
            result.setStatus(ResultStatusEnum.success);
        }
        return result;
    }

    /**
     * 变更账户余额
     *
     * @param subAccount              虚拟账户对象
     * @param amont                   变动额
     * @param actamont                实际变动金额
     * @param changeBalanceDirectEnum 变动方向
     * @return 记账结果对象
     */
    private static AccountResult doCustChange(AmsCustSubaccount subAccount, BigDecimal amont, BigDecimal actamont, ChangeBalanceDirectEnum changeBalanceDirectEnum) {
        result.setBeforbalance(subAccount.getBalance());
        result.setBeformoney(subAccount.getMoney());
        switch (changeBalanceDirectEnum) {
            case Plus:
                subAccount.setBalance(subAccount.getBalance().add(amont));
                subAccount.setMoney(subAccount.getMoney().add(actamont));
                break;
            case Minus:
                subAccount.setBalance(subAccount.getBalance().subtract(amont));
                subAccount.setMoney(subAccount.getMoney().subtract(actamont));
                break;
            default:
                result.setStatus(ResultStatusEnum.failed);
                result.setMessage("余额操作非法！");
                return result;
        }

        if (!customerService.updateCustSubAccount(subAccount)) {
            log.error("修改账户余额失败：code=" + subAccount.getCode());
            result.setStatus(ResultStatusEnum.failed);
            result.setMessage("修改账户余额失败");
        } else {
            AmsCustChangelog custChangeLog = new AmsCustChangelog();
            custChangeLog.setCustid(subAccount.getCustid());
            custChangeLog.setType(subAccount.getType());
            custChangeLog.setChangedate(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
            custChangeLog.setChangedirect(changeBalanceDirectEnum.getValue());
            custChangeLog.setAmont(amont);
            custChangeLog.setMoney(actamont);
            boolean ret = customerService.createCustChangelog(custChangeLog);

            result.setAfterbalance(subAccount.getBalance());
            result.setAftermoney(subAccount.getMoney());
            result.setStatus(ResultStatusEnum.success);
        }
        return result;
    }


}
