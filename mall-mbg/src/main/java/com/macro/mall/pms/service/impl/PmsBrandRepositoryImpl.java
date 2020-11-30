package com.macro.mall.pms.service.impl;

import com.macro.mall.pms.model.PmsBrand;
import com.macro.mall.pms.mapper.PmsBrandMapper;
import com.macro.mall.pms.service.PmsBrandRepository;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 品牌表 服务实现类
 * </p>
 *
 * @author dongjb
 * @since 2020-11-27
 */
@Service
public class PmsBrandRepositoryImpl extends ServiceImpl<PmsBrandMapper, PmsBrand> implements PmsBrandRepository {
    @Autowired
    private PmsBrandMapper brandMapper;
    @Override
    public List<PmsBrand> getRecommendBrandList(@Param("offset") Integer offset, @Param("limit") Integer limit) {
        return brandMapper.getRecommendBrandList(offset, limit);
    }

}
