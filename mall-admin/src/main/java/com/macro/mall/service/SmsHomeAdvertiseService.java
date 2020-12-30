package com.macro.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.sms.model.SmsHomeAdvertise;
import com.macro.mall.sms.service.SmsHomeAdvertiseRepository;

import java.util.List;

/**
 * 首页广告管理Service
 * Created by macro on 2018/11/7.
 */
public interface SmsHomeAdvertiseService extends SmsHomeAdvertiseRepository {
    /**
     * 添加广告
     */
    boolean create(SmsHomeAdvertise advertise);

    /**
     * 批量删除广告
     */
    boolean delete(List<Long> ids);

    /**
     * 修改上、下线状态
     */
    boolean updateStatus(Long id, Integer status);

    /**
     * 获取广告详情
     */
    SmsHomeAdvertise getItem(Long id);

    /**
     * 更新广告
     */
    boolean update(Long id, SmsHomeAdvertise advertise);

    /**
     * 分页查询广告
     */
    Page<SmsHomeAdvertise> list(String name, Integer type, String endTime, Integer pageSize, Integer pageNum);
}
