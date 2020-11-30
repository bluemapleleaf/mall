package com.macro.mall.service.impl;

import com.macro.mall.service.UmsMemberLevelService;
import com.macro.mall.ums.model.UmsMemberLevel;
import com.macro.mall.ums.service.impl.UmsMemberLevelRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 会员等级管理Service实现类
 * Created by macro on 2018/4/26.
 */
@Service
public class UmsMemberLevelServiceImpl extends UmsMemberLevelRepositoryImpl implements UmsMemberLevelService{
    @Override
    public List<UmsMemberLevel> getlist(Integer defaultStatus) {
        return list();
    }
}
