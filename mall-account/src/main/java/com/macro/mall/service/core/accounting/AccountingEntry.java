package com.macro.mall.service.core.accounting;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.macro.mall.ams.model.AmsDicAccountItem;
import com.macro.mall.ams.model.AmsDicOrdertype;
import com.macro.mall.ams.model.AmsDicOrdertypeSubjectrule;
import com.macro.mall.ams.service.AmsDicOrdertypeSubjectruleRepository;
import com.macro.mall.domain.result.AccountResult;
import com.macro.mall.domain.EntryItemParam;
import com.macro.mall.domain.order.OrderBase;
import com.macro.mall.service.AccountItemService;
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
 * 会计分录
 *
 * @author dongjb
 * @date 2021/01/15
 */
@Slf4j
public class AccountingEntry {
    @Autowired
    AmsDicOrdertypeSubjectruleRepository amsDicOrdertypeSubjectruleRepository;

    @Autowired
    AccountItemService accountItemService;

    @Autowired
    OrderService orderService;

    /**
     * 获取会计分录实例
     *
     * @param order          订单对象
     * @param amontDecompose 发生额分解类
     * @param orderTypeObj   订单类型对象
     * @return 分解实例
     */
    public static AccountingEntry getInstance(OrderBase order, AmontDecompose amontDecompose, AmsDicOrdertype orderTypeObj) {
        return new AccountingEntry(order, amontDecompose, orderTypeObj);
    }

    /**
     * 构造函数
     *
     * @param order          订单对象
     * @param amontDecompose 发生额分解类
     * @param orderTypeObj   订单类型对象
     */
    private AccountingEntry(OrderBase order, AmontDecompose amontDecompose, AmsDicOrdertype orderTypeObj) {
        this.orderTypeObj = orderTypeObj;
        this.amontDecompose = amontDecompose;
        this.order = order;
        doInit();
    }

    /**
     * 初始化会计分录规则
     */
    private void doInit() {

        LambdaQueryWrapper<AmsDicOrdertypeSubjectrule> lambda = new LambdaQueryWrapper<>();
        lambda.eq(AmsDicOrdertypeSubjectrule::getOrdertypeid, orderTypeObj.getId())
                .orderByDesc(AmsDicOrdertypeSubjectrule::getModifydate);
        subjectRules = amsDicOrdertypeSubjectruleRepository.list();
        subjectRules = doFilterRules();
        if (subjectRules.size() == 0) {
            log.error("会计科目分录规则未配置");
        }
    }

    /**
     * 过滤规则
     *
     * @return 过滤后的规则
     */
    private List<AmsDicOrdertypeSubjectrule> doFilterRules() {
        List<AmsDicOrdertypeSubjectrule> tmpRules = new ArrayList<>();
        Map<String, AmsDicOrdertypeSubjectrule> tmpMap = new HashMap<>(8);
        for(AmsDicOrdertypeSubjectrule subjectRule : subjectRules) {
            if(StringUtils.isEmpty(subjectRule.getBusinessid())) {
                log.info("会计科目分录规则没有指定B户客户号");
            }
            if(StringUtils.isEmpty(subjectRule.getPaytypecode())) {
                String customerid = getCustomerId(ChangeAccountTypeEnum.getEnum(subjectRule.getType()));
                if (subjectRule.getBusinessid().equals(customerid)) {
                    if (!tmpMap.containsKey(OrderStatusEnum.getEnum(subjectRule.getOldstatus()).getName() + OrderStatusEnum.getEnum(subjectRule.getNewstatus()).getName() + ChangeAccountTypeEnum.getEnum(subjectRule.getType()).getValue() + subjectRule.getBusinessid() + subjectRule.getAmontruleid() + subjectRule.getAccountsubitemid() + BalanceDirectEnum.getEnum(subjectRule.getBalancedirect()).getValue())) {
                        tmpMap.put(OrderStatusEnum.getEnum(subjectRule.getOldstatus()).getName() + OrderStatusEnum.getEnum(subjectRule.getNewstatus()).getName() + ChangeAccountTypeEnum.getEnum(subjectRule.getType()).getValue() + subjectRule.getBusinessid() + subjectRule.getAmontruleid() + subjectRule.getAccountsubitemid() + BalanceDirectEnum.getEnum(subjectRule.getBalancedirect()).getValue(), subjectRule);
                    }
                }
            }
        }
        for (AmsDicOrdertypeSubjectrule subjectRule : subjectRules) {
            if (!StringUtils.isEmpty(subjectRule.getPaytypecode())) {
                String customerid = getCustomerId(ChangeAccountTypeEnum.getEnum(subjectRule.getType()));
                if (subjectRule.getBusinessid().equals(customerid)) {
                    tmpMap.remove(OrderStatusEnum.getEnum(subjectRule.getOldstatus()).getName() + OrderStatusEnum.getEnum(subjectRule.getNewstatus()).getName() + ChangeAccountTypeEnum.getEnum(subjectRule.getType()).getValue() + subjectRule.getBusinessid() + subjectRule.getAmontruleid() + subjectRule.getAccountsubitemid() + BalanceDirectEnum.getEnum(subjectRule.getBalancedirect()).getValue());
                    tmpMap.put(OrderStatusEnum.getEnum(subjectRule.getOldstatus()).getName() + OrderStatusEnum.getEnum(subjectRule.getNewstatus()).getName() + ChangeAccountTypeEnum.getEnum(subjectRule.getType()).getValue() + subjectRule.getBusinessid() + subjectRule.getAmontruleid() + subjectRule.getAccountsubitemid() + BalanceDirectEnum.getEnum(subjectRule.getBalancedirect()).getValue() + subjectRule.getPaytypecode(), subjectRule);
                }
            }
        }
        for (Map.Entry<String, AmsDicOrdertypeSubjectrule> entry : tmpMap.entrySet()) {
            tmpRules.add(entry.getValue());
        }
        return tmpRules;
    }

    /**
     * 执行会计分录记账
     *
     * @return 记账结果对象
     */
    public AccountResult doAccounting(OrderBase orderBase) {
        AccountResult result = new AccountResult();
        result.setStatus(ResultStatusEnum.success);
        try {
            /* 校验借贷必相等Map */
            Map<String, BigDecimal> tmpBalanceMap = new HashMap<>(8);
            if (subjectRules.size() > 0) {
                OrderStatusEnum oldStatus = orderService.findCurrOrderStatus(orderBase);
                if (oldStatus.equals(OrderStatusEnum.other.getValue())) {
                    oldStatus = OrderStatusEnum.create;
                }
                OrderStatusEnum newStatus = OrderStatusEnum.getEnum(order.getStatus());
                for (AmsDicOrdertypeSubjectrule subjectRule : subjectRules) {
                    if (oldStatus.equals(OrderStatusEnum.getEnum(subjectRule.getOldstatus()).getValue())
                            && newStatus.equals(OrderStatusEnum.getEnum(subjectRule.getNewstatus()).getValue())) {
                        boolean isNeedAccounting = false;
                        String payTypeCode = subjectRule.getPaytypecode();
                        if (StringUtils.isEmpty(payTypeCode)) {
                            isNeedAccounting = true;
                        } else {
                            if (payTypeCode.equals(order.getPaytype())) {
                                isNeedAccounting = true;
                            }
                        }
                        if (isNeedAccounting) {
                            ChangeAccountTypeEnum changeAccountTypeEnum = ChangeAccountTypeEnum.getEnum(subjectRule.getType());
                            String customerid = getCustomerId(changeAccountTypeEnum);
                            if (StringUtils.isEmpty(customerid)) {
                                throw new AccountException("【" + changeAccountTypeEnum.getDescript() + "】客户号获取失败");
                            }
                            BigDecimal amont = amontDecompose.getAmontDetailById(subjectRule.getAmontruleid()).getAmont();
                            /* 校验借贷必相等 start */
                            if (tmpBalanceMap.containsKey(customerid)) {
                                BigDecimal tmp = tmpBalanceMap.get(customerid);
                                tmp = tmp.subtract(amont);
                                tmpBalanceMap.put(customerid, tmp);
                            } else {
                                tmpBalanceMap.put(customerid, amont.negate());
                            }
                            /* 校验借贷必相等 end */

                            AmsDicAccountItem accountsubitem = accountItemService.getAccountItem(subjectRule.getAccountsubitemid());
                            accountItemService.doRecord(order.getOrderNo(), customerid, amont, order.getAccountdate(), order.getStatementdate(), order.getBindaccountdate(), accountsubitem, BalanceDirectEnum.getEnum(subjectRule.getBalancedirect()));
                        }
                    }
                }
                /* 执行校验借贷必相等 */
                for (Map.Entry<String, BigDecimal> entry : tmpBalanceMap.entrySet()) {
                    if (entry.getValue().compareTo(BigDecimal.ZERO) != 0) {
                        log.error("主体B户【" + entry.getKey() + "】会计记账借贷方不相等");
                    }
                }
            } else {
                List<EntryItemParam> entryItemParams = order.getEntryItemParams();
                if (entryItemParams != null) {
                    String customerid = order.getBusinessid();
                    BigDecimal balance = BigDecimal.ZERO;
                    for (EntryItemParam entryItemParam : entryItemParams) {
                        BalanceDirectEnum balanceDirectEnum = BalanceDirectEnum.getEnum(entryItemParam.getBalanceDirect());
                        BigDecimal amont = entryItemParam.getAmont();
                        if (balanceDirectEnum.equals(BalanceDirectEnum.credits)) {
                            balance = balance.add(amont);
                        } else {
                            balance = balance.subtract(amont);
                        }
                        AmsDicAccountItem accountsubitem = accountItemService.getSubAccountItem(entryItemParam.getItemcode(), entryItemParam.getSubitemcode());
                        if (accountsubitem == null) {
                            log.error("会计科目【" + entryItemParam.getItemcode() + entryItemParam.getSubitemcode() + "】不合法");
                        }
                        accountItemService.doRecord(order.getOrderNo(), customerid, amont, order.getAccountdate(), order.getStatementdate(), order.getBindaccountdate(), accountsubitem, balanceDirectEnum);
                    }
                    if (balance.compareTo(BigDecimal.ZERO) != 0) {
                        log.error("主体B户【" + customerid + "】会计记账借贷方不相等");
                    }
                } else {
                    log.error("会计科目分录规则未配置");
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
     * 会计分录规则
     */
    private List<AmsDicOrdertypeSubjectrule> subjectRules;

    /**
     * 订单类型字典配置
     */
    private final AmsDicOrdertype orderTypeObj;

    /**
     * 发生额分解类
     */
    private final AmontDecompose amontDecompose;

}
