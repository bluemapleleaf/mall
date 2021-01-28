package com.macro.mall.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 发生额计算模式
 *
 * @author dongjb
 * @date 2021/01/15
 */
public enum CalculateModeEnum {

    /**
     * 发生额计算模式
     */

    //计算
    calculate(1),

    //兑换
    convert(2);

    private final Integer value;

    private final static Map<Integer, CalculateModeEnum> ENUM_MAP;

    static {
        ENUM_MAP = new HashMap<>();
        for (CalculateModeEnum type : values()) {
            ENUM_MAP.put(type.getValue(), type);
        }
    }

    CalculateModeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public Boolean equals(Integer value) {
        return this.value.equals(value);
    }

    public static CalculateModeEnum getEnum(Integer value) {
        return ENUM_MAP.getOrDefault(value, null);
    }

}
