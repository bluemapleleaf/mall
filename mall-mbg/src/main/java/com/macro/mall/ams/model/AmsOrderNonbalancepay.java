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
@TableName("ams_order_nonbalancepay")
@ApiModel(value="AmsOrderNonbalancepay对象", description="")
public class AmsOrderNonbalancepay implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "订单编号")
    @TableId("orderNo")
    private String orderNo;

    @ApiModelProperty(value = "购买人第三方id")
    private String buyerid;

    @ApiModelProperty(value = "购买人银行卡开户名称")
    private String buyername;

    @ApiModelProperty(value = "购买人第三方账号名")
    private String buyeraccount;

    @ApiModelProperty(value = "支付时间")
    private String paydate;


}
