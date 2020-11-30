package com.macro.mall.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.macro.mall.portal.service.UmsMemberReceiveAddressService;
import com.macro.mall.portal.service.UmsMemberService;
import com.macro.mall.ums.model.UmsMember;
import com.macro.mall.ums.model.UmsMemberReceiveAddress;
import com.macro.mall.ums.service.impl.UmsMemberReceiveAddressRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户地址管理Service实现类
 * Created by macro on 2018/8/28.
 */
@Service
public class UmsMemberReceiveAddressServiceImpl extends UmsMemberReceiveAddressRepositoryImpl implements UmsMemberReceiveAddressService {
    @Autowired
    private UmsMemberService memberService;
    @Override
    public boolean add(UmsMemberReceiveAddress address) {
        UmsMember currentMember = memberService.getCurrentMember();
        address.setMemberId(currentMember.getId());
        return save(address);
    }

    @Override
    public boolean delete(Long id) {
        UmsMember currentMember = memberService.getCurrentMember();
        LambdaQueryWrapper<UmsMemberReceiveAddress> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UmsMemberReceiveAddress::getMemberId, currentMember.getId())
                .eq(UmsMemberReceiveAddress::getId, id);
        return remove(lambdaQueryWrapper);
    }

    @Override
    public boolean update(Long id, UmsMemberReceiveAddress address) {
        address.setId(null);
        UmsMember currentMember = memberService.getCurrentMember();
        LambdaUpdateWrapper<UmsMemberReceiveAddress> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(UmsMemberReceiveAddress::getMemberId, currentMember.getId())
                .eq(UmsMemberReceiveAddress::getId, id);
        if(address.getDefaultStatus()==1){
            lambdaUpdateWrapper.set(UmsMemberReceiveAddress::getDefaultStatus, 0);
        }
        return update(lambdaUpdateWrapper);
    }

    @Override
    public List<UmsMemberReceiveAddress> list() {
        UmsMember currentMember = memberService.getCurrentMember();
        LambdaQueryWrapper<UmsMemberReceiveAddress> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UmsMemberReceiveAddress::getMemberId, currentMember.getId());
        return list(lambdaQueryWrapper);
    }

    @Override
    public UmsMemberReceiveAddress getItem(Long id) {
        UmsMember currentMember = memberService.getCurrentMember();
        LambdaQueryWrapper<UmsMemberReceiveAddress> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UmsMemberReceiveAddress::getMemberId, currentMember.getId())
                .eq(UmsMemberReceiveAddress::getId, id);
        return getOne(lambdaQueryWrapper);
    }
}
