package com.macro.mall.controller;

import com.macro.mall.common.api.CommonResult;
import com.macro.mall.domain.BusinessAccount;
import com.macro.mall.domain.order.OrderBase;
import com.macro.mall.domain.order.OrderCurrencypay;
import com.macro.mall.domain.order.OrderCurrencyrecharge;
import com.macro.mall.domain.order.OrderCurrencyrefund;
import com.macro.mall.domain.result.CurrencyResult;
import com.macro.mall.service.BusinessService;
import com.macro.mall.service.CurrencyService;
import com.macro.mall.util.Utility;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * 自有币种交易接口
 *
 * @author dongjb
 * @date 2021/01/23
 */

@Slf4j
@RestController
@Api(tags = "CurrencyController", description = "自有币种交易接口")
@RequestMapping(value = "/currency")
public class CurrencyController {

    /**
     * 自有币种服务
     */
    @Autowired
    CurrencyService currencyService;

    @Autowired
    BusinessService businessService;

    @ApiOperation(value = "申请入账")
    @RequestMapping(value = "/doApplyRecharge", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<CurrencyResult> doApplyRecharge(@RequestBody OrderCurrencyrecharge customCurrencyRechargeOrder, @RequestParam String amont) {
        CurrencyResult result = new CurrencyResult();
        if (StringUtils.isEmpty("amont")) {
            if (!Utility.isAvailableAmont(amont)) {
                return CommonResult.failed("入账额不合法");
            }
        } else {
            return CommonResult.failed("入账额为空");
        }
        if (customCurrencyRechargeOrder == null) {
            CommonResult.failed("data拼写错误!");
        }
        if (StringUtils.isEmpty(customCurrencyRechargeOrder.getBusinesstradeno())) {
            return CommonResult.failed("商户交易号为空");
        }
        if (StringUtils.isEmpty(customCurrencyRechargeOrder.getCustid())) {
            return CommonResult.failed("用户客户号为空");
        }
        int comresult = customCurrencyRechargeOrder.getAmont().compareTo(BigDecimal.ZERO);
        if (comresult < 0) {
            return CommonResult.failed("入账额最低为0.01");
        }
        if (StringUtils.isEmpty(customCurrencyRechargeOrder.getDescription())) {
            return CommonResult.failed("订单描述为空");
        }
        BusinessAccount businessAccount = businessService.findBusinessAccountById(customCurrencyRechargeOrder.getCustid());
        customCurrencyRechargeOrder.setBusinessid(businessAccount.getBusinessid());
        customCurrencyRechargeOrder.setBusinessname(businessAccount.getBusinessname());
        customCurrencyRechargeOrder.setChannel(businessAccount.getChannel());
        if (currencyService.doApplyCurrencyRecharge(customCurrencyRechargeOrder)) {
            result = getResult(customCurrencyRechargeOrder, result);
            result.setCurrencyname(customCurrencyRechargeOrder.getCurrencyname());
            return CommonResult.success(result, "入账申请成功！" );
        } else {
            return CommonResult.failed("入账申请失败！");
        }
    }

    @ApiOperation(value = "确认入账")
    @RequestMapping(value = "/doRecharge", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<CurrencyResult> doRecharge(@RequestBody OrderCurrencyrecharge customCurrencyRechargeOrder) {
        CurrencyResult result  = new CurrencyResult();
        if (customCurrencyRechargeOrder == null) {
            return CommonResult.failed("data拼写错误!");
        }
        if (StringUtils.isEmpty(customCurrencyRechargeOrder.getOrderNo())) {
            return CommonResult.failed("账务系统订单号为空");
        }
        if (currencyService.doCurrencyRecharge(customCurrencyRechargeOrder)) {
            result = getResult(customCurrencyRechargeOrder, result);
            result.setCurrencyname(customCurrencyRechargeOrder.getCurrencyname());
            result.setFinishdate(customCurrencyRechargeOrder.getLastmodifydate());
            return CommonResult.success(result, "入账成功！");
        } else {
            return CommonResult.failed("入账失败！");
        }
    }

    @ApiOperation(value = "取消入账")
    @RequestMapping(value = "/doCancleRecharge", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<CurrencyResult> doCancleRecharge(@RequestBody OrderCurrencyrecharge customCurrencyRechargeOrder) {
        CurrencyResult result = new CurrencyResult();
        if (customCurrencyRechargeOrder == null) {
            return CommonResult.failed("data拼写错误!");
        }
        if (StringUtils.isEmpty(customCurrencyRechargeOrder.getOrderNo())) {
            return CommonResult.failed("账务系统订单号为空");
        }
        if (currencyService.doCancleCurrencyRecharge(customCurrencyRechargeOrder.getOrderNo())) {
            return CommonResult.success(result, "入账订单取消成功！");
        } else {
            return CommonResult.failed("取消入账失败！");
        }
    }

    @ApiOperation(value = "申请消费")
    @RequestMapping(value = "/doApplyPayment", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<CurrencyResult> doApplyPayment(@RequestBody OrderCurrencypay customCurrencyPayOrder, @RequestParam String amont) {
        CurrencyResult result = new CurrencyResult();
        if (StringUtils.isEmpty(amont)) {
            if (!Utility.isAvailableAmont(amont)) {
                return CommonResult.failed("消费额不合法");
            }
        } else {
            return CommonResult.failed("消费额为空");
        }
        if (customCurrencyPayOrder == null) {
            return CommonResult.failed("data拼写错误!");
        }
        if (StringUtils.isEmpty(customCurrencyPayOrder.getBusinesstradeno())) {
            return CommonResult.failed("商户交易号为空");
        }
        if (StringUtils.isEmpty(customCurrencyPayOrder.getCustid())) {
            return CommonResult.failed("用户客户号为空");
        }
        int comresult = customCurrencyPayOrder.getAmont().compareTo(BigDecimal.ZERO);
        if (comresult <= 0) {
            return CommonResult.failed("消费额为零或负数");
        }
        if (StringUtils.isEmpty(customCurrencyPayOrder.getDescription())) {
            return CommonResult.failed("订单描述为空");
        }
        BusinessAccount businessAccount = businessService.findBusinessAccountById(customCurrencyPayOrder.getCustid());
        customCurrencyPayOrder.setBusinessid(businessAccount.getBusinessid());
        customCurrencyPayOrder.setBusinessname(businessAccount.getBusinessname());
        customCurrencyPayOrder.setChannel(businessAccount.getChannel());
        if (currencyService.doApplyCurrencyPayment(customCurrencyPayOrder)) {
            result = getResult(customCurrencyPayOrder, result);
            result.setCurrencyname(customCurrencyPayOrder.getCurrencyname());
            return CommonResult.success(result, "消费申请成功！");
        } else {
            return CommonResult.failed("消费申请失败！");
        }
    }

    @ApiOperation(value = "确认消费")
    @RequestMapping(value = "/doPayment", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<CurrencyResult> doPayment(@RequestBody OrderCurrencypay customCurrencyPayOrder) {
        CurrencyResult result = new CurrencyResult();
        if (customCurrencyPayOrder == null) {
            return CommonResult.failed("data拼写错误!");
        }
        if (StringUtils.isEmpty(customCurrencyPayOrder.getOrderNo())) {
            return CommonResult.failed("账务系统订单号为空");
        }
        if (currencyService.doCurrencyPayment(customCurrencyPayOrder)) {
            result = getResult(customCurrencyPayOrder, result);
            result.setCurrencyname(customCurrencyPayOrder.getCurrencyname());
            result.setFinishdate(customCurrencyPayOrder.getLastmodifydate());
            return CommonResult.success(result, "消费成功！");
        } else {
            return CommonResult.failed("入账失败！");
        }
    }

    @ApiOperation(value = "取消消费")
    @RequestMapping(value = "/doCanclePayment", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<CurrencyResult> doCanclePayment(@RequestBody OrderCurrencypay customCurrencyPayOrder) {
        CurrencyResult result = new CurrencyResult();
        if (customCurrencyPayOrder == null) {
            return CommonResult.failed("data拼写错误!");
        }
        if (StringUtils.isEmpty(customCurrencyPayOrder.getOrderNo())) {
            return CommonResult.failed("账务系统订单号为空");
        }
        if (currencyService.doCancleCurrencyPayment(customCurrencyPayOrder.getOrderNo())) {
            return CommonResult.success(result, "消费订单取消成功！");
        } else {
            return CommonResult.failed("取消消费失败！");
        }
    }

    @ApiOperation(value = "申请退款")
    @RequestMapping(value = "/doApplyRefund", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<CurrencyResult> doApplyRefund(@RequestBody OrderCurrencyrefund customCurrencyRefundOrder, @RequestParam String amont) {
        CurrencyResult result = new CurrencyResult();
        if (StringUtils.isEmpty(amont)) {
            if (!Utility.isAvailableAmont(amont)) {
                return CommonResult.failed("退款额不合法");
            }
        } else {
            return CommonResult.failed("退款额为空");
        }
        if (customCurrencyRefundOrder == null) {
            CommonResult.failed("data拼写错误!");
        }
        if (StringUtils.isEmpty(customCurrencyRefundOrder.getBusinesstradeno())) {
            return CommonResult.failed("商户交易号为空");
        }
        if (StringUtils.isEmpty(customCurrencyRefundOrder.getOrigorderno())) {
            return CommonResult.failed("原消费订单号为空");
        }
        if (StringUtils.isEmpty(customCurrencyRefundOrder.getReason())) {
            CommonResult.failed("退款原因为空");
        }
        int comresult = customCurrencyRefundOrder.getAmont().compareTo(BigDecimal.ZERO);
        if (comresult <= 0) {
            return CommonResult.failed("退款额为零或负数");
        }
        if (StringUtils.isEmpty(customCurrencyRefundOrder.getDescription())) {
            CommonResult.failed("订单描述为空");
        }
        BusinessAccount businessAccount = businessService.findBusinessAccountById(customCurrencyRefundOrder.getCustid());
        customCurrencyRefundOrder.setBusinessid(businessAccount.getBusinessid());
        customCurrencyRefundOrder.setBusinessname(businessAccount.getBusinessname());
        customCurrencyRefundOrder.setChannel(businessAccount.getChannel());
        if (currencyService.doApplyCurrencyRefund(customCurrencyRefundOrder)) {
            result = getResult(customCurrencyRefundOrder, result);
            result.setCurrencyname(customCurrencyRefundOrder.getCurrencyname());
            result.setSuramont(customCurrencyRefundOrder.getSuramont());
            return CommonResult.success(result, "退款申请成功！");
        } else {
            return CommonResult.failed("退款申请失败！");
        }
    }

    @ApiOperation(value = "确认退款")
    @RequestMapping(value = "/doRefund", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<CurrencyResult> doRefund(@RequestBody OrderCurrencyrefund customCurrencyRefundOrder) {
        CurrencyResult result = new CurrencyResult();
        if (customCurrencyRefundOrder == null) {
            return CommonResult.failed("data拼写错误!");
        }
        if (StringUtils.isEmpty(customCurrencyRefundOrder.getOrderNo())) {
            return CommonResult.failed("账务系统订单号为空");
        }
        if (currencyService.doCurrencyRefund(customCurrencyRefundOrder)) {
            result = getResult(customCurrencyRefundOrder,result);
            result.setCurrencyname(customCurrencyRefundOrder.getCurrencyname());
            result.setSuramont(customCurrencyRefundOrder.getSuramont());
            result.setFinishdate(customCurrencyRefundOrder.getLastmodifydate());
            return CommonResult.success(result, "退款成功！");
        } else {
            return CommonResult.failed("退款失败！");
        }
    }

    @ApiOperation(value = "取消退款")
    @RequestMapping(value = "/doCancleRefund", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<CurrencyResult> doCancleRefund(@RequestBody OrderCurrencyrefund customCurrencyRefundOrder) {
        CurrencyResult result = new CurrencyResult();
        if (customCurrencyRefundOrder == null) {
            return CommonResult.failed("data拼写错误!");
        }
        if (StringUtils.isEmpty(customCurrencyRefundOrder.getOrderNo())) {
            return CommonResult.failed("账务系统订单号为空");
        }
        if (currencyService.doCancleCurrencyRefund(customCurrencyRefundOrder.getOrderNo())) {
            return CommonResult.success(result, "退款订单取消成功！");
        } else {
            return CommonResult.failed("取消退款失败！");
        }
    }

    private CurrencyResult getResult(OrderBase order, CurrencyResult result) {
        result.setCustid(order.getCustid());
        result.setOrderno(order.getOrderNo());
        result.setAmont(order.getAmont());
        return result;
    }

}
