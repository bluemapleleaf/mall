package com.macro.mall.sms.service.impl;

import com.macro.mall.sms.dto.FlashPromotionProduct;
import com.macro.mall.sms.dto.SmsFlashPromotionProduct;
import com.macro.mall.sms.model.SmsFlashPromotionProductRelation;
import com.macro.mall.sms.mapper.SmsFlashPromotionProductRelationMapper;
import com.macro.mall.sms.service.SmsFlashPromotionProductRelationRepository;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品限时购与商品关系表 服务实现类
 * </p>
 *
 * @author dongjb
 * @since 2020-11-29
 */
@Service
public class SmsFlashPromotionProductRelationRepositoryImpl extends ServiceImpl<SmsFlashPromotionProductRelationMapper, SmsFlashPromotionProductRelation> implements SmsFlashPromotionProductRelationRepository {
    /**
     * 获取限时购及相关商品信息
     */
    @Autowired
    SmsFlashPromotionProductRelationMapper flashPromotionProductRelationMapper;
    @Override
    public List<SmsFlashPromotionProduct> getList(Long flashPromotionId, Long flashPromotionSessionId){
        return flashPromotionProductRelationMapper.getList(flashPromotionId,flashPromotionSessionId);
    }

    /**
     * 获取秒杀商品
     */
    @Override
    public List<FlashPromotionProduct> getFlashProductList(Long flashPromotionId, Long sessionId){
        return flashPromotionProductRelationMapper.getFlashProductList(flashPromotionId, sessionId);
    }

}
