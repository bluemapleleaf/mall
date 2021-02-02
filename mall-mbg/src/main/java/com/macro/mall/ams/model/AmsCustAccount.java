package com.macro.mall.ams.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.List;

import io.lettuce.core.dynamic.annotation.Key;
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
 * @since 2021-01-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ams_cust_account")
@ApiModel(value = "AmsCustAccount对象", description = "")
public class AmsCustAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "C户客户号")
    @TableId(type = IdType.INPUT)
    private String custid;

    @ApiModelProperty(value = "用户id	Foreignkey  t_user(id)")
    private String userid;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "姓名	预留实名验证使用")
    private String name;

    @ApiModelProperty(value = "证件类型	预留实名验证使用")
    private String certtype;

    @ApiModelProperty(value = "证件号码	预留实名验证使用")
    private String certno;

    @ApiModelProperty(value = "状态	激活|禁用等")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private String createdate;

    @ApiModelProperty(value = "渠道	C户注册渠道")
    private String channel;

    @ApiModelProperty(value = "电话号码")
    private String telephone;

    @ApiModelProperty(value = "支付密码")
    private String password;

    @ApiModelProperty(value = "编辑人id")
    private String edituserid;

    @ApiModelProperty(value = "授权人id")
    private String authuserid;

    //-------新增字段---------------
    @ApiModelProperty(value = "新支付密码")
    @TableField(exist = false)
    private String passwordnew;

    @ApiModelProperty(value = "登录密码")
    @TableField(exist = false)
    private String loginpwd;

    @ApiModelProperty(value = "新登录密码")
    @TableField(exist = false)
    private String loginpwdnew;

    @ApiModelProperty(value = "新电话号码")
    @TableField(exist = false)
    private String telephonenew;

    @ApiModelProperty(value = "子账号信息")
    @TableField(exist = false)
    private List<AmsCustSubaccount> subaccount;

}
