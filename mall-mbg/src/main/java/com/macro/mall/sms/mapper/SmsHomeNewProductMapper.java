package com.macro.mall.sms.mapper;

import com.macro.mall.pms.model.PmsProduct;
import com.macro.mall.sms.model.SmsHomeNewProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 新鲜好物表 Mapper 接口
 * </p>
 *
 * @author dongjb
 * @since 2020-11-29
 */
public interface SmsHomeNewProductMapper extends BaseMapper<SmsHomeNewProduct> {
    /**
     * 获取新品推荐
     */
    List<PmsProduct> getNewProductList(@Param("offset") Integer offset, @Param("limit") Integer limit);

}
