package com.macro.mall.sms.service;

import com.macro.mall.pms.model.PmsProduct;
import com.macro.mall.sms.model.SmsHomeRecommendProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 人气推荐商品表 服务类
 * </p>
 *
 * @author dongjb
 * @since 2020-11-29
 */
public interface SmsHomeRecommendProductRepository extends IService<SmsHomeRecommendProduct> {
    /**
     * 获取人气推荐
     */
    List<PmsProduct> getHotProductList(Integer offset, Integer limit);

}
