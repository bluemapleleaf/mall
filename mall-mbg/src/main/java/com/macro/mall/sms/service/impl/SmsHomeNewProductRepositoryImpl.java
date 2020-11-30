package com.macro.mall.sms.service.impl;

import com.macro.mall.pms.model.PmsProduct;
import com.macro.mall.sms.model.SmsHomeNewProduct;
import com.macro.mall.sms.mapper.SmsHomeNewProductMapper;
import com.macro.mall.sms.service.SmsHomeNewProductRepository;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 新鲜好物表 服务实现类
 * </p>
 *
 * @author dongjb
 * @since 2020-11-29
 */
@Service
public class SmsHomeNewProductRepositoryImpl extends ServiceImpl<SmsHomeNewProductMapper, SmsHomeNewProduct> implements SmsHomeNewProductRepository {
    /**
     * 获取新品推荐
     */
    @Autowired
    SmsHomeNewProductMapper homeNewProductMapper;
    @Override
    public List<PmsProduct> getNewProductList(Integer offset, Integer limit){
        return homeNewProductMapper.getNewProductList(offset, limit);
    }

}
