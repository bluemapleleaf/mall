package com.macro.mall.domain.result;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * C户交易接口返回报文
 *
 * @author dongjb
 * @date 2021/01/13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "CustomerPayResult", description = "C户交易接口返回报文")
public class CustomerPayResult {

    private String custid;

    private String rececustid;

    private String orderno;

    private BigDecimal amont;

    private BigDecimal totalbalance;

    private BigDecimal availablebalance;

    private BigDecimal beforebalance;

    private BigDecimal afterbalance;

    private BigDecimal suramont;

    private String finishdate;

}
