package com.macro.mall.service;

import com.macro.mall.ums.model.UmsMemberLevel;
import com.macro.mall.ums.service.UmsMemberLevelRepository;

import java.util.List;

/**
 * 会员等级管理Service
 * Created by macro on 2018/4/26.
 */
public interface UmsMemberLevelService extends UmsMemberLevelRepository {
    /**
     * 获取所有会员登录
     * @param defaultStatus 是否为默认会员
     */
    List<UmsMemberLevel> getlist(Integer defaultStatus);
}
