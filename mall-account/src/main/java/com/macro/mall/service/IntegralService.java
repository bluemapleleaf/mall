package com.macro.mall.service;


import com.macro.mall.domain.order.OrderIntegralpay;
import com.macro.mall.domain.order.OrderIntegralrecharge;
import com.macro.mall.domain.order.OrderIntegralrefund;
import com.macro.mall.domain.order.OrderIntegralsettlement;
import org.springframework.stereotype.Service;

/**
 * 积分服务
 *
 * @author dongjb
 * @date 2021/01/22
 */
@Service
public interface IntegralService {

    /**
     * 申请入账
     *
     * @param integralRechargeOrder 积分入账订单
     * @return true|false
     */
    boolean doApplyRecharge(OrderIntegralrecharge integralRechargeOrder);

    /**
     * 确认入账
     *
     * @param integralRechargeOrder 积分入账订单
     * @return true|false
     */
    boolean doRecharge(OrderIntegralrecharge integralRechargeOrder);

    /**
     * 取消入账
     *
     * @param orderNo 积分入账订单号
     * @return true|false
     */
    boolean doCancleRecharge(String orderNo);

    /**
     * 申请消费
     *
     * @param integralPayOrder 积分消费订单
     * @return true|false
     */
    boolean doApplyPayment(OrderIntegralpay integralPayOrder);

    /**
     * 确认消费
     *
     * @param integralPayOrder 积分消费订单
     * @return true|false
     */
    boolean doPayment(OrderIntegralpay integralPayOrder);

    /**
     * 取消消费
     *
     * @param orderNo 积分消费订单号
     * @return true|false
     */
    boolean doCanclePayment(String orderNo);

    /**
     * 申请退款
     *
     * @param integralRefundOrder 退款订单
     * @return true|false
     */
    boolean doApplyRefund(OrderIntegralrefund integralRefundOrder);

    /**
     * 确认退款
     *
     * @param integralRefundOrder 退款订单
     * @return true|false
     */
    boolean doRefund(OrderIntegralrefund integralRefundOrder);

    /**
     * 取消退款
     *
     * @param orderNo 退款订单号
     * @return true|false
     */
    boolean doCancleRefund(String orderNo);

    /**
     * B户积分结算申请
     *
     * @param integralSettlementOrder B户积分结算订单
     * @return true|false
     */
    boolean doApplyIntegralSettlement(OrderIntegralsettlement integralSettlementOrder);

    /**
     * B户积分结算
     *
     * @param integralSettlementOrder B户积分结算订单
     * @return true|false
     */
    boolean doIntegralSettlement(OrderIntegralsettlement integralSettlementOrder);

    /**
     * B户积分结算取消
     *
     * @param orderNo B户积分结算订单号
     * @param content 审核意见
     * @param userid  操作员id
     * @return true|false
     */
    boolean doCancleIntegralSettlement(String orderNo, String content, String userid);

}
