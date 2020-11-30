package com.macro.mall.pms.service.impl;

import com.macro.mall.pms.model.PmsProductCategoryAttributeRelation;
import com.macro.mall.pms.mapper.PmsProductCategoryAttributeRelationMapper;
import com.macro.mall.pms.service.PmsProductCategoryAttributeRelationRepository;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 产品的分类和属性的关系表，用于设置分类筛选条件（只支持一级分类） 服务实现类
 * </p>
 *
 * @author dongjb
 * @since 2020-11-28
 */
@Service
public class PmsProductCategoryAttributeRelationRepositoryImpl extends ServiceImpl<PmsProductCategoryAttributeRelationMapper, PmsProductCategoryAttributeRelation> implements PmsProductCategoryAttributeRelationRepository {

}
