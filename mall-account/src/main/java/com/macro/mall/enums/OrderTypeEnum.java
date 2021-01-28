package com.macro.mall.enums;

import com.macro.mall.domain.order.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单类型
 * 内部特殊交易不进行配置
 *
 * @author dongjb
 * @date 2021/01/08
 */
public enum OrderTypeEnum {

    /**
     * 订单类型
     */
    BalancePay(OrderBalancepay.class, "余额支付订单", 101),

    BalanceRefund(OrderBalancerefund.class, "余额退款订单", 102),

    BalanceRecharge(OrderBalancerecharge.class, "余额充值订单", 103),

    BalanceTransfer(OrderBalancetransfer.class, "余额转账订单", 104),

    BalanceCash(OrderBalancecash.class, "余额提现订单", 105),

    BalanceSettlement(OrderBalancesettlement.class, "余额结算订单", 106),

    NonBalancePay(OrderNonbalancepay.class, "非余额支付订单", 201),

    NonBalanceRefund(OrderNonbalancerefund.class, "非余额退款订单", 202),

    NonBalanceTransfer(OrderNonbalancetransfer.class, "非余额转账订单", 204),

    IntegralPay(OrderIntegralpay.class, "积分消费订单", 301),

    IntegralRefund(OrderIntegralrefund.class, "积分退款订单", 302),

    IntegralRecharge(OrderIntegralrecharge.class, "积分获取订单", 303),

    IntegralSettlement(OrderIntegralsettlement.class, "积分结算订单", 306),

    CustomCurrencyPay(OrderCurrencypay .class, "电子券消费订单", 401),

    CustomCurrencyRefund(OrderCurrencyrefund.class, "电子券退款订单", 402),

    CustomCurrencyRecharge(OrderCurrencyrecharge.class, "电子券入账订单", 403),

    ShopSettlement(OrderShopsettlement.class, "二级商户结算订单", 506),

    InternalTrade(OrderInternaltrade.class, "内部交易", 8077),

    Reviewrecord(OrderInternaltrade.class, "订单审核", 8088),

    NormalTrade(OrderNormaltrade.class, "B户通用记账交易订单", 8099);

    private final Class<? extends OrderBase> cls;

    private final String name;

    private final Integer value;

    private final static Map<Integer, OrderTypeEnum> ENUM_MAP;

    static {
        ENUM_MAP = new HashMap<>();
        for (OrderTypeEnum type : values()) {
            ENUM_MAP.put(type.getValue(), type);
        }
    }

    OrderTypeEnum(Class<? extends OrderBase> cls, String name, Integer value) {
        this.cls = cls;
        this.name = name;
        this.value = value;
    }

    public Class<? extends OrderBase> getOrderClass() {
        return this.cls;
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

    public static OrderTypeEnum getEnum(Integer value) {
        return ENUM_MAP.getOrDefault(value, null);
    }

}
