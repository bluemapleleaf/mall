package com.macro.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.domain.PmsProductCategoryParam;
import com.macro.mall.pms.model.PmsProduct;
import com.macro.mall.pms.model.PmsProductCategory;
import com.macro.mall.pms.model.PmsProductCategoryAttributeRelation;
import com.macro.mall.pms.service.PmsProductCategoryAttributeRelationRepository;
import com.macro.mall.pms.service.impl.PmsProductCategoryRepositoryImpl;
import com.macro.mall.service.PmsProductCategoryService;
import com.macro.mall.service.PmsProductService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * PmsProductCategoryService实现类
 * Created by macro on 2018/4/26.
 */
@Service
public class PmsProductCategoryServiceImpl extends PmsProductCategoryRepositoryImpl implements PmsProductCategoryService {
    @Autowired
    private PmsProductService productService;
    @Autowired
    private PmsProductCategoryAttributeRelationRepository productCategoryAttributeRelationRepository;

    @Override
    public boolean create(PmsProductCategoryParam pmsProductCategoryParam) {
        PmsProductCategory productCategory = new PmsProductCategory();
        productCategory.setProductCount(0);
        BeanUtils.copyProperties(pmsProductCategoryParam, productCategory);
        //没有父分类时为一级分类
        setCategoryLevel(productCategory);
        boolean ret = save(productCategory);
        //创建筛选属性关联
        List<Long> productAttributeIdList = pmsProductCategoryParam.getProductAttributeIdList();
        if(!CollectionUtils.isEmpty(productAttributeIdList)){
            insertRelationList(productCategory.getId(), productAttributeIdList);
        }
        return ret;
    }

    /**
     * 批量插入商品分类与筛选属性关系表
     * @param productCategoryId 商品分类id
     * @param productAttributeIdList 相关商品筛选属性id集合
     */
    private void insertRelationList(Long productCategoryId, List<Long> productAttributeIdList) {
        List<PmsProductCategoryAttributeRelation> relationList = new ArrayList<>();
        for (Long productAttrId : productAttributeIdList) {
            PmsProductCategoryAttributeRelation relation = new PmsProductCategoryAttributeRelation();
            relation.setProductAttributeId(productAttrId);
            relation.setProductCategoryId(productCategoryId);
            relationList.add(relation);
        }
        productCategoryAttributeRelationRepository.saveBatch(relationList);
    }

    @Override
    public boolean update(Long id, PmsProductCategoryParam pmsProductCategoryParam) {
        PmsProductCategory productCategory = new PmsProductCategory();
        productCategory.setId(id);
        BeanUtils.copyProperties(pmsProductCategoryParam, productCategory);
        setCategoryLevel(productCategory);
        //更新商品分类时要更新商品中的名称
        LambdaUpdateWrapper<PmsProduct> lambda = new LambdaUpdateWrapper<>();
        lambda.eq(PmsProduct::getProductCategoryId, id);
        lambda.set(PmsProduct::getName, productCategory.getName());
        productService.update(lambda);
        //同时更新筛选属性的信息
        if(!CollectionUtils.isEmpty(pmsProductCategoryParam.getProductAttributeIdList())){
            LambdaQueryWrapper<PmsProductCategoryAttributeRelation> lambdaRelation = new LambdaQueryWrapper<>();
            lambdaRelation.eq(PmsProductCategoryAttributeRelation::getProductCategoryId, id);
            productCategoryAttributeRelationRepository.remove(lambdaRelation);
            insertRelationList(id,pmsProductCategoryParam.getProductAttributeIdList());
        }else{
            LambdaQueryWrapper<PmsProductCategoryAttributeRelation> lambdaRelation = new LambdaQueryWrapper<>();
            lambdaRelation.eq(PmsProductCategoryAttributeRelation::getProductCategoryId, id);
            productCategoryAttributeRelationRepository.remove(lambdaRelation);
        }
        return updateById(productCategory);
    }

    @Override
    public Page<PmsProductCategory> getList(Long parentId, Integer pageSize, Integer pageNum) {
        Page<PmsProductCategory> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PmsProductCategory> lambda = new LambdaQueryWrapper<>();
        if(parentId != null){
            lambda.eq(PmsProductCategory::getParentId, parentId);
        }
        return page(page, lambda);
    }

    @Override
    public boolean delete(Long id) {
        return removeById(id);
    }

    @Override
    public PmsProductCategory getItem(Long id) {
        return getById(id);
    }

    @Override
    public boolean updateNavStatus(List<Long> ids, Integer navStatus) {
        LambdaUpdateWrapper<PmsProductCategory> lambda = new LambdaUpdateWrapper<>();
        lambda.in(PmsProductCategory::getId, ids);
        lambda.set(PmsProductCategory::getNavStatus, navStatus);
        return update(lambda);
    }

    @Override
    public boolean updateShowStatus(List<Long> ids, Integer showStatus) {
        LambdaUpdateWrapper<PmsProductCategory> lambda = new LambdaUpdateWrapper();
        lambda.in(PmsProductCategory::getId, ids);
        lambda.set(PmsProductCategory::getShowStatus, showStatus);
        return update(lambda);
    }

    /**
     * 根据分类的parentId设置分类的level
     */
    private void setCategoryLevel(PmsProductCategory productCategory) {
        //没有父分类时为一级分类
        if (productCategory.getParentId() == 0) {
            productCategory.setLevel(0);
        } else {
            //有父分类时选择根据父分类level设置
            PmsProductCategory parentCategory =getById(productCategory.getParentId());
            if (parentCategory != null) {
                productCategory.setLevel(parentCategory.getLevel() + 1);
            } else {
                productCategory.setLevel(0);
            }
        }
    }
}
