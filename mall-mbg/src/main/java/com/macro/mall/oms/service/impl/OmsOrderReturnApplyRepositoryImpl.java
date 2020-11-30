package com.macro.mall.oms.service.impl;

import com.macro.mall.oms.dto.OmsOrderReturnApplyResult;
import com.macro.mall.oms.model.OmsOrderReturnApply;
import com.macro.mall.oms.mapper.OmsOrderReturnApplyMapper;
import com.macro.mall.oms.service.OmsOrderReturnApplyRepository;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单退货申请 服务实现类
 * </p>
 *
 * @author dongjb
 * @since 2020-11-28
 */
@Service
public class OmsOrderReturnApplyRepositoryImpl extends ServiceImpl<OmsOrderReturnApplyMapper, OmsOrderReturnApply> implements OmsOrderReturnApplyRepository {
    /**
     * 获取申请详情
     */
    @Autowired
    private OmsOrderReturnApplyMapper orderReturnApplyMapper;
    @Override
    public OmsOrderReturnApplyResult getDetail(Long id) {
        return orderReturnApplyMapper.getDetail(id);
    }
}
