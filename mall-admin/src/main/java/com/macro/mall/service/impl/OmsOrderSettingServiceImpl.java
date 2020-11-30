package com.macro.mall.service.impl;

import com.macro.mall.oms.model.OmsOrderSetting;
import com.macro.mall.oms.service.impl.OmsOrderSettingRepositoryImpl;
import com.macro.mall.service.OmsOrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 订单设置管理Service实现类
 * Created by macro on 2018/10/16.
 */
@Service
public class OmsOrderSettingServiceImpl extends OmsOrderSettingRepositoryImpl implements OmsOrderSettingService {

    @Override
    public OmsOrderSetting getItem(Long id) {
        return getById(id);
    }

    @Override
    public boolean update(Long id, OmsOrderSetting orderSetting) {
        orderSetting.setId(id);
        return updateById(orderSetting);
    }
}
