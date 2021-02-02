package com.macro.mall.qrtz.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.qrtz.domain.JobAndTrigger;
import com.macro.mall.qrtz.model.QrtzJobDetails;
import com.macro.mall.qrtz.mapper.QrtzJobDetailsMapper;
import com.macro.mall.qrtz.service.QrtzJobDetailsRepository;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
public class QrtzJobDetailsRepositoryImpl extends ServiceImpl<QrtzJobDetailsMapper, QrtzJobDetails> implements QrtzJobDetailsRepository {
    @Autowired
    QrtzJobDetailsMapper qrtzJobDetailsMapper;

    @Override
    public Page<JobAndTrigger> getJobAndTriggerList(Page<JobAndTrigger> page)  {

        return page.setRecords(qrtzJobDetailsMapper.getJobAndTriggerList(page));
    }

}
