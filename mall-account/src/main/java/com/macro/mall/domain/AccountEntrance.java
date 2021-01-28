package com.macro.mall.domain;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.macro.mall.ams.model.AmsDicOrdertype;
import com.macro.mall.ams.service.AmsDicOrdertypeRepository;
import com.macro.mall.domain.order.OrderBase;
import com.macro.mall.domain.result.AccountResult;
import com.macro.mall.service.core.accounting.AccountingEntry;
import com.macro.mall.service.core.amount.AmontDecompose;
import com.macro.mall.service.core.customer.SubAccountEntry;
import com.macro.mall.enums.ResultStatusEnum;
import com.macro.mall.enums.StatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 核心记账入口
 *
 * @author dongjb
 * @date 2021/01/15
 */
@Slf4j
public class AccountEntrance {

    @Autowired
    static AmsDicOrdertypeRepository amsDicOrdertypeRepository;
    /**
     * 获取实例
     *
     * @param order             订单
     * @return 实例对象
     */
    public static AccountEntrance getInstance(OrderBase order) {
        try {
            /* 获取订单类型对象 */
            LambdaQueryWrapper<AmsDicOrdertype> lambda = new LambdaQueryWrapper<>();
            lambda.eq(AmsDicOrdertype::getCode, order.getOrdertype())
                    .eq(AmsDicOrdertype::getStatus, StatusEnum.ACTIVATE.getValue());
            AmsDicOrdertype orderTypeObj = amsDicOrdertypeRepository.getOne(lambda);

            if (orderTypeObj == null) {
                log.error("订单类型非法！");
            }
            return new AccountEntrance(order, orderTypeObj);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 构造函数
     *
     * @param order             订单
     * @param orderTypeObj      订单类型对象
     */
    private AccountEntrance(OrderBase order, AmsDicOrdertype orderTypeObj) {
        /* 初始化各实例 */
        doInit(order, orderTypeObj);
    }

    /**
     * 初始化各实例
     */
    private void doInit(OrderBase order, AmsDicOrdertype orderTypeObj) {
        AmontDecompose amontDecompose = AmontDecompose.getInstance(order, orderTypeObj);
        subAccountEntry = SubAccountEntry.getInstance(order, amontDecompose, orderTypeObj);
        accountingEntry = AccountingEntry.getInstance(order, amontDecompose, orderTypeObj);
    }

    /**
     * 记账前校验
     *
     * @return 记账结果对象
     */
    public AccountResult doValidate() {
        return subAccountEntry.doValidate();
    }

    /**
     * 执行记账处理
     *
     * @return 记账结果对象
     */
    public AccountResult doProcess(OrderBase order) {
        AccountResult result = doValidate();
        if (result.getStatus().equals(ResultStatusEnum.success.getCode())) {
            result = subAccountEntry.doBalanceChange(order);
            if (result.getStatus().equals(ResultStatusEnum.success.getCode())) {
                AccountResult accountresult = accountingEntry.doAccounting(order);
                if (!accountresult.getStatus().equals(ResultStatusEnum.success.getCode())) {
                    log.error("会计记账失败：" + accountresult.getMessage());
                    result.setStatus(accountresult.getStatus());
                    result.setMessage(accountresult.getMessage());
                }
            } else {
                log.error("余额变更失败：" + result.getMessage());
            }
        } else {
            log.error("记账校验失败：" + result.getMessage());
        }
        return result;
    }

    /**
     * 虚拟账户余额操作对象
     */
    private SubAccountEntry subAccountEntry;

    /**
     * 会计分录对象
     */
    private AccountingEntry accountingEntry;

}
