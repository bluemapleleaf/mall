package com.macro.mall.domain.order;

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
@TableName("ams_order_balancesettlement")
@ApiModel(value="AmsOrderBalancesettlement对象", description="")
public class OrderBalancesettlement extends OrderBase implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "订单编号")
    @TableId("orderNo")
    private String orderNo;

    @ApiModelProperty(value = "客户第三方类型")
    private String bbindtype;

    @ApiModelProperty(value = "开户行名称")
    private String buyerbankname;

    @ApiModelProperty(value = "客户银行卡开户名称")
    private String buyername;

    @ApiModelProperty(value = "客户第三方账号名")
    private String buyeraccount;

    @ApiModelProperty(value = "结算前余额")
    private BigDecimal beforebalance;

    @ApiModelProperty(value = "结算后余额")
    private BigDecimal afterbalance;

    @ApiModelProperty(value = "附加说明")
    private String remark;

    @ApiModelProperty(value = "结算时间")
    private String settlementdate;

    @ApiModelProperty(value = "绑定信息id")
    private String bindinfoid;

    @ApiModelProperty(value = "审核意见")
    private String opinion;


}
