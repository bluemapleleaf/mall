package com.macro.mall.service;

import com.macro.mall.sms.model.SmsHomeRecommendSubject;
import com.macro.mall.sms.service.SmsHomeRecommendSubjectRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 首页专题推荐管理Service
 * Created by macro on 2018/11/7.
 */
public interface SmsHomeRecommendSubjectService extends SmsHomeRecommendSubjectRepository {
    /**
     * 添加首页推荐
     */
    @Transactional
    boolean create(List<SmsHomeRecommendSubject> recommendSubjectList);

    /**
     * 修改推荐排序
     */
    boolean updateSort(Long id, Integer sort);

    /**
     * 批量删除推荐
     */
    boolean delete(List<Long> ids);

    /**
     * 更新推荐状态
     */
    boolean updateRecommendStatus(List<Long> ids, Integer recommendStatus);

    /**
     * 分页查询推荐
     */
    List<SmsHomeRecommendSubject> list(String subjectName, Integer recommendStatus, Integer pageSize, Integer pageNum);
}
