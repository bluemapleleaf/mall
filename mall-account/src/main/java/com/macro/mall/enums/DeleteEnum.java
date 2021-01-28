package com.macro.mall.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 是否逻辑删除
 *
 * @author dongjb
 * @date 2021/01/06
 */
public enum DeleteEnum {

    /**
     * 已删除，末删除
     */
    ISDELETE(1),

    NOTDELETE(0);

    private final Integer value;

    private final static Map<Integer, DeleteEnum> ENUM_MAP;

    static {
        ENUM_MAP = new HashMap<>();
        for (DeleteEnum type : values()) {
            ENUM_MAP.put(type.getValue(), type);
        }
    }

    DeleteEnum(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public Boolean equals(Integer value) {
        return this.value.equals(value);
    }

    public static DeleteEnum getEnum(Integer value) {
        return ENUM_MAP.getOrDefault(value, null);
    }
}
