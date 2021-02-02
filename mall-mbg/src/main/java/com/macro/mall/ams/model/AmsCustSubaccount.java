package com.macro.mall.ams.model;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2021-01-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ams_cust_subaccount")
@ApiModel(value="AmsCustSubaccount", description="")
public class AmsCustSubaccount implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    @ApiModelProperty(value = "C户客户号")
    private String custid;

    @ApiModelProperty(value = "唯一编码")
    private String code;

    @ApiModelProperty(value = "状态 激活|禁用 等")
    private Integer status;

    @ApiModelProperty(value = "子账户类型	CHANGE|RED|COUPON|INTEGRAL 余额（零钱）|红包|优惠券|积分")
    private String type;

    @ApiModelProperty(value = "创建时间")
    private String createdate;

    @ApiModelProperty(value = "可用余额 子账户余额")
    private BigDecimal balance;

    @ApiModelProperty(value = "折算金额 对应的人民币金额")
    private BigDecimal money;

    @ApiModelProperty(value = "编辑人id")
    private String edituserid;

    @ApiModelProperty(value = "授权人id")
    private String authuserid;


}
