package com.macro.mall.service;

import com.macro.mall.oms.model.OmsOrderSetting;
import com.macro.mall.oms.service.OmsOrderSettingRepository;

/**
 * 订单设置Service
 * Created by macro on 2018/10/16.
 */
public interface OmsOrderSettingService extends OmsOrderSettingRepository {
    /**
     * 获取指定订单设置
     */
    OmsOrderSetting getItem(Long id);

    /**
     * 修改指定订单设置
     */
    boolean update(Long id, OmsOrderSetting orderSetting);
}
