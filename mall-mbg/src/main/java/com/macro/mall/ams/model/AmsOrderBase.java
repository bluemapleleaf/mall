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
 * 基础订单
 * </p>
 *
 * @author dongjb
 * @since 2021-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ams_order_base")
@ApiModel(value="AmsOrderBase", description="基础订单")
public class AmsOrderBase implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "订单编号")
    @TableId("orderNo")
    private String orderNo;

    @ApiModelProperty(value = "订单类型")
    private Integer ordertype;

    @ApiModelProperty(value = "B户客户号")
    private String businessid;

    @ApiModelProperty(value = "收款方B户客户号")
    private String recebusinessid;

    @ApiModelProperty(value = "B户名称")
    private String businessname;

    @ApiModelProperty(value = "商户交易号")
    private String businesstradeno;

    @ApiModelProperty(value = "B户虚拟账户编号")
    private String businesssubaccountcode;

    @ApiModelProperty(value = "C户客户号")
    private String custid;

    @ApiModelProperty(value = "收款方C户客户号")
    private String rececustid;

    @ApiModelProperty(value = "C户虚拟账户编号")
    private String custsubaccountcode;

    @ApiModelProperty(value = "支付方式")
    private String paytype;

    @ApiModelProperty(value = "支付方式名称")
    private String paytypename;

    @ApiModelProperty(value = "交易额")
    private BigDecimal amont;

    @ApiModelProperty(value = "实际交易额比例")
    private BigDecimal ratio;

    @ApiModelProperty(value = "交易金额")
    private BigDecimal actamont;

    private String accountdate;

    @ApiModelProperty(value = "会计出账日期")
    private String statementdate;

    @ApiModelProperty(value = "会计扎账日期")
    private String bindaccountdate;

    @ApiModelProperty(value = "订单创建时间")
    private String createdate;

    @ApiModelProperty(value = "交易渠道号")
    private String channel;

    @ApiModelProperty(value = "订单描述")
    private String description;

    @ApiModelProperty(value = "订单状态")
    private Integer status;

    @ApiModelProperty(value = "订单复核状态")
    private Integer checkstatus;

    @ApiModelProperty(value = "第三方交易号")
    private String tradeno;

    @ApiModelProperty(value = "第三方交易状态")
    private String tradestatus;

    @ApiModelProperty(value = "第三方通知时间")
    private String notifydate;

    @ApiModelProperty(value = "订单处理时间")
    private String processdate;

    @ApiModelProperty(value = "订单处理人id")
    private String userid;

    @ApiModelProperty(value = "订单最后修改时间")
    private String lastmodifydate;

    @ApiModelProperty(value = "已退额")
    private BigDecimal refundamont;

    @ApiModelProperty(value = "已退金额")
    private BigDecimal refundactamont;

    @ApiModelProperty(value = "扩展额1")
    private BigDecimal extamont1;

    @ApiModelProperty(value = "扩展额2")
    private BigDecimal extamont2;

    @ApiModelProperty(value = "扩展额3")
    private BigDecimal extamont3;

    @ApiModelProperty(value = "扩展额4")
    private BigDecimal extamont4;

    @ApiModelProperty(value = "扩展额5")
    private BigDecimal extamont5;

    @ApiModelProperty(value = "扩展额6")
    private BigDecimal extamont6;

    @ApiModelProperty(value = "扩展额7")
    private BigDecimal extamont7;

    @ApiModelProperty(value = "扩展额8")
    private BigDecimal extamont8;

    @ApiModelProperty(value = "扩展额9")
    private BigDecimal extamont9;

    @ApiModelProperty(value = "扩展额10")
    private BigDecimal extamont10;

    @ApiModelProperty(value = "备注1")
    private String remark1;

    @ApiModelProperty(value = "备注2")
    private String remark2;

    @ApiModelProperty(value = "备注3")
    private String remark3;

    @ApiModelProperty(value = "备注4")
    private String remark4;

    @ApiModelProperty(value = "备注5")
    private String remark5;

    @ApiModelProperty(value = "扩展字段1")
    private String field1;

    @ApiModelProperty(value = "扩展字段2")
    private String field2;

    @ApiModelProperty(value = "扩展字段3")
    private String field3;

    @ApiModelProperty(value = "扩展字段4")
    private String field4;

    @ApiModelProperty(value = "扩展字段5")
    private String field5;

    @ApiModelProperty(value = "扩展字段6")
    private String field6;

    @ApiModelProperty(value = "扩展字段7")
    private String field7;

    @ApiModelProperty(value = "扩展字段8")
    private String field8;

    @ApiModelProperty(value = "扩展字段9")
    private String field9;

    @ApiModelProperty(value = "扩展字段10")
    private String field10;

}
