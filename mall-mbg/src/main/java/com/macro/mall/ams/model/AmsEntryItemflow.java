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
 * @since 2021-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ams_entry_itemflow")
@ApiModel(value="AmsEntryItemflow", description="")
public class AmsEntryItemflow implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    @ApiModelProperty(value = "交易订单号")
    private String orderno;

    @ApiModelProperty(value = "B户客户号")
    private String businessid;

    @ApiModelProperty(value = "科目第一级name")
    private String accountitemfirstname;

    @ApiModelProperty(value = "科目第一级code")
    private String accountitemfirstcode;

    @ApiModelProperty(value = "科目第二级name")
    private String accountitemsecdname;

    @ApiModelProperty(value = "科目第二级code")
    private String accountitemsecdcode;

    @ApiModelProperty(value = "科目第三级name")
    private String accountitemthirdname;

    @ApiModelProperty(value = "科目第三级code")
    private String accountitemthirdcode;

    @ApiModelProperty(value = "科目第四级name")
    private String accountsubitemname;

    @ApiModelProperty(value = "科目第四级code")
    private String accountsubitemcode;

    @ApiModelProperty(value = "科目类型")
    private Integer type;

    @ApiModelProperty(value = "余额方向")
    private Integer balancedirect;

    @ApiModelProperty(value = "发生额")
    private BigDecimal amont;

    @ApiModelProperty(value = "会计日期")
    private String accountdate;

    @ApiModelProperty(value = "会计出账日期")
    private String statementdate;

    @ApiModelProperty(value = "会计扎账日期")
    private String bindaccountdate;

    @ApiModelProperty(value = "创建时间")
    private String createdate;

    @ApiModelProperty(value = "扩展字段1")
    private String field1;

    @ApiModelProperty(value = "扩展字段2")
    private String field2;

    @ApiModelProperty(value = "扩展字段3")
    private String field3;

    @ApiModelProperty(value = "扩展字段4")
    private String field4;

    @ApiModelProperty(value = "扩展字段5")
    private String field5;

    @ApiModelProperty(value = "扩展字段6")
    private String field6;

    @ApiModelProperty(value = "扩展字段7")
    private String field7;

    @ApiModelProperty(value = "扩展字段8")
    private String field8;

    @ApiModelProperty(value = "扩展字段9")
    private String field9;

    @ApiModelProperty(value = "扩展字段10")
    private String field10;


}
