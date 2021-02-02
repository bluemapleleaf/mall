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
@TableName("ams_cust_bindinfo")
@ApiModel(value="AmsCustBindinfo对象", description="")
public class AmsCustBindinfo implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    private String custid;

    private String type;

    @ApiModelProperty(value = "创建时间")
    private String createdate;

    @ApiModelProperty(value = "开户行")
    private String bank;

    @ApiModelProperty(value = "开户名")
    private String accountname;

    @ApiModelProperty(value = "卡号|微信号|支付宝号")
    private String account;

    @ApiModelProperty(value = "是否默认")
    private Integer isdefault;

    @ApiModelProperty(value = "激活|禁用等")
    private Integer status;

    @ApiModelProperty(value = "是否删除")
    private Integer isdel;

    @ApiModelProperty(value = "渠道")
    private String channel;

    @ApiModelProperty(value = "序号")
    private Integer sort;


}
