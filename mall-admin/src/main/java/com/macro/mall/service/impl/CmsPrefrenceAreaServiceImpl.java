package com.macro.mall.service.impl;

import com.macro.mall.cms.service.impl.CmsPrefrenceAreaRepositoryImpl;
import com.macro.mall.cms.model.CmsPrefrenceArea;
import com.macro.mall.service.CmsPrefrenceAreaService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品优选Service实现类
 *
 * @author dongjb
 * @date 2020/11/26
 */
@Service
public class CmsPrefrenceAreaServiceImpl extends CmsPrefrenceAreaRepositoryImpl implements CmsPrefrenceAreaService {

    @Override
    public List<CmsPrefrenceArea> listAll() {
        return list();
    }
}
