package com.macro.mall.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 余额变动虚拟账户类型
 *
 * @author dongjb
 * @date 2021/01/15
 */
public enum ChangeAccountTypeEnum {

    /**
     * 余额变动虚拟账户类型
     */
    Account(0, "账务平台B户"),

    Business(1, "B类账户"),

    DisBusiness(2, "目标B类账户"),

    Cust(3, "C类账户"),

    DisCust(4, "目标C类账户");

    private final Integer value;

    private final String descript;

    private final static Map<Integer, ChangeAccountTypeEnum> ENUM_MAP;

    static {
        ENUM_MAP = new HashMap<>();
        for (ChangeAccountTypeEnum type : values()) {
            ENUM_MAP.put(type.getValue(), type);
        }
    }

    ChangeAccountTypeEnum(Integer value, String descript) {
        this.value = value;
        this.descript = descript;
    }

    public Integer getValue() {
        return value;
    }

    public String getDescript() {
        return descript;
    }

    public Boolean equals(Integer value) {
        return this.value.equals(value);
    }

    public static ChangeAccountTypeEnum getEnum(Integer value) {
        return ENUM_MAP.getOrDefault(value, null);
    }

}
