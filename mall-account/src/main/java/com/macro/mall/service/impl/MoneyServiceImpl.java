package com.macro.mall.service.impl;

import com.macro.mall.ams.model.*;
import com.macro.mall.convert.OrderNonBalancepayConvert;
import com.macro.mall.domain.BusinessAccount;
import com.macro.mall.domain.CustAccount;
import com.macro.mall.domain.order.*;
import com.macro.mall.enums.*;
import com.macro.mall.service.BusinessService;
import com.macro.mall.service.CustomerService;
import com.macro.mall.service.MoneyService;
import com.macro.mall.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * C户信息服务接口
 *
 * @author dongjb
 * @date 2021/01/13
 */

@Slf4j
@Service
public class MoneyServiceImpl implements MoneyService {
    @Autowired
    CustomerService customerService;

    @Autowired
    BusinessService businessService;

    @Autowired
    OrderService orderService;

    @Override
    public boolean doApplyRecharge(OrderBalancerecharge balanceRechargeOrder) {
        PayTypeEnum payTypeEnum = PayTypeEnum.getEnum(balanceRechargeOrder.getPaytype());
        balanceRechargeOrder.setPaytypename(payTypeEnum.getDescription());

        CustAccount custAccount = customerService.findCustAccountByCustId(balanceRechargeOrder.getCustid());
        if (custAccount == null) {
            log.error("客户信息不存在：" + balanceRechargeOrder.getCustid());
            return false;
        }
        BusinessAccount YYbusinessAccount = businessService.findOperBusinessAccount();
        if (YYbusinessAccount != null) {
            log.error("找不到账户平台账户");
            return false;
        }

        balanceRechargeOrder.setBusinessid(YYbusinessAccount.getBusinessid());
        balanceRechargeOrder.setBusinessname(YYbusinessAccount.getBusinessname());
        balanceRechargeOrder.setBusinesssubaccountcode(YYbusinessAccount.getSubAccountByType(AccountTypeEnum.CHANGE).getCode());
        balanceRechargeOrder.setCustsubaccountcode(custAccount.getSubAccountByType(AccountTypeEnum.CHANGE).getCode());
        balanceRechargeOrder.setDescription("余额充值交易");
        balanceRechargeOrder.setRececustid(balanceRechargeOrder.getCustid());
        boolean ret = orderService.doCreateOrder(balanceRechargeOrder);
        if(!ret) {
            log.error("创建订单失败");
            return false;
        }

        return orderService.doChangeOrderSatus(balanceRechargeOrder, OrderStatusEnum.paySuccess);

    }

    @Override
    public BigDecimal[] doRecharge(OrderBalancerecharge balanceRechargeOrder) {
        OrderBase rechargeOrder = orderService.findOrderByOrderNo(balanceRechargeOrder.getOrderNo());
        AmsOrderBalancerecharge rechargeOrderExt = orderService.findOrderBalancerecharge(balanceRechargeOrder.getOrderNo());
        if (rechargeOrder == null) {
            log.error("找不到充值订单");
            return null;
        }
        if (!rechargeOrder.getPaytype().equals(balanceRechargeOrder.getPaytype())) {
            log.error("支付类型不匹配");
            return null;
        }
        CustAccount custAccount = customerService.findCustAccountByCustId(balanceRechargeOrder.getCustid());
        if (custAccount == null) {
            log.error("客户信息不存在：" + balanceRechargeOrder.getCustid());
            return null;
        }
        BigDecimal[] balances = new BigDecimal[2];

        if (orderService.doChangeOrderSatus(balanceRechargeOrder, OrderStatusEnum.success)) {
            custAccount = customerService.findCustAccountByCustId(rechargeOrder.getCustid());
            AmsCustSubaccount custSubAccount = custAccount.getSubAccountByType(AccountTypeEnum.ALL);
            balances[0] = custSubAccount.getBalance();
            balances[1] = rechargeOrderExt.getAfterbalance();
        } else {
            orderService.doChangeOrderSatus(rechargeOrder, OrderStatusEnum.failed);
            balances = new BigDecimal[0];
        }
        return balances;
    }

    @Override
    public boolean doCancleRecharge(String orderNo) {
        OrderBase rechargeOrder = orderService.findOrderByOrderNo(orderNo);
        return orderService.doChangeOrderSatus(rechargeOrder, OrderStatusEnum.cancle);
    }

    @Override
    public boolean doApplyBalancePay(OrderBalancepay balancePayOrder) {
        PayTypeEnum payTypeEnum = PayTypeEnum.getEnum(balancePayOrder.getPaytype());
        balancePayOrder.setPaytypename(payTypeEnum.getDescription());

        CustAccount custAccount = customerService.findCustAccountByCustId(balancePayOrder.getCustid());
        if (custAccount == null) {
            log.error("客户信息不存在：" + balancePayOrder.getCustid());
            return false;
        }
        balancePayOrder.setCustsubaccountcode(custAccount.getSubAccountByType(AccountTypeEnum.CHANGE).getCode());
        custAccount.setPassword(balancePayOrder.getPassword());
        if (!customerService.doValidatePayPassword(custAccount)) {
            log.error("支付密码不正确");
            return false;
        }
        if (!orderService.doValidateOrder(balancePayOrder)) {
            log.error("支付失败：账户余额不足！");
            return false;
        }

        balancePayOrder.setRecebusinessid(balancePayOrder.getBusinessid());

        boolean ret = orderService.doCreateOrder(balancePayOrder);
        if(!ret) {
            log.error("创建订单失败");
            return false;
        }

        return orderService.doChangeOrderSatus(balancePayOrder, OrderStatusEnum.paySuccess);
    }

    @Override
    public boolean doBalancePay(OrderBalancepay balancePayOrder) {

        OrderBase payOrder = orderService.findOrderByOrderNo(balancePayOrder.getOrderNo());
        if (payOrder == null) {
            log.error("找不到支付订单");
            return false;
        }
        if (!payOrder.getPaytype().equals(balancePayOrder.getPaytype())) {
            log.error("支付类型不匹配");
            return false;
        }
        CustAccount custAccount = customerService.findCustAccountByCustId(payOrder.getCustid());
        if (custAccount == null) {
            log.error("客户信息不存在：" + payOrder.getCustid());
        }
        custAccount.setPassword(balancePayOrder.getPassword());
        if (!customerService.doValidatePayPassword(custAccount)) {
            log.error("支付密码不正确");
            return false;
        }
        if (orderService.doChangeOrderSatus(balancePayOrder, OrderStatusEnum.success)) {
            balancePayOrder.setAmont(payOrder.getAmont());
            balancePayOrder.setCustid(payOrder.getCustid());
            balancePayOrder.setLastmodifydate(payOrder.getLastmodifydate());
            return true;
        } else {
            orderService.doChangeOrderSatus(payOrder, OrderStatusEnum.failed);
            return false;
        }
    }

    @Override
    public boolean doCancleBalancePay(String orderNo) {
        OrderBase balancePayOrder = orderService.findOrderByOrderNo(orderNo);
        return orderService.doChangeOrderSatus(balancePayOrder, OrderStatusEnum.cancle);
    }

    @Override
    public boolean doApplyNonBalancePay(OrderNonbalancepay nonBalancePayOrder) {
        PayTypeEnum payTypeEnum = PayTypeEnum.getEnum(nonBalancePayOrder.getPaytype());
        nonBalancePayOrder.setPaytypename(payTypeEnum.getDescription());

        CustAccount custAccount = customerService.findCustAccountByCustId(nonBalancePayOrder.getCustid());
        if (custAccount == null) {
            log.error("客户信息不存在：" + nonBalancePayOrder.getCustid());
            return false;
        }
        if(!orderService.doCreateOrder(nonBalancePayOrder)) {
            log.error("创建订单失败");
            return false;
        }
        return orderService.doChangeOrderSatus(nonBalancePayOrder, OrderStatusEnum.paySuccess);
    }

    @Override
    public boolean doNonBalancePay(OrderNonbalancepay nonBalancePayOrder) {
        OrderBase payOrder = orderService.findOrderByOrderNo(nonBalancePayOrder.getOrderNo());
        OrderNonbalancepay payOrderExt = OrderNonBalancepayConvert.INSTANCE.po2do(orderService.findOrderNonbalancepay(nonBalancePayOrder.getOrderNo()));

        if (payOrder == null) {
            log.error("找不到支付订单");
            return false;
        }
        if (!payOrder.getPaytype().equals(nonBalancePayOrder.getPaytype())) {
            log.error("支付类型不匹配");
            return false;
        }
        payOrderExt.setTradeno(nonBalancePayOrder.getTradeno());
        payOrderExt.setTradestatus(nonBalancePayOrder.getTradestatus());
        payOrderExt.setBuyerid(nonBalancePayOrder.getBuyerid());
        payOrderExt.setBuyername(nonBalancePayOrder.getBuyername());
        payOrderExt.setBuyeraccount(nonBalancePayOrder.getBuyeraccount());
        if (orderService.doChangeOrderSatus(payOrderExt, OrderStatusEnum.success)) {
            nonBalancePayOrder.setCustid(payOrder.getCustid());
            nonBalancePayOrder.setAmont(payOrder.getAmont());
            nonBalancePayOrder.setLastmodifydate(payOrder.getLastmodifydate());
            return true;
        } else {
            orderService.doChangeOrderSatus(payOrder, OrderStatusEnum.failed);
            return false;
        }
    }

    @Override
    public boolean doCancleNonBalancePay(String orderNo) {
        OrderBase orderBase = orderService.findOrderByOrderNo(orderNo);
        return orderService.doChangeOrderSatus(orderBase, OrderStatusEnum.cancle);
    }

    @Override
    public boolean doApplyBalancePayRefund(OrderBalancerefund balanceRefundOrder) {
        OrderBase payOrder = orderService.findOrderByOrderNo(balanceRefundOrder.getOrigorderno());
        if (payOrder == null) {
            log.error("找不到原支付订单！");
            return false;
        }
        if (!balanceRefundOrder.getBusinessid().equals(payOrder.getBusinessid())) {
            log.error("退款请求非法，没有操作权限！");
            return false;
        }
        if (!orderService.validateRefundPayTypeAndStatus(payOrder, balanceRefundOrder, 0)) {
            log.error("原订单不允许进行退款操作！");
            return false;
        }
        BusinessAccount businessAccount = businessService.findBusinessAccountByBusinessId(payOrder.getBusinessid());
        CustAccount custAccount = customerService.findCustAccountByCustId(payOrder.getCustid());
        if (custAccount == null) {
            log.error("客户信息不存在：" + payOrder.getCustid());
            return false;
        }
        if (!orderService.doValidateOrder(balanceRefundOrder)) {
            log.error("退款金额超过原订单剩余可退款额");
        }
        balanceRefundOrder.setBusinessid(businessAccount.getBusinessid());
        balanceRefundOrder.setBusinessname(businessAccount.getBusinessname());
        balanceRefundOrder.setBusinesssubaccountcode(businessAccount.getSubAccountByType(AccountTypeEnum.CHANGE).getCode());
        balanceRefundOrder.setChannel(businessAccount.getChannel());
        balanceRefundOrder.setCustid(custAccount.getCustid());
        balanceRefundOrder.setCustsubaccountcode(custAccount.getSubAccountByType(AccountTypeEnum.CHANGE).getCode());
        balanceRefundOrder.setDescription("余额订单：" + balanceRefundOrder.getOrigorderno() + " 退款");
        balanceRefundOrder.setRececustid(balanceRefundOrder.getCustid());
        if (orderService.doCreateOrder(balanceRefundOrder)) {
            balanceRefundOrder.setSuramont(payOrder.getAmont().subtract(payOrder.getRefundamont().add(balanceRefundOrder.getAmont())));
            orderService.doChangeOrderSatus(balanceRefundOrder, OrderStatusEnum.reviewing);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean doBalancePayRefund(OrderBalancerefund balanceRefundOrder) {
        OrderBase refundOrder = orderService.findOrderByOrderNo(balanceRefundOrder.getOrderNo());
        AmsOrderBalancerefund refundOrderExt = orderService.findOrderBalanceRefund(balanceRefundOrder.getOrderNo());
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
        CustAccount custAccount = customerService.findCustAccountByCustId(payOrder.getCustid());
        if (custAccount == null) {
            log.error("客户信息不存在：" + payOrder.getCustid());
            return false;
        }
        if (orderService.doChangeOrderSatus(balanceRefundOrder, OrderStatusEnum.success)) {
            balanceRefundOrder.setCustid(refundOrder.getCustid());
            balanceRefundOrder.setAmont(refundOrder.getAmont());
            balanceRefundOrder.setBeforebalance(refundOrderExt.getBeforebalance());
            balanceRefundOrder.setAfterbalance(refundOrderExt.getAfterbalance());
            balanceRefundOrder.setLastmodifydate(refundOrder.getLastmodifydate());
            balanceRefundOrder.setSuramont(payOrder.getAmont().subtract(payOrder.getRefundamont()));
            return true;
        } else {
            orderService.doChangeOrderSatus(refundOrder, OrderStatusEnum.failed);
            log.error("退款失败");
            return false;
        }
    }

    @Override
    public boolean doCancleBalancePayRefund(String orderNo) {
        OrderBase orderBase = orderService.findOrderByOrderNo(orderNo);
        return orderService.doChangeOrderSatus(orderBase, OrderStatusEnum.cancle);
    }

    @Override
    public boolean doApplyNonBalancePayRefund(OrderNonbalancerefund nonBalanceRefundOrder) {
        OrderBase payOrder = orderService.findOrderByOrderNo(nonBalanceRefundOrder.getOrigorderno());
        if (payOrder == null) {
            log.error("找不到原支付订单！");
            return false;
        }
        if (!payOrder.getTradeno().equals(nonBalanceRefundOrder.getOrigtradeno())) {
            log.error("原订单第三方交易号不匹配！");
            return false;
        }
        if (!nonBalanceRefundOrder.getBusinessid().equals(payOrder.getBusinessid())) {
            log.error("退款请求非法，没有操作权限！");
            return false;
        }
        if (!orderService.validateRefundPayTypeAndStatus(payOrder, nonBalanceRefundOrder, 0)) {
            log.error("原订单不允许进行退款操作！");
            return false;
        }
        BusinessAccount businessAccount = businessService.findBusinessAccountByBusinessId(payOrder.getBusinessid());
        CustAccount custAccount = customerService.findCustAccountByCustId(payOrder.getCustid());
        if (custAccount == null) {
            log.error("客户信息不存在：" + payOrder.getCustid());
            return false;
        }
        if (!orderService.doValidateOrder(nonBalanceRefundOrder)) {
            log.error("退款金额超过原订单剩余可退款额");
            return false;
        }
        nonBalanceRefundOrder.setBusinessid(businessAccount.getBusinessid());
        nonBalanceRefundOrder.setBusinessname(businessAccount.getBusinessname());
        nonBalanceRefundOrder.setBusinesssubaccountcode(businessAccount.getSubAccountByType(AccountTypeEnum.CHANGE).getCode());
        nonBalanceRefundOrder.setChannel(businessAccount.getChannel());
        nonBalanceRefundOrder.setCustid(custAccount.getCustid());
        nonBalanceRefundOrder.setCustsubaccountcode(custAccount.getSubAccountByType(AccountTypeEnum.CHANGE).getCode());
        nonBalanceRefundOrder.setDescription("非余额订单：" + nonBalanceRefundOrder.getOrigorderno() + " 退款");
        if (!orderService.doCreateOrder(nonBalanceRefundOrder)) {
            log.error("创建订单失败！");
            return false;
        }
        nonBalanceRefundOrder.setSuramont(payOrder.getAmont().subtract(payOrder.getRefundamont().add(nonBalanceRefundOrder.getAmont())));
        orderService.doChangeOrderSatus(nonBalanceRefundOrder, OrderStatusEnum.reviewing);
        return true;

    }

    @Override
    public boolean doNonBalancePayRefund(OrderNonbalancerefund nonBalanceRefundOrder) {
        OrderBase refundOrder = orderService.findOrderByOrderNo(nonBalanceRefundOrder.getOrderNo());
        AmsOrderNonbalancerefund refundOrderExt = orderService.findOrderNonBalanceRefund(nonBalanceRefundOrder.getOrderNo());
        if (refundOrder == null) {
            log.error("找不到退款支付订单！");
            return false;
        }
        refundOrder.setTradeno(nonBalanceRefundOrder.getTradeno());
        refundOrder.setTradestatus(nonBalanceRefundOrder.getTradestatus());
        refundOrderExt.setBuyerid(nonBalanceRefundOrder.getBuyerid());
        refundOrderExt.setBuyername(nonBalanceRefundOrder.getBuyername());
        refundOrderExt.setBuyeraccount(nonBalanceRefundOrder.getBuyeraccount());
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
            nonBalanceRefundOrder.setCustid(refundOrder.getCustid());
            nonBalanceRefundOrder.setAmont(refundOrder.getAmont());
            nonBalanceRefundOrder.setLastmodifydate(refundOrder.getLastmodifydate());
            nonBalanceRefundOrder.setSuramont(payOrder.getAmont().subtract(payOrder.getRefundamont()));
            return true;
        } else {
            orderService.doChangeOrderSatus(refundOrder, OrderStatusEnum.failed);
            log.error("退款失败");
            return false;
        }
    }

    @Override
    public boolean doCancleNonBalancePayRefund(String orderNo) {
        OrderBase orderBase = orderService.findOrderByOrderNo(orderNo);
        return orderService.doChangeOrderSatus(orderBase, OrderStatusEnum.cancle);
    }

    @Override
    public boolean doApplyBalanceTransfer(OrderBalancetransfer balanceTransferOrder) {
        PayTypeEnum payTypeEnum = PayTypeEnum.getEnum(balanceTransferOrder.getPaytype());
        balanceTransferOrder.setPaytypename(payTypeEnum.getDescription());

        if (balanceTransferOrder.getCustid().equals(balanceTransferOrder.getRececustid())) {
            log.error("收款客户号与付款客户号相同：" + balanceTransferOrder.getCustid());
            return false;
        }
        CustAccount custAccount = customerService.findCustAccountByCustId(balanceTransferOrder.getCustid());
        if (custAccount == null) {
            log.error("客户信息不存在：" + balanceTransferOrder.getCustid());
            return false;
        }
        CustAccount receCustAccount = customerService.findCustAccountByCustId(balanceTransferOrder.getRececustid());
        if (receCustAccount == null) {
            log.error("收款客户信息不存在：" + balanceTransferOrder.getRececustid());
            return false;
        }
        balanceTransferOrder.setCustsubaccountcode(custAccount.getSubAccountByType(AccountTypeEnum.CHANGE).getCode());
        custAccount.setPassword(balanceTransferOrder.getPassword());
        if (!customerService.doValidatePayPassword(custAccount)) {
            log.error("支付密码不正确");
            return false;
        }
        if (!orderService.doValidateOrder(balanceTransferOrder)) {
            log.error("支付失败：账户余额不足！");
            return false;
        }
        if (!orderService.doCreateOrder(balanceTransferOrder)) {
            log.error("创建订单失败！");
            return false;
        }
        return orderService.doChangeOrderSatus(balanceTransferOrder, OrderStatusEnum.paySuccess);
    }

    @Override
    public boolean doApplyNonBalanceTransfer(OrderNonbalancetransfer nonBalanceTransferOrder) {
        PayTypeEnum payTypeEnum = PayTypeEnum.getEnum(nonBalanceTransferOrder.getPaytype());
        nonBalanceTransferOrder.setPaytypename(payTypeEnum.getDescription());

        CustAccount custAccount = customerService.findCustAccountByCustId(nonBalanceTransferOrder.getCustid());
        if (custAccount == null) {
            log.error("客户信息不存在：" + nonBalanceTransferOrder.getCustid());
            return false;
        }
        if(!orderService.doCreateOrder(nonBalanceTransferOrder)){
            log.error("创建订单失败！");
            return false;
        }
        return orderService.doChangeOrderSatus(nonBalanceTransferOrder, OrderStatusEnum.paySuccess);
    }

    @Override
    public boolean doBalanceTransfer(OrderBalancetransfer balanceTransferOrder) {
        OrderBase transferOrder = orderService.findOrderByOrderNo(balanceTransferOrder.getOrderNo());
        AmsOrderBalancetransfer transferOrderExt = orderService.findOrderBalancetransfer(balanceTransferOrder.getOrderNo());
        if (transferOrder == null) {
            log.error("找不到转账订单");
            return false;
        }
        if (!transferOrder.getPaytype().equals(balanceTransferOrder.getPaytype())) {
            log.error("支付类型不匹配");
            return false;
        }
        CustAccount custAccount = customerService.findCustAccountByCustId(transferOrder.getCustid());
        if (custAccount == null) {
            log.error("客户信息不存在：" + transferOrder.getCustid());
            return false;
        }
        CustAccount receCustAccount = customerService.findCustAccountByCustId(transferOrder.getRececustid());
        if (receCustAccount == null) {
            log.error("收款客户信息不存在：" + transferOrder.getRececustid());
            return false;
        }
        if (orderService.doChangeOrderSatus(transferOrder, OrderStatusEnum.success)) {
            balanceTransferOrder.setCustid(transferOrder.getCustid());
            balanceTransferOrder.setRececustid(transferOrder.getRececustid());
            balanceTransferOrder.setTransferdate(transferOrderExt.getTransferdate());
            balanceTransferOrder.setAmont(transferOrder.getAmont());
            balanceTransferOrder.setLastmodifydate(transferOrder.getLastmodifydate());
            return true;
        } else {
            orderService.doChangeOrderSatus(transferOrder, OrderStatusEnum.failed);
            return false;
        }
    }

    @Override
    public boolean doNonBalanceTransfer(OrderNonbalancetransfer nonBalanceTransferOrder) {
        OrderBase transferOrder = orderService.findOrderByOrderNo(nonBalanceTransferOrder.getOrderNo());
        AmsOrderNonbalancetransfer transferOrderExt = orderService.findOrderNonBalancetransfer(nonBalanceTransferOrder.getOrderNo());
        if (transferOrder == null) {
            log.error("找不到支付订单");
            return false;
        }
        if (!transferOrder.getPaytype().equals(nonBalanceTransferOrder.getPaytype())) {
            log.error("支付类型不匹配");
            return false;
        }
        transferOrder.setTradeno(nonBalanceTransferOrder.getTradeno());
        transferOrder.setTradestatus(nonBalanceTransferOrder.getTradestatus());
        transferOrderExt.setBuyerid(nonBalanceTransferOrder.getBuyerid());
        transferOrderExt.setBuyername(nonBalanceTransferOrder.getBuyername());
        transferOrderExt.setBuyeraccount(nonBalanceTransferOrder.getBuyeraccount());
        if (orderService.doChangeOrderSatus(transferOrder, OrderStatusEnum.success)) {
            nonBalanceTransferOrder.setAmont(transferOrder.getAmont());
            nonBalanceTransferOrder.setCustid(transferOrder.getCustid());
            nonBalanceTransferOrder.setTransferdate(transferOrderExt.getTransferdate());
            nonBalanceTransferOrder.setLastmodifydate(transferOrder.getLastmodifydate());
            return true;
        } else {
            orderService.doChangeOrderSatus(transferOrder, OrderStatusEnum.failed);
            return false;
        }
    }

    @Override
    public boolean doCancleBalanceTransfer(String orderNo) {
        OrderBase orderBase = orderService.findOrderByOrderNo(orderNo);
        return orderService.doChangeOrderSatus(orderBase, OrderStatusEnum.cancle);
    }

    @Override
    public boolean doCancleNonBalanceTransfer(String orderNo) {
        OrderBase orderBase = orderService.findOrderByOrderNo(orderNo);
        return orderService.doChangeOrderSatus(orderBase, OrderStatusEnum.cancle);
    }

    @Override
    public boolean doApplyBalanceCash(OrderBalancecash balanceCashOrder) {
        CustAccount custAccount = customerService.findCustAccountByCustId(balanceCashOrder.getCustid());
        if (custAccount == null) {
            log.error("客户信息不存在：" + balanceCashOrder.getCustid());
            return false;
        }
        AmsCustBindinfo custBindInfo = customerService.findCustBindInfoById(balanceCashOrder.getBindinfoid());
        if (custBindInfo == null) {
            log.error("找不到提现账户：" + balanceCashOrder.getBindinfoid());
            return false;
        } else {
            if (!(custBindInfo.getIsdel().equals(DeleteEnum.NOTDELETE.getValue()) && custBindInfo.getStatus().equals(StatusEnum.ACTIVATE.getValue()))) {
                log.error("提现账户不可用：" + custBindInfo.getAccount());
                return false;
            }
        }
        custAccount.setPassword(balanceCashOrder.getPassword());
        if (!customerService.doValidatePayPassword(custAccount)) {
            log.error("支付密码不正确");
            return false;
        }
        if (!orderService.doValidateOrder(balanceCashOrder)) {
            log.error("提现申请失败：账户余额不足！");
        }
        BusinessAccount YYbusinessAccount = businessService.findOperBusinessAccount();
        if (YYbusinessAccount != null) {
            balanceCashOrder.setBusinessid(YYbusinessAccount.getBusinessid());
            balanceCashOrder.setBusinessname(YYbusinessAccount.getBusinessname());
            balanceCashOrder.setBusinesssubaccountcode(YYbusinessAccount.getSubAccountByType(AccountTypeEnum.CHANGE).getCode());
            balanceCashOrder.setCustsubaccountcode(custAccount.getSubAccountByType(AccountTypeEnum.CHANGE).getCode());
            balanceCashOrder.setPaytype(PayTypeEnum.otherspay.getName());
            balanceCashOrder.setPaytypename(PayTypeEnum.otherspay.getDescription());
            balanceCashOrder.setCbindtype(custBindInfo.getType());
            balanceCashOrder.setBuyerbankname(custBindInfo.getBank());
            balanceCashOrder.setBuyername(custBindInfo.getAccountname());
            balanceCashOrder.setBuyeraccount(custBindInfo.getAccount());
            balanceCashOrder.setDescription("余额提现交易");
            if (!orderService.doCreateOrder(balanceCashOrder)) {
                log.error("创建订单失败！");
                return false;
            }
            return orderService.doChangeOrderSatus(balanceCashOrder, OrderStatusEnum.reviewing);
        } else {
            log.error("提现申请失败：找不到账户平台账户");
            return false;
        }
    }

    @Override
    public boolean doBalanceCash(OrderBalancecash balanceCashOrder) {
        AmsUser sysUser = customerService.findUserById(balanceCashOrder.getUserid());
        OrderBase cashOrder = orderService.findOrderByOrderNo(balanceCashOrder.getOrderNo());
        if (cashOrder == null) {
            log.error("找不到提现订单");
            return false;
        }
        cashOrder.setUserid(sysUser.getId());
        CustAccount custAccount = customerService.findCustAccountByCustId(cashOrder.getCustid());
        if (custAccount == null) {
            log.error("客户信息不存在：" + cashOrder.getCustid());
            return false;
        }
        if (orderService.doOrderReview(cashOrder.getOrderNo(), "审核通过", ReviewResultEnum.pass, sysUser.getId(), sysUser.getName())) {
            orderService.doChangeOrderSatus(balanceCashOrder, OrderStatusEnum.success);
            balanceCashOrder.setLastmodifydate(cashOrder.getLastmodifydate());
            balanceCashOrder.setCustid(cashOrder.getCustid());
            balanceCashOrder.setAmont(cashOrder.getAmont());
            return true;
        } else {
            orderService.doChangeOrderSatus(cashOrder, OrderStatusEnum.failed);
            return false;
        }
    }

    @Override
    public boolean doCancleBalanceCash(String orderNo, String content, String userid) {
        AmsUser sysUser = customerService.findUserById(userid);
        OrderBase orderBase = orderService.findOrderByOrderNo(orderNo);
        orderBase.setUserid(userid);
        if (orderService.doOrderReview(orderNo, content, ReviewResultEnum.notpass, sysUser.getId(), sysUser.getName())) {
            return orderService.doChangeOrderSatus(orderBase, OrderStatusEnum.cancle);
        }
        return true;
    }

    @Override
    public boolean doApplyBalanceSettlement(OrderBalancesettlement balanceSettlementOrder) {
        BusinessAccount businessAccount = businessService.findBusinessAccountByBusinessId(balanceSettlementOrder.getBusinessid());
        if (businessAccount == null) {
            log.error("找不到待结算的B账户：" + balanceSettlementOrder.getBusinessid());
            return false;
        }
        AmsBusinessBindinfo businessBindInfo = businessService.findBusinessBindInfoById(balanceSettlementOrder.getBindinfoid());
        if (businessBindInfo == null) {
            log.error("找不到结算账户：" + balanceSettlementOrder.getBindinfoid());
            return false;
        } else {
            if (!businessBindInfo.getStatus().equals(StatusEnum.ACTIVATE.getValue())) {
                log.error("结算账户不可用：" + businessBindInfo.getId());
                return false;
            }
        }
        if (!orderService.doValidateOrder(balanceSettlementOrder)) {
            log.error("结算申请失败：账户余额不足！");
            return false;
        }
        BusinessAccount YYbusinessAccount = businessService.findOperBusinessAccount();
        if (YYbusinessAccount != null) {
            balanceSettlementOrder.setBusinessname(businessAccount.getBusinessname());
            balanceSettlementOrder.setBusinesstradeno("");
            balanceSettlementOrder.setBusinesssubaccountcode(businessAccount.getSubAccountByType(AccountTypeEnum.CHANGE).getCode());
            balanceSettlementOrder.setCustid("");
            balanceSettlementOrder.setPaytype(PayTypeEnum.otherspay.getName());
            balanceSettlementOrder.setPaytypename(PayTypeEnum.otherspay.getDescription());
            balanceSettlementOrder.setChannel(YYbusinessAccount.getChannel());
            balanceSettlementOrder.setBbindtype(businessBindInfo.getType());
            try {
                switch (BBindTypeEnum.getEnum(businessBindInfo.getType())) {
                    case AliPay:
                        balanceSettlementOrder.setBuyername(businessBindInfo.getPartner());
                        balanceSettlementOrder.setBuyeraccount(businessBindInfo.getSellerEmail());
                        break;
                    case WeiXinPublic:
                    case WeiXinOpen:
                        balanceSettlementOrder.setBuyername(businessBindInfo.getPartner());
                        balanceSettlementOrder.setBuyeraccount(businessBindInfo.getPartner());
                        break;
                    case BankCard:
                        balanceSettlementOrder.setBuyerbankname(businessBindInfo.getBankname());
                        balanceSettlementOrder.setBuyername(businessBindInfo.getName());
                        balanceSettlementOrder.setBuyeraccount(businessBindInfo.getAccount());
                        break;
                    default:
                        log.error("结算申请失败：结算账户类型非法！");
                }
            } catch (Exception e) {
                log.error("结算申请失败：结算账户类型非法！");
            }
            if (!orderService.doCreateOrder(balanceSettlementOrder)) {
                log.error("创建订单失败！");
                return false;
            }
            return orderService.doChangeOrderSatus(balanceSettlementOrder, OrderStatusEnum.reviewing);
        } else {
            log.error("结算申请失败：找不到账户平台账户");
            return false;
        }
    }

    @Override
    public boolean doBalanceSettlement(OrderBalancesettlement balanceSettlementOrder) {
        AmsUser sysUser = customerService.findUserById(balanceSettlementOrder.getUserid());
        OrderBase settlementOrder = orderService.findOrderByOrderNo(balanceSettlementOrder.getOrderNo());
        if (settlementOrder == null) {
            log.error("找不到余额结算订单");
            return false;
        }
        settlementOrder.setUserid(sysUser.getId());
        BusinessAccount businessAccount = businessService.findBusinessAccountByBusinessId(settlementOrder.getBusinessid());
        if (businessAccount == null) {
            log.error("找不到待结算的B账户：" + settlementOrder.getBusinessid());
        }
        if (orderService.doOrderReview(settlementOrder.getOrderNo(), "审核通过", ReviewResultEnum.pass, sysUser.getId(), sysUser.getName())) {
            orderService.doChangeOrderSatus(balanceSettlementOrder, OrderStatusEnum.success);
            balanceSettlementOrder.setLastmodifydate(settlementOrder.getLastmodifydate());
            balanceSettlementOrder.setBusinessid(settlementOrder.getBusinessid());
            balanceSettlementOrder.setAmont(settlementOrder.getAmont());
            return true;
        } else {
            orderService.doChangeOrderSatus(settlementOrder, OrderStatusEnum.failed);
            return false;
        }
    }

    @Override
    public boolean doCancleBalanceSettlement(String orderNo, String content, String userid) {
        AmsUser sysUser = customerService.findUserById(userid);
        if (orderService.doOrderReview(orderNo, content, ReviewResultEnum.notpass, sysUser.getId(), sysUser.getName())) {
            OrderBase balanceSettlementOrder = orderService.findOrderByOrderNo(orderNo);
            balanceSettlementOrder.setUserid(userid);
            return orderService.doChangeOrderSatus(balanceSettlementOrder, OrderStatusEnum.cancle);
        }
        return true;
    }

    @Override
    public boolean doApplyShopSettlement(OrderShopsettlement shopSettlementOrder) {
        shopSettlementOrder.setPaytype(PayTypeEnum.otherspay.getName());
        shopSettlementOrder.setPaytypename(PayTypeEnum.otherspay.getDescription());
        shopSettlementOrder.setCustid("");
        if (!orderService.doCreateOrder(shopSettlementOrder)) {
            log.error("创建订单失败！");
            return false;
        }
        return orderService.doChangeOrderSatus(shopSettlementOrder, OrderStatusEnum.reviewing);
    }

    @Override
    public boolean doShopSettlement(OrderShopsettlement shopSettlementOrder) {
        OrderBase settlementOrder = orderService.findOrderByOrderNo(shopSettlementOrder.getOrderNo());
        AmsOrderShopsettlement settlementOrderExt = orderService.findOrderShopsettlement(shopSettlementOrder.getOrderNo());
        if (settlementOrder == null) {
            log.error("找不到二级商户结算订单");
            return false;
        }
        BusinessAccount businessAccount = businessService.findBusinessAccountByBusinessId(settlementOrder.getBusinessid());
        if (businessAccount == null) {
            log.error("找不到对应B账户：" + settlementOrder.getBusinessid());
            return false;
        }
        if (orderService.doChangeOrderSatus(shopSettlementOrder, OrderStatusEnum.success)) {
            shopSettlementOrder.setLastmodifydate(settlementOrder.getLastmodifydate());
            shopSettlementOrder.setShopno(settlementOrderExt.getShopno());
            shopSettlementOrder.setAmont(settlementOrder.getAmont());
            shopSettlementOrder.setRate(settlementOrderExt.getRate());
            return true;
        } else {
            orderService.doChangeOrderSatus(settlementOrder, OrderStatusEnum.failed);
            return false;
        }
    }

    @Override
    public boolean doCancleShopSettlement(String orderNo) {
        OrderBase orderBase = orderService.findOrderByOrderNo(orderNo);
        return orderService.doChangeOrderSatus(orderBase, OrderStatusEnum.cancle);
    }

    @Override
    public boolean doApplyNormalTrade(OrderNormaltrade normalTradeOrder) {
        if(!orderService.doCreateOrder(normalTradeOrder)) {
            log.error("创建订单失败！");
            return false;
        }
        return orderService.doChangeOrderSatus(normalTradeOrder, OrderStatusEnum.reviewing);
    }

    @Override
    public boolean doNormalTrade(OrderNormaltrade normalTradeOrder) {
        OrderBase tradeOrder = orderService.findOrderByOrderNo(normalTradeOrder.getOrderNo());
        if (tradeOrder == null) {
            log.error("找不到B户通用记账订单");
            return false;
        }
        if (orderService.doChangeOrderSatus(normalTradeOrder, OrderStatusEnum.success)) {
            normalTradeOrder.setLastmodifydate(tradeOrder.getLastmodifydate());
            return true;
        } else {
            orderService.doChangeOrderSatus(normalTradeOrder, OrderStatusEnum.failed);
            return false;
        }
    }

    @Override
    public boolean doCancleNormalTrade(String orderNo) {
        OrderBase tradeOrder = orderService.findOrderByOrderNo(orderNo);
        return orderService.doChangeOrderSatus(tradeOrder, OrderStatusEnum.cancle);
    }

}
