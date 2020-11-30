package com.macro.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.sms.model.SmsHomeNewProduct;
import com.macro.mall.sms.service.SmsHomeNewProductRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 首页新品管理Service
 * Created by macro on 2018/11/6.
 */
public interface SmsHomeNewProductService extends SmsHomeNewProductRepository {
    /**
     * 添加首页推荐
     */
    @Transactional
    boolean create(List<SmsHomeNewProduct> homeNewProductList);

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
    Page<SmsHomeNewProduct> list(String productName, Integer recommendStatus, Integer pageSize, Integer pageNum);
}
