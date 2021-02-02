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
 * @since 2021-01-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ams_customerid_serial")
@ApiModel(value="AmsCustomeridSerial对象", description="")
public class AmsCustomeridSerial implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    @ApiModelProperty(value = "行政区域编码")
    private String regioncode;

    @ApiModelProperty(value = "客户类型(0-C户，1-B户)")
    private Integer custtype;

    @ApiModelProperty(value = "时间戳")
    private String timestamp;

    @ApiModelProperty(value = "当前可用顺序号")
    private Integer number;

    @ApiModelProperty(value = "创建时间")
    private String createdate;

    @ApiModelProperty(value = "修改时间")
    private String modifydate;

    @ApiModelProperty(value = "创建人")
    private String userid;


}
