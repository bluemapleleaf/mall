package com.macro.mall.controller;

import com.macro.mall.common.api.CommonResult;
import com.macro.mall.domain.result.BackInnerResult;
import com.macro.mall.domain.BusinessAccount;
import com.macro.mall.domain.EntryItemParam;
import com.macro.mall.domain.order.OrderBalancesettlement;
import com.macro.mall.domain.order.OrderIntegralsettlement;
import com.macro.mall.domain.order.OrderInternaltrade;
import com.macro.mall.domain.order.OrderNormaltrade;
import com.macro.mall.enums.PayTypeEnum;
import com.macro.mall.enums.ReviewResultEnum;
import com.macro.mall.service.*;
import com.macro.mall.util.Utility;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 内部交易处理（B户处理及特殊交易）
 *
 * @author dongjb
 * @date 2021/01/22
 */
@Slf4j
@RestController
@Api(tags = "InnerBackController", description = "内部交易处理（B户处理及特殊交易）")
@RequestMapping(value = "/inner/back")
public class InnerBackController {

    /**
     * 人民币服务
     */
    @Autowired
    MoneyService moneyService;

    @Autowired
    IntegralService integralService;

    @Autowired
    OrderService orderService;

    @Autowired
    AccountService accountService;

    @Autowired
    BusinessService businessService;

    @ApiOperation(value = "B户余额结算（申请）")
    @RequestMapping(value = "/doApplyBalanceSettlement", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<BackInnerResult> doApplyBalanceSettlement(@RequestBody OrderBalancesettlement balanceSettlementOrder, @RequestParam String amount ) {
        BackInnerResult result = new BackInnerResult();
        if (!StringUtils.isEmpty(amount)) {
            if (!Utility.isAvailableAmont(amount)) {
                return CommonResult.failed("结算金额不合法");
            }
        } else {
            CommonResult.failed("结算金额为空");
        }
        if (balanceSettlementOrder == null) {
            return CommonResult.failed("data拼写错误!");
        }
        if (StringUtils.isEmpty(balanceSettlementOrder.getBusinessid())) {
            return CommonResult.failed("B户客户号为空");
        }
        if (StringUtils.isEmpty(balanceSettlementOrder.getBindinfoid())) {
            CommonResult.failed("B户绑定信息id为空");
        }
        int comresult = balanceSettlementOrder.getAmont().compareTo(BigDecimal.ZERO);
        if (comresult < 0) {
            CommonResult.failed("结算金额最低为0.01元");
        }
        if (StringUtils.isEmpty(balanceSettlementOrder.getRemark())) {
            CommonResult.failed("交易描述为空");
        }
        if (moneyService.doApplyBalanceSettlement(balanceSettlementOrder)) {
            result.setOrderno(balanceSettlementOrder.getOrderNo());
            result.setAmont(balanceSettlementOrder.getAmont());
            return CommonResult.success(result, "结算申请成功！");
        } else {
            return CommonResult.failed("结算申请失败！");
        }
    }

    @ApiOperation(value = "B户余额结算（确认）")
    @RequestMapping(value = "/doBalanceSettlement", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<BackInnerResult> doBalanceSettlement(@RequestBody OrderBalancesettlement balanceSettlementOrder, @RequestParam String userid) {
        BackInnerResult result = new BackInnerResult();
        if (balanceSettlementOrder == null) {
            return CommonResult.failed("输入的参数对象不能为空");
        }
        if (StringUtils.isEmpty(balanceSettlementOrder.getOrderNo())) {
            CommonResult.failed("账务系统订单号为空");
        }
        balanceSettlementOrder.setUserid(userid);
        if (moneyService.doBalanceSettlement(balanceSettlementOrder)) {
            result.setOrderno(balanceSettlementOrder.getOrderNo());
            result.setAmont(balanceSettlementOrder.getAmont());
            result.setFinishdate(balanceSettlementOrder.getLastmodifydate());
            return CommonResult.success(result, "余额结算成功！");
        } else {
            return CommonResult.failed("余额结算失败！");
        }
    }

    @ApiOperation(value = "B户余额结算（取消）")
    @RequestMapping(value = "/doCancleBalanceSettlement", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<BackInnerResult> doCancleBalanceSettlement(@RequestBody OrderBalancesettlement balanceSettlementOrder,  @RequestParam String userid) {
        BackInnerResult result = new BackInnerResult();
        if (balanceSettlementOrder == null) {
            return CommonResult.failed("data拼写错误!");
        }
        if (StringUtils.isEmpty(balanceSettlementOrder.getOrderNo())) {
            return CommonResult.failed("账务系统订单号为空");
        }
        if (StringUtils.isEmpty(balanceSettlementOrder.getOpinion())) {
            return CommonResult.failed("取消原因不能为空");
        }
        if (moneyService.doCancleBalanceSettlement(balanceSettlementOrder.getOrderNo(), balanceSettlementOrder.getOpinion(), userid)) {
            return CommonResult.success(result,"余额结算订单取消成功！");
        } else {
            return  CommonResult.failed("余额结算取消失败！");
        }
    }

    @ApiOperation(value = "B户积分结算（申请）")
    @RequestMapping(value = "/doApplyIntegralSettlement", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<BackInnerResult> doApplyIntegralSettlement(@RequestBody OrderIntegralsettlement integralSettlementOrder, @RequestParam String amount) {
        BackInnerResult result = new BackInnerResult();
        if (!StringUtils.isEmpty(amount)) {
            if (!Utility.isAvailableAmont(amount)) {
                return CommonResult.failed("结算额不合法");
            }
        } else {
            return CommonResult.failed("结算额为空");
        }
        if (integralSettlementOrder == null) {
            return CommonResult.failed("data拼写错误!");
        }
        if (StringUtils.isEmpty(integralSettlementOrder.getBusinessid())) {
            return CommonResult.failed("B户客户号为空");
        }
        if (StringUtils.isEmpty(integralSettlementOrder.getBindinfoid())) {
            return CommonResult.failed("B户绑定信息id为空");
        }
        int comresult = integralSettlementOrder.getAmont().compareTo(BigDecimal.ZERO);
        if (comresult < 0) {
            return CommonResult.failed("结算额最低为0.01元");
        }
        if (StringUtils.isEmpty(integralSettlementOrder.getRemark())) {
            return CommonResult.failed("交易描述为空");
        }
        if (integralService.doApplyIntegralSettlement(integralSettlementOrder)) {
            result.setOrderno(integralSettlementOrder.getOrderNo());
            result.setAmont(integralSettlementOrder.getAmont());
            return CommonResult.success(result, "结算申请成功！");
        } else {
            return CommonResult.failed("结算申请失败！");
        }
    }

    @ApiOperation(value = "B户积分结算（确认）")
    @RequestMapping(value = "/doIntegralSettlement", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<BackInnerResult> doIntegralSettlement(@RequestBody OrderIntegralsettlement integralSettlementOrder, @RequestParam String userid) {
        BackInnerResult result = new BackInnerResult();
        if (integralSettlementOrder == null) {
            return CommonResult.failed("data拼写错误!");
        }
        if (StringUtils.isEmpty(integralSettlementOrder.getOrderNo())) {
            return CommonResult.failed("账务系统订单号为空");
        }
        integralSettlementOrder.setUserid(userid);
        if (integralService.doIntegralSettlement(integralSettlementOrder)) {
            result.setOrderno(integralSettlementOrder.getOrderNo());
            result.setAmont(integralSettlementOrder.getAmont());
            result.setFinishdate(integralSettlementOrder.getLastmodifydate());
            return CommonResult.success(result, "积分结算成功！");
        } else {
            return CommonResult.failed("积分结算失败！");
        }
    }

    @ApiOperation(value = "B户积分结算（取消）")
    @RequestMapping(value = "/doCancleIntegralSettlement", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<BackInnerResult> doCancleIntegralSettlement(@RequestBody OrderIntegralsettlement integralSettlementOrder, @RequestParam String userid) {
        BackInnerResult result = new BackInnerResult();
        if (integralSettlementOrder == null) {
            return CommonResult.failed("data拼写错误!");
        }
        if (StringUtils.isEmpty(integralSettlementOrder.getOrderNo())) {
            return CommonResult.failed("账务系统订单号为空");
        }
        if (StringUtils.isEmpty(integralSettlementOrder.getOpinion())) {
            return CommonResult.failed("取消原因不能为空");
        }
        if (integralService.doCancleIntegralSettlement(integralSettlementOrder.getOrderNo(), integralSettlementOrder.getOpinion(), userid)) {
            return CommonResult.success(result,"积分结算订单取消成功！");
        } else {
            return CommonResult.failed("积分结算取消失败！");
        }
    }

    @ApiOperation(value = "特殊交易（申请）")
    @RequestMapping(value = "/doApplyInternalTrade", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<BackInnerResult> doApplyInternalTrade(@RequestBody OrderInternaltrade internalTradeOrder, @RequestParam String amont) {
        BackInnerResult result = new BackInnerResult();
        if (!StringUtils.isEmpty("amont")) {
            if (!Utility.isAvailableAmont(amont)) {
                return CommonResult.failed("交易额不合法");
            }
        } else {
            return CommonResult.failed("交易额为空");
        }
        if (internalTradeOrder == null) {
            return CommonResult.failed("data拼写错误!");
        }
        if (internalTradeOrder.getOrdertype() == 0) {
            return CommonResult.failed("订单类型为空");
        }
        if (StringUtils.isEmpty(internalTradeOrder.getBusinessid())) {
            return CommonResult.failed("B户客户号为空");
        }
        int comresult = internalTradeOrder.getAmont().compareTo(BigDecimal.ZERO);
        if (comresult <= 0) {
            return CommonResult.failed("交易额为零或负数");
        }
        if (StringUtils.isEmpty(internalTradeOrder.getTradedescription())) {
            return CommonResult.failed("交易描述为空");
        }
        if (accountService.doApplyInternalTrade(internalTradeOrder)) {
            result.setOrderno(internalTradeOrder.getOrderNo());
            result.setAmont(internalTradeOrder.getAmont());
            return CommonResult.success(result, "内部交易申请成功！");
        } else {
            return CommonResult.failed("内部交易申请失败！");
        }
    }

    @ApiOperation(value = "特殊交易（审核）")
    @RequestMapping(value = "/doReviewInternalTrade", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<BackInnerResult> doReviewInternalTrade(@RequestBody OrderInternaltrade internalTradeOrder, @RequestParam String userid) {
        BackInnerResult result = new BackInnerResult();
        if (internalTradeOrder == null) {
            return CommonResult.failed("data拼写错误!");
        }
        if (StringUtils.isEmpty(internalTradeOrder.getOrderNo())) {
            return CommonResult.failed("账务系统订单号为空");
        }
        internalTradeOrder.setUserid(userid);
        if (accountService.doReviewInternalTrade(internalTradeOrder.getOrderNo(), userid, internalTradeOrder.getOpinion(), ReviewResultEnum.getEnum(internalTradeOrder.getReviewresult()), internalTradeOrder.getCheckstatus())) {
            return CommonResult.success(result, "内部交易复核成功！");
        } else {
            return CommonResult.failed("内部交易复核失败！");
        }
    }

    @ApiOperation(value = "特殊交易（确认）")
    @RequestMapping(value = "/doInternalTrade", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<BackInnerResult> doInternalTrade(@RequestBody OrderInternaltrade internalTradeOrder, @RequestParam String userid) {
        BackInnerResult result = new BackInnerResult();
        if (internalTradeOrder == null) {
            return CommonResult.failed("data拼写错误!");
        }
        if (StringUtils.isEmpty(internalTradeOrder.getOrderNo())) {
            return CommonResult.failed("账务系统订单号为空");
        }
        internalTradeOrder.setUserid(userid);
        if (accountService.doInternalTrade(internalTradeOrder)) {
            result.setOrderno(internalTradeOrder.getOrderNo());
            result.setAmont(internalTradeOrder.getAmont());
            result.setFinishdate(internalTradeOrder.getLastmodifydate());
            return CommonResult.success(result, "内部交易成功！");
        } else {
            return CommonResult.failed("内部交易失败！");
        }
    }

    @ApiOperation(value = "特殊交易（取消）")
    @RequestMapping(value = "/doCancleInternalTrade", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<BackInnerResult> doCancleInternalTrade(@RequestBody OrderInternaltrade internalTradeOrder, @RequestParam String userid) {
        BackInnerResult result = new BackInnerResult();
        if (internalTradeOrder == null) {
            return CommonResult.failed("data拼写错误!");
        }
        if (StringUtils.isEmpty(internalTradeOrder.getOrderNo())) {
            CommonResult.failed("账务系统订单号为空");
        }
        if (StringUtils.isEmpty(internalTradeOrder.getOpinion())) {
            return CommonResult.failed("取消原因不能为空");
        }
        if (accountService.doCancleInternalTrade(internalTradeOrder.getOrderNo(), internalTradeOrder.getOpinion(), userid)) {
            return CommonResult.success(result, "内部交易取消成功！");
        } else {
            return CommonResult.failed("内部交易取消失败！");
        }
    }

    @ApiOperation(value = "通用记账")
    @RequestMapping(value = "/doNormal", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<BackInnerResult> doNormal(@RequestBody OrderNormaltrade normalTradeOrder, @RequestParam String amont, @RequestParam List<EntryItemParam> entryItemParams, @RequestParam String customerid) {
        if (StringUtils.isEmpty(amont)) {
            if (!Utility.isAvailableAmont(amont)) {
                return CommonResult.failed("订单金额不合法");
            }
        } else {
            return CommonResult.failed("订单金额为空");
        }
        BackInnerResult result = new BackInnerResult();
        if (normalTradeOrder == null) {
            return CommonResult.failed("data拼写错误!");
        }
        int comresult = normalTradeOrder.getAmont().compareTo(BigDecimal.ZERO);
        if (comresult <= 0) {
            return CommonResult.failed("订单金额为零或负数");
        }
        if (StringUtils.isEmpty(normalTradeOrder.getTradedescription())) {
            return CommonResult.failed("订单描述为空");
        }
        if (CollectionUtils.isEmpty(entryItemParams)) {
            return CommonResult.failed("会计记账参数为空");
        }
        normalTradeOrder.setEntryItemParams(entryItemParams);
        if (normalTradeOrder.getEntryItemParams() == null || normalTradeOrder.getEntryItemParams().isEmpty()) {
            return CommonResult.failed("会计记账参数为空");
        }
        for (EntryItemParam entryItemParam : normalTradeOrder.getEntryItemParams()) {
            int entryamont = entryItemParam.getAmont().compareTo(BigDecimal.ZERO);
            if (entryamont <= 0) {
                return CommonResult.failed("会计记账金额为零或负数");
            }
            if (StringUtils.isEmpty(entryItemParam.getItemcode())) {
                return CommonResult.failed("第三级科目编号为空");
            }
            if (StringUtils.isEmpty(entryItemParam.getSubitemcode())) {
                CommonResult.failed("科目下立子账户序号为空");
            }
            try {
                entryItemParam.getBalanceDirect();
            } catch (Exception e) {
                return CommonResult.failed("会计记账类型不合法");
            }
        }
        BusinessAccount businessAccount = businessService.findBusinessAccountById(customerid);
        normalTradeOrder.setBusinessid(businessAccount.getId());
        normalTradeOrder.setBusinessname(businessAccount.getBusinessname());
        normalTradeOrder.setChannel(businessAccount.getChannel());
        normalTradeOrder.setCustid("");
        normalTradeOrder.setPaytype(PayTypeEnum.otherspay.getName());
        normalTradeOrder.setPaytypename(PayTypeEnum.otherspay.getDescription());
        normalTradeOrder.setDescription("通用记账交易");
        entryItemParams = normalTradeOrder.getEntryItemParams();
        normalTradeOrder.setEntryItemParams(null);
        if (moneyService.doApplyNormalTrade(normalTradeOrder)) {
            normalTradeOrder.setEntryItemParams(entryItemParams);
            if (moneyService.doNormalTrade(normalTradeOrder)) {
                result.setOrderno(normalTradeOrder.getOrderNo());
                result.setFinishdate(normalTradeOrder.getLastmodifydate());
                return CommonResult.success(result, "通用记账成功！");
            } else {
                moneyService.doCancleNormalTrade(normalTradeOrder.getOrderNo());
                return CommonResult.failed("通用记账失败！");
            }
        } else {
            return CommonResult.failed("通用记账失败！");
        }
    }

}
