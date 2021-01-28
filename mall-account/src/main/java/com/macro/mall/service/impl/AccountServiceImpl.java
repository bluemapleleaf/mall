package com.macro.mall.service.impl;

import com.macro.mall.ams.model.AmsUser;
import com.macro.mall.domain.BusinessAccount;
import com.macro.mall.domain.order.OrderBase;
import com.macro.mall.domain.order.OrderInternaltrade;
import com.macro.mall.domain.order.OrderReviewrecord;
import com.macro.mall.enums.*;
import com.macro.mall.service.AccountService;
import com.macro.mall.service.BusinessService;
import com.macro.mall.service.CustomerService;
import com.macro.mall.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 账务系统服务
 *
 * @author dongjb
 * @date 2021/01/22
 */
@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    BusinessService businessService;

    @Autowired
    OrderService orderService;

    @Autowired
    CustomerService customerService;

    @Override
    public boolean doApplyInternalTrade(OrderInternaltrade internalTradeOrder) {
        BusinessAccount businessAccount = businessService.findBusinessAccountByBusinessId(internalTradeOrder.getBusinessid());
        if (businessAccount == null) {
            log.error("找不到关联的B账户：" + internalTradeOrder.getBusinessid());
            return false;
        }
        internalTradeOrder.setBusinessname(businessAccount.getBusinessname());
        internalTradeOrder.setBusinesstradeno("");
        internalTradeOrder.setPaytype(PayTypeEnum.otherspay.getName());
        internalTradeOrder.setPaytypename(PayTypeEnum.otherspay.getDescription());
        internalTradeOrder.setDescription("内部特殊交易订单");
        try {
            internalTradeOrder.setTradetypename(TradeTypeEnum.getEnum(internalTradeOrder.getTradetype()).getName());
            if (!StringUtils.isEmpty(internalTradeOrder.getOrigorderno())) {
                OrderTypeEnum orderTypeEnum = OrderTypeEnum.getEnum(internalTradeOrder.getOrigordertype());
                if (orderTypeEnum == null) {
                    log.error("原订单类型为空：" + internalTradeOrder.getOrigorderno());
                    return false;
                }
                OrderBase origOrder = orderService.findOrderByOrderNo(internalTradeOrder.getOrderNo());
                internalTradeOrder.setOrigstatus(origOrder.getStatus());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        if (!orderService.doValidateOrder(internalTradeOrder)) {
            log.error("内部交易申请失败：账户余额不足！");
            return false;
        }
        BusinessAccount operBusinessAccount = businessService.findOperBusinessAccount();
        if (operBusinessAccount != null) {
            internalTradeOrder.setChannel(operBusinessAccount.getChannel());
            if (orderService.doCreateOrder(internalTradeOrder)) {
                return orderService.doChangeOrderSatus(internalTradeOrder, OrderStatusEnum.reviewing);
            } else {
                log.error("创建订单失败！");
                return false;
            }
        } else {
            log.error("内部交易申请失败：找不到账户平台账户");
            return false;
        }
    }

    @Override
    public boolean doInternalTrade(OrderInternaltrade internalTradeOrder) {
        OrderBase tradeOrder = orderService.findOrderByOrderNo(internalTradeOrder.getOrderNo());
        if (tradeOrder == null) {
            log.error("找不到特殊交易订单");
            return false;
        }
        tradeOrder.setUserid(internalTradeOrder.getUserid());
        if (doReviewInternalTrade(tradeOrder.getOrderNo(), tradeOrder.getUserid(), ReviewResultEnum.pass.getDescription(), ReviewResultEnum.pass, ReviewResultEnum.pass.getValue())) {
            if (!orderService.doChangeOrderSatus(internalTradeOrder, OrderStatusEnum.success)) {
                return false;
            }
            internalTradeOrder.setBusinessid(tradeOrder.getBusinessid());
            internalTradeOrder.setAmont(tradeOrder.getAmont());
            internalTradeOrder.setLastmodifydate(tradeOrder.getLastmodifydate());
        } else {
            return orderService.doChangeOrderSatus(tradeOrder, OrderStatusEnum.failed);
        }
        return true;
    }

    @Override
    public boolean doCancleInternalTrade(String orderNo, String content, String userid) {
        if (doReviewInternalTrade(orderNo, userid, content, ReviewResultEnum.notpass, ReviewResultEnum.notpass.getValue())) {
            OrderBase internalTradeOrder = orderService.findOrderByOrderNo(orderNo);
            internalTradeOrder.setUserid(userid);
            return orderService.doChangeOrderSatus(internalTradeOrder, OrderStatusEnum.cancle);
        } else {
            return false;
        }
    }

    @Override
    public boolean doReviewInternalTrade(String orderNo, String userid, String reviewContent, ReviewResultEnum reviewResultEnum, int checkstatus) {
        AmsUser sysUser = customerService.findUserById(userid);
        OrderBase internalTradeOrder = orderService.findOrderByOrderNo(orderNo);
        internalTradeOrder.setUserid(userid);
        internalTradeOrder.setCheckstatus(checkstatus);

        OrderReviewrecord orderReviewRecord = new OrderReviewrecord();
        orderReviewRecord.setOrderNo(orderNo);
        orderReviewRecord.setContent(reviewContent);
        orderReviewRecord.setResult(reviewResultEnum.getValue());
        orderReviewRecord.setUserid(sysUser.getId());
        orderReviewRecord.setUsername(sysUser.getName());
        orderReviewRecord.setAccountdate(orderService.getAccountDate("accountdate"));
        if (!orderService.doCreateOrder(orderReviewRecord)) {
            log.error("创建订单失败！");
            return false;
        }

        return orderService.doUpdateOrder(internalTradeOrder);

    }

}
