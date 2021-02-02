package com.macro.mall.qrtz.model;

import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("qrtz_fired_triggers")
@ApiModel(value="QrtzFiredTriggers对象", description="")
public class QrtzFiredTriggers implements Serializable {

    private static final long serialVersionUID=1L;

    private String schedName;

    private String entryId;

    private String triggerName;

    private String triggerGroup;

    private String instanceName;

    private Long firedTime;

    private Long schedTime;

    private Integer priority;

    private String state;

    private String jobName;

    private String jobGroup;

    private String isNonconcurrent;

    private String requestsRecovery;


}
