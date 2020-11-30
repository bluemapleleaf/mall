package com.macro.mall.sms.service.impl;

import com.macro.mall.cms.model.CmsSubject;
import com.macro.mall.sms.model.SmsHomeRecommendSubject;
import com.macro.mall.sms.mapper.SmsHomeRecommendSubjectMapper;
import com.macro.mall.sms.service.SmsHomeRecommendSubjectRepository;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 首页推荐专题表 服务实现类
 * </p>
 *
 * @author dongjb
 * @since 2020-11-29
 */
@Service
public class SmsHomeRecommendSubjectRepositoryImpl extends ServiceImpl<SmsHomeRecommendSubjectMapper, SmsHomeRecommendSubject> implements SmsHomeRecommendSubjectRepository {
    /**
     * 获取推荐专题
     */
    @Autowired
    SmsHomeRecommendSubjectMapper homeRecommendSubjectMapper;
    @Override
    public List<CmsSubject> getRecommendSubjectList(Integer offset, Integer limit){
        return homeRecommendSubjectMapper.getRecommendSubjectList(offset,limit);
    }

}
