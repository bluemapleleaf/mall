package com.macro.mall.pms.service;

import com.macro.mall.oms.model.OmsOrderItem;
import com.macro.mall.pms.model.PmsSkuStock;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * sku的库存 服务类
 * </p>
 *
 * @author dongjb
 * @since 2020-11-27
 */
public interface PmsSkuStockRepository extends IService<PmsSkuStock> {
    /**
     * 修改 pms_sku_stock表的锁定库存及真实库存
     */
    int updateSkuStock(List<OmsOrderItem> orderItemList);

    /**
     * 解除取消订单的库存锁定
     */
    int releaseSkuStockLock(List<OmsOrderItem> orderItemList);

}
