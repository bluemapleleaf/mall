package com.macro.mall.pms.mapper;

import com.macro.mall.oms.model.OmsOrderItem;
import com.macro.mall.pms.model.PmsSkuStock;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * sku的库存 Mapper 接口
 * </p>
 *
 * @author dongjb
 * @since 2020-11-27
 */
public interface PmsSkuStockMapper extends BaseMapper<PmsSkuStock> {
    /**
     * 修改 pms_sku_stock表的锁定库存及真实库存
     */
    int updateSkuStock(@Param("itemList") List<OmsOrderItem> orderItemList);

    /**
     * 解除取消订单的库存锁定
     */
    int releaseSkuStockLock(@Param("itemList") List<OmsOrderItem> orderItemList);

}
