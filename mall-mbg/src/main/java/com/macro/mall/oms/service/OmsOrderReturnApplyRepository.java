package com.macro.mall.oms.service;

import com.macro.mall.oms.dto.OmsOrderReturnApplyResult;
import com.macro.mall.oms.model.OmsOrderReturnApply;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 订单退货申请 服务类
 * </p>
 *
 * @author dongjb
 * @since 2020-11-28
 */
public interface OmsOrderReturnApplyRepository extends IService<OmsOrderReturnApply> {
    /**
     * 获取申请详情
     */
    OmsOrderReturnApplyResult getDetail(@Param("id")Long id);
}
