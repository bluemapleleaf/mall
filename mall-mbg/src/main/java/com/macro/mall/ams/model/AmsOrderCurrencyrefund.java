package com.macro.mall.ams.model;

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
@TableName("ams_order_currencyrefund")
@ApiModel(value="AmsOrderCurrencyrefund对象", description="")
public class AmsOrderCurrencyrefund implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "订单编号")
    @TableId("orderNo")
    private String orderNo;

    @ApiModelProperty(value = "名称")
    private String currencyname;

    @ApiModelProperty(value = "原支付订单号")
    private String origorderno;

    @ApiModelProperty(value = "原支付订单退款前状态")
    private Integer origstatus;

    @ApiModelProperty(value = "退款时间")
    private String refunddate;

    @ApiModelProperty(value = "退款原因")
    private String reason;


}
