package com.macro.mall.domain.result;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 内部交易返回
 *
 * @author dongjb
 * @date 2021/01/22
 */
@Data
public class BackInnerResult {

    private String orderno;

    private BigDecimal amont;

    private String finishdate;

}
