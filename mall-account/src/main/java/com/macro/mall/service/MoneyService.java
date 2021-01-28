package com.macro.mall.service;

import com.macro.mall.domain.order.*;
import java.math.BigDecimal;

/**
 * C户信息服务接口
 *
 * @author dongjb
 * @date 2021/01/13
 */
public interface MoneyService {

    /**
     * C户充值申请
     *
     * @param balanceRechargeOrder 充值订单
     * @return true|false
     */
    boolean doApplyRecharge(OrderBalancerecharge balanceRechargeOrder);

    /**
     * C户充值
     *
     * @param balanceRechargeOrder 充值订单
     * @return [0]-充值后总余额，[1]-充值后可用余额
     */
    BigDecimal[] doRecharge(OrderBalancerecharge balanceRechargeOrder);

    /**
     * C户充值取消
     *
     * @param orderNo 充值订单号
     * @return true|false
     */
    boolean doCancleRecharge(String orderNo);

    /**
     * C户余额支付到B户（申请）
     *
     * @param balancePayOrder 余额支付订单
     * @return true|false
     */
    boolean doApplyBalancePay(OrderBalancepay balancePayOrder);

    /**
     * C户余额支付到B户（确认）
     *
     * @param balancePayOrder 余额支付订单
     * @return true|false
     */
    boolean doBalancePay(OrderBalancepay balancePayOrder);

    /**
     * C户余额支付到B户取消
     *
     * @param orderNo 余额支付订单号
     * @return true|false
     */
    boolean doCancleBalancePay(String orderNo);

    /**
     * C户非余额支付到B户申请
     *
     * @param nonBalancePayOrder 非余额支付订单
     * @return true|false
     */
    boolean doApplyNonBalancePay(OrderNonbalancepay nonBalancePayOrder);

    /**
     * C户非余额支付到B户
     *
     * @param nonBalancePayOrder 非余额支付订单
     * @return true|false
     */
    boolean doNonBalancePay(OrderNonbalancepay nonBalancePayOrder);

    /**
     * C户非余额支付取消
     *
     * @param orderNo 非余额支付订单号
     * @return true|false
     */
    boolean doCancleNonBalancePay(String orderNo);

    /**
     * C户余额退款申请
     *
     * @param balanceRefundOrder 余额退款订单
     * @return true|false
     */
    boolean doApplyBalancePayRefund(OrderBalancerefund balanceRefundOrder);

    /**
     * C户余额退款确认
     *
     * @param balanceRefundOrder 余额退款订单
     * @return true|false
     */
    boolean doBalancePayRefund(OrderBalancerefund balanceRefundOrder);

    /**
     * C户余额退款取消
     *
     * @param orderNo 余额退款订单号
     * @return true|false
     */
    boolean doCancleBalancePayRefund(String orderNo);

    /**
     * C户非余额退款申请
     *
     * @param nonBalanceRefundOrder 非余额退款订单
     * @return true|false
     */
    boolean doApplyNonBalancePayRefund(OrderNonbalancerefund nonBalanceRefundOrder);

    /**
     * C户非余额退款
     *
     * @param nonBalanceRefundOrder 非余额退款订单
     * @return true|false
     */
    boolean doNonBalancePayRefund(OrderNonbalancerefund nonBalanceRefundOrder);

    /**
     * C户非余额退款取消
     *
     * @param orderNo 非余额退款订单号
     * @return true|false
     */
    boolean doCancleNonBalancePayRefund(String orderNo);

    /**
     * 余额转账申请
     *
     * @param balanceTransferOrder 余额转账订单
     * @return true|false
     */
    boolean doApplyBalanceTransfer(OrderBalancetransfer balanceTransferOrder);

    /**
     * 非余额转账申请
     *
     * @param nonBalanceTransferOrder 非余额转账订单
     * @return true|false
     */
    boolean doApplyNonBalanceTransfer(OrderNonbalancetransfer nonBalanceTransferOrder);

    /**
     * 余额转账确认
     *
     * @param balanceTransferOrder 余额转账订单
     * @return true|false
     */
    boolean doBalanceTransfer(OrderBalancetransfer balanceTransferOrder);

    /**
     * 非余额转账确认
     *
     * @param nonBalanceTransferOrder 非余额转账订单
     * @return true|false
     */
    boolean doNonBalanceTransfer(OrderNonbalancetransfer nonBalanceTransferOrder);

    /**
     * 余额转账取消
     *
     * @param orderNo 余额转账订单号
     * @return true|false
     */
    boolean doCancleBalanceTransfer(String orderNo);

    /**
     * 非余额转账取消
     *
     * @param orderNo 非余额转账订单号
     * @return true|false
     */
    boolean doCancleNonBalanceTransfer(String orderNo);

    /**
     * 余额提现申请
     *
     * @param balanceCashOrder 余额提现订单
     * @return true|false
     */
    boolean doApplyBalanceCash(OrderBalancecash balanceCashOrder);

    /**
     * 余额提现确认
     *
     * @param balanceCashOrder 余额提现订单
     * @return true|false
     */
    boolean doBalanceCash(OrderBalancecash balanceCashOrder);

    /**
     * 余额提现取消
     *
     * @param orderNo 余额提现订单号
     * @param content 审核内容
     * @param userid  操作员id
     * @return true|false
     */
    boolean doCancleBalanceCash(String orderNo, String content, String userid);

    /**
     * B户余额结算申请
     *
     * @param balanceSettlementOrder B户余额结算订单
     * @return true|false
     */
    boolean doApplyBalanceSettlement(OrderBalancesettlement balanceSettlementOrder);

    /**
     * B户余额结算
     *
     * @param balanceSettlementOrder B户余额结算订单
     * @return true|false
     */
    boolean doBalanceSettlement(OrderBalancesettlement balanceSettlementOrder);

    /**
     * B户余额结算取消
     *
     * @param orderNo B户余额结算订单号
     * @param content 审核意见
     * @param userid  操作员id
     * @return true|false
     */
    boolean doCancleBalanceSettlement(String orderNo, String content, String userid);

    /**
     * 二级商户结算申请
     *
     * @param shopSettlementOrder 二级商户结算订单
     * @return true|false
     */
    boolean doApplyShopSettlement(OrderShopsettlement shopSettlementOrder);

    /**
     * 二级商户结算确认
     *
     * @param shopSettlementOrder 二级商户结算订单
     * @return true|false
     */
    boolean doShopSettlement(OrderShopsettlement shopSettlementOrder);

    /**
     * 二级商户结算取消
     *
     * @param orderNo 二级商户结算订单号
     * @return true|false
     */
    boolean doCancleShopSettlement(String orderNo);

    /**
     * B户通用记账申请
     *
     * @param normalTradeOrder B户通用记账订单
     * @return true|false
     */
    boolean doApplyNormalTrade(OrderNormaltrade normalTradeOrder);

    /**
     * B户通用记账确认
     *
     * @param normalTradeOrder B户通用记账订单
     * @return true|false
     */
    boolean doNormalTrade(OrderNormaltrade normalTradeOrder);

    /**
     * B户通用记账取消
     *
     * @param orderNo B户通用记账订单号
     * @return true|false
     */
    boolean doCancleNormalTrade(String orderNo);

}
