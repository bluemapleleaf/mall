package com.macro.mall.oms.mapper;

import com.macro.mall.oms.dto.OmsOrderReturnApplyResult;
import com.macro.mall.oms.model.OmsOrderReturnApply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 订单退货申请 Mapper 接口
 * </p>
 *
 * @author dongjb
 * @since 2020-11-28
 */
public interface OmsOrderReturnApplyMapper extends BaseMapper<OmsOrderReturnApply> {
    /**
     * 获取申请详情
     */
    OmsOrderReturnApplyResult getDetail(@Param("id")Long id);
}
