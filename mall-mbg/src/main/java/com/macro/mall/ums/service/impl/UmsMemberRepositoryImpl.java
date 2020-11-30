package com.macro.mall.ums.service.impl;

import com.macro.mall.ums.model.UmsMember;
import com.macro.mall.ums.mapper.UmsMemberMapper;
import com.macro.mall.ums.service.UmsMemberRepository;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author dongjb
 * @since 2020-11-30
 */
@Service
public class UmsMemberRepositoryImpl extends ServiceImpl<UmsMemberMapper, UmsMember> implements UmsMemberRepository {

}
