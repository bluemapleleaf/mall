package com.macro.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.service.SmsFlashPromotionProductRelationService;
import com.macro.mall.sms.dto.SmsFlashPromotionProduct;
import com.macro.mall.sms.model.SmsFlashPromotionProductRelation;
import com.macro.mall.sms.service.impl.SmsFlashPromotionProductRelationRepositoryImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 限时购商品关联管理Service实现类
 * Created by macro on 2018/11/16.
 */
@Service
public class SmsFlashPromotionProductRelationServiceImpl extends SmsFlashPromotionProductRelationRepositoryImpl implements SmsFlashPromotionProductRelationService {
    @Override
    public int create(List<SmsFlashPromotionProductRelation> relationList) {
        for (SmsFlashPromotionProductRelation relation : relationList) {
            save(relation);
        }
        return relationList.size();
    }

    @Override
    public boolean update(Long id, SmsFlashPromotionProductRelation relation) {
        relation.setId(id);
        return updateById(relation);
    }

    @Override
    public boolean delete(Long id) {
        return removeById(id);
    }

    @Override
    public SmsFlashPromotionProductRelation getItem(Long id) {
        return getById(id);
    }

    @Override
    public Page<SmsFlashPromotionProduct> list(Long flashPromotionId, Long flashPromotionSessionId, Integer pageSize, Integer pageNum) {
        Page<SmsFlashPromotionProduct> page = new Page<>(pageNum,pageSize);
        return getList(page, flashPromotionId,flashPromotionSessionId);
    }

    @Override
    public long getCount(Long flashPromotionId, Long flashPromotionSessionId) {
        LambdaQueryWrapper<SmsFlashPromotionProductRelation> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SmsFlashPromotionProductRelation::getFlashPromotionId,flashPromotionId)
                .eq(SmsFlashPromotionProductRelation::getFlashPromotionSessionId, flashPromotionSessionId);
        return count(lambdaQueryWrapper);
    }
}
