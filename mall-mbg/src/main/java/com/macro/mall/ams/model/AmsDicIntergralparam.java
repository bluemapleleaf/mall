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
 * @since 2021-01-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ams_dic_intergralparam")
@ApiModel(value="AmsDicIntergralparam对象", description="")
public class AmsDicIntergralparam implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "参数值")
    private String code;

    @ApiModelProperty(value = "说明")
    private String remark;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "上级名称")
    private String parent;

    @ApiModelProperty(value = "上级ID")
    private String parentid;

    @ApiModelProperty(value = "创建时间")
    private String createdate;

    @ApiModelProperty(value = "修改时间")
    private String modifydate;

    @ApiModelProperty(value = "创建人")
    private String userid;

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
