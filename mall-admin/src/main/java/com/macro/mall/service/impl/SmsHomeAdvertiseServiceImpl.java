package com.macro.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.macro.mall.service.SmsHomeAdvertiseService;
import com.macro.mall.sms.model.SmsHomeAdvertise;
import com.macro.mall.sms.service.impl.SmsHomeAdvertiseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 首页广告管理Service实现类
 * Created by macro on 2018/11/7.
 */
@Service
public class SmsHomeAdvertiseServiceImpl extends SmsHomeAdvertiseRepositoryImpl implements SmsHomeAdvertiseService {
    @Override
    public boolean create(SmsHomeAdvertise advertise) {
        advertise.setClickCount(0);
        advertise.setOrderCount(0);
        return save(advertise);
    }

    @Override
    public boolean delete(List<Long> ids) {
        return removeByIds(ids);
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        SmsHomeAdvertise record = new SmsHomeAdvertise();
        record.setId(id);
        record.setStatus(status);
        return updateById(record);
    }

    @Override
    public SmsHomeAdvertise getItem(Long id) {
        return getById(id);
    }

    @Override
    public boolean update(Long id, SmsHomeAdvertise advertise) {
        advertise.setId(id);
        return updateById(advertise);
    }

    @Override
    public Page<SmsHomeAdvertise> list(String name, Integer type, String endTime, Integer pageSize, Integer pageNum) {

        Page<SmsHomeAdvertise> page = new Page<>();
        LambdaQueryWrapper<SmsHomeAdvertise> lambda = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            lambda.like(SmsHomeAdvertise::getName, name);
        }
        if (type != null) {
            lambda.eq(SmsHomeAdvertise::getType, type);
        }
        if (!StringUtils.isEmpty(endTime)) {
            String startStr = endTime + " 00:00:00";
            String endStr = endTime + " 23:59:59";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start = null;
            try {
                start = sdf.parse(startStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date end = null;
            try {
                end = sdf.parse(endStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (start != null && end != null) {
                lambda.between(SmsHomeAdvertise::getEndTime, start, end);
            }
        }
        lambda.orderByDesc(SmsHomeAdvertise::getSort);
        return page(page, lambda);
    }
}
