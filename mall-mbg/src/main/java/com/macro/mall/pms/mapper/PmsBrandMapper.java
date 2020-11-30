package com.macro.mall.pms.mapper;

import com.macro.mall.pms.model.PmsBrand;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.macro.mall.ums.model.UmsMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 品牌表 Mapper 接口
 * </p>
 *
 * @author dongjb
 * @since 2020-11-27
 */
public interface PmsBrandMapper extends BaseMapper<PmsBrand> {

    /**
     * 获取推荐品牌
     * @param offset 便宜量
     * @param limit 限制条数
     * @return 品牌列表
     */
    List<PmsBrand> getRecommendBrandList(@Param("offset") Integer offset, @Param("limit") Integer limit);

}
