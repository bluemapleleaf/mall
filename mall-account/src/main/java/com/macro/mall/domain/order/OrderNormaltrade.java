package com.macro.mall.domain.order;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

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
@TableName("ams_order_normaltrade")
@ApiModel(value="AmsOrderNormaltrade", description="")
public class OrderNormaltrade extends OrderBase implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "交易订单号")
    private String orderno;

    @ApiModelProperty(value = "交易描述")
    private String tradedescription;

    @ApiModelProperty(value = "商户编号")
    private String shopno;

    @ApiModelProperty(value = "扩展字段1")
    private String reserve1;

    @ApiModelProperty(value = "扩展字段2")
    private String reserve2;

    @ApiModelProperty(value = "扩展字段3")
    private String reserve3;

    @ApiModelProperty(value = "扩展字段4")
    private String reserve4;

    @ApiModelProperty(value = "扩展字段5")
    private String reserve5;

    @ApiModelProperty(value = "扩展字段6")
    private String reserve6;

    @ApiModelProperty(value = "扩展字段7")
    private String reserve7;

    @ApiModelProperty(value = "扩展字段8")
    private String reserve8;

    @ApiModelProperty(value = "扩展字段9")
    private String reserve9;

    @ApiModelProperty(value = "扩展字段10")
    private String reserve10;

    @ApiModelProperty(value = "扩展字段11")
    private String reserve11;

    @ApiModelProperty(value = "扩展字段12")
    private String reserve12;

    @ApiModelProperty(value = "扩展字段13")
    private String reserve13;

    @ApiModelProperty(value = "扩展字段14")
    private String reserve14;

    @ApiModelProperty(value = "扩展字段15")
    private String reserve15;

    @ApiModelProperty(value = "扩展字段16")
    private String reserve16;

    @ApiModelProperty(value = "扩展字段17")
    private String reserve17;

    @ApiModelProperty(value = "扩展字段18")
    private String reserve18;

    @ApiModelProperty(value = "扩展字段19")
    private String reserve19;

    @ApiModelProperty(value = "扩展字段20")
    private String reserve20;


}
