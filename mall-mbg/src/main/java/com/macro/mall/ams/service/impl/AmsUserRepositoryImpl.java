package com.macro.mall.ams.service.impl;

import com.macro.mall.ams.model.AmsUser;
import com.macro.mall.ams.mapper.AmsUserMapper;
import com.macro.mall.ams.service.AmsUserRepository;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dongjb
 * @since 2021-01-05
 */
@Service
public class AmsUserRepositoryImpl extends ServiceImpl<AmsUserMapper, AmsUser> implements AmsUserRepository {

}
