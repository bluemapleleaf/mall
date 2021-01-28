package com.macro.mall.domain.result;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 积分服务结果
 * @author dongjb
 * @date 2021/01/24
 */
@Data
public class IntegralResult {
    private String custid;

    private String orderno;

    private BigDecimal amont;

    private BigDecimal beforebalance;

    private BigDecimal afterbalance;

    private BigDecimal suramont;

    private String finishdate;

}
