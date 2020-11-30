package com.macro.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.macro.mall.oms.model.OmsOrderReturnReason;
import com.macro.mall.oms.service.impl.OmsOrderReturnReasonRepositoryImpl;
import com.macro.mall.service.OmsOrderReturnReasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 订单原因管理Service实现类
 * Created by macro on 2018/10/17.
 */
@Service
public class OmsOrderReturnReasonServiceImpl extends OmsOrderReturnReasonRepositoryImpl implements OmsOrderReturnReasonService {
    @Override
    public boolean create(OmsOrderReturnReason returnReason) {
        returnReason.setCreateTime(new Date());
        return save(returnReason);
    }

    @Override
    public boolean update(Long id, OmsOrderReturnReason returnReason) {
        returnReason.setId(id);
        return updateById(returnReason);
    }

    @Override
    public boolean delete(List<Long> ids) {
        return removeByIds(ids);
    }

    @Override
    public Page<OmsOrderReturnReason> list(Integer pageSize, Integer pageNum) {
        Page<OmsOrderReturnReason> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<OmsOrderReturnReason> lambda = new  LambdaQueryWrapper<>();
        lambda.orderByDesc(OmsOrderReturnReason::getSort);
        return page(page, lambda);
    }

    @Override
    public boolean updateStatus(List<Long> ids, Integer status) {
        if(!status.equals(0)&&!status.equals(1)){
            return false;
        }
        LambdaUpdateWrapper<OmsOrderReturnReason> lambda = new LambdaUpdateWrapper<>();
        lambda.in(OmsOrderReturnReason::getId, ids);
        lambda.set(OmsOrderReturnReason::getStatus, status);
        return update(lambda);
    }

    @Override
    public OmsOrderReturnReason getItem(Long id) {
        return getById(id);
    }
}
