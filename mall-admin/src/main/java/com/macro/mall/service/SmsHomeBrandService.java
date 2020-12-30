package com.macro.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.sms.model.SmsHomeBrand;
import com.macro.mall.sms.service.SmsHomeBrandRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 首页品牌管理Service
 * Created by macro on 2018/11/6.
 */
public interface SmsHomeBrandService extends SmsHomeBrandRepository {
    /**
     * 添加首页品牌推荐
     */
    @Transactional
    boolean create(List<SmsHomeBrand> homeBrandList);

    /**
     * 修改品牌推荐排序
     */
    boolean updateSort(Long id, Integer sort);

    /**
     * 批量删除品牌推荐
     */
    boolean delete(List<Long> ids);

    /**
     * 更新推荐状态
     */
    boolean updateRecommendStatus(List<Long> ids, Integer recommendStatus);

    /**
     * 分页查询品牌推荐
     */
    Page<SmsHomeBrand> list(String brandName, Integer recommendStatus, Integer pageSize, Integer pageNum);
}
