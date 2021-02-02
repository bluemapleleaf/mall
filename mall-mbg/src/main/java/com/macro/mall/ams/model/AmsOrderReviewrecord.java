package com.macro.mall.ams.model;

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
 * @since 2021-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ams_order_reviewrecord")
@ApiModel(value="AmsOrderReviewrecord对象", description="")
public class AmsOrderReviewrecord implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    @ApiModelProperty(value = "订单编号")
    private String orderno;

    @ApiModelProperty(value = "审核内容")
    private String content;

    @ApiModelProperty(value = "审核结果0-不通过，1-通过")
    private Integer result;

    @ApiModelProperty(value = "审核人id")
    private String userid;

    @ApiModelProperty(value = "审核人名称")
    private String username;

    @ApiModelProperty(value = "会计日期")
    private String accountdate;

    @ApiModelProperty(value = "审核时间")
    private String reviewdate;


}
