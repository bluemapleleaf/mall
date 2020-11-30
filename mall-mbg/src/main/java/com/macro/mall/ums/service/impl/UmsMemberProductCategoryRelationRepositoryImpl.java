package com.macro.mall.ums.service.impl;

import com.macro.mall.ums.model.UmsMemberProductCategoryRelation;
import com.macro.mall.ums.mapper.UmsMemberProductCategoryRelationMapper;
import com.macro.mall.ums.service.UmsMemberProductCategoryRelationRepository;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员与产品分类关系表（用户喜欢的分类） 服务实现类
 * </p>
 *
 * @author dongjb
 * @since 2020-11-30
 */
@Service
public class UmsMemberProductCategoryRelationRepositoryImpl extends ServiceImpl<UmsMemberProductCategoryRelationMapper, UmsMemberProductCategoryRelation> implements UmsMemberProductCategoryRelationRepository {

}
