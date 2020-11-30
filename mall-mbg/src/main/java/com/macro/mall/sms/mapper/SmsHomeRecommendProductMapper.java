package com.macro.mall.sms.mapper;

import com.macro.mall.pms.model.PmsProduct;
import com.macro.mall.sms.model.SmsHomeRecommendProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 人气推荐商品表 Mapper 接口
 * </p>
 *
 * @author dongjb
 * @since 2020-11-29
 */
public interface SmsHomeRecommendProductMapper extends BaseMapper<SmsHomeRecommendProduct> {
    /**
     * 获取人气推荐
     */
    List<PmsProduct> getHotProductList(@Param("offset") Integer offset, @Param("limit") Integer limit);

}
