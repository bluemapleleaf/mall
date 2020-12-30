package com.macro.mall.portal.service;


import com.macro.mall.common.api.CommonPage;
import com.macro.mall.oms.domain.OmsOrderDetailPortal;
import com.macro.mall.portal.domain.ConfirmOrderResult;
import com.macro.mall.portal.domain.OrderParam;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 前台订单管理Service
 *
 * @author dongjb
 * @date 2020/11/30
 */
public interface OmsPortalOrderService {

    /**
     * 根据用户购物车信息生成确认单信息
     * @param cartIds 购物车标识列表
     * @return 确认单信息
     */
    ConfirmOrderResult generateConfirmOrder(List<Long> cartIds);

    /**
     * 根据提交信息生成订单
     * @param orderParam 提交信息
     * @return 订单
     */
    @Transactional(rollbackFor = Exception.class)
    Map<String, Object> generateOrder(OrderParam orderParam);

    /**
     * 支付成功后的回调
     * @param orderId 订单号
     * @param payType 支付类型
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    Integer paySuccess(Long orderId, Integer payType);

    /**
     * 自动取消超时订单
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    Integer cancelTimeOutOrder();

    /**
     * 取消单个超时订单
     * @param orderId 订单号
     */
    @Transactional(rollbackFor = Exception.class)
    void cancelOrder(Long orderId);

    /**
     * 发送延迟消息取消订单
     * @param orderId 订单号
     */
    void sendDelayMessageCancelOrder(Long orderId);

    /**
     * 确认收货
     * @param orderId 订单号
     */
    void confirmReceiveOrder(Long orderId);

    /**
     * 分页获取用户订单
     * @param status 状态
     * @param pageNum 页码
     * @param pageSize 页条数
     * @return 订单列表
     */
    CommonPage<OmsOrderDetailPortal> list(Integer status, Integer pageNum, Integer pageSize);

    /**
     * 根据订单ID获取订单详情
     * @param orderId 订单号
     * @return 订单详情
     */
    OmsOrderDetailPortal detail(Long orderId);

    /**
     * 用户根据订单ID删除订单
     * @param orderId 订单号
     */
    void deleteOrder(Long orderId);
}
