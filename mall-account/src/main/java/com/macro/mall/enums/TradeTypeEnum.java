package com.macro.mall.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 内部交易类型
 *
 * @author dongjb
 * @date 2021/01/22
 */
public enum TradeTypeEnum {

    /**
     * 内部交易类型
     */
    normal("普通交易", 0),

    recovery("冲正交易", 1);

    private final String name;

    private final Integer value;

    private final static Map<Integer, TradeTypeEnum> ENUM_MAP;

    static {
        ENUM_MAP = new HashMap<>();
        for (TradeTypeEnum type : values()) {
            ENUM_MAP.put(type.getValue(), type);
        }
    }

    TradeTypeEnum(String name, Integer value) {
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

    public static TradeTypeEnum getEnum(Integer value) {
        return ENUM_MAP.getOrDefault(value, null);
    }

}
