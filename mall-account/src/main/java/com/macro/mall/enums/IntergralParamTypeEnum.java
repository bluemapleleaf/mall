package com.macro.mall.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 积分参数
 *
 * @author dongjb
 * @date 2021/01/22
 */
public enum IntergralParamTypeEnum {

    /**
     * 积分价值比例
     */
    ratio(1);

    private final Integer value;

    private final static Map<Integer, IntergralParamTypeEnum> ENUM_MAP;

    static {
        ENUM_MAP = new HashMap<>();
        for (IntergralParamTypeEnum type : values()) {
            ENUM_MAP.put(type.getValue(), type);
        }
    }

    IntergralParamTypeEnum(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public Boolean equals(Integer value) {
        return this.value.equals(value);
    }

    public static IntergralParamTypeEnum getEnum(Integer value) {
        return ENUM_MAP.getOrDefault(value, null);
    }

}
