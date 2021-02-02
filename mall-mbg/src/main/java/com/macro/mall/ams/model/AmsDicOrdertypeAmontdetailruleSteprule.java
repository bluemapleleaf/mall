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
@TableName("ams_dic_ordertype_amontdetailrule_steprule")
@ApiModel(value="AmsDicOrdertypeAmontdetailruleSteprule", description="")
public class AmsDicOrdertypeAmontdetailruleSteprule implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    @ApiModelProperty(value = "发生额分解规则id")
    private String amontruleid;

    @ApiModelProperty(value = "开始额")
    private BigDecimal beginamont;

    @ApiModelProperty(value = "结束额")
    private BigDecimal endamont;

    @ApiModelProperty(value = "计算方式")
    private Integer calculatetype;

    @ApiModelProperty(value = "计算参数")
    private BigDecimal param;

    @ApiModelProperty(value = "创建时间")
    private String createdate;

    @ApiModelProperty(value = "修改时间")
    private String modifydate;

    @ApiModelProperty(value = "创建人")
    private String userid;


}
