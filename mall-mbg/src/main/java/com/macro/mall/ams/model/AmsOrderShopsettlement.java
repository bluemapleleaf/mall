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
@TableName("ams_order_shopsettlement")
@ApiModel(value="AmsOrderShopsettlement对象", description="")
public class AmsOrderShopsettlement implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "订单编号")
    @TableId("orderNo")
    private String orderNo;

    @ApiModelProperty(value = "商户编号")
    private String shopno;

    @ApiModelProperty(value = "结算账户类型")
    private String accounttype;

    @ApiModelProperty(value = "开户行名称")
    private String bankname;

    @ApiModelProperty(value = "账户名称")
    private String accountname;

    @ApiModelProperty(value = "第三方账号名")
    private String account;

    @ApiModelProperty(value = "费率")
    private BigDecimal rate;

    @ApiModelProperty(value = "商户计划结算时间")
    private String plansettlementdate;

    @ApiModelProperty(value = "结算时间")
    private String settlementdate;

    @ApiModelProperty(value = "附加说明")
    private String remark;


}
