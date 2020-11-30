package com.macro.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.macro.mall.pms.model.PmsSkuStock;
import com.macro.mall.pms.service.impl.PmsSkuStockRepositoryImpl;
import com.macro.mall.service.PmsSkuStockService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品sku库存管理Service实现类
 * Created by macro on 2018/4/27.
 */
@Service
public class PmsSkuStockServiceImpl extends PmsSkuStockRepositoryImpl implements PmsSkuStockService {
    @Override
    public List<PmsSkuStock> getList(Long pid, String keyword) {
        LambdaQueryWrapper<PmsSkuStock> lambda = new LambdaQueryWrapper<>();
        lambda.eq(PmsSkuStock::getProductId, pid);
        lambda.like(PmsSkuStock::getSkuCode, keyword);
        return list(lambda);

    }

    @Override
    public boolean update(Long pid, List<PmsSkuStock> skuStockList) {
        return saveBatch(skuStockList);
    }
}
