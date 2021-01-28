package com.macro.mall.service.impl;

import com.macro.mall.ams.model.AmsOrderCurrencypay;
import com.macro.mall.ams.model.AmsOrderCurrencyrecharge;
import com.macro.mall.ams.model.AmsOrderCurrencyrefund;
import com.macro.mall.domain.BusinessAccount;
import com.macro.mall.domain.CustAccount;
import com.macro.mall.domain.order.OrderBase;
import com.macro.mall.domain.order.OrderCurrencypay;
import com.macro.mall.domain.order.OrderCurrencyrecharge;
import com.macro.mall.domain.order.OrderCurrencyrefund;
import com.macro.mall.enums.OrderStatusEnum;
import com.macro.mall.enums.PayTypeEnum;
import com.macro.mall.service.BusinessService;
import com.macro.mall.service.CurrencyService;
import com.macro.mall.service.CustomerService;
import com.macro.mall.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 自有币种服务
 *
 * @author dongjb
 * @date 2021/01/23
 */
@Slf4j
@Service
public class CurrencyServiceImpl implements CurrencyService {
    @Autowired
    CustomerService customerService;

    @Autowired
    OrderService orderService;

    @Autowired
    BusinessService businessService;


    @Override
    public boolean doApplyCurrencyRecharge(OrderCurrencyrecharge customCurrencyRechargeOrder) {
        CustAccount custAccount = customerService.findCustAccountByCustId(customCurrencyRechargeOrder.getCustid());
        if (custAccount == null) {
            log.error("客户信息不存在：" + customCurrencyRechargeOrder.getCustid());
            return false;
        }
        customCurrencyRechargeOrder.setPaytype(PayTypeEnum.otherspay.getName());
        customCurrencyRechargeOrder.setPaytypename(PayTypeEnum.otherspay.getDescription());
        return orderService.doCreateOrder(customCurrencyRechargeOrder)
                && orderService.doChangeOrderSatus(customCurrencyRechargeOrder, OrderStatusEnum.paySuccess);
    }

    @Override
    public boolean doCurrencyRecharge(OrderCurrencyrecharge customCurrencyRechargeOrder) {
        OrderBase recharge = orderService.findOrderByOrderNo(customCurrencyRechargeOrder.getOrderNo());
        AmsOrderCurrencyrecharge rechargeExt = orderService.findOrderCurrencyrecharge(customCurrencyRechargeOrder.getOrderNo());
        if (recharge == null) {
            log.error("找不到入账订单！");
        }
        if (orderService.doCreateOrder(recharge)) {
            customCurrencyRechargeOrder.setCurrencyname(rechargeExt.getCurrencyname());
            customCurrencyRechargeOrder.setAmont(recharge.getAmont());
            customCurrencyRechargeOrder.setCustid(recharge.getCustid());
            customCurrencyRechargeOrder.setLastmodifydate(recharge.getLastmodifydate());
            return true;
        } else {
            orderService.doChangeOrderSatus(recharge, OrderStatusEnum.failed);
            return false;
        }
    }

    @Override
    public boolean doCancleCurrencyRecharge(String orderNo) {
        OrderBase recharge = orderService.findOrderByOrderNo(orderNo);
        return orderService.doChangeOrderSatus(recharge, OrderStatusEnum.cancle);
    }

    @Override
    public boolean doApplyCurrencyPayment(OrderCurrencypay customCurrencyPayOrder) {
        CustAccount custAccount = customerService.findCustAccountByCustId(customCurrencyPayOrder.getCustid());
        if (custAccount == null) {
            log.error("客户信息不存在：" + customCurrencyPayOrder.getCustid());
        }
        customCurrencyPayOrder.setPaytype(PayTypeEnum.otherspay.getName());
        customCurrencyPayOrder.setPaytypename(PayTypeEnum.otherspay.getDescription());
        return (orderService.doCreateOrder(customCurrencyPayOrder) && orderService.doChangeOrderSatus(customCurrencyPayOrder, OrderStatusEnum.paySuccess));
    }

    @Override
    public boolean doCurrencyPayment(OrderCurrencypay customCurrencyPayOrder) {
        OrderBase pay = orderService.findOrderByOrderNo(customCurrencyPayOrder.getOrderNo());
        AmsOrderCurrencypay payExt = orderService.findOrderCurrencypay(customCurrencyPayOrder.getOrderNo());
        if (pay == null) {
            log.error("找不到入账订单！");
            return false;
        }
        if (orderService.doUpdateOrder(pay)) {
            customCurrencyPayOrder.setAmont(pay.getAmont());
            customCurrencyPayOrder.setCustid(pay.getCustid());
            customCurrencyPayOrder.setCurrencyname(payExt.getCurrencyname());
            customCurrencyPayOrder.setLastmodifydate(pay.getLastmodifydate());
            return true;
        } else {
            orderService.doChangeOrderSatus(pay, OrderStatusEnum.failed);
            return false;
        }
    }

    @Override
    public boolean doCancleCurrencyPayment(String orderNo) {
        OrderBase orderBase = orderService.findOrderByOrderNo(orderNo);
        return orderService.doChangeOrderSatus(orderBase, OrderStatusEnum.cancle);
    }

    @Override
    public boolean doApplyCurrencyRefund(OrderCurrencyrefund customCurrencyRefundOrder) {
        OrderBase payOrder = orderService.findOrderByOrderNo(customCurrencyRefundOrder.getOrigorderno());
        if (payOrder == null) {
            log.error("找不到原支付订单！");
        }
        if (!customCurrencyRefundOrder.getBusinessid().equals(payOrder.getBusinessid())) {
            log.error("退款请求非法，没有操作权限！");
        }
        customCurrencyRefundOrder.setPaytype(PayTypeEnum.otherspay.getName());
        customCurrencyRefundOrder.setPaytypename(PayTypeEnum.otherspay.getDescription());
        if (!orderService.validateRefundPayTypeAndStatus(payOrder, customCurrencyRefundOrder, 0)) {
            log.error("原订单不允许进行退款操作！");
        }
        BusinessAccount businessAccount = businessService.findBusinessAccountByBusinessId(payOrder.getBusinessid());
        CustAccount custAccount = customerService.findCustAccountByCustId(payOrder.getCustid());
        if (custAccount == null) {
            log.error("客户信息不存在：" + payOrder.getCustid());
        }
        if (!orderService.doValidateOrder(customCurrencyRefundOrder)) {
            log.error("退款金额超过原订单剩余可退款额");
        }
        customCurrencyRefundOrder.setBusinessid(businessAccount.getBusinessid());
        customCurrencyRefundOrder.setBusinessname(businessAccount.getBusinessname());
        customCurrencyRefundOrder.setChannel(businessAccount.getChannel());
        customCurrencyRefundOrder.setCustid(custAccount.getCustid());
        customCurrencyRefundOrder.setDescription("自有币种支付订单：" + customCurrencyRefundOrder.getOrigorderno() + " 退款");
        if (orderService.doCreateOrder(customCurrencyRefundOrder) && orderService.doChangeOrderSatus(customCurrencyRefundOrder, OrderStatusEnum.reviewing)) {
            customCurrencyRefundOrder.setSuramont(payOrder.getAmont().subtract(payOrder.getRefundamont().add(customCurrencyRefundOrder.getAmont())));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean doCurrencyRefund(OrderCurrencyrefund customCurrencyRefundOrder) {
        OrderBase refundOrder  = orderService.findOrderByOrderNo(customCurrencyRefundOrder.getOrderNo());
        AmsOrderCurrencyrefund refundOrderExt  = orderService.findOrderCurrencyrefund(customCurrencyRefundOrder.getOrderNo());
        if (refundOrder == null) {
            log.error("找不到退款支付订单！");
        }
        OrderBase payOrder = orderService.findOrderByOrderNo(refundOrderExt.getOrigorderno());
        if (payOrder == null) {
            log.error("找不到原支付订单！");
        }
        if (!refundOrder.getBusinessid().equals(payOrder.getBusinessid())) {
            log.error("退款请求非法，没有操作权限！");
        }
        if (!orderService.validateRefundPayTypeAndStatus(payOrder, refundOrder, 1)) {
            log.error("原订单不允许进行退款操作！");
        }
        customCurrencyRefundOrder.setStatus(OrderStatusEnum.success.getValue());
        if (orderService.doUpdateOrder(customCurrencyRefundOrder)) {
            customCurrencyRefundOrder.setCustid(refundOrder.getCustid());
            customCurrencyRefundOrder.setCurrencyname(refundOrderExt.getCurrencyname());
            customCurrencyRefundOrder.setAmont(refundOrder.getAmont());
            customCurrencyRefundOrder.setLastmodifydate(refundOrder.getLastmodifydate());
            customCurrencyRefundOrder.setSuramont(payOrder.getAmont().subtract(payOrder.getRefundamont()));
            return true;
        } else {
            orderService.doChangeOrderSatus(refundOrder, OrderStatusEnum.failed);
            log.error("退款失败");
            return false;
        }
    }

    @Override
    public boolean doCancleCurrencyRefund(String orderNo) {
        OrderBase orderBase = orderService.findOrderByOrderNo(orderNo);
        return orderService.doChangeOrderSatus(orderBase, OrderStatusEnum.cancle);
    }

}
