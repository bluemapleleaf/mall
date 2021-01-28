package com.macro.mall.enums;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 小数处理方式
 *
 * @author dongjb
 * @date 2021/01/15
 */
public enum DecimalProcessModeEnum {

    /**
     * 小数处理方式
     */
    //入
    Up(1, BigDecimal.ROUND_UP),

    //舍
    Down(2, BigDecimal.ROUND_DOWN),

    //正无穷大
    Ceiling(3, BigDecimal.ROUND_CEILING),

    //负无穷大
    Floor(4, BigDecimal.ROUND_FLOOR),

    //四舍五入
    Half_UP(5, BigDecimal.ROUND_HALF_UP),

    //五舍六入
    Half_DOWN(6, BigDecimal.ROUND_HALF_DOWN),

    //银行家舍入（四舍六入；五，左奇入）
    Half_EVEN(7, BigDecimal.ROUND_HALF_EVEN);

    private final Integer value;

    private final Integer mode;

    private final static Map<Integer, DecimalProcessModeEnum> ENUM_MAP;

    static {
        ENUM_MAP = new HashMap<>();
        for (DecimalProcessModeEnum type : values()) {
            ENUM_MAP.put(type.getValue(), type);
        }
    }

    DecimalProcessModeEnum(Integer value, Integer mode) {
        this.value = value;
        this.mode = mode;
    }

    public Integer getValue() {
        return value;
    }

    public Integer getMode() {
        return mode;
    }

    public Boolean equals(Integer value) {
        return this.value.equals(value);
    }

    public static DecimalProcessModeEnum getEnum(Integer value) {
        return ENUM_MAP.getOrDefault(value, null);
    }

}
