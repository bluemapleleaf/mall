package com.macro.mall.service;

import com.macro.mall.cms.service.CmsPrefrenceAreaRepository;
import com.macro.mall.cms.model.CmsPrefrenceArea;

import java.util.List;

/**
 * 优选专区Service
 *
 * @author dongjb
 * @date 2020/11/26
 */
public interface CmsPrefrenceAreaService extends CmsPrefrenceAreaRepository {
    /**
     * 获取所有优选专区
     * @return 优选专区列表
     */
    List<CmsPrefrenceArea> listAll();
}
