package com.macro.mall.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 发生额分解类型
 *
 * @author dongjb
 * @date 2021/01/15
 */
public enum DecomposeTypeEnum {

    /**
     * 发生额分解类型
     */

    //价外
    outer(1),

    //价内
    inner(2);

    private final Integer value;

    private final static Map<Integer, DecomposeTypeEnum> ENUM_MAP;

    static {
        ENUM_MAP = new HashMap<>();
        for (DecomposeTypeEnum type : values()) {
            ENUM_MAP.put(type.getValue(), type);
        }
    }

    DecomposeTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public Boolean equals(Integer value) {
        return this.value.equals(value);
    }

    public static DecomposeTypeEnum getEnum(Integer value) {
        return ENUM_MAP.getOrDefault(value, null);
    }

}
