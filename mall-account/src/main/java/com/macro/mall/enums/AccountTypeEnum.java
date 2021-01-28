package com.macro.mall.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 虚拟账户类型
 *
 * @author dongjb
 * @date 2021/01/06
 */
public enum AccountTypeEnum {

    /**
     * 总余额账户,可用余额账户,红包账户,优惠券账户,积分账户
     */
    ALL("00", "ALL", "总余额账户"),

    CHANGE("01", "CHANGE", "可用余额账户"),

    RED("02", "RED", "红包账户"),

    COUPON("03", "COUPON", "优惠券账户"),

    INTEGRAL("04", "INTEGRAL", "积分账户");

    public String getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    private final String code;

    private final String type;

    private final String name;

    private static final Map<String, AccountTypeEnum> ENUMS_MAP;

    static {
        ENUMS_MAP = new HashMap<>();
        for (AccountTypeEnum type : values()) {
            ENUMS_MAP.put(type.getType(), type);
        }
    }

    AccountTypeEnum(String code, String type, String name) {
        this.code = code;
        this.type = type.toUpperCase();
        this.name = name;
    }

    public Boolean equals(String type) {
        return this.type.equals(type.toUpperCase());
    }

    public static AccountTypeEnum getEnum(String type) {
        return ENUMS_MAP.getOrDefault(type.toUpperCase(), null);
    }
}
