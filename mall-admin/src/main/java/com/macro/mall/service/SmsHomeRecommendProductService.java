package com.macro.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.sms.model.SmsHomeRecommendProduct;
import com.macro.mall.sms.service.SmsHomeRecommendProductRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 首页人气推荐管理Service
 * Created by macro on 2018/11/7.
 */
public interface SmsHomeRecommendProductService extends SmsHomeRecommendProductRepository {
    /**
     * 添加首页推荐
     */
    @Transactional
    boolean create(List<SmsHomeRecommendProduct> homeRecommendProductList);

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
    Page<SmsHomeRecommendProduct> list(String productName, Integer recommendStatus, Integer pageSize, Integer pageNum);
}
