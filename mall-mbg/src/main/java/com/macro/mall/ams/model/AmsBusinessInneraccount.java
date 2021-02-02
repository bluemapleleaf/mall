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
@TableName("ams_business_inneraccount")
@ApiModel(value="AmsBusinessInneraccount对象", description="")
public class AmsBusinessInneraccount implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    @ApiModelProperty(value = "B户客户号")
    private String businessid;

    @ApiModelProperty(value = "内部账号")
    private String innerid;

    @ApiModelProperty(value = "科目名称")
    private String accountitemfirstname;

    @ApiModelProperty(value = "科目编码")
    private String accountitemfirstcode;

    @ApiModelProperty(value = "科目名称")
    private String accountitemsecdname;

    @ApiModelProperty(value = "科目编码")
    private String accountitemsecdcode;

    @ApiModelProperty(value = "科目名称")
    private String accountitemthirdname;

    @ApiModelProperty(value = "科目编码")
    private String accountitemthirdcode;

    @ApiModelProperty(value = "科目名称")
    private String accountsubitemname;

    @ApiModelProperty(value = "科目编码")
    private String accountsubitemcode;

    @ApiModelProperty(value = "内部账户类型")
    private Integer type;

    @ApiModelProperty(value = "账户余额")
    private BigDecimal balance;

    @ApiModelProperty(value = "更新时统计的分录会计日期")
    private String accountdate;

    @ApiModelProperty(value = "创建时间")
    private String createdate;

    @ApiModelProperty(value = "更新时间")
    private String updatedate;


}
