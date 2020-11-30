package com.macro.mall.oms.service.impl;

import com.macro.mall.oms.model.OmsCartItem;
import com.macro.mall.oms.mapper.OmsCartItemMapper;
import com.macro.mall.oms.service.OmsCartItemRepository;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 购物车表 服务实现类
 * </p>
 *
 * @author dongjb
 * @since 2020-11-28
 */
@Service
public class OmsCartItemRepositoryImpl extends ServiceImpl<OmsCartItemMapper, OmsCartItem> implements OmsCartItemRepository {

}
