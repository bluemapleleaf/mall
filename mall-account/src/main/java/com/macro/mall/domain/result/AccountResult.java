package com.macro.mall.domain.result;

import com.macro.mall.enums.AccountTypeEnum;
import com.macro.mall.enums.AmontTypeEnum;
import com.macro.mall.enums.ChangeAccountTypeEnum;
import com.macro.mall.enums.ResultStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 记账结果
 *
 * @author dongjb
 * @date 2021/01/15
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AccountResult {
    private ResultStatusEnum status;

    private String message;

    private BigDecimal beforbalance;

    private BigDecimal afterbalance;

    private BigDecimal beformoney;

    private BigDecimal aftermoney;

    private final Map<String, BigDecimal> beforbalanceMap = new HashMap<>();

    private final Map<String, BigDecimal> afterbalanceMap = new HashMap<>();

    private final Map<String, BigDecimal> amontMap = new HashMap<>();

    public BigDecimal getResultBeforBalance(ChangeAccountTypeEnum changeAccountTypeEnum, AccountTypeEnum accountTypeEnum, AmontTypeEnum amontTypeEnum) {
        return beforbalanceMap.get(changeAccountTypeEnum.getValue() + accountTypeEnum.getType() + amontTypeEnum.getValue());
    }

    public void setResultBeforBalance(ChangeAccountTypeEnum changeAccountTypeEnum, AccountTypeEnum accountTypeEnum, AmontTypeEnum amontTypeEnum, BigDecimal beforbalance) {
        beforbalanceMap.put(changeAccountTypeEnum.getValue() + accountTypeEnum.getType() + amontTypeEnum.getValue(), beforbalance);
    }

    public BigDecimal getResultAfterBalance(ChangeAccountTypeEnum changeAccountTypeEnum, AccountTypeEnum accountTypeEnum, AmontTypeEnum amontTypeEnum) {
        return afterbalanceMap.get(changeAccountTypeEnum.getValue() + accountTypeEnum.getType() + amontTypeEnum.getValue());
    }

    public void setResultAfterBalance(ChangeAccountTypeEnum changeAccountTypeEnum, AccountTypeEnum accountTypeEnum, AmontTypeEnum amontTypeEnum, BigDecimal afterbalance) {
        afterbalanceMap.put(changeAccountTypeEnum.getValue() + accountTypeEnum.getType() + amontTypeEnum.getValue(), afterbalance);
    }

    public BigDecimal getResultAmontMap(ChangeAccountTypeEnum changeAccountTypeEnum, AccountTypeEnum accountTypeEnum, AmontTypeEnum amontTypeEnum) {
        return amontMap.get(changeAccountTypeEnum.getValue() + accountTypeEnum.getType() + amontTypeEnum.getValue());
    }

    public void setResultAmontMap(ChangeAccountTypeEnum changeAccountTypeEnum, AccountTypeEnum accountTypeEnum, AmontTypeEnum amontTypeEnum, BigDecimal amont) {
        amontMap.put(changeAccountTypeEnum.getValue() + accountTypeEnum.getType() + amontTypeEnum.getValue(), amont);
    }

}
