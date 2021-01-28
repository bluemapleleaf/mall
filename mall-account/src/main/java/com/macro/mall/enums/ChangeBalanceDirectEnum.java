package com.macro.mall.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 虚拟账户余额变动方向
 *
 * @author dongjb
 * @date 2021/01/17
 */
public enum ChangeBalanceDirectEnum {

    /**
     * 虚拟账户余额变动方向
     */

    //加
    Plus(1),
    //减
    Minus(2);

    private final Integer value;

    private final static Map<Integer, ChangeBalanceDirectEnum> ENUM_MAP;

    static {
        ENUM_MAP = new HashMap<>();
        for (ChangeBalanceDirectEnum type : values()) {
            ENUM_MAP.put(type.getValue(), type);
        }
    }

    ChangeBalanceDirectEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public Boolean equals(Integer value) {
        return this.value.equals(value);
    }

    public static ChangeBalanceDirectEnum getEnum(Integer value) {
        return ENUM_MAP.getOrDefault(value, null);
    }
}
