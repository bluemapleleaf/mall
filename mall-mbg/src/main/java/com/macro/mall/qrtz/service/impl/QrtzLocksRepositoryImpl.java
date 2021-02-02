package com.macro.mall.qrtz.service.impl;

import com.macro.mall.qrtz.model.QrtzLocks;
import com.macro.mall.qrtz.mapper.QrtzLocksMapper;
import com.macro.mall.qrtz.service.QrtzLocksRepository;
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
public class QrtzLocksRepositoryImpl extends ServiceImpl<QrtzLocksMapper, QrtzLocks> implements QrtzLocksRepository {

}
