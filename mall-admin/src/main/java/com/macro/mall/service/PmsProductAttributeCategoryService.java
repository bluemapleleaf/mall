package com.macro.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.pms.model.PmsProductAttributeCategory;
import com.macro.mall.pms.service.PmsProductAttributeCategoryRepository;

import java.util.List;

/**
 * 商品属性分类Service
 * Created by macro on 2018/4/26.
 */
public interface PmsProductAttributeCategoryService extends PmsProductAttributeCategoryRepository {
    /**
     * 创建属性分类
     */
    boolean create(String name);

    /**
     * 修改属性分类
     */
    boolean  update(Long id, String name);

    /**
     * 删除属性分类
     */
    boolean delete(Long id);

    /**
     * 获取属性分类详情
     */
    PmsProductAttributeCategory getItem(Long id);

    /**
     * 分页查询属性分类
     */
    Page<PmsProductAttributeCategory> getList(Integer pageSize, Integer pageNum);

}
