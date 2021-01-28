package com.macro.mall.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 处理结果状态
 *
 * @author dongjb
 * @date 2021/01/15
 */
public enum ResultStatusEnum {

    /**
     * 处理结果状态
     */
    success(1, "success"),

    failed(-1, "failed");

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    private final Integer code;

    private final String name;

    private final static Map<Integer, ResultStatusEnum> ENUMS_MAP;

    static {
        ENUMS_MAP = new HashMap<>();
        for (ResultStatusEnum type : values()) {
            ENUMS_MAP.put(type.getCode(), type);
        }
    }

    ResultStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name.toLowerCase();
    }

    public Boolean equals(Integer code) {
        return this.code.equals(code);
    }

    public static ResultStatusEnum getEnum(Integer code) {
        return ENUMS_MAP.getOrDefault(code, null);
    }

}
