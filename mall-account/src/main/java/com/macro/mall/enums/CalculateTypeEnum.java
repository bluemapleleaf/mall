package com.macro.mall.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 发生额计算方式
 *
 * @author dongjb
 * @date 2021/01/15
 */
public enum CalculateTypeEnum {

    /**
     * 发生额计算方式
     */
    //定额
    quota(1),

    //比例
    ratio(2),

    //阶梯
    step(3),

    //满减
    fullCut(4),

    //余额
    balance(5),

    //订单额
    orderAmont(6);

    private final Integer value;

    private final static Map<Integer, CalculateTypeEnum> ENUM_MAP;

    static {
        ENUM_MAP = new HashMap<>();
        for (CalculateTypeEnum type : values()) {
            ENUM_MAP.put(type.getValue(), type);
        }
    }

    CalculateTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public Boolean equals(Integer value) {
        return this.value.equals(value);
    }

    public static CalculateTypeEnum getEnum(Integer value) {
        return ENUM_MAP.getOrDefault(value, null);
    }

}
