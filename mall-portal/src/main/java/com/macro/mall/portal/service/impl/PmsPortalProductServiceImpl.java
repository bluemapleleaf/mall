package com.macro.mall.portal.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.pms.model.*;
import com.macro.mall.pms.service.*;
import com.macro.mall.portal.domain.PmsPortalProductDetail;
import com.macro.mall.portal.domain.PmsProductCategoryNode;
import com.macro.mall.portal.service.PmsPortalProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 前台订单管理Service实现类
 * Created by macro on 2020/4/6.
 */
@Service
public class PmsPortalProductServiceImpl implements PmsPortalProductService {
    @Autowired
    private PmsProductRepository productRepository;
    @Autowired
    private PmsProductCategoryRepository productCategoryRepository;
    @Qualifier("pmsBrandRepositoryImpl")
    @Autowired
    private PmsBrandRepository brandRepository;
    @Autowired
    private PmsProductAttributeRepository productAttributeRepository;
    @Autowired
    private PmsProductAttributeValueRepository productAttributeValueRepository;
    @Autowired
    private PmsSkuStockRepository skuStockRepository;
    @Autowired
    private PmsProductLadderRepository productLadderRepository;
    @Autowired
    private PmsProductFullReductionRepository productFullReductionRepository;

    @Override
    public Page<PmsProduct> search(String keyword, Long brandId, Long productCategoryId, Integer pageNum, Integer pageSize, Integer sort) {
        Page<PmsProduct> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PmsProduct> lambda = new LambdaQueryWrapper<>();
        lambda.eq(PmsProduct::getDeleteStatus, 0);
        if (StrUtil.isNotEmpty(keyword)) {
            lambda.like(PmsProduct::getName, keyword);
        }
        if (brandId != null) {
            lambda.eq(PmsProduct::getBrandId, brandId);
        }
        if (productCategoryId != null) {
            lambda.eq(PmsProduct::getProductCategoryId, productCategoryId);
        }
        //1->按新品；2->按销量；3->价格从低到高；4->价格从高到低
        if (sort == 1) {
            lambda.orderByDesc(PmsProduct::getId);
        } else if (sort == 2) {
            lambda.orderByDesc(PmsProduct::getSale);
        } else if (sort == 3) {
            lambda.orderByAsc(PmsProduct::getPrice);
        } else if (sort == 4) {
            lambda.orderByDesc(PmsProduct::getPrice);
        }
        return productRepository.page(page, lambda);
    }

    @Override
    public List<PmsProductCategoryNode> categoryTreeList() {
        List<PmsProductCategory> allList = productCategoryRepository.list();
        List<PmsProductCategoryNode> result = allList.stream()
                .filter(item -> item.getParentId().equals(0L))
                .map(item -> covert(item, allList)).collect(Collectors.toList());
        return result;
    }

    @Override
    public PmsPortalProductDetail detail(Long id) {
        PmsPortalProductDetail result = new PmsPortalProductDetail();
        //获取商品信息
        PmsProduct product = productRepository.getById(id);
        result.setProduct(product);
        //获取品牌信息
        PmsBrand brand = brandRepository.getById(product.getBrandId());
        result.setBrand(brand);
        //获取商品属性信息

        LambdaQueryWrapper<PmsProductAttribute> lambda = new LambdaQueryWrapper<>();
        lambda.eq(PmsProductAttribute::getProductAttributeCategoryId, product.getProductAttributeCategoryId());
        List<PmsProductAttribute> productAttributeList = productAttributeRepository.list(lambda);
        result.setProductAttributeList(productAttributeList);
        //获取商品属性值信息
        if(CollUtil.isNotEmpty(productAttributeList)){
            List<Long> attributeIds = productAttributeList.stream().map(PmsProductAttribute::getId).collect(Collectors.toList());
            LambdaQueryWrapper<PmsProductAttributeValue> lambdaQuery = new LambdaQueryWrapper<>();
            lambdaQuery.eq(PmsProductAttributeValue::getId, product.getId());
            lambdaQuery.in(PmsProductAttributeValue::getProductAttributeId, attributeIds);
            List<PmsProductAttributeValue> productAttributeValueList = productAttributeValueRepository.list(lambdaQuery);
            result.setProductAttributeValueList(productAttributeValueList);
        }
        //获取商品SKU库存信息
        LambdaQueryWrapper<PmsSkuStock> lambdaStock = new LambdaQueryWrapper<>();
        lambdaStock.eq(PmsSkuStock::getProductId,product.getId());
        List<PmsSkuStock> skuStockList = skuStockRepository.list(lambdaStock);
        result.setSkuStockList(skuStockList);
        //商品阶梯价格设置
        if(product.getPromotionType()==3){
            LambdaQueryWrapper<PmsProductLadder> lambdaLadder = new LambdaQueryWrapper<>();
            lambdaLadder.eq(PmsProductLadder::getProductId, product.getId());
            List<PmsProductLadder> productLadderList = productLadderRepository.list(lambdaLadder);
            result.setProductLadderList(productLadderList);
        }
        //商品满减价格设置
        if(product.getPromotionType()==4){
            LambdaQueryWrapper<PmsProductFullReduction> lambdaFullReduction = new LambdaQueryWrapper<>();
            lambdaFullReduction.eq(PmsProductFullReduction::getProductId, product.getId());
            List<PmsProductFullReduction> productFullReductionList  = productFullReductionRepository.list(lambdaFullReduction);
            result.setProductFullReductionList(productFullReductionList);
        }
        //商品可用优惠券
        result.setCouponList(productRepository.getAvailableCouponList(product.getId(),product.getProductCategoryId()));
        return result;
    }


    /**
     * 初始对象转化为节点对象
     */
    private PmsProductCategoryNode covert(PmsProductCategory item, List<PmsProductCategory> allList) {
        PmsProductCategoryNode node = new PmsProductCategoryNode();
        BeanUtils.copyProperties(item, node);
        List<PmsProductCategoryNode> children = allList.stream()
                .filter(subItem -> subItem.getParentId().equals(item.getId()))
                .map(subItem -> covert(subItem, allList)).collect(Collectors.toList());
        node.setChildren(children);
        return node;
    }
}
