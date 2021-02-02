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
@TableName("ams_order_integralsettlement")
@ApiModel(value="AmsOrderIntegralsettlement对象", description="")
public class AmsOrderIntegralsettlement  implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "订单编号")
    @TableId("orderNo")
    private String orderNo;

    @ApiModelProperty(value = "结算金额")
    private BigDecimal money;

    @ApiModelProperty(value = "客户第三方类型")
    private String bbindtype;

    @ApiModelProperty(value = "开户行名称")
    private String buyerbankname;

    @ApiModelProperty(value = "客户银行卡开户名称")
    private String buyername;

    @ApiModelProperty(value = "客户第三方账号名")
    private String buyeraccount;

    @ApiModelProperty(value = "结算前积分")
    private BigDecimal beforebalance;

    @ApiModelProperty(value = "结算后积分")
    private BigDecimal afterbalance;

    @ApiModelProperty(value = "附加说明")
    private String remark;

    @ApiModelProperty(value = "结算时间")
    private String settlementdate;


}
