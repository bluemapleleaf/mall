package com.macro.mall.service;

import com.macro.mall.ums.model.UmsResourceCategory;
import com.macro.mall.ums.service.UmsResourceCategoryRepository;

import java.util.List;

/**
 * 后台资源分类管理Service
 *
 * @author dongjb
 * @date 2020/11/21
 */
public interface UmsResourceCategoryService extends UmsResourceCategoryRepository {

    /**
     * 获取所有资源分类
     * @return 资源分类列表
     */
    List<UmsResourceCategory> listAll();

    /**
     * 创建资源分类
     * @param umsResourceCategory 资源分类对象
     * @return 是否成功
     */
    boolean create(UmsResourceCategory umsResourceCategory);
}

