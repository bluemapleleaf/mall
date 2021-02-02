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
@TableName("ams_cust_changelog")
@ApiModel(value="AmsCustChangelog对象", description="")
public class AmsCustChangelog implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    @ApiModelProperty(value = "C户客户号")
    private String custid;

    @ApiModelProperty(value = "子账户类型	CHANGE|RED|COUPON|INTEGRAL 余额（零钱）|红包|优惠券|积分")
    private String type;

    @ApiModelProperty(value = "变更时间")
    private String changedate;

    @ApiModelProperty(value = "余额变动方向")
    private Integer changedirect;

    @ApiModelProperty(value = "变动额")
    private BigDecimal amont;

    @ApiModelProperty(value = "变动金额")
    private BigDecimal money;


}
