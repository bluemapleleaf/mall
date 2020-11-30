package com.macro.mall.portal.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.macro.mall.oms.model.OmsCartItem;
import com.macro.mall.oms.service.impl.OmsCartItemRepositoryImpl;
import com.macro.mall.pms.domain.CartProduct;
import com.macro.mall.pms.service.PmsProductRepository;
import com.macro.mall.portal.domain.CartPromotionItem;
import com.macro.mall.portal.service.OmsCartItemService;
import com.macro.mall.portal.service.OmsPromotionService;
import com.macro.mall.portal.service.UmsMemberService;
import com.macro.mall.ums.model.UmsMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 购物车管理Service实现类
 * Created by macro on 2018/8/2.
 */
@Service
public class OmsCartItemServiceImpl extends OmsCartItemRepositoryImpl implements OmsCartItemService {
    @Autowired
    private PmsProductRepository productRepository;
    @Autowired
    private OmsPromotionService promotionService;
    @Autowired
    private UmsMemberService memberService;

    @Override
    public boolean add(OmsCartItem cartItem) {
        boolean ret;
        UmsMember currentMember =memberService.getCurrentMember();
        cartItem.setMemberId(currentMember.getId());
        cartItem.setMemberNickname(currentMember.getNickname());
        cartItem.setDeleteStatus(0);
        OmsCartItem existCartItem = getCartItem(cartItem);
        if (existCartItem == null) {
            cartItem.setCreateDate(new Date());
            ret = save(cartItem);
        } else {
            cartItem.setModifyDate(new Date());
            existCartItem.setQuantity(existCartItem.getQuantity() + cartItem.getQuantity());
            ret = updateById(existCartItem);
        }
        return ret;
    }

    /**
     * 根据会员id,商品id和规格获取购物车中商品
     */
    private OmsCartItem getCartItem(OmsCartItem cartItem) {
        LambdaQueryWrapper<OmsCartItem> lambda = new LambdaQueryWrapper<>();
        lambda.eq(OmsCartItem::getMemberId, cartItem.getMemberId());
        lambda.eq(OmsCartItem::getProductId, cartItem.getProductId());
        lambda.eq(OmsCartItem::getDeleteStatus, 0);
        if (!StringUtils.isEmpty(cartItem.getProductSkuId())) {
            lambda.eq(OmsCartItem::getProductSkuId, cartItem.getProductSkuId());
        }
        return getOne(lambda);
    }

    @Override
    public List<OmsCartItem> list(Long memberId) {
        LambdaQueryWrapper<OmsCartItem> lambda = new LambdaQueryWrapper<>();
        lambda.eq(OmsCartItem::getMemberId, memberId);
        lambda.eq(OmsCartItem::getDeleteStatus, 0);
        return list(lambda);
    }

    @Override
    public List<CartPromotionItem> listPromotion(Long memberId, List<Long> cartIds) {
        List<OmsCartItem> cartItemList = list(memberId);
        if(CollUtil.isNotEmpty(cartIds)){
            cartItemList = cartItemList.stream().filter(item->cartIds.contains(item.getId())).collect(Collectors.toList());
        }
        List<CartPromotionItem> cartPromotionItemList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(cartItemList)){
            cartPromotionItemList = promotionService.calcCartPromotion(cartItemList);
        }
        return cartPromotionItemList;
    }

    @Override
    public boolean updateQuantity(Long id, Long memberId, Integer quantity) {
        LambdaUpdateWrapper<OmsCartItem> lambda = new LambdaUpdateWrapper<>();
        lambda.eq(OmsCartItem::getDeleteStatus, 0);
        lambda.eq(OmsCartItem::getId, id);
        lambda.eq(OmsCartItem::getMemberId, memberId);
        lambda.set(OmsCartItem::getQuantity, quantity);
        return update(lambda);
    }

    @Override
    public boolean delete(Long memberId, List<Long> ids) {
        LambdaUpdateWrapper<OmsCartItem> lambda = new LambdaUpdateWrapper<>();
        lambda.in(OmsCartItem::getId, ids);
        lambda.eq(OmsCartItem::getMemberId, memberId);
        lambda.set(OmsCartItem::getDeleteStatus, 0);
        return update(lambda);
    }

    @Override
    public CartProduct getCartProduct(Long productId) {
        return productRepository.getCartProduct(productId);
    }

    @Override
    public int updateAttr(OmsCartItem cartItem) {
        //删除原购物车信息
        OmsCartItem updateCart = new OmsCartItem();
        updateCart.setId(cartItem.getId());
        updateCart.setModifyDate(new Date());
        updateCart.setDeleteStatus(1);
        updateById(updateCart);

        cartItem.setId(null);
        add(cartItem);
        return 1;
    }

    @Override
    public boolean clear(Long memberId) {
        LambdaUpdateWrapper<OmsCartItem> lambda = new LambdaUpdateWrapper<>();
        lambda.eq(OmsCartItem::getMemberId, memberId);
        lambda.set(OmsCartItem::getDeleteStatus, 1);
        return update(lambda);
    }
}
