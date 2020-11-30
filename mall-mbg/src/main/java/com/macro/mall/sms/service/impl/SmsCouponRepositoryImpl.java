package com.macro.mall.sms.service.impl;

import com.macro.mall.sms.dto.SmsCouponParam;
import com.macro.mall.sms.model.SmsCoupon;
import com.macro.mall.sms.mapper.SmsCouponMapper;
import com.macro.mall.sms.service.SmsCouponRepository;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 优惠券表 服务实现类
 * </p>
 *
 * @author dongjb
 * @since 2020-11-29
 */
@Service
public class SmsCouponRepositoryImpl extends ServiceImpl<SmsCouponMapper, SmsCoupon> implements SmsCouponRepository {
    /**
     * 获取优惠券详情包括绑定关系
     */
    @Autowired
    private SmsCouponMapper couponMapper;
    @Override
    public SmsCouponParam getItem(Long id){
        return couponMapper.getItem(id);
    }
}
