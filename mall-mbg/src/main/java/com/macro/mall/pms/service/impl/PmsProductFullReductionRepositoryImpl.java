package com.macro.mall.pms.service.impl;

import com.macro.mall.pms.model.PmsProductFullReduction;
import com.macro.mall.pms.mapper.PmsProductFullReductionMapper;
import com.macro.mall.pms.service.PmsProductFullReductionRepository;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 产品满减表(只针对同商品) 服务实现类
 * </p>
 *
 * @author dongjb
 * @since 2020-11-28
 */
@Service
public class PmsProductFullReductionRepositoryImpl extends ServiceImpl<PmsProductFullReductionMapper, PmsProductFullReduction> implements PmsProductFullReductionRepository {

}
