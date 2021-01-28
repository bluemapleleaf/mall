package com.macro.mall.enums;

import java.util.HashMap;
import java.util.Map;


/**
 * 是否是内部特殊交易
 *
 * @author dongjb
 * @date 2021/01/16
 */
public enum InternalTrade {

    /**
     * 是否是内部特殊交易
     */
    No("不是", 0),

    Yes("是", 1);

    private final String name;

    private final Integer value;

    private final static Map<Integer, InternalTrade> ENUMS_MAP;

    static {
        ENUMS_MAP = new HashMap<>();
        for (InternalTrade type : values()) {
            ENUMS_MAP.put(type.getValue(), type);
        }
    }

    InternalTrade(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }

    public Boolean equals(Integer integer) {
        return this.value.equals(integer);
    }

    public static InternalTrade getEnum(Integer value) {
        return ENUMS_MAP.getOrDefault(value, null);
    }

}
