package com.macro.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.domain.OmsReturnApplyQueryParam;
import com.macro.mall.domain.OmsUpdateStatusParam;
import com.macro.mall.oms.dto.OmsOrderReturnApplyResult;
import com.macro.mall.oms.model.OmsOrderReturnApply;
import com.macro.mall.oms.service.OmsOrderReturnApplyRepository;

import java.util.List;

/**
 * 退货申请管理Service
 * Created by macro on 2018/10/18.
 */
public interface OmsOrderReturnApplyService extends OmsOrderReturnApplyRepository {
    /**
     * 分页查询申请
     */
    Page<OmsOrderReturnApply> list(OmsReturnApplyQueryParam queryParam, Integer pageSize, Integer pageNum);

    /**
     * 批量删除申请
     */
    boolean delete(List<Long> ids);

    /**
     * 修改申请状态
     */
    boolean updateStatus(Long id, OmsUpdateStatusParam statusParam);

    /**
     * 获取指定申请详情
     */
    OmsOrderReturnApplyResult getItem(Long id);
}
