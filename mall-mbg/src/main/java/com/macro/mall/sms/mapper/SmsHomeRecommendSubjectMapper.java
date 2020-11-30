package com.macro.mall.sms.mapper;

import com.macro.mall.cms.model.CmsSubject;
import com.macro.mall.sms.model.SmsHomeRecommendSubject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 首页推荐专题表 Mapper 接口
 * </p>
 *
 * @author dongjb
 * @since 2020-11-29
 */
public interface SmsHomeRecommendSubjectMapper extends BaseMapper<SmsHomeRecommendSubject> {
    /**
     * 获取推荐专题
     */
    List<CmsSubject> getRecommendSubjectList(@Param("offset") Integer offset, @Param("limit") Integer limit);

}
