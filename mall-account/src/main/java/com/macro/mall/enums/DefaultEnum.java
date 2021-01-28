package com.macro.mall.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 是否默认
 *
 * @author dongjb
 * @date 2021/01/05
 */

public enum DefaultEnum {

    /**
     * 默认, 非默认
     */
    ISDEFAULT(1),

    NOTDEFAULT(0);

    private final Integer value;

    private final static Map<Integer, DefaultEnum> ENUMS_MAP;

    static {
        ENUMS_MAP = new HashMap<>();
        for (DefaultEnum type : values()) {
            ENUMS_MAP.put(type.getValue(), type);
        }
    }

    DefaultEnum(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public Boolean equals(Integer value) {
        return this.value.equals(value);
    }

    public static DefaultEnum getEnum(Integer value) {
        return ENUMS_MAP.getOrDefault(value, null);
    }
}
