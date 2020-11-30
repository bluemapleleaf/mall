package com.macro.mall.portal.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.macro.mall.common.exception.Asserts;
import com.macro.mall.pms.model.PmsProduct;
import com.macro.mall.pms.service.PmsProductRepository;
import com.macro.mall.portal.domain.CartPromotionItem;
import com.macro.mall.portal.service.UmsMemberCouponService;
import com.macro.mall.portal.service.UmsMemberService;
import com.macro.mall.sms.domain.SmsCouponHistoryDetail;
import com.macro.mall.sms.model.SmsCoupon;
import com.macro.mall.sms.model.SmsCouponHistory;
import com.macro.mall.sms.model.SmsCouponProductCategoryRelation;
import com.macro.mall.sms.model.SmsCouponProductRelation;
import com.macro.mall.sms.service.SmsCouponHistoryRepository;
import com.macro.mall.sms.service.SmsCouponProductCategoryRelationRepository;
import com.macro.mall.sms.service.SmsCouponProductRelationRepository;
import com.macro.mall.sms.service.SmsCouponRepository;
import com.macro.mall.ums.model.UmsMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 会员优惠券管理Service实现类
 * Created by macro on 2018/8/29.
 */
@Service
public class UmsMemberCouponServiceImpl implements UmsMemberCouponService {
    @Autowired
    private UmsMemberService memberService;
    @Autowired
    private SmsCouponRepository couponRepository;
    @Autowired
    private SmsCouponHistoryRepository couponHistoryRepository;
    @Autowired
    private SmsCouponProductRelationRepository couponProductRelationRepository;
    @Autowired
    private SmsCouponProductCategoryRelationRepository couponProductCategoryRelationRepository;
    @Autowired
    private PmsProductRepository productRepository;
    @Override
    public void add(Long couponId) {
        UmsMember currentMember = memberService.getCurrentMember();
        //获取优惠券信息，判断数量
        SmsCoupon coupon = couponRepository.getById(couponId);
        if(coupon==null){
            Asserts.fail("优惠券不存在");
        }
        if(coupon.getCount()<=0){
            Asserts.fail("优惠券已经领完了");
        }
        Date now = new Date();
        if(now.before(coupon.getEnableTime())){
            Asserts.fail("优惠券还没到领取时间");
        }
        //判断用户领取的优惠券数量是否超过限制
        LambdaQueryWrapper<SmsCouponHistory> lambda = new LambdaQueryWrapper<>();
        lambda.eq(SmsCouponHistory::getCouponId, couponId)
                .eq(SmsCouponHistory::getMemberId,currentMember.getId());
        long count = couponHistoryRepository.count();
        if(count>=coupon.getPerLimit()){
            Asserts.fail("您已经领取过该优惠券");
        }
        //生成领取优惠券历史
        SmsCouponHistory couponHistory = new SmsCouponHistory();
        couponHistory.setCouponId(couponId);
        couponHistory.setCouponCode(generateCouponCode(currentMember.getId()));
        couponHistory.setCreateTime(now);
        couponHistory.setMemberId(currentMember.getId());
        couponHistory.setMemberNickname(currentMember.getNickname());
        //主动领取
        couponHistory.setGetType(1);
        //未使用
        couponHistory.setUseStatus(0);
        couponHistoryRepository.save(couponHistory);
        //修改优惠券表的数量、领取数量
        coupon.setCount(coupon.getCount()-1);
        coupon.setReceiveCount(coupon.getReceiveCount()==null?1:coupon.getReceiveCount()+1);
        couponRepository.updateById(coupon);
    }

    /**
     * 16位优惠码生成：时间戳后8位+4位随机数+用户id后4位
     */
    private String generateCouponCode(Long memberId) {
        StringBuilder sb = new StringBuilder();
        Long currentTimeMillis = System.currentTimeMillis();
        String timeMillisStr = currentTimeMillis.toString();
        sb.append(timeMillisStr.substring(timeMillisStr.length() - 8));
        for (int i = 0; i < 4; i++) {
            sb.append(new Random().nextInt(10));
        }
        String memberIdStr = memberId.toString();
        if (memberIdStr.length() <= 4) {
            sb.append(String.format("%04d", memberId));
        } else {
            sb.append(memberIdStr.substring(memberIdStr.length()-4));
        }
        return sb.toString();
    }

    @Override
    public List<SmsCouponHistory> listHistory(Integer useStatus) {
        UmsMember currentMember = memberService.getCurrentMember();
        LambdaQueryWrapper<SmsCouponHistory> lambda = new LambdaQueryWrapper<>();
        lambda.eq(SmsCouponHistory::getMemberId, currentMember.getId());
        if(useStatus!=null){
            lambda.eq(SmsCouponHistory::getUseStatus, useStatus);
        }
        return couponHistoryRepository.list(lambda);
    }

    @Override
    public List<SmsCouponHistoryDetail> listCart(List<CartPromotionItem> cartItemList, Integer type) {
        UmsMember currentMember = memberService.getCurrentMember();
        Date now = new Date();
        //获取该用户所有优惠券
        List<SmsCouponHistoryDetail> allList = couponHistoryRepository.getDetailList(currentMember.getId());
        //根据优惠券使用类型来判断优惠券是否可用
        List<SmsCouponHistoryDetail> enableList = new ArrayList<>();
        List<SmsCouponHistoryDetail> disableList = new ArrayList<>();
        for (SmsCouponHistoryDetail couponHistoryDetail : allList) {
            Integer useType = couponHistoryDetail.getCoupon().getUseType();
            BigDecimal minPoint = couponHistoryDetail.getCoupon().getMinPoint();
            Date endTime = couponHistoryDetail.getCoupon().getEndTime();
            if(useType.equals(0)){
                //0->全场通用
                //判断是否满足优惠起点
                //计算购物车商品的总价
                BigDecimal totalAmount = calcTotalAmount(cartItemList);
                if(now.before(endTime)&&totalAmount.subtract(minPoint).intValue()>=0){
                    enableList.add(couponHistoryDetail);
                }else{
                    disableList.add(couponHistoryDetail);
                }
            }else if(useType.equals(1)){
                //1->指定分类
                //计算指定分类商品的总价
                List<Long> productCategoryIds = new ArrayList<>();
                for (SmsCouponProductCategoryRelation categoryRelation : couponHistoryDetail.getCategoryRelationList()) {
                    productCategoryIds.add(categoryRelation.getProductCategoryId());
                }
                BigDecimal totalAmount = calcTotalAmountByproductCategoryId(cartItemList,productCategoryIds);
                if(now.before(endTime)&&totalAmount.intValue()>0&&totalAmount.subtract(minPoint).intValue()>=0){
                    enableList.add(couponHistoryDetail);
                }else{
                    disableList.add(couponHistoryDetail);
                }
            }else if(useType.equals(2)){
                //2->指定商品
                //计算指定商品的总价
                List<Long> productIds = new ArrayList<>();
                for (SmsCouponProductRelation productRelation : couponHistoryDetail.getProductRelationList()) {
                    productIds.add(productRelation.getProductId());
                }
                BigDecimal totalAmount = calcTotalAmountByProductId(cartItemList,productIds);
                if(now.before(endTime)&&totalAmount.intValue()>0&&totalAmount.subtract(minPoint).intValue()>=0){
                    enableList.add(couponHistoryDetail);
                }else{
                    disableList.add(couponHistoryDetail);
                }
            }
        }
        if(type.equals(1)){
            return enableList;
        }else{
            return disableList;
        }
    }

    @Override
    public List<SmsCoupon> listByProduct(Long productId) {
        List<Long> allCouponIds = new ArrayList<>();
        //获取指定商品优惠券
        LambdaQueryWrapper<SmsCouponProductRelation> lambda = new LambdaQueryWrapper<>();
        lambda.eq(SmsCouponProductRelation::getProductId, productId);
        List<SmsCouponProductRelation> cprList = couponProductRelationRepository.list(lambda);
        if(CollUtil.isNotEmpty(cprList)){
            List<Long> couponIds = cprList.stream().map(SmsCouponProductRelation::getCouponId).collect(Collectors.toList());
            allCouponIds.addAll(couponIds);
        }
        //获取指定分类优惠券
        PmsProduct product =  productRepository.getById(productId);
        LambdaQueryWrapper<SmsCouponProductCategoryRelation> lambdaPcr = new LambdaQueryWrapper<>();
        lambdaPcr.eq(SmsCouponProductCategoryRelation::getProductCategoryId, product.getProductCategoryId());
        List<SmsCouponProductCategoryRelation> cpcrList = couponProductCategoryRelationRepository.list(lambdaPcr);
        if(CollUtil.isNotEmpty(cpcrList)){
            List<Long> couponIds = cpcrList.stream().map(SmsCouponProductCategoryRelation::getCouponId).collect(Collectors.toList());
            allCouponIds.addAll(couponIds);
        }
        if(CollUtil.isEmpty(allCouponIds)){
            return new ArrayList<>();
        }
        //所有优惠券
        LambdaQueryWrapper<SmsCoupon> lambdaCoupon = new LambdaQueryWrapper<>();
        lambdaCoupon.gt(SmsCoupon::getEndTime, new Date())
                .lt(SmsCoupon::getStartTime, new Date())
                .eq(SmsCoupon::getUseType, 0);
        lambdaCoupon.or().gt(SmsCoupon::getEndTime, new Date())
                .lt(SmsCoupon::getStartTime, new Date())
                .eq(SmsCoupon::getUseType, 0)
                .in(SmsCoupon::getId, allCouponIds);
        return couponRepository.list(lambdaCoupon);
    }

    @Override
    public List<SmsCoupon> list(Integer useStatus) {
        UmsMember member = memberService.getCurrentMember();
        return couponHistoryRepository.getCouponList(member.getId(),useStatus);
    }

    private BigDecimal calcTotalAmount(List<CartPromotionItem> cartItemList) {
        BigDecimal total = new BigDecimal("0");
        for (CartPromotionItem item : cartItemList) {
            BigDecimal realPrice = item.getPrice().subtract(item.getReduceAmount());
            total=total.add(realPrice.multiply(new BigDecimal(item.getQuantity())));
        }
        return total;
    }

    private BigDecimal calcTotalAmountByproductCategoryId(List<CartPromotionItem> cartItemList,List<Long> productCategoryIds) {
        BigDecimal total = new BigDecimal("0");
        for (CartPromotionItem item : cartItemList) {
            if(productCategoryIds.contains(item.getProductCategoryId())){
                BigDecimal realPrice = item.getPrice().subtract(item.getReduceAmount());
                total=total.add(realPrice.multiply(new BigDecimal(item.getQuantity())));
            }
        }
        return total;
    }

    private BigDecimal calcTotalAmountByProductId(List<CartPromotionItem> cartItemList,List<Long> productIds) {
        BigDecimal total = new BigDecimal("0");
        for (CartPromotionItem item : cartItemList) {
            if(productIds.contains(item.getProductId())){
                BigDecimal realPrice = item.getPrice().subtract(item.getReduceAmount());
                total=total.add(realPrice.multiply(new BigDecimal(item.getQuantity())));
            }
        }
        return total;
    }

}
