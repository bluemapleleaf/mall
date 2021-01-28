package com.macro.mall.service;

import com.macro.mall.ams.model.*;
import com.macro.mall.ams.service.AmsOrderBaseRepository;
import com.macro.mall.domain.order.OrderBase;
import com.macro.mall.domain.order.OrderCurrencyrecharge;
import com.macro.mall.domain.order.OrderIntegralrecharge;
import com.macro.mall.enums.OrderStatusEnum;
import com.macro.mall.enums.ReviewResultEnum;

import java.math.BigDecimal;
import java.util.List;

/**
 * /**
 * 订单服务
 *
 * @author dongjb
 * @date 2021/01/12
 */
public interface OrderService extends AmsOrderBaseRepository {

    /**
     * 获取订单审核记录
     *
     * @param orderNo 订单号
     * @return 订单审核记录
     */
    List<AmsOrderReviewrecord> findReviewRecords(String orderNo);

    /**
     * 获取订单审核记录
     *
     * @param reviewid 审核记录id
     * @return 审核记录实例
     */
    AmsOrderReviewrecord findReviewRecord(String reviewid);

    /**
     * 审核订单
     *
     * @param orderNo          订单号
     * @param content          审核内容
     * @param reviewResultEnum 审核结果
     * @param userid           审核人
     * @param username         审核人名称
     * @return true|false
     */
    boolean doOrderReview(String orderNo, String content, ReviewResultEnum reviewResultEnum, String userid, String username);

    /**
     * 删除审核记录
     *
     * @param reviewid 审核记录id
     * @return true|false
     */
    boolean doDeleteOrderReview(String reviewid);

    /**
     * 删除审核记录
     *
     * @param orderNo 订单号
     * @return true|false
     */
    boolean doDeleteOrderReviews(String orderNo);

    /**
     * 校验订单类型
     *
     * @return true|false
     */
    boolean doValidateOrderType(OrderBase orderBase);

    /**
     * 创建订单
     *
     * @return 创建成功否
     */
    boolean doCreateOrder(OrderBase orderBase);

    /**
     * 更新订单
     *
     * @return 更新成功否
     */
    boolean doUpdateOrder(OrderBase orderBase);

    /**
     * 设置会计相关日期
     */
    void doUpdateAccountDate(OrderBase orderBase);


    /**
     * 获取当前订单状态
     *
     * @param orderBase 订单信息
     * @return 订单状态
     */
    OrderStatusEnum findCurrOrderStatus(OrderBase orderBase);

    /**
     * 自动设置创建时间
     *
     * @param orderBase 订单信息
     */
    void doUpdateCreateDate(OrderBase orderBase);

    /**
     * 更改订单状态
     *
     * @param orderBase       订单信息
     * @param orderStatusEnum 订单状态
     * @return
     */
    boolean doChangeOrderSatus(OrderBase orderBase, OrderStatusEnum orderStatusEnum);

    /**
     * 获取订单信息
     *
     * @param orderNo 订单号
     * @return 订单信息
     */
    OrderBase findOrderByOrderNo(String orderNo);

    /**
     * 获取余额充值订单信息
     *
     * @param orderNo 订单号
     * @return 订单信息
     */
    AmsOrderBalancerecharge findOrderBalancerecharge(String orderNo);

    /**
     * 获取非余额支付订单信息
     *
     * @param orderNo 订单号
     * @return 订单信息
     */
    AmsOrderNonbalancepay findOrderNonbalancepay(String orderNo);

    /**
     * 获取余额退款订单信息
     *
     * @param orderNo 订单号
     * @return 订单信息
     */
    AmsOrderBalancerefund findOrderBalanceRefund(String orderNo);

    /**
     * 获取非余额退款订单信息
     *
     * @param orderNo 订单号
     * @return 订单信息
     */
    AmsOrderNonbalancerefund findOrderNonBalanceRefund(String orderNo);

    /**
     * 获取余额转账订单信息
     *
     * @param orderNo 订单号
     * @return 订单信息
     */
    AmsOrderBalancetransfer findOrderBalancetransfer(String orderNo);

    /**
     * 获取非余额转账订单信息
     *
     * @param orderNo 订单号
     * @return 订单信息
     */
    AmsOrderNonbalancetransfer findOrderNonBalancetransfer(String orderNo);

    /**
     * 获取店铺结算订单信息
     *
     * @param orderNo 订单号
     * @return 订单信息
     */
    AmsOrderShopsettlement findOrderShopsettlement(String orderNo);

    /**
     * 获取积分充值订单信息
     *
     * @param orderNo 订单号
     * @return 订单信息
     */
    AmsOrderIntegralrecharge findOrderIntegralrecharge(String orderNo);

    /**
     * 获取积分缴费订单信息
     *
     * @param orderNo 订单号
     * @return 订单信息
     */
    AmsOrderIntegralpay findOrderIntegralpay(String orderNo);

    /**
     * 获取积分退款订单信息
     *
     * @param orderNo 订单号
     * @return 订单信息
     */
    AmsOrderIntegralrefund findOrderIntegralRefund(String orderNo);

    /**
     * 自有货币充值订单信息
     *
     * @param orderNo 订单号
     * @return 订单信息
     */
    AmsOrderCurrencyrecharge findOrderCurrencyrecharge(String orderNo);

    /**
     * 自有货币缴费订单信息
     *
     * @param orderNo 订单号
     * @return 订单信息
     */
    AmsOrderCurrencypay findOrderCurrencypay(String orderNo);

    /**
     * 自有货币退款订单信息
     *
     * @param orderNo 订单号
     * @return 订单信息
     */
    AmsOrderCurrencyrefund findOrderCurrencyrefund(String orderNo);


    /**
         * 订单记账校验
         *
         * @param orderBase 订单信息
         * @return 交易成功与否
         */
    boolean doValidateOrder(OrderBase orderBase);

    /**
     * 校验退款订单支付类型及状态
     *
     * @param payOrder    原支付订单
     * @param refundOrder 退款订单
     * @param flag        0-申请，1-确认
     * @return true|false
     */
    boolean validateRefundPayTypeAndStatus(OrderBase payOrder, OrderBase refundOrder, int flag);

    /**
     * 获取账期日期
     *
     * @param dateType 日期类型
     * @return 日期
     */
    String getAccountDate(String dateType);

    /**
     * 获取积分兑换比例
     *
     * @return 积分兑换比例
     */
    BigDecimal getIntegralRatio();
}
