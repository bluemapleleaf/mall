package com.macro.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.cms.service.impl.CmsSubjectRepositoryImpl;
import com.macro.mall.cms.model.CmsSubject;
import com.macro.mall.service.CmsSubjectService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 商品专题Service实现类
 *
 * @author dongjb
 * @date 2020/11/26
 */
@Service
public class CmsSubjectServiceImpl extends CmsSubjectRepositoryImpl implements CmsSubjectService {
    @Override
    public List<CmsSubject> listAll() {
        return list();
    }

    @Override
    public Page<CmsSubject> list(String keyword, Integer pageNum, Integer pageSize) {
        Page<CmsSubject> page = new Page<>(pageNum, pageSize);
        QueryWrapper<CmsSubject> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<CmsSubject> lambda = wrapper.lambda();
        if(!StringUtils.isEmpty(keyword)) {
            lambda.like(CmsSubject::getTitle, keyword);
        }
        return page(page, wrapper);
    }

}
