package com.macro.mall.domain.order;

import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("ams_order_nonbalancetransfer")
@ApiModel(value="AmsOrderNonbalancetransfer", description="")
public class OrderNonbalancetransfer extends OrderBase implements Serializable {

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

    @ApiModelProperty(value = "收款人账户类型")
    private String recetype;

    @ApiModelProperty(value = "开户行名称")
    private String recebankname;

    @ApiModelProperty(value = "收款人银行卡开户名称")
    private String recename;

    @ApiModelProperty(value = "收款人第三方账号名")
    private String receaccount;

    @ApiModelProperty(value = "转账时间")
    private String transferdate;


}
