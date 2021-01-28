package com.macro.mall.service.impl;

import com.macro.mall.ams.model.*;
import com.macro.mall.domain.BusinessAccount;
import com.macro.mall.domain.CustAccount;
import com.macro.mall.domain.order.*;
import com.macro.mall.enums.*;
import com.macro.mall.service.BusinessService;
import com.macro.mall.service.CustomerService;
import com.macro.mall.service.IntegralService;
import com.macro.mall.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 积分服务
 *
 * @author dongjb
 * @date 2021/01/22
 */
@Slf4j
@Service
public class IntegralServiceImpl implements IntegralService {
    @Autowired
    CustomerService customerService;

    @Autowired
    OrderService orderService;

    @Autowired
    BusinessService businessService;

    @Override
    public boolean doApplyRecharge(OrderIntegralrecharge integralRechargeOrder) {
        CustAccount custAccount = customerService.findCustAccountByCustId(integralRechargeOrder.getCustid());
        if (custAccount == null) {
            log.error("客户信息不存在：" + integralRechargeOrder.getCustid());
        }
        integralRechargeOrder.setPaytype(PayTypeEnum.otherspay.getName());
        integralRechargeOrder.setPaytypename(PayTypeEnum.otherspay.getDescription());
        integralRechargeOrder.setRececustid(integralRechargeOrder.getCustid());


        return orderService.doCreateOrder(integralRechargeOrder) && orderService.doChangeOrderSatus(integralRechargeOrder, OrderStatusEnum.paySuccess);
    }

    @Override
    public boolean doRecharge(OrderIntegralrecharge integralRechargeOrder) {
        OrderBase recharge = orderService.findOrderByOrderNo(integralRechargeOrder.getOrderNo());
        AmsOrderIntegralrecharge rechargeExt = orderService.findOrderIntegralrecharge(integralRechargeOrder.getOrderNo());


        if (recharge == null) {
            log.error("找不到入账订单！");
            return false;
        }
        if (orderService.doChangeOrderSatus(recharge,OrderStatusEnum.success)) {
            integralRechargeOrder.setAmont(recharge.getAmont());
            integralRechargeOrder.setCustid(recharge.getCustid());
            integralRechargeOrder.setBeforebalance(rechargeExt.getBeforebalance());
            integralRechargeOrder.setAfterbalance(rechargeExt.getAfterbalance());
            integralRechargeOrder.setLastmodifydate(recharge.getLastmodifydate());
            return true;
        } else {
            orderService.doChangeOrderSatus(recharge, OrderStatusEnum.failed);
            return false;
        }
    }

    @Override
    public boolean doCancleRecharge(String orderNo) {
        OrderBase integralRechargeOrder = orderService.findOrderByOrderNo(orderNo);
        return orderService.doChangeOrderSatus(integralRechargeOrder, OrderStatusEnum.cancle);
    }

    @Override
    public boolean doApplyPayment(OrderIntegralpay integralPayOrder) {
        CustAccount custAccount = customerService.findCustAccountByCustId(integralPayOrder.getCustid());
        if (custAccount == null) {
            log.error("客户信息不存在：" + integralPayOrder.getCustid());
            return false;
        }
        integralPayOrder.setPaytype(PayTypeEnum.otherspay.getName());
        integralPayOrder.setPaytypename(PayTypeEnum.otherspay.getDescription());
        integralPayOrder.setRatio(orderService.getIntegralRatio());
        if (!orderService.doValidateOrder(integralPayOrder)) {
            log.error("消费失败：剩余积分不足！");
        }
        return orderService.doCreateOrder(integralPayOrder) && orderService.doChangeOrderSatus(integralPayOrder, OrderStatusEnum.paySuccess);
    }

    @Override
    public boolean doPayment(OrderIntegralpay integralPayOrder) {
        OrderBase pay =orderService.findOrderByOrderNo(integralPayOrder.getOrderNo());
        AmsOrderIntegralpay payExt =orderService.findOrderIntegralpay(integralPayOrder.getOrderNo());


        if (pay == null) {
            log.error("找不到入账订单！");
            return false;
        }
        if (orderService.doChangeOrderSatus(pay, OrderStatusEnum.success)) {
            integralPayOrder.setAmont(pay.getAmont());
            integralPayOrder.setBeforebalance(payExt.getBeforebalance());
            integralPayOrder.setAfterbalance(payExt.getAfterbalance());
            integralPayOrder.setRecebeforebalance(payExt.getRecebeforebalance());
            integralPayOrder.setReceafterbalance(payExt.getReceafterbalance());
            integralPayOrder.setCustid(pay.getCustid());
            integralPayOrder.setLastmodifydate(pay.getLastmodifydate());
            return true;
        } else {
            orderService.doChangeOrderSatus(pay, OrderStatusEnum.failed);
            return false;
        }
    }

    @Override
    public boolean doCanclePayment(String orderNo) {

        OrderBase orderBase = orderService.findOrderByOrderNo(orderNo);
        return orderService.doChangeOrderSatus(orderBase, OrderStatusEnum.cancle);
    }

    @Override
    public boolean doApplyRefund(OrderIntegralrefund integralRefundOrder) {
        OrderBase payOrder = orderService.findOrderByOrderNo(integralRefundOrder.getOrigorderno());
        if (payOrder == null) {
            log.error("找不到原支付订单！");
            return false;
        }
        if (!integralRefundOrder.getBusinessid().equals(payOrder.getBusinessid())) {
            log.error("退款请求非法，没有操作权限！");
            return false;
        }
        integralRefundOrder.setPaytype(PayTypeEnum.otherspay.getName());
        integralRefundOrder.setPaytypename(PayTypeEnum.otherspay.getDescription());
        if (!orderService.validateRefundPayTypeAndStatus(payOrder, integralRefundOrder, 0)) {
            log.error("原订单不允许进行退款操作！");
            return false;
        }
        BusinessAccount businessAccount = businessService.findBusinessAccountByBusinessId(payOrder.getBusinessid());
        CustAccount custAccount = customerService.findCustAccountByCustId(payOrder.getCustid());
        if (custAccount == null) {
            log.error("客户信息不存在：" + payOrder.getCustid());
            return false;
        }
        if (!orderService.doValidateOrder(integralRefundOrder)) {
            log.error("退款金额超过原订单剩余可退款额");
            return false;
        }
        integralRefundOrder.setBusinessid(businessAccount.getBusinessid());
        integralRefundOrder.setBusinessname(businessAccount.getBusinessname());
        integralRefundOrder.setChannel(businessAccount.getChannel());
        integralRefundOrder.setCustid(custAccount.getCustid());
        integralRefundOrder.setDescription("积分消费订单：" + integralRefundOrder.getOrigorderno() + " 退款");
        integralRefundOrder.setRatio(payOrder.getRatio());
        integralRefundOrder.setRececustid(integralRefundOrder.getCustid());
        if (orderService.doCreateOrder(integralRefundOrder) && orderService.doChangeOrderSatus(integralRefundOrder, OrderStatusEnum.reviewing)) {
            integralRefundOrder.setSuramont(payOrder.getAmont().subtract(payOrder.getRefundamont().add(integralRefundOrder.getAmont())));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean doRefund(OrderIntegralrefund integralRefundOrder) {
        OrderBase refundOrder = orderService.findOrderByOrderNo(integralRefundOrder.getOrderNo());
        AmsOrderIntegralrefund refundOrderExt = orderService.findOrderIntegralRefund(integralRefundOrder.getOrderNo());
        if (refundOrder == null) {
            log.error("找不到退款支付订单！");
            return false;
        }
        OrderBase payOrder = orderService.findOrderByOrderNo(refundOrderExt.getOrigorderno());
        if (payOrder == null) {
            log.error("找不到原支付订单！");
            return false;
        }
        if (!refundOrder.getBusinessid().equals(payOrder.getBusinessid())) {
            log.error("退款请求非法，没有操作权限！");
            return false;
        }
        if (!orderService.validateRefundPayTypeAndStatus(payOrder, refundOrder, 1)) {
            log.error("原订单不允许进行退款操作！");
            return false;
        }
        if (orderService.doChangeOrderSatus(refundOrder, OrderStatusEnum.success)) {
            integralRefundOrder.setCustid(refundOrder.getCustid());
            integralRefundOrder.setBeforebalance(refundOrderExt.getBeforebalance());
            integralRefundOrder.setAfterbalance(refundOrderExt.getAfterbalance());
            integralRefundOrder.setRecebeforebalance(refundOrderExt.getRecebeforebalance());
            integralRefundOrder.setReceafterbalance(refundOrderExt.getReceafterbalance());
            integralRefundOrder.setAmont(refundOrder.getAmont());
            integralRefundOrder.setLastmodifydate(refundOrder.getLastmodifydate());
            integralRefundOrder.setSuramont(payOrder.getAmont().subtract(payOrder.getRefundamont()));
            return true;
        } else {
            orderService.doChangeOrderSatus(refundOrder, OrderStatusEnum.failed);
            log.error("退款失败");
            return false;
        }
    }

    @Override
    public boolean doCancleRefund(String orderNo) {
        OrderBase orderBase  = orderService.findOrderByOrderNo(orderNo);
        return orderService.doChangeOrderSatus(orderBase, OrderStatusEnum.cancle);
    }

    @Override
    public boolean doApplyIntegralSettlement(OrderIntegralsettlement integralSettlementOrder) {
        BusinessAccount businessAccount = businessService.findBusinessAccountByBusinessId(integralSettlementOrder.getBusinessid());
        if (businessAccount == null) {
            log.error("找不到待结算的B账户：" + integralSettlementOrder.getBusinessid());
            return false;
        }
        AmsBusinessSubaccount businessSubAccount = businessAccount.getSubAccountByType(AccountTypeEnum.INTEGRAL);
        AmsBusinessBindinfo businessBindInfo = businessService.findBusinessBindInfoById(integralSettlementOrder.getBindinfoid());
        if (businessBindInfo == null) {
            log.error("找不到结算账户：" + integralSettlementOrder.getBindinfoid());
            return false;
        } else {
            if (!businessBindInfo.getStatus().equals(StatusEnum.ACTIVATE.getValue())) {
                log.error("结算账户不可用：" + businessBindInfo.getId());
                return false;
            }
        }
        integralSettlementOrder.setAmont(businessSubAccount.getBalance());
        integralSettlementOrder.setRatio(businessSubAccount.getMoney().divide(businessSubAccount.getBalance()).setScale(6, DecimalProcessModeEnum.Half_UP.getMode()));
        integralSettlementOrder.setMoney(businessSubAccount.getMoney());
        if (!orderService.doValidateOrder(integralSettlementOrder)) {
            log.error("结算申请失败：账户余额不足！");
            return false;
        }
        BusinessAccount operBusinessAccount = businessService.findOperBusinessAccount();
        if (operBusinessAccount != null) {
            integralSettlementOrder.setBusinessname(businessAccount.getBusinessname());
            integralSettlementOrder.setBusinesstradeno("");
            integralSettlementOrder.setBusinesssubaccountcode(businessSubAccount.getCode());
            integralSettlementOrder.setCustid("");
            integralSettlementOrder.setPaytype(PayTypeEnum.otherspay.getName());
            integralSettlementOrder.setPaytypename(PayTypeEnum.otherspay.getDescription());
            integralSettlementOrder.setChannel(operBusinessAccount.getChannel());
            integralSettlementOrder.setBbindtype(businessBindInfo.getType());
            try {
                switch (BBindTypeEnum.getEnum(businessBindInfo.getType())) {
                    case AliPay:
                        integralSettlementOrder.setBuyername(businessBindInfo.getPartner());
                        integralSettlementOrder.setBuyeraccount(businessBindInfo.getSellerEmail());
                        break;
                    case WeiXinPublic:
                    case WeiXinOpen:
                        integralSettlementOrder.setBuyername(businessBindInfo.getPartner());
                        integralSettlementOrder.setBuyeraccount(businessBindInfo.getPartner());
                        break;
                    case BankCard:
                        integralSettlementOrder.setBuyername(businessBindInfo.getName());
                        integralSettlementOrder.setBuyeraccount(businessBindInfo.getAccount());
                        integralSettlementOrder.setBuyerbankname(businessBindInfo.getBankname());
                        break;
                    default:
                        log.error("结算申请失败：结算账户类型非法！");
                }
            } catch (Exception e) {
                log.error("结算申请失败：结算账户类型非法！");
            }
            integralSettlementOrder.setRecebusinessid(integralSettlementOrder.getBusinessid());
            return orderService.doCreateOrder(integralSettlementOrder) && orderService.doChangeOrderSatus(integralSettlementOrder, OrderStatusEnum.reviewing);
        } else {
            log.error("结算申请失败：找不到账户平台账户");
            return false;
        }
    }

    @Override
    public boolean doIntegralSettlement(OrderIntegralsettlement integralSettlementOrder) {
        AmsUser sysUser = customerService.findUserById(integralSettlementOrder.getUserid());
        OrderBase settlementOrder = orderService.findOrderByOrderNo(integralSettlementOrder.getOrderNo());
        if (settlementOrder == null) {
            log.error("找不到余额结算订单");
            return false;
        }
        settlementOrder.setUserid(sysUser.getId());
        BusinessAccount businessAccount = businessService.findBusinessAccountByBusinessId(settlementOrder.getBusinessid());
        if (businessAccount == null) {
            log.error("找不到待结算的B账户：" + settlementOrder.getBusinessid());
            return false;
        }
        integralSettlementOrder.setStatus(OrderStatusEnum.success.getValue());
        if (orderService.doOrderReview(settlementOrder.getOrderNo(), "审核通过", ReviewResultEnum.pass, sysUser.getId(), sysUser.getName()) && orderService.doUpdateOrder(settlementOrder)) {
            integralSettlementOrder.setAmont(settlementOrder.getAmont());
            integralSettlementOrder.setLastmodifydate(settlementOrder.getLastmodifydate());
            integralSettlementOrder.setBusinessid(settlementOrder.getBusinessid());
            return true;
        } else {
            orderService.doChangeOrderSatus(settlementOrder, OrderStatusEnum.failed);
            return false;
        }
    }

    @Override
    public boolean doCancleIntegralSettlement(String orderNo, String content, String userid) {
        AmsUser sysUser = customerService.findUserById(userid);
        if (orderService.doOrderReview(orderNo, content, ReviewResultEnum.notpass, sysUser.getId(), sysUser.getName())) {
            OrderBase integralSettlementOrder = orderService.findOrderByOrderNo(orderNo);
            integralSettlementOrder.setUserid(userid);
            integralSettlementOrder.setStatus(OrderStatusEnum.cancle.getValue());
            return orderService.doChangeOrderSatus(integralSettlementOrder, OrderStatusEnum.cancle);
        } else {
            return false;
        }
    }

}
