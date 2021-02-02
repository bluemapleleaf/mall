package com.macro.mall.ams.model;

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
@TableName("ams_dic_ordertype_balancerule")
@ApiModel(value="AmsDicOrdertypeBalancerule对象", description="")
public class AmsDicOrdertypeBalancerule implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    @ApiModelProperty(value = "订单类型id")
    private String ordertypeid;

    @ApiModelProperty(value = "订单原状态")
    private Integer oldstatus;

    @ApiModelProperty(value = "订单目标态")
    private Integer newstatus;

    @ApiModelProperty(value = "变更账户类型")
    private Integer type;

    @ApiModelProperty(value = "B户客户号")
    private String businessid;

    @ApiModelProperty(value = "发生额分解规则id")
    private String amontruleid;

    @ApiModelProperty(value = "虚拟账户类型编码")
    private String accounttype;

    @ApiModelProperty(value = "余额变动方向")
    private Integer balancedirect;

    @ApiModelProperty(value = "创建时间")
    private String createdate;

    @ApiModelProperty(value = "修改时间")
    private String modifydate;

    @ApiModelProperty(value = "创建人")
    private String userid;

    private String field1;

    private String field2;

    private String field3;

    private String field4;

    private String field5;


}
