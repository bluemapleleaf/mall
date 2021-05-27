package com.macro.mall.task.quartz.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.common.api.ResultCode;
import com.macro.mall.qrtz.domain.JobAndTrigger;
import com.macro.mall.task.quartz.domain.JobForm;
import com.macro.mall.task.quartz.service.JobService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * Job Controller
 * </p>
 *
 * @author dongjb
 * @date 2021/02/02
 */
@RestController
@RequestMapping("/job")
@Slf4j
@Api(tags = "JobController", description = "定时任务维护")
public class JobController {
    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @Autowired
    private StringEncryptor encryptor;
    /**
     * 保存定时任务
     */
    @PostMapping
    public CommonResult<HttpStatus> addJob(@Valid JobForm form) {
        try {
            jobService.addJob(form);
        } catch (Exception e) {
            return CommonResult.failed(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        }

        return CommonResult.success(HttpStatus.CREATED,"操作成功");
    }

    /**
     * 删除定时任务
     */
    @DeleteMapping
    public CommonResult<HttpStatus> deleteJob(JobForm form) throws SchedulerException {
        if (StrUtil.hasBlank(form.getJobGroupName(), form.getJobClassName())) {
            return CommonResult.failed(ResultCode.VALIDATE_FAILED, "参数不能为空");
        }

        jobService.deleteJob(form);
        return CommonResult.success(HttpStatus.OK, "删除成功");
    }

    /**
     * 暂停定时任务
     */
    @PutMapping(params = "pause")
    public CommonResult<HttpStatus> pauseJob(JobForm form) throws SchedulerException {
        if (StrUtil.hasBlank(form.getJobGroupName(), form.getJobClassName())) {
            return CommonResult.failed(ResultCode.VALIDATE_FAILED, "参数不能为空");
        }

        jobService.pauseJob(form);
        return CommonResult.success(HttpStatus.OK, "暂停成功");
    }

    /**
     * 恢复定时任务
     */
    @PutMapping(params = "resume")
    public CommonResult<HttpStatus> resumeJob(JobForm form) throws SchedulerException {
        if (StrUtil.hasBlank(form.getJobGroupName(), form.getJobClassName())) {
            return CommonResult.failed(ResultCode.VALIDATE_FAILED, "参数不能为空");
        }

        jobService.resumeJob(form);
        return CommonResult.success(HttpStatus.OK, "恢复成功");
    }

    /**
     * 修改定时任务，定时时间
     */
    @PutMapping(params = "cron")
    public CommonResult<HttpStatus> cronJob(@Valid JobForm form) {
        try {
            jobService.cronJob(form);
        } catch (Exception e) {
            return CommonResult.failed(ResultCode.FAILED, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        }

        return CommonResult.success(HttpStatus.OK,"修改成功");
    }

    @GetMapping
    public CommonResult<CommonPage<JobAndTrigger>> jobList(Integer currentPage, Integer pageSize) {
        if (ObjectUtil.isNull(currentPage)) {
            currentPage = 1;
        }
        if (ObjectUtil.isNull(pageSize)) {
            pageSize = 10;
        }

        Page<JobAndTrigger> all = jobService.list(currentPage, pageSize);
        return CommonResult.success(CommonPage.restPage(all));

    }

}
