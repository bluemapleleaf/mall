package com.macro.mall.service;

import com.macro.mall.pms.model.PmsSkuStock;
import com.macro.mall.pms.service.PmsSkuStockRepository;

import java.util.List;

/**
 * sku商品库存管理Service
 * Created by macro on 2018/4/27.
 */
public interface PmsSkuStockService extends PmsSkuStockRepository {
    /**
     * 根据产品id和skuCode模糊搜索
     */
    List<PmsSkuStock> getList(Long pid, String keyword);

    /**
     * 批量更新商品库存信息
     */
    boolean update(Long pid, List<PmsSkuStock> skuStockList);
}
