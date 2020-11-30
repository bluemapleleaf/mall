package com.macro.mall.oms.service.impl;

import com.macro.mall.oms.model.OmsOrderItem;
import com.macro.mall.oms.mapper.OmsOrderItemMapper;
import com.macro.mall.oms.service.OmsOrderItemRepository;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单中所包含的商品 服务实现类
 * </p>
 *
 * @author dongjb
 * @since 2020-11-28
 */
@Service
public class OmsOrderItemRepositoryImpl extends ServiceImpl<OmsOrderItemMapper, OmsOrderItem> implements OmsOrderItemRepository {

}
