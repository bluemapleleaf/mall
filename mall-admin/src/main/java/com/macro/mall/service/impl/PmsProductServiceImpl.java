package com.macro.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.cms.model.CmsPrefrenceAreaProductRelation;
import com.macro.mall.cms.model.CmsSubjectProductRelation;
import com.macro.mall.cms.service.CmsPrefrenceAreaProductRelationRepository;
import com.macro.mall.cms.service.CmsSubjectProductRelationRepository;
import com.macro.mall.domain.PmsProductQueryParam;
import com.macro.mall.pms.dto.PmsProductParam;
import com.macro.mall.pms.model.*;
import com.macro.mall.pms.service.*;
import com.macro.mall.pms.service.impl.PmsProductRepositoryImpl;
import com.macro.mall.service.PmsProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品管理Service实现类
 * Created by macro on 2018/4/26.
 */
@Service
public class PmsProductServiceImpl extends PmsProductRepositoryImpl implements PmsProductService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PmsProductServiceImpl.class);
    @Autowired
    private PmsMemberPriceRepository memberPriceRepository;
    @Autowired
    private PmsProductLadderRepository productLadderRepository;
    @Autowired
    private PmsProductFullReductionRepository productFullReductionRepository;
    @Qualifier("pmsSkuStockRepositoryImpl")
    @Autowired
    private PmsSkuStockRepository skuStockRepository;
    @Autowired
    private PmsProductAttributeValueRepository productAttributeValueRepository;
    @Autowired
    private CmsSubjectProductRelationRepository cmsSubjectProductRelationRepository;
    @Autowired
    private CmsPrefrenceAreaProductRelationRepository cmsPrefrenceAreaProductRelationRepository;

    @Autowired
    private PmsProductVertifyRecordRepository pmsProductVertifyRecordRepository;


    @Override
    public int create(PmsProductParam productParam) {
        int count;
        //创建商品
        PmsProduct product = productParam;
        product.setId(null);
        save(product);
        //根据促销类型设置价格：会员价格、阶梯价格、满减价格
        Long productId = product.getId();
        //会员价格
        relateAndInsertList(memberPriceRepository, productParam.getMemberPriceList(), productId);
        //阶梯价格
        relateAndInsertList(productLadderRepository, productParam.getProductLadderList(), productId);
        //满减价格
        relateAndInsertList(productFullReductionRepository, productParam.getProductFullReductionList(), productId);
        //处理sku的编码
        handleSkuStockCode(productParam.getSkuStockList(),productId);
        //添加sku库存信息
        relateAndInsertList(skuStockRepository, productParam.getSkuStockList(), productId);
        //添加商品参数,添加自定义商品规格
        relateAndInsertList(productAttributeValueRepository, productParam.getProductAttributeValueList(), productId);
        //关联专题
        relateSubjects(productId, productParam.getSubjectProductRelationList());
        //关联优选
        relatePrefrenceAreas(productId, productParam.getPrefrenceAreaProductRelationList());
        count = 1;
        return count;
    }

    private void handleSkuStockCode(List<PmsSkuStock> skuStockList, Long productId) {
        if(CollectionUtils.isEmpty(skuStockList)) {
            return;
        }
        for(int i=0;i<skuStockList.size();i++){
            PmsSkuStock skuStock = skuStockList.get(i);
            if(StringUtils.isEmpty(skuStock.getSkuCode())){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                StringBuilder sb = new StringBuilder();
                //日期
                sb.append(sdf.format(new Date()));
                //四位商品id
                sb.append(String.format("%04d", productId));
                //三位索引id
                sb.append(String.format("%03d", i+1));
                skuStock.setSkuCode(sb.toString());
            }
        }
    }

    @Override
    public int update(Long id, PmsProductParam productParam) {
        int count;
        //更新商品信息
        PmsProduct product = productParam;
        product.setId(id);
        updateById(product);
        //会员价格
        LambdaQueryWrapper<PmsMemberPrice> lambdaMemberPrice = new LambdaQueryWrapper<>();
        lambdaMemberPrice.eq(PmsMemberPrice::getProductId, id);
        memberPriceRepository.remove(lambdaMemberPrice);
        relateAndInsertList(memberPriceRepository, productParam.getMemberPriceList(), id);
        //阶梯价格
        LambdaQueryWrapper<PmsProductLadder> lambdaProductLadder = new LambdaQueryWrapper<>();
        lambdaProductLadder.eq(PmsProductLadder::getProductId, id);
        productLadderRepository.remove(lambdaProductLadder);
        relateAndInsertList(productLadderRepository, productParam.getProductLadderList(), id);
        //满减价格
        LambdaQueryWrapper<PmsProductFullReduction> lambdafullReduction = new LambdaQueryWrapper<>();
        lambdafullReduction.eq(PmsProductFullReduction::getProductId, id);
        productFullReductionRepository.remove(lambdafullReduction);
        relateAndInsertList(productFullReductionRepository, productParam.getProductFullReductionList(), id);
        //修改sku库存信息
        handleUpdateSkuStockList(id, productParam);
        //修改商品参数,添加自定义商品规格
        productAttributeValueRepository.removeById(id);
        relateAndInsertList(productAttributeValueRepository, productParam.getProductAttributeValueList(), id);
        //关联专题
        relateSubjects(id, productParam.getSubjectProductRelationList());
        //关联优选
        relatePrefrenceAreas(id, productParam.getPrefrenceAreaProductRelationList());
        count = 1;
        return count;
    }

    private void handleUpdateSkuStockList(Long id, PmsProductParam productParam) {
        //当前的sku信息
        List<PmsSkuStock> currSkuList = productParam.getSkuStockList();
        //当前没有sku直接删除
        if(CollUtil.isEmpty(currSkuList)){
            skuStockRepository.removeById(id);
            return;
        }
        //获取初始sku信息
        LambdaQueryWrapper<PmsSkuStock>  lambda = new LambdaQueryWrapper<>();
        lambda.eq(PmsSkuStock::getProductId, id);
        List<PmsSkuStock> oriStuList = skuStockRepository.list(lambda);
        //获取新增sku信息
        List<PmsSkuStock> insertSkuList = currSkuList.stream().filter(item->item.getId()==null).collect(Collectors.toList());
        //获取需要更新的sku信息
        List<PmsSkuStock> updateSkuList = currSkuList.stream().filter(item->item.getId()!=null).collect(Collectors.toList());
        List<Long> updateSkuIds = updateSkuList.stream().map(PmsSkuStock::getId).collect(Collectors.toList());
        //获取需要删除的sku信息
        List<PmsSkuStock> removeSkuList = oriStuList.stream().filter(item-> !updateSkuIds.contains(item.getId())).collect(Collectors.toList());
        handleSkuStockCode(insertSkuList,id);
        handleSkuStockCode(updateSkuList,id);
        //新增sku
        if(CollUtil.isNotEmpty(insertSkuList)){
            relateAndInsertList(skuStockRepository, insertSkuList, id);
        }
        //删除sku
        if(CollUtil.isNotEmpty(removeSkuList)){
            List<Long> removeSkuIds = removeSkuList.stream().map(PmsSkuStock::getId).collect(Collectors.toList());
            skuStockRepository.removeByIds(removeSkuIds);
        }
        //修改sku
        if(CollUtil.isNotEmpty(updateSkuList)){
            for (PmsSkuStock pmsSkuStock : updateSkuList) {
                skuStockRepository.updateById(pmsSkuStock);
            }
        }
    }

    @Override
    public Page<PmsProduct> list(PmsProductQueryParam productQueryParam, Integer pageSize, Integer pageNum) {
        Page<PmsProduct> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PmsProduct> lambda = new LambdaQueryWrapper<>();
        lambda.eq(PmsProduct::getDeleteStatus, 0);
        if (productQueryParam.getPublishStatus() != null) {
            lambda.eq(PmsProduct::getPublishStatus, productQueryParam.getPublishStatus());
        }
        if (productQueryParam.getVerifyStatus() != null) {
            lambda.eq(PmsProduct::getVerifyStatus, productQueryParam.getVerifyStatus());
        }
        if (!StringUtils.isEmpty(productQueryParam.getKeyword())) {
            lambda.like(PmsProduct::getName, productQueryParam.getKeyword());
        }
        if (!StringUtils.isEmpty(productQueryParam.getProductSn())) {
            lambda.eq(PmsProduct::getProductSn, productQueryParam.getProductSn());
        }
        if (productQueryParam.getBrandId() != null) {
            lambda.eq(PmsProduct::getBrandId, productQueryParam.getBrandId());
        }
        if (productQueryParam.getProductCategoryId() != null) {
            lambda.eq(PmsProduct::getProductCategoryId, productQueryParam.getProductCategoryId());
        }
        return page(page, lambda);
    }

    @Override
    public int updateVerifyStatus(List<Long> ids, Integer verifyStatus, String detail) {
        List<PmsProductVertifyRecord> list = new ArrayList<>();
        int count = ids.size();

        LambdaUpdateWrapper<PmsProduct> lambdaUpdateProductStatus = new LambdaUpdateWrapper<>();
        lambdaUpdateProductStatus.in(PmsProduct::getId, ids);
        lambdaUpdateProductStatus.set(PmsProduct::getVerifyStatus, verifyStatus);
        update(lambdaUpdateProductStatus);
        //修改完审核状态后插入审核记录
        for (Long id : ids) {
            PmsProductVertifyRecord record = new PmsProductVertifyRecord();
            record.setProductId(id);
            record.setCreateTime(new Date());
            record.setDetail(detail);
            record.setStatus(verifyStatus);
            record.setVertifyMan("test");
            list.add(record);
        }
        pmsProductVertifyRecordRepository.saveBatch(list);
        return count;
    }

    @Override
    public int updatePublishStatus(List<Long> ids, Integer publishStatus) {
        LambdaUpdateWrapper<PmsProduct> lambda = new LambdaUpdateWrapper<>();
        lambda.in(PmsProduct::getId, ids);
        lambda.set(PmsProduct::getPublishStatus, publishStatus);
        update(lambda);
        return ids.size();
    }

    @Override
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        LambdaUpdateWrapper<PmsProduct> lambda = new LambdaUpdateWrapper<>();
        lambda.in(PmsProduct::getId, ids);
        lambda.set(PmsProduct::getRecommandStatus, recommendStatus);
        update(lambda);
        return ids.size();
    }

    @Override
    public int updateNewStatus(List<Long> ids, Integer newStatus) {
        LambdaUpdateWrapper<PmsProduct> lambda = new LambdaUpdateWrapper<>();
        lambda.in(PmsProduct::getId, ids);
        lambda.set(PmsProduct::getNewStatus, newStatus);
        update(lambda);
        return ids.size();
    }

    @Override
    public int updateDeleteStatus(List<Long> ids, Integer deleteStatus) {
        LambdaUpdateWrapper<PmsProduct> lambda = new LambdaUpdateWrapper<>();
        lambda.in(PmsProduct::getId, ids);
        lambda.set(PmsProduct::getDeleteStatus, deleteStatus);
        update(lambda);
        return ids.size();
    }

    @Override
    public List<PmsProduct> list(String keyword) {
        LambdaQueryWrapper<PmsProduct> lambda = new LambdaQueryWrapper<>();
        lambda.eq(PmsProduct::getDeleteStatus, 0);
        if(!StringUtils.isEmpty(keyword)){
            lambda.like(PmsProduct::getName, keyword);
            lambda.or().like(PmsProduct::getProductSn, keyword);
        }
        return list(lambda);
    }

    /**
     * 建立和插入关系表操作
     *
     * @param repository 可以操作的dao
     * @param dataList   要插入的数据
     * @param productId  建立关系的id
     */
    private void relateAndInsertList(Object repository, List dataList, Long productId) {
        try {
            if (CollectionUtils.isEmpty(dataList)) {
                return;
            }
            for (Object item : dataList) {
                Method setId = item.getClass().getMethod("setId", Long.class);
                setId.invoke(item, (Long) null);
                Method setProductId = item.getClass().getMethod("setProductId", Long.class);
                setProductId.invoke(item, productId);
            }
            Method insertList = repository.getClass().getMethod("saveBatch", Collection.class);
            insertList.invoke(repository, dataList);
        } catch (Exception e) {
            LOGGER.warn("创建产品出错:{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public int relateSubjects(Long productId, List<CmsSubjectProductRelation> relations) {
        //先删除原有关系
        QueryWrapper<CmsSubjectProductRelation> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(CmsSubjectProductRelation::getProductId, productId);
        //批量插入新关系
        cmsSubjectProductRelationRepository.remove(wrapper);
        for (CmsSubjectProductRelation relation : relations) {
            relation.setProductId(productId);
        }
        cmsSubjectProductRelationRepository.saveBatch(relations);
        return relations.size();
    }

    @Override
    public int relatePrefrenceAreas(Long productId, List<CmsPrefrenceAreaProductRelation> relations) {
        //先删除原有关系
        QueryWrapper<CmsPrefrenceAreaProductRelation> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(CmsPrefrenceAreaProductRelation::getProductId, productId);
        //批量插入新关系
        cmsPrefrenceAreaProductRelationRepository.remove(wrapper);
        for (CmsPrefrenceAreaProductRelation relation : relations) {
            relation.setProductId(productId);
        }
        cmsPrefrenceAreaProductRelationRepository.saveBatch(relations);
        return relations.size();
    }

}
