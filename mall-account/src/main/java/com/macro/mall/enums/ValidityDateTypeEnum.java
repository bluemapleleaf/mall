package com.macro.mall.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 有效期类型
 *
 * @author dongjb
 * @date 2021/01/22
 */
public enum ValidityDateTypeEnum {

    /**
     * 会计日期
     */
    accountdate(0),

    /**
     * 系统日期
     */
    sysdate(1);

    private final Integer value;

    private final static Map<Integer, ValidityDateTypeEnum> ENUM_MAP;

    static {
        ENUM_MAP = new HashMap<>();
        for (ValidityDateTypeEnum type : values()) {
            ENUM_MAP.put(type.getValue(), type);
        }
    }

    ValidityDateTypeEnum(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public Boolean equals(Integer value) {
        return this.value.equals(value);
    }

    public static ValidityDateTypeEnum getEnum(Integer value) {
        return ENUM_MAP.getOrDefault(value, null);
    }

}
