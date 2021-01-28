package com.macro.mall.controller;

import com.macro.mall.common.api.CommonResult;
import com.macro.mall.domain.result.BackCustomerResult;
import com.macro.mall.domain.order.OrderBalancecash;
import com.macro.mall.service.MoneyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * C户交易接口（后台）
 *
 * @author dongjb
 * @date 2021/01/22
 */

@Slf4j
@RestController
@Api(tags = "CustomerBackController", consumes = "C户交易接口（后台）")
@RequestMapping(value = "/customer/back")
public class CustomerBackController {

    /**
     * C户服务
     */
    @Autowired
    private MoneyService moneyService;

    @ApiOperation(value = "C户余额提现（确认）")
    @RequestMapping(value = "/custCash", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<BackCustomerResult> custCash(@RequestBody OrderBalancecash balanceCashOrder, @RequestParam String userid) {
        BackCustomerResult result = new BackCustomerResult();
        if (balanceCashOrder == null) {
            return CommonResult.failed("data拼写错误!");
        }
        if (StringUtils.isEmpty(balanceCashOrder.getOrderNo())) {
            return CommonResult.failed("账务系统订单号为空");
        }
        balanceCashOrder.setUserid(userid);
        if (moneyService.doBalanceCash(balanceCashOrder)) {
            result.setCustid(balanceCashOrder.getCustid());
            result.setOrderno(balanceCashOrder.getOrderNo());
            result.setAmont(balanceCashOrder.getAmont());
            result.setFinishdate(balanceCashOrder.getLastmodifydate());
            return CommonResult.success(result,"提现成功！");
        } else {
            return CommonResult.failed("提现失败！");
        }
    }

    @ApiOperation(value = "C户余额提现（取消）")
    @RequestMapping(value = "/custCancleCash", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<BackCustomerResult> custCancleCash(@RequestBody OrderBalancecash balanceCashOrder,  @RequestParam String userid) {
        BackCustomerResult result = new BackCustomerResult();
        if (balanceCashOrder == null) {
            return CommonResult.failed("data拼写错误!");
        }
        if (StringUtils.isEmpty(balanceCashOrder.getOrderNo())) {
            return CommonResult.failed("账务系统订单号为空");
        }
        if (StringUtils.isEmpty(balanceCashOrder.getOpinion())) {
            return CommonResult.failed("取消原因不能为空");
        }
        if (moneyService.doCancleBalanceCash(balanceCashOrder.getOrderNo(), balanceCashOrder.getOpinion(), userid)) {
            return CommonResult.success(result, "提现订单取消成功！");
        } else {
            return CommonResult.failed("提现取消失败！");
        }
    }

}
