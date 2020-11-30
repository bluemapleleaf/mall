package com.macro.mall.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.cms.model.CmsSubject;
import com.macro.mall.cms.service.impl.CmsSubjectRepositoryImpl;
import com.macro.mall.pms.model.PmsProduct;
import com.macro.mall.pms.model.PmsProductCategory;
import com.macro.mall.pms.service.PmsBrandRepository;
import com.macro.mall.pms.service.PmsProductCategoryRepository;
import com.macro.mall.pms.service.PmsProductRepository;
import com.macro.mall.portal.domain.HomeContentResult;
import com.macro.mall.portal.domain.HomeFlashPromotion;
import com.macro.mall.portal.service.HomeService;
import com.macro.mall.portal.util.DateUtil;
import com.macro.mall.sms.dto.FlashPromotionProduct;
import com.macro.mall.sms.model.SmsFlashPromotion;
import com.macro.mall.sms.model.SmsFlashPromotionSession;
import com.macro.mall.sms.model.SmsHomeAdvertise;
import com.macro.mall.sms.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 首页内容管理Service实现类
 * Created by macro on 2019/1/28.
 */
@Service
public class HomeServiceImpl extends CmsSubjectRepositoryImpl implements HomeService {
    @Qualifier("pmsBrandRepositoryImpl")
    @Autowired
    private PmsBrandRepository pmsBrandRepository;
    @Autowired
    private SmsFlashPromotionRepository flashPromotionRepository;
    @Autowired
    private SmsFlashPromotionSessionRepository flashPromotionSessionRepository;
    @Autowired
    private PmsProductRepository productRepository;
    @Autowired
    private PmsProductCategoryRepository productCategoryRepository;
    @Autowired
    private SmsHomeAdvertiseRepository homeAdvertiseRepository;
    @Autowired
    private SmsFlashPromotionProductRelationRepository flashPromotionProductRelationRepository;
    @Autowired
    private SmsHomeNewProductRepository homeNewProductRepository;
    @Autowired
    private SmsHomeRecommendProductRepository homeRecommendProductRepository;
    @Autowired
    private SmsHomeRecommendSubjectRepository homeRecommendSubjectRepository;
    @Override
    public HomeContentResult content() {
        HomeContentResult result = new HomeContentResult();
        //获取首页广告
        result.setAdvertiseList(getHomeAdvertiseList());
        //获取推荐品牌
        result.setBrandList(pmsBrandRepository.getRecommendBrandList(0,6));
        //获取秒杀信息
        result.setHomeFlashPromotion(getHomeFlashPromotion());
        //获取新品推荐
        result.setNewProductList(homeNewProductRepository.getNewProductList(0,4));
        //获取人气推荐
        result.setHotProductList(homeRecommendProductRepository.getHotProductList(0,4));
        //获取推荐专题
        result.setSubjectList(homeRecommendSubjectRepository.getRecommendSubjectList(0,4));
        return result;
    }

    @Override
    public Page<PmsProduct> recommendProductList(Integer pageSize, Integer pageNum) {
        // TODO: 2019/1/29 暂时默认推荐所有商品
        Page<PmsProduct> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PmsProduct> lambda = new LambdaQueryWrapper<>();
        lambda.eq(PmsProduct::getDeleteStatus, 0);
        lambda.eq(PmsProduct::getPublishStatus, 1);
        return productRepository.page(page, lambda);
    }

    @Override
    public List<PmsProductCategory> getProductCateList(Long parentId) {
        LambdaQueryWrapper<PmsProductCategory> lambda = new LambdaQueryWrapper<>();
        lambda.eq(PmsProductCategory::getShowStatus, 1);
        lambda.eq(PmsProductCategory::getParentId, parentId);
        lambda.orderByDesc(PmsProductCategory::getSort);
        return productCategoryRepository.list(lambda);
    }

    @Override
    public Page<CmsSubject> getSubjectList(Long cateId, Integer pageSize, Integer pageNum) {

        Page<CmsSubject> page = new Page<>(pageNum, pageSize);
        QueryWrapper<CmsSubject> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<CmsSubject> lambda = wrapper.lambda();
        if(cateId != null){
            lambda.eq(CmsSubject::getCategoryId, cateId);
        }
        return page(page, lambda);
    }

    @Override
    public List<PmsProduct> hotProductList(Integer pageNum, Integer pageSize) {
        int offset = pageSize * (pageNum - 1);
        return homeRecommendProductRepository.getHotProductList(offset, pageSize);
    }

    @Override
    public List<PmsProduct> newProductList(Integer pageNum, Integer pageSize) {
        int offset = pageSize * (pageNum - 1);
        return homeNewProductRepository.getNewProductList(offset, pageSize);
    }

    private HomeFlashPromotion getHomeFlashPromotion() {
        HomeFlashPromotion homeFlashPromotion = new HomeFlashPromotion();
        //获取当前秒杀活动
        Date now = new Date();
        SmsFlashPromotion flashPromotion = getFlashPromotion(now);
        if (flashPromotion != null) {
            //获取当前秒杀场次
            SmsFlashPromotionSession flashPromotionSession = getFlashPromotionSession(now);
            if (flashPromotionSession != null) {
                homeFlashPromotion.setStartTime(flashPromotionSession.getStartTime());
                homeFlashPromotion.setEndTime(flashPromotionSession.getEndTime());
                //获取下一个秒杀场次
                SmsFlashPromotionSession nextSession = getNextFlashPromotionSession(homeFlashPromotion.getStartTime());
                if(nextSession!=null){
                    homeFlashPromotion.setNextStartTime(nextSession.getStartTime());
                    homeFlashPromotion.setNextEndTime(nextSession.getEndTime());
                }
                //获取秒杀商品
                List<FlashPromotionProduct> flashProductList = flashPromotionProductRelationRepository.getFlashProductList(flashPromotion.getId(), flashPromotionSession.getId());
                homeFlashPromotion.setProductList(flashProductList);
            }
        }
        return homeFlashPromotion;
    }

    //获取下一个场次信息
    private SmsFlashPromotionSession getNextFlashPromotionSession(Date date) {
        LambdaQueryWrapper<SmsFlashPromotionSession> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.gt(SmsFlashPromotionSession::getStartTime,date);
        lambdaQueryWrapper.orderByAsc(SmsFlashPromotionSession::getStartTime);
        return flashPromotionSessionRepository.getOne(lambdaQueryWrapper);
    }

    private List<SmsHomeAdvertise> getHomeAdvertiseList() {
        LambdaQueryWrapper<SmsHomeAdvertise> lambda = new LambdaQueryWrapper<>();
        lambda.eq(SmsHomeAdvertise::getType, 1).
                eq(SmsHomeAdvertise::getStatus, 1)
                .orderByDesc(SmsHomeAdvertise::getSort);
        return homeAdvertiseRepository.list(lambda);
    }

    //根据时间获取秒杀活动
    private SmsFlashPromotion getFlashPromotion(Date date) {
        Date currDate = DateUtil.getDate(date);
        LambdaQueryWrapper<SmsFlashPromotion> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SmsFlashPromotion::getStatus, 1)
                .lt(SmsFlashPromotion::getStartDate, currDate)
                .gt(SmsFlashPromotion::getEndDate, currDate);
        return flashPromotionRepository.getOne(lambdaQueryWrapper);
    }

    //根据时间获取秒杀场次
    private SmsFlashPromotionSession getFlashPromotionSession(Date date) {
        Date currTime = DateUtil.getTime(date);
        LambdaQueryWrapper<SmsFlashPromotionSession> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.lt(SmsFlashPromotionSession::getStartTime, currTime)
                .gt(SmsFlashPromotionSession::getEndTime, currTime);
        return flashPromotionSessionRepository.getOne(lambdaQueryWrapper);
    }
}
