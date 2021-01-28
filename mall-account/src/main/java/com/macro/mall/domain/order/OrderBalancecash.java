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
@TableName("ams_order_balancecash")
@ApiModel(value="AmsOrderBalancecash", description="")
public class OrderBalancecash extends OrderBase implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "订单编号")
    @TableId("orderNo")
    private String orderNo;

    @ApiModelProperty(value = "客户第三方类型")
    private String cbindtype;

    @ApiModelProperty(value = "开户行名称")
    private String buyerbankname;

    @ApiModelProperty(value = "客户银行卡开户名称")
    private String buyername;

    @ApiModelProperty(value = "客户第三方账号名")
    private String buyeraccount;

    @ApiModelProperty(value = "提现前余额")
    private BigDecimal beforebalance;

    @ApiModelProperty(value = "提现后余额")
    private BigDecimal afterbalance;

    @ApiModelProperty(value = "附加说明")
    private String remark;

    @ApiModelProperty(value = "提取时间")
    private String cashdate;


    @ApiModelProperty(value = "绑定信息id")
    @TableField(exist = false)
    private String bindinfoid;

    @ApiModelProperty(value = "支付密码")
    @TableField(exist = false)
    private String password;

    @ApiModelProperty(value = "审核意见")
    @TableField(exist = false)
    private String opinion;

}
