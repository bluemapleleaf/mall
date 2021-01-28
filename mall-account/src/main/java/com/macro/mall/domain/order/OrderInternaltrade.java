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
@TableName("ams_order_internaltrade")
@ApiModel(value="AmsOrderInternaltrade对象", description="")
public class OrderInternaltrade extends OrderBase implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "交易订单号")
    private String orderno;

    @ApiModelProperty(value = "交易类型")
    private Integer tradetype;

    @ApiModelProperty(value = "交易类型名称")
    private String tradetypename;

    @ApiModelProperty(value = "交易描述")
    private String tradedescription;

    @ApiModelProperty(value = "原订单号")
    private String origorderno;

    @ApiModelProperty(value = "原订单类型")
    private Integer origordertype;

    @ApiModelProperty(value = "原订单状态")
    private Integer origstatus;

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


    @ApiModelProperty(value = "审核意见")
    private String opinion;

    @ApiModelProperty(value = "审核结果")
    private int reviewresult;

}
