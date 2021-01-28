package com.macro.mall.service;

import com.macro.mall.domain.order.OrderCurrencypay;
import com.macro.mall.domain.order.OrderCurrencyrecharge;
import com.macro.mall.domain.order.OrderCurrencyrefund;

/**
 * 自有币种服务
 *
 * @author dongjb
 * @date 2021/01/23
 */
public interface CurrencyService {

    /**
     * 申请入账
     *
     * @param customCurrencyRechargeOrder 入账订单
     * @return true|false
     */
    boolean doApplyCurrencyRecharge(OrderCurrencyrecharge customCurrencyRechargeOrder);

    /**
     * 确认入账
     *
     * @param customCurrencyRechargeOrder 入账订单
     * @return true|false
     */
    boolean doCurrencyRecharge(OrderCurrencyrecharge customCurrencyRechargeOrder);

    /**
     * 取消入账
     *
     * @param orderNo 入账订单号
     * @return true|false
     */
    boolean doCancleCurrencyRecharge(String orderNo);

    /**
     * 申请消费
     *
     * @param customCurrencyPayOrder 消费订单
     * @return true|false
     */
    boolean doApplyCurrencyPayment(OrderCurrencypay customCurrencyPayOrder);

    /**
     * 确认消费
     *
     * @param customCurrencyPayOrder 消费订单
     * @return true|false
     */
    boolean doCurrencyPayment(OrderCurrencypay customCurrencyPayOrder);

    /**
     * 消费取消
     *
     * @param orderNo 消费订单号
     * @return true|false
     */
    boolean doCancleCurrencyPayment(String orderNo);

    /**
     * 申请退款
     *
     * @param customCurrencyRefundOrder 退款订单
     * @return true|false
     */
    boolean doApplyCurrencyRefund(OrderCurrencyrefund customCurrencyRefundOrder);

    /**
     * 确认退款
     *
     * @param customCurrencyRefundOrder 退款订单
     * @return true|false
     */
    boolean doCurrencyRefund(OrderCurrencyrefund customCurrencyRefundOrder);

    /**
     * 退款取消
     *
     * @param orderNo 退款订单号
     * @return true|false
     */
    boolean doCancleCurrencyRefund(String orderNo);

}
