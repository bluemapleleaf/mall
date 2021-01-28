package com.macro.mall.domain;

import cn.hutool.core.collection.CollectionUtil;
import com.macro.mall.ams.model.AmsCustSubaccount;
import com.macro.mall.enums.AccountTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *  C户账户
 * </p>
 *
 * @author dongjb
 * @since 2021-01-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "CustAccount", description = "C户账户")
@Slf4j
public class CustAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "C户客户号")
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
    private String passwordnew;

    @ApiModelProperty(value = "登录密码")
    private String loginpwd;

    @ApiModelProperty(value = "新登录密码")
    private String loginpwdnew;

    @ApiModelProperty(value = "新电话号码")
    private String telephonenew;

    @ApiModelProperty(value = "子账号信息")
    private List<AmsCustSubaccount> subAccounts;

    /**
     * 根据虚拟账户类型获取子账户对象
     *
     * @param accountTypeEnum 账户类型
     * @return 账户对象
     */
    public AmsCustSubaccount getSubAccountByType(AccountTypeEnum accountTypeEnum) {
        if (CollectionUtil.isNotEmpty(subAccounts)) {
            return subAccounts.stream()
                    .filter(subAccount -> subAccount.getType().equals(accountTypeEnum.getType()))
                    .findFirst()
                    .get();
        } else {
            log.error("找不到子账户：" + accountTypeEnum.getName());
            return null;
        }
    }
}
