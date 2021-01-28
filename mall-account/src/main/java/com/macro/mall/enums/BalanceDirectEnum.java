package com.macro.mall.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 会计科目余额方向
 *
 * @author dongjb
 * @date 2021/01/15
 */
public enum BalanceDirectEnum {

    //借
    debits(1),
    //贷
    credits(2);

    private final Integer value;

    private final static Map<Integer, BalanceDirectEnum> ENUM_MAP;

    static {
        ENUM_MAP = new HashMap<>();
        for (BalanceDirectEnum type : values()) {
            ENUM_MAP.put(type.getValue(), type);
        }
    }

    BalanceDirectEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public Boolean equals(Integer value) {
        return this.value.equals(value);
    }

    public static BalanceDirectEnum getEnum(Integer value) {
        return ENUM_MAP.getOrDefault(value, null);
    }

}
