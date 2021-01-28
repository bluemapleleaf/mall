package com.macro.mall.domain;

import cn.hutool.core.collection.CollectionUtil;
import com.macro.mall.ams.model.AmsBusinessBindinfo;
import com.macro.mall.ams.model.AmsBusinessSubaccount;
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
 * B户账户
 * </p>
 *
 * @author dongjb
 * @since 2021-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="B户账户", description="B户账户")
@Slf4j
public class BusinessAccount implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    @ApiModelProperty(value = "B户客户号")
    private String businessid;

    @ApiModelProperty(value = "外键：用户id")
    private String userid;

    @ApiModelProperty(value = "B户名称")
    private String businessname;

    @ApiModelProperty(value = "行政区域编码")
    private String regioncode;

    @ApiModelProperty(value = "行政区域名称")
    private String regionname;

    @ApiModelProperty(value = "国家行业分类代码")
    private String industrycode;

    @ApiModelProperty(value = "国家行业分类名称")
    private String industryname;

    @ApiModelProperty(value = "是否运营公司，0-否；1-是")
    private Integer isdefault;

    @ApiModelProperty(value = "营业执照图片")
    private String businesslicense;

    @ApiModelProperty(value = "法人姓名")
    private String name;

    @ApiModelProperty(value = "法人证件类型")
    private String certtype;

    @ApiModelProperty(value = "法人证件号码")
    private String certno;

    @ApiModelProperty(value = "证件图片")
    private String certpicture;

    @ApiModelProperty(value = "预留电话号码")
    private String telephone;

    @ApiModelProperty(value = "对公账户号")
    private String bankaccount;

    @ApiModelProperty(value = "对公账户名称")
    private String accountname;

    @ApiModelProperty(value = "开户行")
    private String bank;

    @ApiModelProperty(value = "创建时间")
    private String createdate;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "C户客户号")
    private String custid;

    @ApiModelProperty(value = "B户安全校验key")
    private String businesskey;

    @ApiModelProperty(value = "B户公钥")
    private String publickey;

    @ApiModelProperty(value = "渠道号")
    private String channel;

    @ApiModelProperty(value = "账户状态")
    private Integer status;

    private String settlementtype;

    private String email;

    @ApiModelProperty(value = "编辑人id")
    private String edituserid;

    @ApiModelProperty(value = "授权人id")
    private String authuserid;

    @ApiModelProperty(value =  "子账户")
    private List<AmsBusinessSubaccount> subAccounts;

    @ApiModelProperty(value =  "绑定信息")
    private List<AmsBusinessBindinfo> bindInfos;

    /**
     * 根据虚拟账户类型获取子账户对象
     *
     * @param accountTypeEnum 账户类型
     * @return 账户对象
     */
    public AmsBusinessSubaccount getSubAccountByType(AccountTypeEnum accountTypeEnum) {
        if(CollectionUtil.isNotEmpty(subAccounts)) {
            return subAccounts.stream()
                    .filter(subAccount -> subAccount.getType().equals(accountTypeEnum.getType()))
                    .findFirst()
                    .get();
        }else {
            log.error("找不到子账户：" + accountTypeEnum.getName());
            return null;
        }
    }

}
