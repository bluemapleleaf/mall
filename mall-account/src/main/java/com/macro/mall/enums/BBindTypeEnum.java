package com.macro.mall.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * B户绑定类型
 *
 * @author dongjb
 * @date 2021/01/08
 */
public enum BBindTypeEnum {

    /**
     * 支付宝,微信PUBLIC,微信OPEN,银行卡
     */
    AliPay("ALIPAY", "支付宝"),

    WeiXinPublic("WEIXINPUBLIC", "微信PUBLIC"),

    WeiXinOpen("WEIXINOPEN", "微信OPEN"),

    BankCard("BANKCARD", "银行卡");

    private final String name;

    private final String value;

    private final static Map<String, BBindTypeEnum> ENUMS_MAP;

    static {
        ENUMS_MAP = new HashMap<>();
        for (BBindTypeEnum type : values()) {
            ENUMS_MAP.put(type.getValue(), type);
        }
    }

    BBindTypeEnum(String value, String name) {
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

    public static BBindTypeEnum getEnum(String value) {
        return ENUMS_MAP.getOrDefault(value,null);
    }
}
