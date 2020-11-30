package com.macro.mall.sms.service.impl;

import com.macro.mall.sms.model.SmsCouponProductRelation;
import com.macro.mall.sms.mapper.SmsCouponProductRelationMapper;
import com.macro.mall.sms.service.SmsCouponProductRelationRepository;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 优惠券和产品的关系表 服务实现类
 * </p>
 *
 * @author dongjb
 * @since 2020-11-29
 */
@Service
public class SmsCouponProductRelationRepositoryImpl extends ServiceImpl<SmsCouponProductRelationMapper, SmsCouponProductRelation> implements SmsCouponProductRelationRepository {

}
