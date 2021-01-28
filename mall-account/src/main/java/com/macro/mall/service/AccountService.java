package com.macro.mall.service;

import com.macro.mall.domain.order.OrderInternaltrade;
import com.macro.mall.enums.ReviewResultEnum;

/**
 * 账务系统服务接口
 *
 * @author dongjb
 * @date 2021/01/22
 */
public interface AccountService {

    /**
     * 内部交易申请
     *
     * @param internalTradeOrder 内部交易订单
     * @return true|false
     */
    boolean doApplyInternalTrade(OrderInternaltrade internalTradeOrder);

    /**
     * 内部交易
     *
     * @param internalTradeOrder 内部交易订单
     * @return true|false
     */
    boolean doInternalTrade(OrderInternaltrade internalTradeOrder);

    /**
     * 内部交易取消
     *
     * @param orderNo 内部交易订单号
     * @param content 取消原因
     * @param userid  审核员id
     * @return true|false
     */
    boolean doCancleInternalTrade(String orderNo, String content, String userid);

    /**
     * 内部交易审核
     *
     * @param orderNo          内部交易订单号
     * @param userid           审核员id
     * @param reviewContent    审核内容
     * @param reviewResultEnum 审核结果
     * @param checkstatus      复核状态
     * @return true|false
     */
    boolean doReviewInternalTrade(String orderNo, String userid, String reviewContent, ReviewResultEnum reviewResultEnum, int checkstatus);

}
