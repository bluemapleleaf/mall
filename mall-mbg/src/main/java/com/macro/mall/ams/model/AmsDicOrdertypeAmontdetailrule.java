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
 * @since 2021-01-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ams_dic_ordertype_amontdetailrule")
@ApiModel(value="AmsDicOrdertypeAmontdetailrule", description="")
public class AmsDicOrdertypeAmontdetailrule implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    @ApiModelProperty(value = "订单类型id")
    private String ordertypeid;

    @ApiModelProperty(value = "名称")
    private String name;

    private Integer ruletype;

    @ApiModelProperty(value = "发生额计算方式")
    private Integer calculatetype;

    @ApiModelProperty(value = "计算模式")
    private Integer calculatemode;

    @ApiModelProperty(value = "计算基数类型")
    private Integer basictype;

    @ApiModelProperty(value = "计算参数")
    private BigDecimal param;

    @ApiModelProperty(value = "最小值")
    private BigDecimal min;

    @ApiModelProperty(value = "最大值")
    private BigDecimal max;

    @ApiModelProperty(value = "计算结果类型")
    private Integer decomposetype;

    @ApiModelProperty(value = "小数处理")
    private Integer decimalprocess;

    @ApiModelProperty(value = "计算序号")
    private Integer sort;

    @ApiModelProperty(value = "创建时间")
    private String createdate;

    @ApiModelProperty(value = "修改时间")
    private String modifydate;

    @ApiModelProperty(value = "创建人")
    private String userid;
}
