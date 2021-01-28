package com.macro.mall.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * C户绑定类型
 *
 * @author dongjb
 * @date 2021/01/06
 */
public enum CBindTypeEnum {

    /**
     * 支付宝,微信号,银行卡
     */
    AliPay("ALIPAY", "支付宝"),

    WeiXin("WEIXIN", "微信号"),

    BankCard("BANKCARD", "银行卡");

    private final String name;

    private final String value;

    private final static Map<String, CBindTypeEnum> ENUMS_MAP;

    static {
        ENUMS_MAP = new HashMap<>();
        for (CBindTypeEnum type : values()) {
            ENUMS_MAP.put(type.getValue(), type);
        }
    }

    CBindTypeEnum(String value, String name) {
        this.value = value.toUpperCase();
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public Boolean equals(String value) {
        return this.value.equals(value.toUpperCase());
    }

    public static CBindTypeEnum getEnum(String value) {
        return ENUMS_MAP.getOrDefault(value, null);
    }
}
