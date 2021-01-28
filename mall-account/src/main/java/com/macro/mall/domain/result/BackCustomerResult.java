package com.macro.mall.domain.result;

import lombok.Data;

import java.math.BigDecimal;

/**
 * C户交易接口返回（后台）
 *
 * @author dongjb
 * @date 2021/01/22
 */
@Data
public class BackCustomerResult {

    private String custid;

    private String orderno;

    private BigDecimal amont;

    private String finishdate;

}
