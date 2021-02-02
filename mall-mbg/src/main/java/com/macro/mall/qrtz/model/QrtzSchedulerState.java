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
@TableName("qrtz_scheduler_state")
@ApiModel(value="QrtzSchedulerState对象", description="")
public class QrtzSchedulerState implements Serializable {

    private static final long serialVersionUID=1L;

    private String schedName;

    private String instanceName;

    private Long lastCheckinTime;

    private Long checkinInterval;


}
