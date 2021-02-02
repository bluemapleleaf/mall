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
 * @since 2021-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ams_business_channel")
@ApiModel(value="AmsBusinessChannel对象", description="")
public class AmsBusinessChannel implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    @ApiModelProperty(value = "行政区域编码")
    private String regioncode;

    @ApiModelProperty(value = "国家行业分类代码")
    private String industrycode;

    @ApiModelProperty(value = "当前可用顺序号")
    private Integer number;

    @ApiModelProperty(value = "创建时间")
    private String createdate;

    @ApiModelProperty(value = "修改时间")
    private String modifydate;

    @ApiModelProperty(value = "创建人")
    private String userid;


}
