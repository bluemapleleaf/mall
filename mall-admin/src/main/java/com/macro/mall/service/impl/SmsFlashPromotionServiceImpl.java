package com.macro.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.macro.mall.service.SmsFlashPromotionService;
import com.macro.mall.sms.model.SmsFlashPromotion;
import com.macro.mall.sms.service.impl.SmsFlashPromotionRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 限时购活动管理Service实现类
 * Created by macro on 2018/11/16.
 */
@Service
public class SmsFlashPromotionServiceImpl extends SmsFlashPromotionRepositoryImpl implements SmsFlashPromotionService {
    @Override
    public boolean create(SmsFlashPromotion flashPromotion) {
        flashPromotion.setCreateTime(new Date());
        return save(flashPromotion);
    }

    @Override
    public boolean update(Long id, SmsFlashPromotion flashPromotion) {
        flashPromotion.setId(id);
        return updateById(flashPromotion);
    }

    @Override
    public boolean delete(Long id) {
        return removeById(id);
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        SmsFlashPromotion flashPromotion = new SmsFlashPromotion();
        flashPromotion.setId(id);
        flashPromotion.setStatus(status);
        return updateById(flashPromotion);
    }

    @Override
    public SmsFlashPromotion getItem(Long id) {
        return getById(id);
    }

    @Override
    public List<SmsFlashPromotion> list(String keyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        LambdaQueryWrapper<SmsFlashPromotion> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(keyword)) {
            lambdaQueryWrapper.like(SmsFlashPromotion::getTitle, keyword);
        }
        return list(lambdaQueryWrapper);
    }
}
