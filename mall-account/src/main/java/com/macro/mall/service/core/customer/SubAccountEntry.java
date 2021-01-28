package com.macro.mall.service.core.customer;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.macro.mall.ams.model.AmsDicOrdertype;
import com.macro.mall.ams.model.AmsDicOrdertypeBalancerule;
import com.macro.mall.ams.service.AmsDicOrdertypeBalanceruleRepository;
import com.macro.mall.domain.result.AccountResult;
import com.macro.mall.domain.order.OrderBase;
import com.macro.mall.service.OrderService;
import com.macro.mall.service.core.amount.AmontDecompose;
import com.macro.mall.enums.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.security.auth.login.AccountException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 虚拟账户（子账户）
 *
 * @author dongjb
 * @date 2021/01/15
 */
@Slf4j
public class SubAccountEntry {
    @Autowired
    AmsDicOrdertypeBalanceruleRepository amsDicOrdertypeBalanceruleRepository;

    @Autowired
    OrderService orderService;

    /**
     * 获取虚拟账户余额操作实例
     *
     * @param order             订单对象
     * @param amontDecompose    发生额分解类
     * @param orderTypeObj      订单类型对象
     * @return 分解实例
     */
    public static SubAccountEntry getInstance(OrderBase order, AmontDecompose amontDecompose, AmsDicOrdertype orderTypeObj) {
        return new SubAccountEntry(order, amontDecompose, orderTypeObj);
    }

    /**
     * 构造函数
     *
     * @param order             订单对象
     * @param amontDecompose    发生额分解类
     * @param orderTypeObj      订单类型对象
     */
    private SubAccountEntry(OrderBase order, AmontDecompose amontDecompose, AmsDicOrdertype orderTypeObj) {
        this.orderTypeObj = orderTypeObj;
        this.amontDecompose = amontDecompose;
        this.order = order;
        doInit();
    }

    /**
     * 初始化虚拟账户余额变化规则
     */
    private void doInit() {

        LambdaQueryWrapper<AmsDicOrdertypeBalancerule> lambda = new LambdaQueryWrapper<>();
        lambda.eq(AmsDicOrdertypeBalancerule::getOrdertypeid, orderTypeObj.getId())
                .orderByDesc(AmsDicOrdertypeBalancerule::getModifydate);
        balanceRules = amsDicOrdertypeBalanceruleRepository.list(lambda);
        balanceRules = doFilterRules();
    }

    /**
     * 过滤规则
     *
     * @return 过滤后的规则
     */
    private List<AmsDicOrdertypeBalancerule> doFilterRules() {
        List<AmsDicOrdertypeBalancerule> tmpRules = new ArrayList<>();
        Map<String, AmsDicOrdertypeBalancerule> tmpMap = new HashMap<>(8);
        for (AmsDicOrdertypeBalancerule balanceRule : balanceRules) {
            if (StringUtils.isEmpty(balanceRule.getBusinessid())) {
                if (!tmpMap.containsKey(OrderStatusEnum.getEnum(balanceRule.getOldstatus()).getName() + OrderStatusEnum.getEnum(balanceRule.getNewstatus()).getName() + ChangeAccountTypeEnum.getEnum(balanceRule.getType()).getValue() + balanceRule.getAmontruleid() + balanceRule.getAccounttype() + BalanceDirectEnum.getEnum(balanceRule.getBalancedirect()).getValue())) {
                    tmpMap.put(OrderStatusEnum.getEnum(balanceRule.getOldstatus()).getName() + OrderStatusEnum.getEnum(balanceRule.getNewstatus()).getName() + ChangeAccountTypeEnum.getEnum(balanceRule.getType()).getValue() + balanceRule.getAmontruleid() + balanceRule.getAccounttype() + BalanceDirectEnum.getEnum(balanceRule.getBalancedirect()).getValue(), balanceRule);
                }
            }
        }
        for (AmsDicOrdertypeBalancerule balanceRule : balanceRules) {
            if (!StringUtils.isEmpty(balanceRule.getBusinessid())) {
                String customerid = getCustomerId(ChangeAccountTypeEnum.getEnum(balanceRule.getType()));
                if (balanceRule.getBusinessid().equals(customerid)) {
                    tmpMap.remove(OrderStatusEnum.getEnum(balanceRule.getOldstatus()).getName() + OrderStatusEnum.getEnum(balanceRule.getNewstatus()).getName() + ChangeAccountTypeEnum.getEnum(balanceRule.getType()).getValue() + balanceRule.getAmontruleid() + balanceRule.getAccounttype() + BalanceDirectEnum.getEnum(balanceRule.getBalancedirect()).getValue());
                    tmpMap.put(OrderStatusEnum.getEnum(balanceRule.getOldstatus()).getName() + OrderStatusEnum.getEnum(balanceRule.getNewstatus()).getName() + ChangeAccountTypeEnum.getEnum(balanceRule.getType()).getValue() + balanceRule.getAmontruleid() + balanceRule.getAccounttype() + BalanceDirectEnum.getEnum(balanceRule.getBalancedirect()).getValue(), balanceRule);
                }
            }
        }
        for (Map.Entry<String, AmsDicOrdertypeBalancerule> entry : tmpMap.entrySet()) {
            tmpRules.add(entry.getValue());
        }
        return tmpRules;
    }

    /**
     * 余额校验
     *
     * @return 记账结果对象
     */
    public AccountResult doValidate() {
        AccountResult result = new AccountResult();
        result.setStatus(ResultStatusEnum.success);
        try {
            try {
                BigDecimal amont = order.getAmont().setScale(2, BigDecimal.ROUND_UNNECESSARY);
                order.setAmont(amont);
            } catch (ArithmeticException ae) {
                log.error("订单发生额不符合规范，最多保留两位小数");
            }
            for (AmsDicOrdertypeBalancerule balanceRule : balanceRules) {
                ChangeAccountTypeEnum changeAccountTypeEnum = ChangeAccountTypeEnum.getEnum(balanceRule.getType());
                ChangeBalanceDirectEnum balanceDirectEnum = ChangeBalanceDirectEnum.getEnum(balanceRule.getBalancedirect());
                if (balanceDirectEnum.equals(ChangeBalanceDirectEnum.Minus.getValue()) && !changeAccountTypeEnum.equals(ChangeAccountTypeEnum.Account.getValue())) {
                    String customerid = getCustomerId(changeAccountTypeEnum);
                    if (StringUtils.isEmpty(customerid)) {
                        log.error("【" + changeAccountTypeEnum.getDescript() + "】客户号获取失败");
                    }
                    BigDecimal balance = SubAccountBalanceControl.getSubAccountBalance(customerid, changeAccountTypeEnum, balanceRule.getAccounttype());
                    BigDecimal amont = amontDecompose.getAmontDetailById(balanceRule.getAmontruleid()).getAmont();
                    if (balance.compareTo(amont) < 0) {
                        log.error("【" + changeAccountTypeEnum.getDescript() + "】账户余额不足");
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setStatus(ResultStatusEnum.failed);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    /**
     * 执行余额变动
     *
     * @return 记账结果对象
     */
    public AccountResult doBalanceChange(OrderBase order) {
        AccountResult result = new AccountResult();
        result.setStatus(ResultStatusEnum.success);
        try {
            Map<String, AccountResult> resultMap = new HashMap<>(8);
            OrderStatusEnum oldStatus = orderService.findCurrOrderStatus(order);
            if (oldStatus.equals(OrderStatusEnum.other.getValue())) {
                oldStatus = OrderStatusEnum.create;
            }
            OrderStatusEnum newStatus = OrderStatusEnum.getEnum(order.getStatus());
            for (AmsDicOrdertypeBalancerule balanceRule : balanceRules) {
                if (oldStatus.equals(OrderStatusEnum.getEnum(balanceRule.getOldstatus()).getValue()) && newStatus.equals(OrderStatusEnum.getEnum(balanceRule.getNewstatus()).getValue())) {
                    ChangeAccountTypeEnum changeAccountTypeEnum = ChangeAccountTypeEnum.getEnum(balanceRule.getType());
                    ChangeBalanceDirectEnum balanceDirectEnum = ChangeBalanceDirectEnum.getEnum(balanceRule.getBalancedirect());
                    String customerid = getCustomerId(changeAccountTypeEnum);
                    if (StringUtils.isEmpty(customerid)) {
                        log.error("【" + changeAccountTypeEnum.getDescript() + "】客户号获取失败");
                    }
                    BigDecimal amont = amontDecompose.getAmontDetailById(balanceRule.getAmontruleid()).getAmont();
                    String key = changeAccountTypeEnum.getValue() + balanceRule.getAccounttype();
                    AccountResult changeResult;
                    if (resultMap.containsKey(key)) {
                        changeResult = resultMap.get(key);
                    } else {
                        BigDecimal actamont = amont.multiply(order.getRatio()).setScale(2, DecimalProcessModeEnum.Half_EVEN.getMode());
                        changeResult = SubAccountBalanceControl.changeBalance(customerid, amont, actamont, changeAccountTypeEnum, balanceRule.getAccounttype(), balanceDirectEnum);
                        resultMap.put(key, changeResult);
                    }
                    if (changeResult.getStatus().equals(ResultStatusEnum.success.getCode())) {

                        if (!changeAccountTypeEnum.equals(ChangeAccountTypeEnum.Account)) {
                            if (changeResult.getAfterbalance().compareTo(BigDecimal.ZERO) < 0 || changeResult.getAftermoney().compareTo(BigDecimal.ZERO) < 0) {
                                throw new AccountException("【" + changeAccountTypeEnum.getDescript() + "】账户余额不足");
                            }
                        }

                        AccountTypeEnum accountTypeEnum = AccountTypeEnum.getEnum(balanceRule.getAccounttype());

                        BigDecimal resultAmont = result.getResultAmontMap(changeAccountTypeEnum, accountTypeEnum, AmontTypeEnum.balance);
                        if (resultAmont != null) {
                            result.setResultAmontMap(changeAccountTypeEnum, accountTypeEnum, AmontTypeEnum.balance, resultAmont.add(amont));
                        } else {
                            result.setResultAmontMap(changeAccountTypeEnum, accountTypeEnum, AmontTypeEnum.balance, amont);
                        }

                        resultAmont = result.getResultAmontMap(changeAccountTypeEnum, accountTypeEnum, AmontTypeEnum.money);
                        if (resultAmont != null) {
                            result.setResultAmontMap(changeAccountTypeEnum, accountTypeEnum, AmontTypeEnum.money, resultAmont.add(amont));
                        } else {
                            result.setResultAmontMap(changeAccountTypeEnum, accountTypeEnum, AmontTypeEnum.money, amont);
                        }

                        BigDecimal resultBeforBalance = result.getResultBeforBalance(changeAccountTypeEnum, accountTypeEnum, AmontTypeEnum.balance);
                        if (resultBeforBalance == null) {
                            result.setResultBeforBalance(changeAccountTypeEnum, accountTypeEnum, AmontTypeEnum.balance, changeResult.getBeforbalance());
                        }

                        resultBeforBalance = result.getResultBeforBalance(changeAccountTypeEnum, accountTypeEnum, AmontTypeEnum.money);
                        if (resultBeforBalance == null) {
                            result.setResultBeforBalance(changeAccountTypeEnum, accountTypeEnum, AmontTypeEnum.money, changeResult.getBeformoney());
                        }

                        result.setResultAfterBalance(changeAccountTypeEnum, accountTypeEnum, AmontTypeEnum.balance, changeResult.getAfterbalance());
                        result.setResultAfterBalance(changeAccountTypeEnum, accountTypeEnum, AmontTypeEnum.money, changeResult.getAftermoney());
                    } else {
                        result = changeResult;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setStatus(ResultStatusEnum.failed);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    /**
     * 获取客户号
     *
     * @param changeAccountTypeEnum 账户类型
     * @return 客户号
     */
    private String getCustomerId(ChangeAccountTypeEnum changeAccountTypeEnum) {
        String customerid = null;
        switch (changeAccountTypeEnum) {
            case Account:
                customerid = order.getYyBusinessId();
                break;
            case Business:
                customerid = order.getBusinessid();
                break;
            case DisBusiness:
                customerid = order.getRecebusinessid();
                break;
            case Cust:
                customerid = order.getCustid();
                break;
            case DisCust:
                customerid = order.getRececustid();
                break;
            default:
                log.info("默认路径");
        }
        return customerid;
    }

    /**
     * 订单对象
     */
    private final OrderBase order;

    /**
     * 虚拟账户余额变化规则
     */
    private List<AmsDicOrdertypeBalancerule> balanceRules;

    /**
     * 订单类型字典配置
     */
    private final AmsDicOrdertype orderTypeObj;

    /**
     * 发生额分解类
     */
    private final AmontDecompose amontDecompose;

}
