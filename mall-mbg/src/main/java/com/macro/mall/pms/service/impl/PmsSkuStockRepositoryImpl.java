package com.macro.mall.pms.service.impl;

import com.macro.mall.oms.model.OmsOrderItem;
import com.macro.mall.pms.model.PmsSkuStock;
import com.macro.mall.pms.mapper.PmsSkuStockMapper;
import com.macro.mall.pms.service.PmsSkuStockRepository;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * sku的库存 服务实现类
 * </p>
 *
 * @author dongjb
 * @since 2020-11-27
 */
@Service
public class PmsSkuStockRepositoryImpl extends ServiceImpl<PmsSkuStockMapper, PmsSkuStock> implements PmsSkuStockRepository {
    /**
     * 修改 pms_sku_stock表的锁定库存及真实库存
     */
    @Autowired
    PmsSkuStockMapper skuStockMapper;
    @Override
    public int updateSkuStock(List<OmsOrderItem> orderItemList){
        return skuStockMapper.updateSkuStock(orderItemList);
    }

    /**
     * 解除取消订单的库存锁定
     */
    @Override
    public int releaseSkuStockLock(List<OmsOrderItem> orderItemList){
        return skuStockMapper.releaseSkuStockLock(orderItemList);
    }

}
