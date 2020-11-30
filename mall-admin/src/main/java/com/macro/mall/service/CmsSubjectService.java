package com.macro.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.cms.service.CmsSubjectRepository;
import com.macro.mall.cms.model.CmsSubject;

import java.util.List;

/**
 * 商品专题Service
 * Created by macro on 2018/6/1.
 */
public interface CmsSubjectService extends CmsSubjectRepository {
    /**
     * 查询所有专题
     */
    List<CmsSubject> listAll();

    /**
     * 分页查询专题
     */
    Page<CmsSubject> list(String keyword, Integer pageNum, Integer pageSize);
}
