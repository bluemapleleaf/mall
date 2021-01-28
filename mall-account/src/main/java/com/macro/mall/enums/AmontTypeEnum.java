package com.macro.mall.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 余额类型
 *
 * @author dongjb
 * @date 2021/01/15
 */
public enum AmontTypeEnum {

    //余额
    balance(1),
    //折算金额
    money(2);

    private final Integer value;

    private final static Map<Integer, AmontTypeEnum> ENUM_MAP;

    static {
        ENUM_MAP = new HashMap<>();
        for (AmontTypeEnum type : values()) {
            ENUM_MAP.put(type.getValue(), type);
        }
    }

    AmontTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public Boolean equals(Integer value) {
        return this.value.equals(value);
    }

    public static AmontTypeEnum getEnum(Integer value) {
        return ENUM_MAP.getOrDefault(value, null);
    }

}
