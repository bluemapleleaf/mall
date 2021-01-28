package com.macro.mall.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 运营B户收款账户信息
 *
 * @author dongjb
 * @date 2021/01/08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="OperBusinessAccount", description="运营B户收款账户信息")
public class OperBusinessAccount {

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "卡号")
    private String cardno;

    @ApiModelProperty(value = "电话号码")
    private String telephone;

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

    @ApiModelProperty(value = "bundleid")
    private String bundleid;

    @ApiModelProperty(value = "包名")
    private String packages;

    @ApiModelProperty(value = "支付宝email")
    private String sellerEmail;

    @ApiModelProperty(value = "支付宝id")
    private String sellerId;

    @ApiModelProperty(value = "支付宝RSA私钥")
    private String privateKey;
}
