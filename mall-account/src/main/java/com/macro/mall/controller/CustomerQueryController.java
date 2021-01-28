package com.macro.mall.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.macro.mall.ams.model.*;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.domain.CustAccount;
import com.macro.mall.domain.OperBusinessAccount;
import com.macro.mall.enums.BBindTypeEnum;
import com.macro.mall.enums.DeleteEnum;
import com.macro.mall.enums.StatusEnum;
import com.macro.mall.service.BusinessService;
import com.macro.mall.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * C户信息查询服务
 *
 * @author dongjb
 * @date 20210105
 */

@Slf4j
@RestController
@Api(tags = "CustomerQueryController", description = "C户信息查询服务")
@RequestMapping("/customer/query")
public class CustomerQueryController {

    /**
     * C户服务
     */
    @Autowired
    CustomerService customerService;

    @Autowired
    BusinessService businessService;

    @ApiOperation(value = "通过C户客户号查询C户信息")
    @RequestMapping(value = "/findByCustId/{custid}", method = RequestMethod.GET)
    @ResponseBody
    private CommonResult<CustAccount> custDescCustid(@PathVariable String custid) {
        CustAccount custAccount = customerService.findCustAccountByCustId(custid);
        return CommonResult.success(custAccount);
    }

    @ApiOperation(value = "根据C户电话号码获取C户信息")
    @RequestMapping(value = "/findByTelephone/{telephone}", method = RequestMethod.GET)
    @ResponseBody
    private CommonResult<CustAccount> custDescTelephone(@PathVariable String telephone) {
        CustAccount custAccount = customerService.findCustAccountByTelephone(telephone);
        return CommonResult.success(custAccount);
    }

    @ApiOperation(value = "通过C户客户号获取可用的C户绑定信息")
    @RequestMapping(value = "/findBindInfoByCustId/{custid}")
    @ResponseBody
    private CommonResult<List<AmsCustBindinfo>> custDescBindinfo(@PathVariable String custid) {
        List<AmsCustBindinfo> bindInfos = customerService.findCustBindInfoByCustId(custid);
        if (CollectionUtil.isNotEmpty(bindInfos)) {
            List<AmsCustBindinfo> results = bindInfos.stream()
                    .filter(bindInfo -> bindInfo.getIsdel().equals(DeleteEnum.NOTDELETE.getValue())
                            && bindInfo.getStatus().equals(StatusEnum.ACTIVATE.getValue()))
                    .collect(Collectors.toList());
            return CommonResult.success(results);
        }
        return CommonResult.failed("没有绑定的信息");
    }

    @ApiOperation(value = "获取可用的C户绑定类型")
    @RequestMapping(value = "/getAllCBindtype", method = RequestMethod.GET)
    @ResponseBody
    private CommonResult<List<AmsDicCBindtype>> custBindinfoType() {
        return CommonResult.success(customerService.getallcbindtype());
    }

    @ApiOperation(value = "检测是否需要设置支付密码")
    @RequestMapping(value = "/payPwdCheck/{custid}", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<Boolean> custPayPwdCheck(@PathVariable String custid) {
        CustAccount custAccount = customerService.findCustAccountByCustId(custid);
        if (custAccount == null) {
            return CommonResult.failed("用户不存在！");
        }
        String password = custAccount.getPassword();
        if (StringUtils.isNotEmpty(password)) {
            return CommonResult.success(false, "支付密码已设置");
        } else {
            return CommonResult.success(true, "支付密码未设置");
        }
    }

    @ApiOperation(value = "验证支付密码")
    @RequestMapping(value = "/payPwdValidate", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<Boolean> custPayPwdValidate(@RequestBody CustAccount custAccount) {
        if (custAccount == null) {
            CommonResult.failed("输入参数对象不能为空");
        } else {
            if (StringUtils.isEmpty(custAccount.getCustid())) {
                CommonResult.failed("C户客户号为空");
            }

            if (StringUtils.isEmpty(custAccount.getPassword())) {
                CommonResult.failed("支付密码为空");
            }
        }

        return CommonResult.success(customerService.doValidatePayPassword(custAccount));
    }

    @ApiOperation(value = "获取可用的B户收款账号类型")
    @RequestMapping(value = "/getBusinessReceiptType", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<List<AmsDicBBindtype>> custPayGetBusinessReceiptType() {
        List<AmsDicBBindtype> dictionaries = businessService.findOperBusinessBindInfoType();
        return CommonResult.success(dictionaries);
    }


    @ApiOperation(value = "获取运营方收款账号信息")
    @RequestMapping(value = "/getReceiptAccount/{accounttype}", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<OperBusinessAccount> custPayGetReceiptAccount(@PathVariable String accounttype) {
        AmsBusinessBindinfo businessBindInfo = businessService.findOperBusinessBindInfo(accounttype);
        if (businessBindInfo == null) {
            CommonResult.failed("找不到可用的收款账户信息");
        }
        OperBusinessAccount yyBusinessAccount = new OperBusinessAccount();
        try {
            switch (BBindTypeEnum.getEnum(Objects.requireNonNull(businessBindInfo).getType())) {
                case BankCard:
                    yyBusinessAccount.setName(businessBindInfo.getName());
                    yyBusinessAccount.setCardno(businessBindInfo.getAccount());
                    yyBusinessAccount.setTelephone(businessBindInfo.getTelephone());
                    break;
                case WeiXinOpen:
                case WeiXinPublic:
                    yyBusinessAccount.setPartner(businessBindInfo.getPartner());
                    yyBusinessAccount.setPartnerkey(businessBindInfo.getPartnerkey());
                    yyBusinessAccount.setAppid(businessBindInfo.getAppid());
                    yyBusinessAccount.setAppsecret(businessBindInfo.getAppsecret());
                    yyBusinessAccount.setAppsingn(businessBindInfo.getAppsingn());
                    yyBusinessAccount.setBundleid(businessBindInfo.getBundleid());
                    yyBusinessAccount.setPackages(businessBindInfo.getPackagename());
                    break;
                case AliPay:
                    yyBusinessAccount.setPartner(businessBindInfo.getPartner());
                    yyBusinessAccount.setPartnerkey(businessBindInfo.getPartnerkey());
                    yyBusinessAccount.setSellerEmail(businessBindInfo.getSellerEmail());
                    yyBusinessAccount.setSellerId(businessBindInfo.getSellerId());
                    yyBusinessAccount.setPrivateKey(businessBindInfo.getPrivateKey());
                    break;
                default:
                    return CommonResult.failed("找不到可用的收款账户信息！");
            }
        } catch (Exception e) {
            return CommonResult.failed("找不到可用的收款账户信息！");
        }
        return null;
    }

}
