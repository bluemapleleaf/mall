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
 * @since 2021-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ams_business_subaccount")
@ApiModel(value="AmsBusinessSubaccount对象", description="")
public class AmsBusinessSubaccount implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    @ApiModelProperty(value = "外键：B户客户号")
    private String businessid;

    @ApiModelProperty(value = "账户类型")
    private String type;

    @ApiModelProperty(value = "是否默认，0-否；1-是")
    private Integer isdefault;

    @ApiModelProperty(value = "虚拟账户编码")
    private String code;

    @ApiModelProperty(value = "可用余额")
    private BigDecimal balance;

    @ApiModelProperty(value = "折算金额 对应的人民币金额")
    private BigDecimal money;

    @ApiModelProperty(value = "创建时间")
    private String createdate;

    @ApiModelProperty(value = "账户状态")
    private Integer status;

    @ApiModelProperty(value = "编辑人id")
    private String edituserid;

    @ApiModelProperty(value = "授权人id")
    private String authuserid;


}
