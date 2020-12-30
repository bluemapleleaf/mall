package com.macro.mall.sms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.sms.dto.FlashPromotionProduct;
import com.macro.mall.sms.dto.SmsFlashPromotionProduct;
import com.macro.mall.sms.model.SmsFlashPromotionProductRelation;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商品限时购与商品关系表 服务类
 * </p>
 *
 * @author dongjb
 * @since 2020-11-29
 */
public interface SmsFlashPromotionProductRelationRepository extends IService<SmsFlashPromotionProductRelation> {
    /**
     * 获取限时购及相关商品信息
     */
    IPage<SmsFlashPromotionProduct> getList(Page<SmsFlashPromotionProduct> page, Long flashPromotionId, Long flashPromotionSessionId);

    /**
     * 获取秒杀商品
     */
    List<FlashPromotionProduct> getFlashProductList(Long flashPromotionId, Long sessionId);

}
