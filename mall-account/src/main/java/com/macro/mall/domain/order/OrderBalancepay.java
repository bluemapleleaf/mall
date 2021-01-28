package com.macro.mall.domain.order;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("ams_order_balancepay")
@ApiModel(value="AmsOrderBalancepay对象", description="")
public class OrderBalancepay extends OrderBase implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "订单编号")
    @TableId("orderNo")
    private String orderNo;

    @ApiModelProperty(value = "支付前余额")
    private BigDecimal beforebalance;

    @ApiModelProperty(value = "支付后余额")
    private BigDecimal afterbalance;

    @ApiModelProperty(value = "收款方收款前余额")
    private BigDecimal recebeforebalance;

    @ApiModelProperty(value = "收款方收款后余额")
    private BigDecimal receafterbalance;

    @ApiModelProperty(value = "支付密码")
    @TableField(exist = false)
    private String password;


}
