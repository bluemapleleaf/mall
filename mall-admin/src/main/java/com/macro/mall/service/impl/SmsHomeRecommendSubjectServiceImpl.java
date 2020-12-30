package com.macro.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.macro.mall.service.SmsHomeRecommendSubjectService;
import com.macro.mall.sms.model.SmsHomeRecommendSubject;
import com.macro.mall.sms.service.impl.SmsHomeRecommendSubjectRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 首页专题推荐管理Service实现类
 * Created by macro on 2018/11/7.
 */
@Service
public class SmsHomeRecommendSubjectServiceImpl extends SmsHomeRecommendSubjectRepositoryImpl implements SmsHomeRecommendSubjectService {
    @Override
    public boolean create(List<SmsHomeRecommendSubject> recommendSubjectList) {
        for (SmsHomeRecommendSubject recommendProduct : recommendSubjectList) {
            recommendProduct.setRecommendStatus(1);
            recommendProduct.setSort(0);
        }
        return saveBatch(recommendSubjectList);
    }

    @Override
    public boolean updateSort(Long id, Integer sort) {
        SmsHomeRecommendSubject recommendProduct = new SmsHomeRecommendSubject();
        recommendProduct.setId(id);
        recommendProduct.setSort(sort);
        return updateById(recommendProduct);
    }

    @Override
    public boolean delete(List<Long> ids) {
        return removeByIds(ids);
    }

    @Override
    public boolean updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        LambdaUpdateWrapper<SmsHomeRecommendSubject> lambda = new LambdaUpdateWrapper<>();
        lambda.in(SmsHomeRecommendSubject::getId, ids);
        lambda.set(SmsHomeRecommendSubject::getRecommendStatus, recommendStatus);
        return update(lambda);
    }

    @Override
    public Page<SmsHomeRecommendSubject> list(String subjectName, Integer recommendStatus, Integer pageSize, Integer pageNum) {
        Page<SmsHomeRecommendSubject> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SmsHomeRecommendSubject> lambda = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(subjectName)){
            lambda.like(SmsHomeRecommendSubject::getSubjectName, subjectName);
        }
        if(recommendStatus!=null){
            lambda.eq(SmsHomeRecommendSubject::getRecommendStatus, recommendStatus);
        }
        lambda.orderByDesc(SmsHomeRecommendSubject::getSort);
        return page(page, lambda);
    }
}
