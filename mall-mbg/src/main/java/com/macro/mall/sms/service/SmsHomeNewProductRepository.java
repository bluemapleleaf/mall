package com.macro.mall.sms.service;

import com.macro.mall.pms.model.PmsProduct;
import com.macro.mall.sms.model.SmsHomeNewProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 新鲜好物表 服务类
 * </p>
 *
 * @author dongjb
 * @since 2020-11-29
 */
public interface SmsHomeNewProductRepository extends IService<SmsHomeNewProduct> {
    /**
     * 获取新品推荐
     */
    List<PmsProduct> getNewProductList(Integer offset, Integer limit);

}
