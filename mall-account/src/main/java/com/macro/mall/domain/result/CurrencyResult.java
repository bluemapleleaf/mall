package com.macro.mall.domain.result;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 自有币种交易接口返回
 *
 * @author dongjb
 * @date 2021/01/24
 */
@Data
public class CurrencyResult {
    private String custid;

    private String orderno;

    private String currencyname;

    private BigDecimal amont;

    private BigDecimal suramont;

    private String finishdate;

}
