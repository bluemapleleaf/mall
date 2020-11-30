package com.macro.mall.pms.service.impl;

import com.macro.mall.pms.dto.ProductAttrInfo;
import com.macro.mall.pms.model.PmsProductAttribute;
import com.macro.mall.pms.mapper.PmsProductAttributeMapper;
import com.macro.mall.pms.service.PmsProductAttributeRepository;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品属性参数表 服务实现类
 * </p>
 *
 * @author dongjb
 * @since 2020-11-27
 */
@Service
public class PmsProductAttributeRepositoryImpl extends ServiceImpl<PmsProductAttributeMapper, PmsProductAttribute> implements PmsProductAttributeRepository {
    /**
     * 获取商品属性信息
     */
    @Autowired
    private PmsProductAttributeMapper productAttributeMapper;
    @Override
    public List<ProductAttrInfo> getProductAttrInfoRepo(Long productCategoryId){
        return productAttributeMapper.getProductAttrInfo(productCategoryId);
    }

}
