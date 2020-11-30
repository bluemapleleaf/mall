package com.macro.mall.service;

import com.macro.mall.oms.model.OmsCompanyAddress;
import com.macro.mall.oms.service.OmsCompanyAddressRepository;

import java.util.List;

/**
 * 收货地址管Service
 * Created by macro on 2018/10/18.
 */
public interface OmsCompanyAddressService extends OmsCompanyAddressRepository {
    /**
     * 获取全部收货地址
     */
    List<OmsCompanyAddress> getlist();
}
