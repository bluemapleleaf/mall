package com.macro.mall.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * 状态枚举类
 *
 * @author dongjb
 * @date 2021/01/06
 */
public enum StatusEnum {

    /**
     * 停用,启用,注册中
     */
    PROHIBIT(0),

    ACTIVATE(1),

    REGISTERING(2);

    private final Integer value;

    private static final Map<Integer, StatusEnum> map;

    static {
        map = new HashMap<>();
        for (StatusEnum type : values()) {
            map.put(type.getValue(), type);
        }
    }

    StatusEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public Boolean equals(Integer value) {
        return this.value.equals(value);
    }

    public static StatusEnum getEnum(Integer value) {
        return map.getOrDefault(value, null);
    }
}
