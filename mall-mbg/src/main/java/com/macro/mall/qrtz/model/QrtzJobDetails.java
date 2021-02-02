package com.macro.mall.qrtz.model;

import com.baomidou.mybatisplus.annotation.TableName;
import java.sql.Blob;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author dongjb
 * @since 2021-01-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("qrtz_job_details")
@ApiModel(value="QrtzJobDetails对象", description="")
public class QrtzJobDetails implements Serializable {

    private static final long serialVersionUID=1L;

    private String schedName;

    private String jobName;

    private String jobGroup;

    private String description;

    private String jobClassName;

    private String isDurable;

    private String isNonconcurrent;

    private String isUpdateData;

    private String requestsRecovery;

    private Blob jobData;


}
