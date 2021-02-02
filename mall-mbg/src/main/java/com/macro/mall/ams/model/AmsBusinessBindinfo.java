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
@TableName("ams_business_bindinfo")
@ApiModel(value="AmsBusinessBindinfo对象", description="")
public class AmsBusinessBindinfo implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    @ApiModelProperty(value = "外键：B户客户号")
    private String businessid;

    @ApiModelProperty(value = "绑定类型")
    private String type;

    @ApiModelProperty(value = "是否默认，0-否；1-是")
    private Integer isdefault;

    @ApiModelProperty(value = "创建时间")
    private String createdate;

    @ApiModelProperty(value = "账户状态")
    private Integer status;

    private String bankname;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "电话号码")
    private String telephone;

    @ApiModelProperty(value = "卡号")
    private String account;

    @ApiModelProperty(value = "证件类型")
    private String certtype;

    @ApiModelProperty(value = "证件号码")
    private String certno;

    @ApiModelProperty(value = "商户号")
    private String partner;

    @ApiModelProperty(value = "商户key")
    private String partnerkey;

    @ApiModelProperty(value = "应用id")
    private String appid;

    @ApiModelProperty(value = "应用密钥")
    private String appsecret;

    @ApiModelProperty(value = "应用签名")
    private String appsingn;

    private String bundleid;

    @ApiModelProperty(value = "包名")
    private String packagename;

    @ApiModelProperty(value = "支付宝email")
    private String sellerEmail;

    @ApiModelProperty(value = "支付宝id")
    private String sellerId;

    @ApiModelProperty(value = "支付宝RSA私钥")
    private String privateKey;

    @ApiModelProperty(value = "编辑人id")
    private String edituserid;

    @ApiModelProperty(value = "授权人id")
    private String authuserid;


}
