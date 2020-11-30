package com.macro.mall.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonPagePlus;
import com.macro.mall.pms.model.PmsBrand;
import com.macro.mall.pms.model.PmsProduct;
import com.macro.mall.pms.service.PmsProductRepository;
import com.macro.mall.pms.service.impl.PmsBrandRepositoryImpl;
import com.macro.mall.portal.service.PortalBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 前台品牌管理Service实现类
 * Created by macro on 2020/5/15.
 */
@Service
public class PortalBrandServiceImpl extends PmsBrandRepositoryImpl implements PortalBrandService {
    @Autowired
    private PmsProductRepository productRepository;

    @Override
    public List<PmsBrand> recommendList(Integer pageNum, Integer pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return getRecommendBrandList(offset, pageSize);
    }

    @Override
    public PmsBrand detail(Long brandId) {
        return getById(brandId);
    }

    @Override
    public CommonPage<PmsProduct> productList(Long brandId, Integer pageNum, Integer pageSize) {
        Page<PmsProduct> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PmsProduct> lambda = new LambdaQueryWrapper<>();
        lambda.eq(PmsProduct::getDeleteStatus, 0);
        lambda.eq(PmsProduct::getBrandId, brandId);
        Page<PmsProduct> productList = productRepository.page(page, lambda);
        return CommonPagePlus.restPage(productList);
    }
}
