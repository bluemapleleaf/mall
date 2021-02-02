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
@TableName("qrtz_triggers")
@ApiModel(value="QrtzTriggers对象", description="")
public class QrtzTriggers implements Serializable {

    private static final long serialVersionUID=1L;

    private String schedName;

    private String triggerName;

    private String triggerGroup;

    private String jobName;

    private String jobGroup;

    private String description;

    private Long nextFireTime;

    private Long prevFireTime;

    private Integer priority;

    private String triggerState;

    private String triggerType;

    private Long startTime;

    private Long endTime;

    private String calendarName;

    private Integer misfireInstr;

    private Blob jobData;


}
