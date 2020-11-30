package com.macro.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.macro.mall.dto.SmsFlashPromotionSessionDetail;
import com.macro.mall.service.SmsFlashPromotionProductRelationService;
import com.macro.mall.service.SmsFlashPromotionSessionService;
import com.macro.mall.sms.model.SmsFlashPromotionSession;
import com.macro.mall.sms.service.impl.SmsFlashPromotionSessionRepositoryImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 限时购场次管理Service实现类
 * Created by macro on 2018/11/16.
 */
@Service
public class SmsFlashPromotionSessionServiceImpl extends SmsFlashPromotionSessionRepositoryImpl implements SmsFlashPromotionSessionService {
    @Autowired
    private SmsFlashPromotionProductRelationService relationService;

    @Override
    public boolean create(SmsFlashPromotionSession promotionSession) {
        promotionSession.setCreateTime(new Date());
        return save(promotionSession);
    }

    @Override
    public boolean update(Long id, SmsFlashPromotionSession promotionSession) {
        promotionSession.setId(id);
        return updateById(promotionSession);
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        SmsFlashPromotionSession promotionSession = new SmsFlashPromotionSession();
        promotionSession.setId(id);
        promotionSession.setStatus(status);
        return updateById(promotionSession);
    }

    @Override
    public boolean delete(Long id) {
        return removeById(id);
    }

    @Override
    public SmsFlashPromotionSession getItem(Long id) {
        return getById(id);
    }

    @Override
    public List<SmsFlashPromotionSession> list() {
        return list();
    }

    @Override
    public List<SmsFlashPromotionSessionDetail> selectList(Long flashPromotionId) {
        List<SmsFlashPromotionSessionDetail> result = new ArrayList<>();
        LambdaQueryWrapper<SmsFlashPromotionSession> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SmsFlashPromotionSession::getStatus, 1);
        List<SmsFlashPromotionSession> list = list(lambdaQueryWrapper);
        for (SmsFlashPromotionSession promotionSession : list) {
            SmsFlashPromotionSessionDetail detail = new SmsFlashPromotionSessionDetail();
            BeanUtils.copyProperties(promotionSession, detail);
            long count = relationService.getCount(flashPromotionId, promotionSession.getId());
            detail.setProductCount(count);
            result.add(detail);
        }
        return result;
    }
}
