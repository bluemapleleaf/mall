package com.macro.mall.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 客户类型
 *
 * @author dongjb
 * @date 2021/01/06
 */
public enum CustomerType {

    /**
     * B户,C户
     */
    BUSINESS(1),

    CUSTOMER(0);

    private final Integer value;

    private final static Map<Integer, CustomerType> ENUMS_MAP;

    static {
        ENUMS_MAP = new HashMap<>();
        for (CustomerType type : values()) {
            ENUMS_MAP.put(type.getValue(), type);
        }
    }

    CustomerType(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public Boolean equals(Integer value) {
        return this.value.equals(value);
    }

    public static CustomerType getEnum(Integer value) {
        return ENUMS_MAP.getOrDefault(value, null);
    }

}
