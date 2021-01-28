package com.macro.mall.controller;

import com.macro.mall.ams.model.AmsCustAccount;
import com.macro.mall.ams.model.AmsCustBindinfo;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.domain.BusinessAccount;
import com.macro.mall.domain.CustAccount;
import com.macro.mall.enums.CBindTypeEnum;
import com.macro.mall.service.BusinessService;
import com.macro.mall.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * C户信息维护
 *
 * @author dongjb
 * @date 2021/01/05
 */

@Slf4j
@RestController
@Api(tags = "CustomerManageController", description = "C户信息维护")
@RequestMapping("/customer/manage")
public class CustomerManageController {
    @Autowired
    BusinessService businessService;

    @Autowired
    CustomerService customerService;

    @ApiOperation(value = "C户添加")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<AmsCustAccount> custRegister(@RequestBody AmsCustAccount custAccount) {
        if (custAccount == null) {
            return CommonResult.failed("data为空!");
        } else {
            if (StringUtils.isEmpty(custAccount.getTelephone())) {
                return CommonResult.failed("手机号为空");
            }
            if (StringUtils.isEmpty(custAccount.getPassword())) {
                return CommonResult.failed("账务系统登录密码为空");
            }
            BusinessAccount businessAccount = businessService.findBusinessAccountById(custAccount.getCustid());
            custAccount.setChannel(businessAccount.getChannel());
            AmsCustAccount resultAccount = customerService.doCreateCustAccount(custAccount);
            CommonResult.success(resultAccount);
        }
        return CommonResult.failed("C户添加失败");
    }

    @ApiOperation(value = "C户手机号变更")
    @RequestMapping(value = "/modifyTelephone", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<Boolean> custTelephoneMod(@RequestBody AmsCustAccount custAccount) {
        if (custAccount == null) {
            return CommonResult.failed("输入数据对象为空!");
        } else {
            if (StringUtils.isEmpty(custAccount.getTelephone())) {
                return CommonResult.failed("原手机号为空");
            }
            if (StringUtils.isEmpty(custAccount.getPassword())) {
                return CommonResult.failed("原登录密码为空");
            }
            if (StringUtils.isEmpty(custAccount.getTelephonenew())) {
                return CommonResult.failed("新手机号为空");
            }
            if (StringUtils.isEmpty(custAccount.getPasswordnew())) {
                return CommonResult.failed("新登录密码为空");
            }
            return CommonResult.success(customerService.doModifyTelephone(custAccount));
        }
    }

    @ApiOperation(value = "C户添加绑定信息")
    @RequestMapping(value = "/addCustBindInfo", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<Boolean> custBindinfoAdd(@RequestBody AmsCustBindinfo custBindinfo) {
        if (custBindinfo == null) {
            CommonResult.failed("输入的数据对象不能为空!");
        } else {
            try {
                CBindTypeEnum cBindTypeEnum = CBindTypeEnum.getEnum(custBindinfo.getType());
                if (cBindTypeEnum.equals(CBindTypeEnum.BankCard.getValue())) {
                    if (StringUtils.isEmpty(custBindinfo.getBank())) {
                        return CommonResult.failed("开户银行为空!");
                    }
                    if (StringUtils.isEmpty(custBindinfo.getAccountname())) {
                        return CommonResult.failed("账户名称为空!");
                    }
                    if (StringUtils.isEmpty(custBindinfo.getAccount())) {
                        return CommonResult.failed("账号为空!");
                    }
                }
            } catch (Exception e) {
                return CommonResult.failed("绑定类型不正确!");
            }
            BusinessAccount businessAccount = businessService.findBusinessAccountById(custBindinfo.getCustid());

            custBindinfo.setChannel(businessAccount.getChannel());
            custBindinfo.setSort(99);
            return CommonResult.success(customerService.doAddCustBindInfo(custBindinfo));
        }
        return CommonResult.success(true,"C户添加绑定信息");
    }

    @ApiOperation(value = "C户绑定信息删除，逻辑删除")
    @RequestMapping(value = "/delCustBinfInfoByIds", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<Boolean> custBindinfoDel(@RequestParam String ids) {
        if (StringUtils.isEmpty(ids)) {
            CommonResult.failed("ids为空!");
        }
        return CommonResult.success(customerService.doDelCustBinfInfo(ids));
    }

    @ApiOperation(value = "C户绑定信息设置为默认")
    @RequestMapping(value = "/setDefaultCustBindInfo", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<Boolean> custBindinfoDefault(@RequestParam AmsCustBindinfo custBindinfo) {
        if (custBindinfo == null) {
            CommonResult.failed("输入的数据对象不能为空!");
        }
        try {
            CBindTypeEnum.getEnum(custBindinfo.getType());
        } catch (Exception e) {
            CommonResult.failed("绑定类型不正确!");
        }
        return CommonResult.success(customerService.setDefaultCustBindInfo(custBindinfo));
    }

    @ApiOperation(value = "设置支付密码")
    @RequestMapping(value = "/setCustPayPasswor", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<Boolean> custPayPwdSet(@RequestBody AmsCustAccount custAccount) {
        if (custAccount == null) {
            return CommonResult.failed("输入的数据对象不能空!");
        }
        if (StringUtils.isEmpty(custAccount.getPassword())) {
            CommonResult.failed("支付密码为空!");
        }
        return CommonResult.success(customerService.doSetCustPayPassword(custAccount));
    }

    @ApiOperation(value = "C户支付密码修改")
    @RequestMapping(value = "/modCustPayPassword", method = RequestMethod.POST)
    private CommonResult<Boolean> custPayPwdMod(@RequestBody CustAccount custAccount) {
        if (custAccount == null) {
            CommonResult.failed("输入的数据对象不能为空!");
        }
        if (StringUtils.isEmpty(custAccount.getPasswordnew())) {
            CommonResult.failed("新支付密码为空");
        }
        return CommonResult.success(customerService.doModCustPayPassword(custAccount));
    }

}
