package com.macro.mall.qrtz.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.qrtz.domain.JobAndTrigger;
import com.macro.mall.qrtz.model.QrtzJobDetails;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dongjb
 * @since 2021-01-26
 */
public interface QrtzJobDetailsRepository extends IService<QrtzJobDetails> {
    /**
     * 按照分页查询任务信息
     *
     * @param page 分页参数对象
     * @return 分页结果
     */
    Page<JobAndTrigger> getJobAndTriggerList(Page<JobAndTrigger> page);
}
