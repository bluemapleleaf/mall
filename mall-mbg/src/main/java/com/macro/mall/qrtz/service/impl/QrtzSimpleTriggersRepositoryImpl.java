package com.macro.mall.qrtz.service.impl;

import com.macro.mall.qrtz.model.QrtzSimpleTriggers;
import com.macro.mall.qrtz.mapper.QrtzSimpleTriggersMapper;
import com.macro.mall.qrtz.service.QrtzSimpleTriggersRepository;
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
public class QrtzSimpleTriggersRepositoryImpl extends ServiceImpl<QrtzSimpleTriggersMapper, QrtzSimpleTriggers> implements QrtzSimpleTriggersRepository {

}
