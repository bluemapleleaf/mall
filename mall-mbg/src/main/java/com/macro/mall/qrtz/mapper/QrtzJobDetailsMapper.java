package com.macro.mall.qrtz.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.qrtz.domain.JobAndTrigger;
import com.macro.mall.qrtz.model.QrtzJobDetails;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dongjb
 * @since 2021-01-26
 */
public interface QrtzJobDetailsMapper extends BaseMapper<QrtzJobDetails> {

    /**
     * 查询定时作业和触发器列表
     *
     * @return 定时作业和触发器列表
     */
    List<JobAndTrigger> getJobAndTriggerList(Page page);
}
