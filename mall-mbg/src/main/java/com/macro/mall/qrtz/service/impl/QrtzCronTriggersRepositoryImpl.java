package com.macro.mall.qrtz.service.impl;

import com.macro.mall.qrtz.model.QrtzCronTriggers;
import com.macro.mall.qrtz.mapper.QrtzCronTriggersMapper;
import com.macro.mall.qrtz.service.QrtzCronTriggersRepository;
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
public class QrtzCronTriggersRepositoryImpl extends ServiceImpl<QrtzCronTriggersMapper, QrtzCronTriggers> implements QrtzCronTriggersRepository {

}
