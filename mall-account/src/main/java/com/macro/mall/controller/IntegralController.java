package com.macro.mall.controller;

import com.macro.mall.common.api.CommonResult;
import com.macro.mall.domain.BusinessAccount;
import com.macro.mall.domain.result.IntegralResult;
import com.macro.mall.domain.order.OrderIntegralpay;
import com.macro.mall.domain.order.OrderIntegralrefund;
import com.macro.mall.enums.AccountTypeEnum;
import com.macro.mall.service.BusinessService;
import com.macro.mall.service.IntegralService;
import com.macro.mall.util.Utility;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * 积分交易接口
 *
 * @author dongjb
 * @date 2021/01/24
 */
@Slf4j
@RestController
@Api(tags = "IntegralController", description = "积分交易接口")
@RequestMapping(value = "/integral")
public class IntegralController {

    /**
     * 积分服务
     */
    @Autowired
    IntegralService integralService;

    @Autowired
    BusinessService businessService;

    @ApiOperation(value = "申请消费")
    @RequestMapping(value = "/doApplyPayment", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<IntegralResult> doApplyPayment(@RequestBody OrderIntegralpay payOrder, @RequestParam String amont) {
        if (StringUtils.isEmpty(amont)) {
            if (!Utility.isAvailableAmont(amont)) {
                return CommonResult.failed("消费额不合法");
            }
        } else {
            return CommonResult.failed("消费额为空");
        }
        IntegralResult result = new IntegralResult();
        if (payOrder == null) {
            return CommonResult.failed("data拼写错误!");
        }
        if (StringUtils.isEmpty(payOrder.getBusinesstradeno())) {
            return CommonResult.failed("商户交易号为空");
        }
        if (StringUtils.isEmpty(payOrder.getCustid())) {
            CommonResult.failed("用户客户号为空");
        }
        int comresult = payOrder.getAmont().compareTo(BigDecimal.ZERO);
        if (comresult <= 0) {
            return CommonResult.failed("消费额为零或负数");
        }
        if (StringUtils.isEmpty(payOrder.getDescription())) {
            CommonResult.failed("订单描述为空");
        }
        BusinessAccount businessAccount = businessService.findBusinessAccountById(payOrder.getCustid());
        payOrder.setBusinessid(businessAccount.getBusinessid());
        payOrder.setBusinessname(businessAccount.getBusinessname());
        payOrder.setBusinesssubaccountcode(businessAccount.getSubAccountByType(AccountTypeEnum.CHANGE).getCode());
        payOrder.setChannel(businessAccount.getChannel());
        if (integralService.doApplyPayment(payOrder)) {
            result.setCustid(payOrder.getCustid());
            result.setOrderno(payOrder.getOrderNo());
            result.setAmont(payOrder.getAmont());
            result.setBeforebalance(payOrder.getBeforebalance());
            result.setAfterbalance(payOrder.getAfterbalance());
            return CommonResult.success(result, "消费申请成功！");
        } else {
            return CommonResult.failed("消费申请失败！");
        }
    }

    @ApiOperation(value = "确认消费")
    @RequestMapping(value = "/doPayment", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<IntegralResult> doPayment(@RequestBody OrderIntegralpay payOrder) {
        IntegralResult result = new IntegralResult();
        if (payOrder == null) {
            return CommonResult.failed("data拼写错误!");
        }
        if (StringUtils.isEmpty(payOrder.getOrderNo())) {
            return CommonResult.failed("账务系统订单号为空");
        }
        if (integralService.doPayment(payOrder)) {
            result.setCustid(payOrder.getCustid());
            result.setOrderno(payOrder.getOrderNo());
            result.setAmont(payOrder.getAmont());
            result.setFinishdate(payOrder.getLastmodifydate());
            return CommonResult.success(result, "消费成功！");
        } else {
            return CommonResult.failed("消费失败！");
        }
    }

    @ApiOperation(value = "取消消费")
    @RequestMapping(value = "/doCanclePayment", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<IntegralResult> doCanclePayment(@RequestBody OrderIntegralpay payOrder) {
        IntegralResult result = new IntegralResult();
        if (payOrder == null) {
            return CommonResult.failed("data拼写错误!");
        }
        if (StringUtils.isEmpty(payOrder.getOrderNo())) {
            return CommonResult.failed("账务系统订单号为空");
        }
        if (integralService.doCanclePayment(payOrder.getOrderNo())) {
            return CommonResult.failed("消费订单取消成功！");
        } else {
            return CommonResult.failed("支付取消失败！");
        }
    }

    @ApiOperation(value = "申请退款")
    @RequestMapping(value = "/doApplyRefund", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<IntegralResult> doApplyRefund(@RequestBody OrderIntegralrefund refundOrder) {
        IntegralResult result = new IntegralResult();
        if (refundOrder == null) {
            return CommonResult.failed("data拼写错误!");
        }
        if (StringUtils.isEmpty(refundOrder.getOrigorderno())) {
            return CommonResult.failed("原消费订单号为空");
        }
        if (StringUtils.isEmpty(refundOrder.getBusinesstradeno())) {
            return  CommonResult.failed("商户交易号为空");
        }
        int comresult = refundOrder.getAmont().compareTo(BigDecimal.ZERO);
        if (comresult <= 0) {
            return CommonResult.failed("退款额为零或负数");
        }
        if (StringUtils.isEmpty(refundOrder.getReason())) {
            return CommonResult.failed("退款原因为空");
        }
        BusinessAccount businessAccount = businessService.findBusinessAccountById(refundOrder.getCustid());
        refundOrder.setBusinessid(businessAccount.getBusinessid());
        if (integralService.doApplyRefund(refundOrder)) {
            result.setCustid(refundOrder.getCustid());
            result.setOrderno(refundOrder.getOrderNo());
            result.setAmont(refundOrder.getAmont());
            return CommonResult.success(result, "退款申请成功！");
        } else {
            return CommonResult.failed("退款申请失败！");
        }
    }

    @ApiOperation(value = "确认退款")
    @RequestMapping(value = "/doRefund", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<IntegralResult> doRefund(@RequestBody OrderIntegralrefund refundOrder) {
        IntegralResult result = new IntegralResult();
        if (refundOrder == null) {
            return CommonResult.failed("data拼写错误!");
        }
        if (StringUtils.isEmpty(refundOrder.getOrderNo())) {
            return CommonResult.failed("账务系统订单号为空");
        }
        if (integralService.doRefund(refundOrder)) {
            result.setCustid(refundOrder.getCustid());
            result.setOrderno(refundOrder.getOrderNo());
            result.setAmont(refundOrder.getAmont());
            result.setBeforebalance(refundOrder.getBeforebalance());
            result.setAfterbalance(refundOrder.getAfterbalance());
            result.setFinishdate(refundOrder.getLastmodifydate());
            return CommonResult.success(result, "退款成功！");
        } else {
            return CommonResult.failed("退款失败！");
        }
    }

    @ApiOperation(value = "取消退款")
    @RequestMapping(value = "/doRdoCancleRefundefund", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<IntegralResult> doCancleRefund(@RequestBody OrderIntegralrefund refundOrder) {
        IntegralResult result = new IntegralResult();
        if (refundOrder == null) {
            return CommonResult.failed("data拼写错误!");
        }
        if (StringUtils.isEmpty(refundOrder.getOrderNo())) {
            return CommonResult.failed("账务系统订单号为空");
        }
        if (integralService.doCancleRefund(refundOrder.getOrderNo())) {
            return CommonResult.success(result,"退款订单取消成功！");
        } else {
            return CommonResult.failed("退款取消失败！");
        }
    }

}
