package com.macro.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.oms.model.OmsOrderReturnReason;
import com.macro.mall.oms.service.OmsOrderReturnReasonRepository;

import java.util.List;

/**
 * 订单原因管理Service
 * Created by macro on 2018/10/17.
 */
public interface OmsOrderReturnReasonService extends OmsOrderReturnReasonRepository {
    /**
     * 添加订单原因
     */
    boolean create(OmsOrderReturnReason returnReason);

    /**
     * 修改退货原因
     */
    boolean update(Long id, OmsOrderReturnReason returnReason);

    /**
     * 批量删除退货原因
     */
    boolean delete(List<Long> ids);

    /**
     * 分页获取退货原因
     */
    Page<OmsOrderReturnReason> list(Integer pageSize, Integer pageNum);

    /**
     * 批量修改退货原因状态
     */
    boolean updateStatus(List<Long> ids, Integer status);

    /**
     * 获取单个退货原因详情信息
     */
    OmsOrderReturnReason getItem(Long id);
}
