package com.macro.mall.qrtz.service.impl;

import com.macro.mall.qrtz.model.QrtzSchedulerState;
import com.macro.mall.qrtz.mapper.QrtzSchedulerStateMapper;
import com.macro.mall.qrtz.service.QrtzSchedulerStateRepository;
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
public class QrtzSchedulerStateRepositoryImpl extends ServiceImpl<QrtzSchedulerStateMapper, QrtzSchedulerState> implements QrtzSchedulerStateRepository {

}
