package com.macro.mall.sms.service;

import com.macro.mall.cms.model.CmsSubject;
import com.macro.mall.sms.model.SmsHomeRecommendSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 首页推荐专题表 服务类
 * </p>
 *
 * @author dongjb
 * @since 2020-11-29
 */
public interface SmsHomeRecommendSubjectRepository extends IService<SmsHomeRecommendSubject> {
    /**
     * 获取推荐专题
     */
    List<CmsSubject> getRecommendSubjectList(Integer offset, Integer limit);

}
