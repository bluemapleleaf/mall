package com.macro.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.sms.dto.SmsCouponParam;
import com.macro.mall.sms.model.SmsCoupon;
import com.macro.mall.sms.service.SmsCouponRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 优惠券管理Service
 * Created by macro on 2018/8/28.
 */
public interface SmsCouponService extends SmsCouponRepository {
    /**
     * 添加优惠券
     */
    @Transactional
    boolean create(SmsCouponParam couponParam);

    /**
     * 根据优惠券id删除优惠券
     */
    @Transactional
    boolean delete(Long id);

    /**
     * 根据优惠券id更新优惠券信息
     */
    @Transactional
    boolean update(Long id, SmsCouponParam couponParam);

    /**
     * 分页获取优惠券列表
     */
    Page<SmsCoupon> list(String name, Integer type, Integer pageSize, Integer pageNum);

    /**
     * 获取优惠券详情
     * @param id 优惠券表id
     */
    SmsCouponParam getDetail(Long id);
}
