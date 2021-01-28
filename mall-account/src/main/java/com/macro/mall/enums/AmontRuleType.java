package com.macro.mall.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 发生额分解规则类型
 *
 * @author dongjb
 * @date 2021/01/15
 */
public enum AmontRuleType {

    /**
     * 发生额分解规则类型
     */
    //订单额
    amont(1),
    //订单金额
    actamont(2),
    //差额
    difference(3),
    //扩展额1
    extamont1(4),
    //扩展额2
    extamont2(5),
    //扩展额3
    extamont3(6),
    //扩展额4
    extamont4(7),
    //扩展额5
    extamont5(8),
    //扩展额6
    extamont6(9),
    //扩展额7
    extamont7(10),
    //扩展额8
    extamont8(11),
    //扩展额9
    extamont9(12),
    //扩展额10
    extamont10(13);

    private final Integer value;

    private final static Map<Integer, AmontRuleType> ENUMS_MAP;

    static {
        ENUMS_MAP = new HashMap<>();
        for (AmontRuleType type : values()) {
            ENUMS_MAP.put(type.getValue(), type);
        }
    }

    AmontRuleType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public Boolean equals(Integer value) {
        return this.value.equals(value);
    }

    public static AmontRuleType getEnum(Integer value) {
        return ENUMS_MAP.getOrDefault(value, null);
    }

}
