package com.macro.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.domain.OmsReturnApplyQueryParam;
import com.macro.mall.domain.OmsUpdateStatusParam;
import com.macro.mall.oms.dto.OmsOrderReturnApplyResult;
import com.macro.mall.oms.model.OmsOrderReturnApply;
import com.macro.mall.oms.service.impl.OmsOrderReturnApplyRepositoryImpl;
import com.macro.mall.service.OmsOrderReturnApplyService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 订单退货管理Service
 * Created by macro on 2018/10/18.
 */
@Service
public class OmsOrderReturnApplyServiceImpl extends OmsOrderReturnApplyRepositoryImpl implements OmsOrderReturnApplyService {
    @Override
    public Page<OmsOrderReturnApply> list(OmsReturnApplyQueryParam queryParam, Integer pageSize, Integer pageNum) {
        Page<OmsOrderReturnApply> page = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<OmsOrderReturnApply> lambda = new LambdaQueryWrapper<>();

        if(queryParam.getId() != null){
            lambda.eq(OmsOrderReturnApply::getId, queryParam.getId());
        }
        if(queryParam.getStatus() != null){
            lambda.eq(OmsOrderReturnApply::getStatus, queryParam.getStatus());
        }
        if(!StringUtils.isEmpty(queryParam.getReceiverKeyword())){
            lambda.like(OmsOrderReturnApply::getReturnName, queryParam.getReceiverKeyword());
            lambda.or().like(OmsOrderReturnApply::getReturnPhone, queryParam.getReceiverKeyword());
        }
        if(!StringUtils.isEmpty(queryParam.getHandleMan())){
            lambda.eq(OmsOrderReturnApply::getHandleMan, queryParam.getHandleMan());
        }
        if(!StringUtils.isEmpty(queryParam.getHandleTime())){
            lambda.like(OmsOrderReturnApply::getHandleTime, queryParam.getHandleTime());
        }
        if(!StringUtils.isEmpty(queryParam.getCreateTime())){
            lambda.like(OmsOrderReturnApply::getCreateTime, queryParam.getCreateTime());
        }


        return page(page, lambda);

    }

    @Override
    public boolean delete(List<Long> ids) {
        LambdaQueryWrapper<OmsOrderReturnApply> lambda = new LambdaQueryWrapper<>();
        lambda.in(OmsOrderReturnApply::getId, ids);
        lambda.in(OmsOrderReturnApply::getStatus, 3);
        return remove(lambda);
    }

    @Override
    public boolean updateStatus(Long id, OmsUpdateStatusParam statusParam) {
        Integer status = statusParam.getStatus();
        OmsOrderReturnApply returnApply = new OmsOrderReturnApply();
        if(status.equals(1)){
            //确认退货
            returnApply.setId(id);
            returnApply.setStatus(1);
            returnApply.setReturnAmount(statusParam.getReturnAmount());
            returnApply.setCompanyAddressId(statusParam.getCompanyAddressId());
            returnApply.setHandleTime(new Date());
            returnApply.setHandleMan(statusParam.getHandleMan());
            returnApply.setHandleNote(statusParam.getHandleNote());
        }else if(status.equals(2)){
            //完成退货
            returnApply.setId(id);
            returnApply.setStatus(2);
            returnApply.setReceiveTime(new Date());
            returnApply.setReceiveMan(statusParam.getReceiveMan());
            returnApply.setReceiveNote(statusParam.getReceiveNote());
        }else if(status.equals(3)){
            //拒绝退货
            returnApply.setId(id);
            returnApply.setStatus(3);
            returnApply.setHandleTime(new Date());
            returnApply.setHandleMan(statusParam.getHandleMan());
            returnApply.setHandleNote(statusParam.getHandleNote());
        }else{
            return false;
        }
        return updateById(returnApply);
    }

    @Override
    public OmsOrderReturnApplyResult getItem(Long id) {
        return getDetail(id);
    }
}
