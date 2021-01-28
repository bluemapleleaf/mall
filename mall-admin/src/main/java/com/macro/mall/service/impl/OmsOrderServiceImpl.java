package com.macro.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.domain.*;
import com.macro.mall.oms.dto.OmsOrderDetail;
import com.macro.mall.oms.model.OmsOrder;
import com.macro.mall.oms.model.OmsOrderOperateHistory;
import com.macro.mall.oms.service.OmsOrderOperateHistoryRepository;
import com.macro.mall.oms.service.impl.OmsOrderRepositoryImpl;
import com.macro.mall.service.OmsOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static cn.hutool.core.date.DateTime.now;

/**
 * 订单管理Service实现类
 * Created by macro on 2018/10/11.
 */
@Service
public class OmsOrderServiceImpl extends OmsOrderRepositoryImpl implements OmsOrderService {
    @Autowired
    private OmsOrderOperateHistoryRepository orderOperateHistoryRepository;

    @Override
    public Page<OmsOrder> list(OmsOrderQueryParam queryParam, Integer pageSize, Integer pageNum) {
        Page<OmsOrder> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<OmsOrder> lambda = new LambdaQueryWrapper<>();
        lambda.eq(OmsOrder::getDeleteStatus, 0);
        if(!StringUtils.isEmpty(queryParam.getOrderSn())){
            lambda.eq(OmsOrder::getOrderSn, queryParam.getOrderSn());
        }
        if(!StringUtils.isEmpty(queryParam.getStatus())){
            lambda.eq(OmsOrder::getStatus, queryParam.getStatus());
        }
        if(!StringUtils.isEmpty(queryParam.getSourceType())){
            lambda.eq(OmsOrder::getSourceType, queryParam.getSourceType());
        }
        if(!StringUtils.isEmpty(queryParam.getOrderType())){
            lambda.eq(OmsOrder::getOrderType, queryParam.getOrderType());
        }
        if(!StringUtils.isEmpty(queryParam.getCreateTime())){
            lambda.like(OmsOrder::getCreateTime, queryParam.getCreateTime());
        }
        if(!StringUtils.isEmpty(queryParam.getReceiverKeyword())){
            lambda.like(OmsOrder::getReceiverName, queryParam.getReceiverKeyword());
            lambda.or().like(OmsOrder::getReceiverPhone, queryParam.getReceiverKeyword());
        }

        return page(page, lambda);

    }

    @Override
    public boolean delivery(List<OmsOrderDeliveryParam> deliveryParamList) {
        //添加操作记录
        List<OmsOrderOperateHistory> operateHistoryList = deliveryParamList.stream()
                .map(omsOrderDeliveryParam -> {
                    OmsOrderOperateHistory history = new OmsOrderOperateHistory();
                    history.setOrderId(omsOrderDeliveryParam.getOrderId());
                    history.setCreateTime(new Date());
                    history.setOperateMan("后台管理员");
                    history.setOrderStatus(2);
                    history.setNote("完成发货");
                    return history;
                }).collect(Collectors.toList());
        orderOperateHistoryRepository.saveBatch(operateHistoryList);
        //批量发货
        ArrayList<OmsOrder> orderList = new ArrayList<>();
        for (OmsOrderDeliveryParam deliveryParam: deliveryParamList
        ) {
            OmsOrder order = new OmsOrder();
            order.setId(deliveryParam.getOrderId());
            order.setDeliverySn(deliveryParam.getDeliverySn());
            order.setDeliveryCompany(deliveryParam.getDeliveryCompany());
            order.setDeliveryTime(now());
            orderList.add(order);
        }
        return updateBatchById(orderList);
    }

    @Override
    public boolean close(List<Long> ids, String note) {
        List<OmsOrderOperateHistory> historyList = ids.stream().map(orderId -> {
            OmsOrderOperateHistory history = new OmsOrderOperateHistory();
            history.setOrderId(orderId);
            history.setCreateTime(new Date());
            history.setOperateMan("后台管理员");
            history.setOrderStatus(4);
            history.setNote("订单关闭:"+note);
            return history;
        }).collect(Collectors.toList());
        orderOperateHistoryRepository.saveBatch(historyList);

        LambdaUpdateWrapper<OmsOrder> lambda = new LambdaUpdateWrapper<>();
        lambda.eq(OmsOrder::getDeleteStatus, 0);
        lambda.in(OmsOrder::getId, ids);
        lambda.set(OmsOrder::getStatus, 4);
        return update(lambda);
    }

    @Override
    public boolean delete(List<Long> ids) {
        LambdaUpdateWrapper<OmsOrder> lambda = new LambdaUpdateWrapper<>();
        lambda.eq(OmsOrder::getDeleteStatus, 0);
        lambda.in(OmsOrder::getId, ids);
        lambda.set(OmsOrder::getDeleteStatus, 1);
        return update(lambda);
    }

    @Override
    public OmsOrderDetail detail(Long id) {
        return getDetail(id);
    }

    @Override
    public boolean updateReceiverInfo(OmsReceiverInfoParam receiverInfoParam) {
        //插入操作记录
        OmsOrderOperateHistory history = new OmsOrderOperateHistory();
        history.setOrderId(receiverInfoParam.getOrderId());
        history.setCreateTime(new Date());
        history.setOperateMan("后台管理员");
        history.setOrderStatus(receiverInfoParam.getStatus());
        history.setNote("修改收货人信息");
        orderOperateHistoryRepository.save(history);

        OmsOrder order = new OmsOrder();
        order.setId(receiverInfoParam.getOrderId());
        order.setReceiverName(receiverInfoParam.getReceiverName());
        order.setReceiverPhone(receiverInfoParam.getReceiverPhone());
        order.setReceiverPostCode(receiverInfoParam.getReceiverPostCode());
        order.setReceiverDetailAddress(receiverInfoParam.getReceiverDetailAddress());
        order.setReceiverProvince(receiverInfoParam.getReceiverProvince());
        order.setReceiverCity(receiverInfoParam.getReceiverCity());
        order.setReceiverRegion(receiverInfoParam.getReceiverRegion());
        order.setModifyTime(new Date());
        return updateById(order);
    }

    @Override
    public boolean updateMoneyInfo(OmsMoneyInfoParam moneyInfoParam) {
        //插入操作记录
        OmsOrderOperateHistory history = new OmsOrderOperateHistory();
        history.setOrderId(moneyInfoParam.getOrderId());
        history.setCreateTime(new Date());
        history.setOperateMan("后台管理员");
        history.setOrderStatus(moneyInfoParam.getStatus());
        history.setNote("修改费用信息");
        orderOperateHistoryRepository.save(history);

        OmsOrder order = new OmsOrder();
        order.setId(moneyInfoParam.getOrderId());
        order.setFreightAmount(moneyInfoParam.getFreightAmount());
        order.setDiscountAmount(moneyInfoParam.getDiscountAmount());
        order.setModifyTime(new Date());
        return updateById(order);
    }

    @Override
    public boolean updateNote(Long id, String note, Integer status) {
        OmsOrderOperateHistory history = new OmsOrderOperateHistory();
        history.setOrderId(id);
        history.setCreateTime(new Date());
        history.setOperateMan("后台管理员");
        history.setOrderStatus(status);
        history.setNote("修改备注信息："+note);
        orderOperateHistoryRepository.save(history);

        OmsOrder order = new OmsOrder();
        order.setId(id);
        order.setNote(note);
        order.setModifyTime(new Date());
        return updateById(order);
    }
}
