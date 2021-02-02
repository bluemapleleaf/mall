package com.macro.mall.ams.model;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("ams_order_integralrecharge")
@ApiModel(value="AmsOrderIntegralrecharge对象", description="")
public class AmsOrderIntegralrecharge implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "订单编号")
    @TableId("orderNo")
    private String orderNo;

    @ApiModelProperty(value = "获取前积分")
    private BigDecimal beforebalance;

    @ApiModelProperty(value = "获取后积分")
    private BigDecimal afterbalance;

    @ApiModelProperty(value = "获取时间")
    private String rechargedate;


}
