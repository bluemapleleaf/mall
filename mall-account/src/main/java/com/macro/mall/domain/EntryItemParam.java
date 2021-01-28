package com.macro.mall.domain;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 会计科目流水参数
 *
 * @author dongjb
 * @date 2021/01/15
 */
@Data
public class EntryItemParam {

    /**
     * 发生额
     */
    private BigDecimal amont;

    /**
     * 第三级科目编号
     */
    private String itemcode;

    /**
     * 科目下立子账户序号
     */
    private String subitemcode;

    /**
     * 记账类型：1-借，2-贷
     */
    private int balanceDirect;

}
