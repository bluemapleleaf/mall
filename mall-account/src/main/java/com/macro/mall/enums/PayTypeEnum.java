package com.macro.mall.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付（充值）类型
 *
 * @author dongjb
 * @date 2021/01/14
 */
public enum PayTypeEnum {

    /**
     * 支付类型枚举
     */
    alipay("alipay", 1, "支付宝"),

    alimobilepay("alimobilepay", 2, "支付宝移动支付"),

    alidirectpay("alidirectpay", 3, "支付宝即时到账"),

    aliwappay("aliwappay", 4, "支付宝wap支付"),

    alitradebybuyerpay("alitradebybuyerpay", 5, "支付宝担保交易"),

    tenpay("tenpay", 6, "财付通"),

    weixinpay("weixinpay", 7, "微信支付"),

    weixinapppay("weixinapppay", 8, "微信APP支付"),

    weixinJSAPIpay("weixinJSAPIpay", 9, "微信JSAPI支付"),

    scrcupay("scrcupay", 10, "农信支付"),

    balance("balance", 11, "余额支付"),

    hsh("hsh", 12, "惠支付"),

    otherspay("otherspay", 13, "其他方式");

    private final String name;

    private final Integer value;

    private final String description;

    private final static Map<String, PayTypeEnum> NAME_MAP;

    private final static Map<Integer, PayTypeEnum> VALUE_MAP;

    static {
        NAME_MAP = new HashMap<>();
        VALUE_MAP = new HashMap<>();
        for (PayTypeEnum type : values()) {
            NAME_MAP.put(type.getName(), type);
            VALUE_MAP.put(type.getValue(), type);
        }
    }

    PayTypeEnum(String name, Integer value, String description) {
        this.name = name.toLowerCase();
        this.value = value;
        this.description = description;
    }

    public Integer getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Boolean equals(Integer integer) {
        return this.value.equals(integer);
    }

    public Boolean equals(String name) {
        return this.name.toLowerCase().equals(name.toLowerCase());
    }

    public PayTypeEnum getEnum(Integer value) {
        return VALUE_MAP.getOrDefault(value, null);
    }

    public static PayTypeEnum getEnum(String name) {
        return NAME_MAP.getOrDefault(name, null);
    }
}
