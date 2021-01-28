package com.macro.mall.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.macro.mall.ams.model.AmsDicOrdertypeAmontdetailruleSteprule;
import com.macro.mall.enums.AmontRuleType;
import com.macro.mall.enums.CalculateModeEnum;
import com.macro.mall.enums.DecimalProcessModeEnum;
import com.macro.mall.enums.DecomposeTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

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
public class DicOrdertypeAmontdetailrule implements Serializable {

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

    /**
     * 新增
     */
    @ApiModelProperty(value = "发生额")
    private BigDecimal amont;

    @ApiModelProperty(value = "计算后余额")
    private BigDecimal balance;

    @ApiModelProperty(value = "阶梯分解规则")
    private List<AmsDicOrdertypeAmontdetailruleSteprule> stepRules;

    public CalculateModeEnum getCalculatemode() {
        return CalculateModeEnum.getEnum(calculatemode);
    }

    public DecomposeTypeEnum getDecomposetype() {
        return DecomposeTypeEnum.getEnum(decomposetype);
    }

}
