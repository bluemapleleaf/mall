package com.macro.mall.service.core.amount;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.macro.mall.ams.model.AmsDicOrdertypeAmontdetailruleSteprule;
import com.macro.mall.ams.service.AmsDicOrdertypeAmontdetailruleStepruleRepository;
import com.macro.mall.domain.DicOrdertypeAmontdetailrule;
import com.macro.mall.domain.order.OrderBase;
import com.macro.mall.enums.AmontRuleType;
import com.macro.mall.enums.CalculateModeEnum;
import com.macro.mall.enums.CalculateTypeEnum;
import com.macro.mall.enums.DecomposeTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发生额计算
 *
 * @author dongjb
 * @date 2021/01/15
 */
@Slf4j
class AmontCalculation {
    @Autowired
    AmsDicOrdertypeAmontdetailruleStepruleRepository amsDicOrdertypeAmontdetailruleStepruleRepository;

    AmontCalculation(OrderBase order, List<DicOrdertypeAmontdetailrule> amontDetailRules) {
        this.amontDetailRules = amontDetailRules;
        this.orderAmont = order.getAmont();
        this.orderActAmont = order.getActamont();
        this.diffAmont = orderAmont.subtract(orderActAmont).abs();
        this.extamont1 = order.getExtamont1();
        this.extamont2 = order.getExtamont2();
        this.extamont3 = order.getExtamont3();
        this.extamont4 = order.getExtamont4();
        this.extamont5 = order.getExtamont5();
        this.extamont6 = order.getExtamont6();
        this.extamont7 = order.getExtamont7();
        this.extamont8 = order.getExtamont8();
        this.extamont9 = order.getExtamont9();
        this.extamont10 = order.getExtamont10();
        this.balance = orderAmont;
        this.actBalance = orderActAmont;
        this.diffBalance = orderAmont.subtract(orderActAmont).abs();
        this.extBalance1 = this.extamont1;
        this.extBalance2 = this.extamont2;
        this.extBalance3 = this.extamont3;
        this.extBalance4 = this.extamont4;
        this.extBalance5 = this.extamont5;
        this.extBalance6 = this.extamont6;
        this.extBalance7 = this.extamont7;
        this.extBalance8 = this.extamont8;
        this.extBalance9 = this.extamont9;
        this.extBalance10 = this.extamont10;
        sortDetail = new HashMap<>();
        doCalculation();
    }

    /**
     * 发生额明细计算
     */
    private void doCalculation() {
        for (DicOrdertypeAmontdetailrule amontDetailRule : amontDetailRules) {
            if (amontDetailRule.getCalculatetype().equals(CalculateTypeEnum.step.getValue())) {
                Map<String, Object> stepparam = new HashMap<>();

                LambdaQueryWrapper<AmsDicOrdertypeAmontdetailruleSteprule> lambda = new LambdaQueryWrapper<>();
                lambda.eq(AmsDicOrdertypeAmontdetailruleSteprule::getAmontruleid, amontDetailRule.getId())
                        .orderByAsc(AmsDicOrdertypeAmontdetailruleSteprule::getBeginamont);
                List<AmsDicOrdertypeAmontdetailruleSteprule> amontDetailStepRules = amsDicOrdertypeAmontdetailruleStepruleRepository.list();

                amontDetailRule.setStepRules(amontDetailStepRules);
            }
            doCalculat(amontDetailRule);
        }
        parseBalanceAndOrderAmont();
    }

    /**
     * 执行计算
     *
     * @param amontDetailRule 发生额明细规则
     */
    private void doCalculat(DicOrdertypeAmontdetailrule amontDetailRule) {
        calculatAmont(amontDetailRule);
        sortDetail.put(amontDetailRule.getSort(), amontDetailRule);
    }

    /**
     * 计算余额明细
     *
     * @param amontDetailRule 发生额明细规则
     */
    private void calculatAmont(DicOrdertypeAmontdetailrule amontDetailRule) {
        AmontRuleType amontRuleType = AmontRuleType.getEnum(amontDetailRule.getRuletype());
        CalculateModeEnum calculateModeEnum = amontDetailRule.getCalculatemode();
        DecomposeTypeEnum decomposeTypeEnum = amontDetailRule.getDecomposetype();
        BigDecimal basic = BigDecimal.ZERO;
        BigDecimal amontBalance = BigDecimal.ZERO;
        switch (amontRuleType) {
            case amont:
                basic = orderAmont;
                amontBalance = balance;
                break;
            case actamont:
                basic = orderActAmont;
                amontBalance = actBalance;
                break;
            case difference:
                basic = diffAmont;
                amontBalance = diffBalance;
                break;
            case extamont1:
                basic = extamont1;
                amontBalance = extBalance1;
                break;
            case extamont2:
                basic = extamont2;
                amontBalance = extBalance2;
                break;
            case extamont3:
                basic = extamont3;
                amontBalance = extBalance3;
                break;
            case extamont4:
                basic = extamont4;
                amontBalance = extBalance4;
                break;
            case extamont5:
                basic = extamont5;
                amontBalance = extBalance5;
                break;
            case extamont6:
                basic = extamont6;
                amontBalance = extBalance6;
                break;
            case extamont7:
                basic = extamont7;
                amontBalance = extBalance7;
                break;
            case extamont8:
                basic = extamont8;
                amontBalance = extBalance8;
                break;
            case extamont9:
                basic = extamont9;
                amontBalance = extBalance9;
                break;
            case extamont10:
                basic = extamont10;
                amontBalance = extBalance10;
                break;
            default:
                log.error("发生额规则类型非法！");
        }
        if (amontDetailRule.getBasictype() > -1) {
            basic = sortDetail.get(amontDetailRule.getBasictype()).getBalance();
        }
        BigDecimal value = doCalculatBalance(basic, amontDetailRule);
        value = doFormat(value, amontDetailRule);
        if (calculateModeEnum.equals(CalculateModeEnum.calculate.getValue())) {
            if (decomposeTypeEnum.equals(DecomposeTypeEnum.inner.getValue())) {
                switch (amontRuleType) {
                    case amont:
                        balance = balance.subtract(value);
                        break;
                    case actamont:
                        actBalance = actBalance.subtract(value);
                        break;
                    case difference:
                        diffBalance = diffBalance.subtract(value);
                        break;
                    case extamont1:
                        extBalance1 = extBalance1.subtract(value);
                        break;
                    case extamont2:
                        extBalance2 = extBalance2.subtract(value);
                        break;
                    case extamont3:
                        extBalance3 = extBalance3.subtract(value);
                        break;
                    case extamont4:
                        extBalance4 = extBalance4.subtract(value);
                        break;
                    case extamont5:
                        extBalance5 = extBalance5.subtract(value);
                        break;
                    case extamont6:
                        extBalance6 = extBalance6.subtract(value);
                        break;
                    case extamont7:
                        extBalance7 = extBalance7.subtract(value);
                        break;
                    case extamont8:
                        extBalance8 = extBalance8.subtract(value);
                        break;
                    case extamont9:
                        extBalance9 = extBalance9.subtract(value);
                        break;
                    case extamont10:
                        extBalance10 = extBalance10.subtract(value);
                        break;
                    default:
                        log.error("规格类型不匹配");
                }
                amontBalance = amontBalance.subtract(value);
            } else {
                switch (amontRuleType) {
                    case amont:
                        balance = balance.add(value);
                        break;
                    case actamont:
                        actBalance = actBalance.add(value);
                        break;
                    case difference:
                        diffBalance = diffBalance.add(value);
                        break;
                    case extamont1:
                        extBalance1 = extBalance1.add(value);
                        break;
                    case extamont2:
                        extBalance2 = extBalance2.add(value);
                        break;
                    case extamont3:
                        extBalance3 = extBalance3.add(value);
                        break;
                    case extamont4:
                        extBalance4 = extBalance4.add(value);
                        break;
                    case extamont5:
                        extBalance5 = extBalance5.add(value);
                        break;
                    case extamont6:
                        extBalance6 = extBalance6.add(value);
                        break;
                    case extamont7:
                        extBalance7 = extBalance7.add(value);
                        break;
                    case extamont8:
                        extBalance8 = extBalance8.add(value);
                        break;
                    case extamont9:
                        extBalance9 = extBalance9.add(value);
                        break;
                    case extamont10:
                        extBalance10 = extBalance10.add(value);
                        break;
                    default:
                        log.error("规格类型不匹配");
                }
                amontBalance = amontBalance.add(value);
            }
        }
        amontDetailRule.setAmont(value);
        amontDetailRule.setBalance(amontBalance);
    }

    /**
     * 结算结果格式化
     *
     * @param amont           带计算数字
     * @param amontDetailRule 发生额明细规则
     * @return 目标数字
     */
    private BigDecimal doFormat(BigDecimal amont, DicOrdertypeAmontdetailrule amontDetailRule) {
        BigDecimal result = amont.setScale(2, amontDetailRule.getDecimalprocess());
        BigDecimal min = amontDetailRule.getMin();
        BigDecimal max = amontDetailRule.getMax();
        if(min.compareTo(max) > 0) {
            log.error("发生额明细规则错误：最小值大于最大值");
        }
        if(min.compareTo(BigDecimal.ZERO) > 0) {
            if (result.compareTo(min) < 0) {
                result = min;
            }
        }
        if(max.compareTo(BigDecimal.ZERO) > 0) {
            if (result.compareTo(max) > 0) {
                result = max;
            }
        }
        return result;
    }

    /**
     * 计算余额结果
     *
     * @param basic           基数
     * @param amontDetailRule 发生额明细规则
     * @return 计算结果
     */
    private BigDecimal doCalculatBalance(BigDecimal basic, DicOrdertypeAmontdetailrule amontDetailRule) {
        AmontRuleType amontRuleType = AmontRuleType.getEnum(amontDetailRule.getRuletype());
        BigDecimal value = BigDecimal.ZERO;
        switch (CalculateTypeEnum.getEnum(amontDetailRule.getCalculatetype())) {
            case quota:
                value = amontDetailRule.getParam();
                break;
            case ratio:
                value = basic.multiply(amontDetailRule.getParam());
                break;
            case step:
                value = doCalculatStep(basic, amontDetailRule.getStepRules());
                break;
            case fullCut:
                value = doCalculatFullCutStep(basic, amontDetailRule.getStepRules());
                break;
            case balance:
                value = BigDecimal.ZERO;
                switch (amontRuleType) {
                    case amont:
                        value = balance;
                        break;
                    case actamont:
                        value = actBalance;
                        break;
                    case difference:
                        value = diffBalance;
                        break;
                    case extamont1:
                        value = extBalance1;
                        break;
                    case extamont2:
                        value = extBalance2;
                        break;
                    case extamont3:
                        value = extBalance3;
                        break;
                    case extamont4:
                        value = extBalance4;
                        break;
                    case extamont5:
                        value = extBalance5;
                        break;
                    case extamont6:
                        value = extBalance6;
                        break;
                    case extamont7:
                        value = extBalance7;
                        break;
                    case extamont8:
                        value = extBalance8;
                        break;
                    case extamont9:
                        value = extBalance9;
                        break;
                    case extamont10:
                        value = extBalance10;
                        break;
                    default:
                        log.error("发生额规则类型非法！");
                }
                break;
            default:
                log.error("默认情况！");
         }
        return value;
    }

    /**
     * 执行阶梯计算
     *
     * @param basic                基数
     * @param amontDetailStepRules 阶梯规则
     * @return 计算结果
     */
    private BigDecimal doCalculatStep(BigDecimal basic, List<AmsDicOrdertypeAmontdetailruleSteprule> amontDetailStepRules) {
        BigDecimal result = BigDecimal.ZERO;
        BigDecimal lastEndAmont = BigDecimal.ZERO;
        for (AmsDicOrdertypeAmontdetailruleSteprule amontDetailStepRule : amontDetailStepRules) {
            BigDecimal beginAmont = amontDetailStepRule.getBeginamont();
            BigDecimal endAmont = amontDetailStepRule.getEndamont();
            if (beginAmont.compareTo(lastEndAmont) != 0 ) {
                log.error("阶梯规则错误：开始额与上一条规则结束额不相等");
            }
            int comBegin = basic.compareTo(beginAmont);
            if (comBegin >= 0) {
                BigDecimal calculatNumber;
                if (endAmont.compareTo(BigDecimal.ZERO) > 0) {
                    if (beginAmont.compareTo(endAmont) > 0) {
                        log.error("阶梯规则错误：开始额大于结束额");
                    }
                    int comEnd = basic.compareTo(endAmont);
                    if (comEnd <= 0) {
                        calculatNumber = basic.subtract(lastEndAmont);
                    } else {
                        calculatNumber = endAmont;
                    }
                } else {
                    calculatNumber = basic.subtract(lastEndAmont);
                }
                if (amontDetailStepRule.getCalculatetype().equals(CalculateTypeEnum.quota.getValue())) {
                    result = result.add(amontDetailStepRule.getParam());
                } else if (amontDetailStepRule.getCalculatetype().equals(CalculateTypeEnum.ratio.getValue())) {
                    result = calculatNumber.multiply(amontDetailStepRule.getParam());
                }
                lastEndAmont = endAmont;
            } else {
                break;
            }
        }
        return result;
    }

    /**
     * 执行满减计算
     *
     * @param basic                基数
     * @param amontDetailStepRules 阶梯规则
     * @return 计算结果
     */
    private BigDecimal doCalculatFullCutStep(BigDecimal basic, List<AmsDicOrdertypeAmontdetailruleSteprule> amontDetailStepRules) {
        BigDecimal result = BigDecimal.ZERO;
        BigDecimal lastEndAmont = BigDecimal.ZERO;
        for (AmsDicOrdertypeAmontdetailruleSteprule amontDetailStepRule : amontDetailStepRules) {
            BigDecimal beginAmont = amontDetailStepRule.getBeginamont();
            BigDecimal endAmont = amontDetailStepRule.getEndamont();
            if (beginAmont.compareTo(lastEndAmont) != 0) {
                log.error("阶梯规则错误：开始额与上一条规则结束额不相等");
            }
            int comBegin = basic.compareTo(beginAmont);
            if (comBegin >= 0) {
                BigDecimal calculatNumber;
                if (endAmont.compareTo(BigDecimal.ZERO) > 0) {
                    if (beginAmont.compareTo(endAmont) > 0) {
                        log.error("阶梯规则错误：开始额大于结束额");
                    }
                    int comEnd = basic.compareTo(endAmont);
                    if (comEnd <= 0) {
                        calculatNumber = basic.subtract(lastEndAmont);
                    } else {
                        continue;
                    }
                } else {
                    calculatNumber = basic.subtract(lastEndAmont);
                }
                if (amontDetailStepRule.getCalculatetype().equals(CalculateTypeEnum.quota.getValue())) {
                    result = result.add(amontDetailStepRule.getParam());
                } else if (amontDetailStepRule.getCalculatetype().equals(CalculateTypeEnum.ratio.getValue())) {
                    result = calculatNumber.multiply(amontDetailStepRule.getParam());
                }
                lastEndAmont = endAmont;
            } else {
                break;
            }
        }
        return result;
    }

    /**
     * 设置余额/订单额类型的规则值
     */
    private void parseBalanceAndOrderAmont() {
        for (DicOrdertypeAmontdetailrule amontDetailRule : amontDetailRules) {
            AmontRuleType amontRuleType = AmontRuleType.getEnum(amontDetailRule.getRuletype());
            BigDecimal resultAmont = BigDecimal.valueOf(0.00D);
            BigDecimal resultBalance = BigDecimal.valueOf(0.00D);
            switch (amontRuleType) {
                case amont:
                    resultAmont = orderAmont;
                    resultBalance = balance;
                    break;
                case actamont:
                    resultAmont = orderActAmont;
                    resultBalance = actBalance;
                    break;
                case difference:
                    resultAmont = diffAmont;
                    resultBalance = diffBalance;
                    break;
                case extamont1:
                    resultAmont = extamont1;
                    resultBalance = extBalance1;
                    break;
                case extamont2:
                    resultAmont = extamont2;
                    resultBalance = extBalance2;
                    break;
                case extamont3:
                    resultAmont = extamont3;
                    resultBalance = extBalance3;
                    break;
                case extamont4:
                    resultAmont = extamont4;
                    resultBalance = extBalance4;
                    break;
                case extamont5:
                    resultAmont = extamont5;
                    resultBalance = extBalance5;
                    break;
                case extamont6:
                    resultAmont = extamont6;
                    resultBalance = extBalance6;
                    break;
                case extamont7:
                    resultAmont = extamont7;
                    resultBalance = extBalance7;
                    break;
                case extamont8:
                    resultAmont = extamont8;
                    resultBalance = extBalance8;
                    break;
                case extamont9:
                    resultAmont = extamont9;
                    resultBalance = extBalance9;
                    break;
                case extamont10:
                    resultAmont = extamont10;
                    resultBalance = extBalance10;
                    break;
                default:
                    log.info("默认情况");
            }
            if (amontDetailRule.getCalculatetype().equals(CalculateTypeEnum.balance.getValue())) {
                amontDetailRule.setAmont(resultBalance);
                amontDetailRule.setBalance(resultBalance);
            } else if (amontDetailRule.getCalculatetype().equals(CalculateTypeEnum.orderAmont.getValue())) {
                amontDetailRule.setAmont(resultAmont);
                amontDetailRule.setBalance(resultAmont);
            }
        }
    }

    /**
     * 获取发生额明细
     *
     * @return 发生额明细
     */
    List<DicOrdertypeAmontdetailrule> getAmontDetailRules() {
        return amontDetailRules;
    }

    BigDecimal getBalance() {
        return balance;
    }

    /**
     * 订单额
     */
    private final BigDecimal orderAmont;

    /**
     * 订单金额
     */
    private final BigDecimal orderActAmont;

    /**
     * 订单差额
     */
    private final BigDecimal diffAmont;

    /**
     * 扩展额1
     */
    private final BigDecimal extamont1;

    /**
     * 扩展额2
     */
    private final BigDecimal extamont2;

    /**
     * 扩展额3
     */
    private final BigDecimal extamont3;

    /**
     * 扩展额4
     */
    private final BigDecimal extamont4;

    /**
     * 扩展额5
     */
    private final BigDecimal extamont5;

    /**
     * 扩展额6
     */
    private final BigDecimal extamont6;

    /**
     * 扩展额7
     */
    private final BigDecimal extamont7;

    /**
     * 扩展额8
     */
    private final BigDecimal extamont8;

    /**
     * 扩展额9
     */
    private final BigDecimal extamont9;

    /**
     * 扩展额10
     */
    private final BigDecimal extamont10;

    /**
     * 发生额计算最终结果
     */
    private BigDecimal balance;

    /**
     * 发生额计算最终结果
     */
    private BigDecimal actBalance;

    /**
     * 差额计算最终结果
     */
    private BigDecimal diffBalance;

    /**
     * 扩展额1计算最终结果
     */
    private BigDecimal extBalance1;

    /**
     * 扩展额2计算最终结果
     */
    private BigDecimal extBalance2;

    /**
     * 扩展额3计算最终结果
     */
    private BigDecimal extBalance3;

    /**
     * 扩展额4计算最终结果
     */
    private BigDecimal extBalance4;

    /**
     * 扩展额5计算最终结果
     */
    private BigDecimal extBalance5;

    /**
     * 扩展额6计算最终结果
     */
    private BigDecimal extBalance6;

    /**
     * 扩展额7计算最终结果
     */
    private BigDecimal extBalance7;

    /**
     * 扩展额8计算最终结果
     */
    private BigDecimal extBalance8;

    /**
     * 扩展额9计算最终结果
     */
    private BigDecimal extBalance9;

    /**
     * 扩展额10计算最终结果
     */
    private BigDecimal extBalance10;

    /**
     * 发生额明细规则
     */
    private final List<DicOrdertypeAmontdetailrule> amontDetailRules;

    /**
     * 发生额明细，sort=>D_OrderTypeAmontDetailRule
     */
    private final Map<Integer, DicOrdertypeAmontdetailrule> sortDetail;

}
