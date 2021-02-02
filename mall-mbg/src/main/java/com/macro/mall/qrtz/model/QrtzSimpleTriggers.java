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
@TableName("qrtz_simple_triggers")
@ApiModel(value="QrtzSimpleTriggers对象", description="")
public class QrtzSimpleTriggers implements Serializable {

    private static final long serialVersionUID=1L;

    private String schedName;

    private String triggerName;

    private String triggerGroup;

    private Long repeatCount;

    private Long repeatInterval;

    private Long timesTriggered;


}
