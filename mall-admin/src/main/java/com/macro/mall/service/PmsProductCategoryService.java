package com.macro.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.domain.PmsProductCategoryParam;
import com.macro.mall.pms.model.PmsProductCategory;
import com.macro.mall.pms.service.PmsProductCategoryRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品分类Service
 * Created by macro on 2018/4/26.
 */
public interface PmsProductCategoryService extends PmsProductCategoryRepository {
    /**
     * 创建商品分类
     */
    @Transactional
    boolean create(PmsProductCategoryParam pmsProductCategoryParam);

    /**
     * 修改商品分类
     */
    @Transactional
    boolean update(Long id, PmsProductCategoryParam pmsProductCategoryParam);

    /**
     * 分页获取商品分类
     */
    Page<PmsProductCategory> getList(Long parentId, Integer pageSize, Integer pageNum);

    /**
     * 删除商品分类
     */
    boolean delete(Long id);

    /**
     * 根据ID获取商品分类
     */
    PmsProductCategory getItem(Long id);

    /**
     * 批量修改导航状态
     */
    boolean updateNavStatus(List<Long> ids, Integer navStatus);

    /**
     * 批量修改显示状态
     */
    boolean updateShowStatus(List<Long> ids, Integer showStatus);

}
