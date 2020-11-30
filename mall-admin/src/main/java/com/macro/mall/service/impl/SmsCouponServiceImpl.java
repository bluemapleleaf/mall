package com.macro.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.service.SmsCouponService;
import com.macro.mall.sms.dto.SmsCouponParam;
import com.macro.mall.sms.model.SmsCoupon;
import com.macro.mall.sms.model.SmsCouponProductCategoryRelation;
import com.macro.mall.sms.model.SmsCouponProductRelation;
import com.macro.mall.sms.service.SmsCouponProductCategoryRelationRepository;
import com.macro.mall.sms.service.SmsCouponProductRelationRepository;
import com.macro.mall.sms.service.impl.SmsCouponRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


/**
 * 优惠券管理Service实现类
 * Created by macro on 2018/8/28.
 */
@Service
public class SmsCouponServiceImpl extends SmsCouponRepositoryImpl implements SmsCouponService {
    @Autowired
    private SmsCouponProductRelationRepository couponProductRelationRepository;
    @Autowired
    private SmsCouponProductCategoryRelationRepository couponProductCategoryRelationRepository;
//    @Autowired
//    private SmsCouponDao couponDao;
    @Override
    public boolean create(SmsCouponParam couponParam) {
        couponParam.setCount(couponParam.getPublishCount());
        couponParam.setUseCount(0);
        couponParam.setReceiveCount(0);
        //插入优惠券表
        boolean ret = save(couponParam);
        //插入优惠券和商品关系表
        if(couponParam.getUseType().equals(2)){
            for(SmsCouponProductRelation productRelation:couponParam.getProductRelationList()){
                productRelation.setCouponId(couponParam.getId());
            }
            couponProductRelationRepository.saveBatch(couponParam.getProductRelationList());
        }
        //插入优惠券和商品分类关系表
        if(couponParam.getUseType().equals(1)){
            for (SmsCouponProductCategoryRelation couponProductCategoryRelation : couponParam.getProductCategoryRelationList()) {
                couponProductCategoryRelation.setCouponId(couponParam.getId());
            }
            couponProductCategoryRelationRepository.saveBatch(couponParam.getProductCategoryRelationList());
        }
        return ret;
    }

    @Override
    public boolean delete(Long id) {
        //删除优惠券
        boolean ret = removeById(id);
        //删除商品关联
        deleteProductRelation(id);
        //删除商品分类关联
        deleteProductCategoryRelation(id);
        return ret;
    }

    private boolean deleteProductCategoryRelation(Long id) {
        LambdaQueryWrapper<SmsCouponProductCategoryRelation> lambda = new LambdaQueryWrapper<>();
        lambda.eq(SmsCouponProductCategoryRelation::getCouponId, id);
        return couponProductCategoryRelationRepository.remove(lambda);
    }

    private void deleteProductRelation(Long id) {
        LambdaQueryWrapper<SmsCouponProductRelation> lambda = new LambdaQueryWrapper<>();
        lambda.eq(SmsCouponProductRelation::getCouponId, id);
        couponProductRelationRepository.remove(lambda);
    }

    @Override
    public boolean update(Long id, SmsCouponParam couponParam) {
        couponParam.setId(id);
        boolean ret =updateById(couponParam);
        //删除后插入优惠券和商品关系表
        if(couponParam.getUseType().equals(2)){
            for(SmsCouponProductRelation productRelation:couponParam.getProductRelationList()){
                productRelation.setCouponId(couponParam.getId());
            }
            deleteProductRelation(id);
            couponProductRelationRepository.saveBatch(couponParam.getProductRelationList());
        }
        //删除后插入优惠券和商品分类关系表
        if(couponParam.getUseType().equals(1)){
            for (SmsCouponProductCategoryRelation couponProductCategoryRelation : couponParam.getProductCategoryRelationList()) {
                couponProductCategoryRelation.setCouponId(couponParam.getId());
            }
            deleteProductCategoryRelation(id);
            couponProductCategoryRelationRepository.saveBatch(couponParam.getProductCategoryRelationList());
        }
        return ret;
    }

    @Override
    public Page<SmsCoupon> list(String name, Integer type, Integer pageSize, Integer pageNum) {
        LambdaQueryWrapper<SmsCoupon> lambda = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(name)){
            lambda.like(SmsCoupon::getName, name);
        }
        if(type!=null){
            lambda.like(SmsCoupon::getType, type);
        }
        Page<SmsCoupon> page = new Page<>(pageNum, pageSize);
        return page(page, lambda);
    }

    @Override
    public SmsCouponParam getDetail(Long id) {
        return getItem(id);
    }
}
