package com.macro.mall.qrtz.service.impl;

import com.macro.mall.qrtz.model.QrtzFiredTriggers;
import com.macro.mall.qrtz.mapper.QrtzFiredTriggersMapper;
import com.macro.mall.qrtz.service.QrtzFiredTriggersRepository;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dongjb
 * @since 2021-01-26
 */
@Service
public class QrtzFiredTriggersRepositoryImpl extends ServiceImpl<QrtzFiredTriggersMapper, QrtzFiredTriggers> implements QrtzFiredTriggersRepository {

}
