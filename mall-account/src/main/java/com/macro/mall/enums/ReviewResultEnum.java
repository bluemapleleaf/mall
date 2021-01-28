package com.macro.mall.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单审核结果
 *
 * @author dongjb
 * @date 2021/01/13
 */
public enum ReviewResultEnum {

    /**
     * 不通过,通过
     */
    notpass(0, "不通过"),

    pass(1, "通过");

    private final Integer value;

    private final String description;


    private final static Map<Integer, ReviewResultEnum> ENUMS_MAP;

    static {
        ENUMS_MAP = new HashMap<>();
        for (ReviewResultEnum type : values()) {
            ENUMS_MAP.put(type.getValue(), type);
        }
    }

    ReviewResultEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    public Integer getValue() {
        return value;
    }

    public String getDescription() { return description; }

    public Boolean equals(Integer value) {
        return this.value.equals(value);
    }

    public static ReviewResultEnum getEnum(Integer value) {
        return ENUMS_MAP.getOrDefault(value, null);
    }
}
