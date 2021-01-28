package com.macro.mall.domain.order;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.macro.mall.ams.model.AmsOrderBase;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

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
@TableName("ams_order_integralrefund")
@ApiModel(value="AmsOrderIntegralrefund对象", description="")
public class OrderIntegralrefund extends OrderBase implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "订单编号")
    @TableId("orderNo")
    private String orderNo;

    @ApiModelProperty(value = "原支付订单号")
    private String origorderno;

    @ApiModelProperty(value = "原支付订单退款前状态")
    private Integer origstatus;

    @ApiModelProperty(value = "退款前余额")
    private BigDecimal beforebalance;

    @ApiModelProperty(value = "退款后余额")
    private BigDecimal afterbalance;

    @ApiModelProperty(value = "收款方退款前余额")
    private BigDecimal recebeforebalance;

    @ApiModelProperty(value = "收款方退款后余额")
    private BigDecimal receafterbalance;

    @ApiModelProperty(value = "退款时间")
    private String refunddate;

    @ApiModelProperty(value = "退款原因")
    private String reason;


}
