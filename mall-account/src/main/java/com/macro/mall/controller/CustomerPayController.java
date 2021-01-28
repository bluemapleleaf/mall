package com.macro.mall.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.macro.mall.ams.model.*;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.domain.BusinessAccount;
import com.macro.mall.domain.result.CustomerPayResult;
import com.macro.mall.domain.order.*;
import com.macro.mall.enums.AccountTypeEnum;
import com.macro.mall.enums.CBindTypeEnum;
import com.macro.mall.enums.PayTypeEnum;
import com.macro.mall.service.BusinessService;
import com.macro.mall.service.MoneyService;
import com.macro.mall.util.Utility;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * C户信息维护、交易服务
 * 人民币服务
 *
 * @author dongjb
 * @date 2021/01/13
 */

@RestController
@Api(tags = "CustomerPayController", description = "C户信息维护、交易服务")
@RequestMapping("/customer/pay")
public class CustomerPayController {

    @Autowired
    BusinessService businessService;

    @Autowired
    MoneyService moneyService;

    @ApiOperation(value = "C户充值入账")
    @RequestMapping(value = "/custPayRecharge", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<CustomerPayResult> custPayRecharge(@RequestBody OrderBalancerecharge orderBalancerecharge) {
        CustomerPayResult result = new CustomerPayResult();
        if (orderBalancerecharge == null) {
            return CommonResult.failed("输入的参数对象不能为空!");
        }
        if (orderBalancerecharge.getAmont() != null) {
            if (!Utility.isAvailableAmont(orderBalancerecharge.getAmont().toString())) {
                return CommonResult.failed("充值金额不合法");
            }
        } else {
            return CommonResult.failed("充值金额为空");
        }
        if(StringUtils.isEmpty(orderBalancerecharge.getBusinesstradeno())) {
            return CommonResult.failed("商户交易号为空");
        }
        if(StringUtils.isEmpty(orderBalancerecharge.getCustid())) {
            return CommonResult.failed("用户客户号为空");
        }
        if(StringUtils.isEmpty(orderBalancerecharge.getPaytype())) {
            return CommonResult.failed("支付类型为空");
        }
        int comresult = orderBalancerecharge.getAmont().compareTo(BigDecimal.valueOf(0.01D));
        if (comresult < 0) {
            return CommonResult.failed("充值金额最低为0.01元");
        }
        if(StringUtils.isEmpty(orderBalancerecharge.getTradeno())) {
            return CommonResult.failed("第三方交易号为空");
        }
        if(StringUtils.isEmpty(orderBalancerecharge.getTradestatus())) {
            return CommonResult.failed("第三方交易状态为空");
        }
        if(StringUtils.isEmpty(orderBalancerecharge.getBuyerid())) {
            return CommonResult.failed("购买人第三方id为空");
        }
        if(StringUtils.isEmpty(orderBalancerecharge.getBuyername())) {
            return CommonResult.failed("购买人银行卡开户名称或第三方账户名称为空");
        }
        if(StringUtils.isEmpty(orderBalancerecharge.getBuyeraccount())) {
            return CommonResult.failed("购买人第三方账号名为空");
        }

        BusinessAccount businessAccount = businessService.findBusinessAccountById(orderBalancerecharge.getBusinessid());
        orderBalancerecharge.setChannel(businessAccount.getChannel());
        if (moneyService.doApplyRecharge(orderBalancerecharge)) {
            BigDecimal[] balances = moneyService.doRecharge(orderBalancerecharge);
            if (balances.length == 2) {
                result.setTotalbalance(balances[0]);
                result.setAvailablebalance(balances[1]);
                return CommonResult.success(result, "充值入账失败！");
            } else {
                moneyService.doCancleRecharge(orderBalancerecharge.getOrderNo());
                return CommonResult.failed("充值入账失败！");
            }
        } else {
            return CommonResult.failed("充值入账失败！");
        }
    }

    @ApiOperation(value = "C户支付给B户（申请）")
    @RequestMapping(value = "/custApplyPayPayment", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<CustomerPayResult> custApplyPayPayment(@RequestBody Object data, @RequestParam String payType, @RequestParam String amount) {
        if(StringUtils.isEmpty(payType))  {
            return CommonResult.failed("支付类型为空");
        }
        if (StringUtils.isEmpty(amount)) {
            if (!Utility.isAvailableAmont(amount)) {
                return CommonResult.failed("支付金额不合法");
            }
        } else {
            return CommonResult.failed("支付金额为空");
        }

        PayTypeEnum payTypeEnum = PayTypeEnum.getEnum(payType);
        switch (payTypeEnum) {
            case balance:
                return doApplyBalancePay(data);
            default:
                return doApplyNonBalancePay(data);
        }
    }

    @ApiOperation(value = "C户支付给B户（确认）")
    @RequestMapping(value = "/custPayPayment", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<CustomerPayResult> custPayPayment(@RequestBody Object data, @RequestParam String payType) {
        if(StringUtils.isEmpty(payType))  {
            return CommonResult.failed("支付类型为空");
        }

        PayTypeEnum payTypeEnum = PayTypeEnum.getEnum(payType);
        switch (payTypeEnum) {
            case balance:
                return doBalancePay(data);
            default:
                return doNonBalancePay(data);
        }
    }

    @ApiOperation(value = "C户支付给B户（取消）")
    @RequestMapping(value = "/custCanclePayPayment", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<CustomerPayResult> custCanclePayPayment(@RequestBody Object data, @RequestParam String payType) {
        if(StringUtils.isEmpty(payType))  {
            return CommonResult.failed("支付类型为空");
        }

        PayTypeEnum payTypeEnum = PayTypeEnum.getEnum(payType);
        switch (payTypeEnum) {
            case balance:
                return doCancleBalancePay(data);
            default:
                return doCancleNonBalancePay(data);
        }
    }

    @ApiOperation(value = "C户退款（申请）")
    @RequestMapping(value = "/custApplyPayRefund", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<CustomerPayResult> custApplyPayRefund(@RequestBody Object data, @RequestParam String payType, @RequestParam String amount) {
        if(StringUtils.isEmpty(payType))  {
            return CommonResult.failed("支付类型为空");
        }
        if(StringUtils.isEmpty(amount)) {
            if (!Utility.isAvailableAmont(amount)) {
                return CommonResult.failed("支付金额不合法");
            }
        } else {
            return CommonResult.failed("支付金额为空");
        }

        PayTypeEnum payTypeEnum = PayTypeEnum.getEnum(payType);
        switch (payTypeEnum) {
            case balance:
                return doApplyBalancePayRefund(data);
            default:
                return doApplyNonBalancePayRefund(data);
        }
    }

    @ApiOperation(value = "C户退款（确认）")
    @RequestMapping(value = "/custPayRefund", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<CustomerPayResult> custPayRefund(@RequestBody Object data, @RequestParam String payType) {
        if(StringUtils.isEmpty(payType))  {
            return CommonResult.failed("支付类型为空");
        }
        PayTypeEnum payTypeEnum = PayTypeEnum.getEnum(payType);
        switch (payTypeEnum) {
            case balance:
                return doBalancePayRefund(data);
            default:
                return doNonBalancePayRefund(data);
        }
    }

    @ApiOperation(value = "C户退款（取消）")
    @RequestMapping(value = "/custCanclePayRefund", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<CustomerPayResult> custCanclePayRefund(@RequestBody Object data, @RequestParam String payType) {
        if(StringUtils.isEmpty(payType))  {
            return CommonResult.failed("支付类型为空");
        }
        PayTypeEnum payTypeEnum = PayTypeEnum.getEnum(payType);
        switch (payTypeEnum) {
            case balance:
                return doCancleBalancePayRefund(data);
            default:
                return doCancleNonBalancePayRefund(data);
        }
    }

    @ApiOperation(value = "C户转账（申请）")
    @RequestMapping(value = "/custApplyTransfer", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<CustomerPayResult> custApplyTransfer(@RequestBody Object data, @RequestParam String payType) {
        if(StringUtils.isEmpty(payType))  {
            return CommonResult.failed("支付类型为空");
        }
        PayTypeEnum payTypeEnum = PayTypeEnum.getEnum(payType);
        switch (payTypeEnum) {
            case balance:
                return doApplyBalanceTransfer(data);
            default:
                return doApplyNonBalanceTransfer(data);
        }
    }

    @ApiOperation(value = "C户转账（确认）")
    @RequestMapping(value = "/custTransfer", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<CustomerPayResult> custTransfer(@RequestBody Object data, @RequestParam String payType) {
        if(StringUtils.isEmpty(payType))  {
            return CommonResult.failed("支付类型为空");
        }
        PayTypeEnum payTypeEnum = PayTypeEnum.getEnum(payType);
        switch (payTypeEnum) {
            case balance:
                return doBalanceTransfer(data);
            default:
                return doNonBalanceTransfer(data);
        }
    }

    @ApiOperation(value = "C户转账（取消）")
    @RequestMapping(value = "/custCancleTransfer", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<CustomerPayResult> custCancleTransfer(@RequestBody Object data, @RequestParam String payType) {
        if(StringUtils.isEmpty(payType))  {
            return CommonResult.failed("支付类型为空");
        }
        PayTypeEnum payTypeEnum = PayTypeEnum.getEnum(payType);
        switch (payTypeEnum) {
            case balance:
                return doCancleBalanceTransfer(data);
            default:
                return doCancleNonBalanceTransfer(data);
        }
    }

    @ApiOperation(value = "C户余额提现（申请）")
    @RequestMapping(value = "/custApplyCash", method = RequestMethod.POST)
    @ResponseBody
    private CommonResult<CustomerPayResult> custApplyCash(@RequestBody Object data, @RequestParam String amount) {
        CustomerPayResult result = new CustomerPayResult() ;
        OrderBalancecash balanceCashOrder =(OrderBalancecash)data;

        if (StringUtils.isEmpty(amount)) {
            if (!Utility.isAvailableAmont(amount)) {
                return CommonResult.failed("支付金额不合法");
            }
        } else {
            return CommonResult.failed("支付金额为空");
        }
        if(StringUtils.isEmpty(balanceCashOrder.getBusinesstradeno())) {
            return CommonResult.failed("商户交易号为空");
        }
        if(StringUtils.isEmpty(balanceCashOrder.getCustid())) {
            return CommonResult.failed("用户客户号为空");
        }
        int comresult = balanceCashOrder.getAmont().compareTo(BigDecimal.valueOf(0.01D));
        if (comresult < 0) {
            return CommonResult.failed("提现金额最低为0.01元");
        }
        if(StringUtils.isEmpty(balanceCashOrder.getBindinfoid())) {
            return CommonResult.failed("客户提现账户为空");
        }
        if(StringUtils.isEmpty(balanceCashOrder.getPassword())) {
            return CommonResult.failed("支付密码为空");
        }

        BusinessAccount businessAccount = businessService.findBusinessAccountById(balanceCashOrder.getBusinessid());
        balanceCashOrder.setChannel(businessAccount.getChannel());
        if (moneyService.doApplyBalanceCash(balanceCashOrder)) {
            result.setCustid(balanceCashOrder.getCustid());
            result.setOrderno(balanceCashOrder.getOrderNo());
            result.setAmont(balanceCashOrder.getAmont());
            result.setBeforebalance(balanceCashOrder.getBeforebalance());
            result.setAfterbalance(balanceCashOrder.getAfterbalance());
        } else {
            return CommonResult.failed("提现申请失败！");
        }
        return CommonResult.success(result, "提现申请成功！");
    }

    private CommonResult<CustomerPayResult> doApplyBalancePay(Object data) {
        CustomerPayResult result = new CustomerPayResult();
        OrderBalancepay balancePayOrder = (OrderBalancepay)data;
        if(StringUtils.isEmpty(balancePayOrder.getBusinesstradeno())) {
            return CommonResult.failed("商户交易号为空");
        }
        if(StringUtils.isEmpty(balancePayOrder.getCustid())) {
            return CommonResult.failed("用户客户号为空");
        }
        int comresult = balancePayOrder.getAmont().compareTo(BigDecimal.valueOf(0.00D));
        if (comresult <= 0) {
            return CommonResult.failed("支付金额为零或负数");
        }
        if(StringUtils.isEmpty(balancePayOrder.getPassword())) {
            return CommonResult.failed("支付密码为空");
        }
        if(StringUtils.isEmpty(balancePayOrder.getDescription())) {
            return CommonResult.failed("订单描述为空");
        }

        BusinessAccount businessAccount = businessService.findBusinessAccountById(balancePayOrder.getBusinessid());
        if(CollectionUtil.isEmpty(businessAccount.getSubAccounts())) {
            return CommonResult.failed("找不到子账户");
        }

        AmsBusinessSubaccount businessSubaccount = businessAccount.getSubAccounts().stream()
                .filter(subAccount -> subAccount.getType().equals(AccountTypeEnum.CHANGE.getCode()))
                .findFirst().get();

        balancePayOrder.setBusinessid(businessAccount.getBusinessid());
        balancePayOrder.setBusinessname(businessAccount.getBusinessname());
        balancePayOrder.setBusinesssubaccountcode(businessSubaccount.getCode());
        balancePayOrder.setChannel(businessAccount.getChannel());
        if (moneyService.doApplyBalancePay(balancePayOrder)) {
            result.setCustid(balancePayOrder.getCustid());
            result.setOrderno(balancePayOrder.getOrderNo());
            result.setAmont(balancePayOrder.getAmont());
            result.setBeforebalance(balancePayOrder.getBeforebalance());
            result.setAfterbalance(balancePayOrder.getAfterbalance());
        } else {
            return CommonResult.failed("支付申请失败！");
        }
        return CommonResult.success(result,"支付申请成功");
    }

    private CommonResult<CustomerPayResult> doApplyNonBalancePay(Object object) {
        CustomerPayResult result = new CustomerPayResult();
        OrderNonbalancepay nonBalancePayOrder = (OrderNonbalancepay)object;

        if(StringUtils.isEmpty(nonBalancePayOrder.getBusinesstradeno())) {
            return CommonResult.failed("商户交易号为空");
        }
        if(StringUtils.isEmpty(nonBalancePayOrder.getCustid())) {
            return CommonResult.failed("用户客户号为空");
        }
        int comresult = nonBalancePayOrder.getAmont().compareTo(BigDecimal.valueOf(0.00D));
        if (comresult <= 0) {
            return CommonResult.failed("支付金额为零或负数");
        }
        if(StringUtils.isEmpty(nonBalancePayOrder.getDescription())) {
            return CommonResult.failed("订单描述为空");
        }
        BusinessAccount businessAccount = businessService.findBusinessAccountByBusinessId(nonBalancePayOrder.getBusinessid());
        nonBalancePayOrder.setBusinessid(businessAccount.getBusinessid());
        nonBalancePayOrder.setBusinessname(businessAccount.getBusinessname());
        nonBalancePayOrder.setChannel(businessAccount.getChannel());
        if (moneyService.doApplyNonBalancePay(nonBalancePayOrder)) {
            result.setCustid(nonBalancePayOrder.getCustid());
            result.setOrderno(nonBalancePayOrder.getOrderNo());
            result.setAmont(nonBalancePayOrder.getAmont());
        } else {
            return CommonResult.failed("支付申请失败！");
        }
        return CommonResult.success(result, "支付申请成功！");
    }

    private CommonResult<CustomerPayResult> doBalancePay(Object data) {
        CustomerPayResult result = new CustomerPayResult();

        OrderBalancepay balancePayOrder = (OrderBalancepay)data;
        if(StringUtils.isEmpty(balancePayOrder.getOrderNo())) {
            return CommonResult.failed("账务系统订单号为空");
        }
        if (StringUtils.isEmpty(balancePayOrder.getPassword())) {
            return CommonResult.failed("支付密码为空");
        }
        if (moneyService.doBalancePay(balancePayOrder)) {
            result = getPayResult(balancePayOrder);
            return CommonResult.success(result,"支付成功！");
        } else {
            return CommonResult.failed("支付失败！");
        }
    }

    private CommonResult<CustomerPayResult> doNonBalancePay(Object data) {
        CustomerPayResult result = new CustomerPayResult();
        OrderNonbalancepay nonBalancePayOrder = (OrderNonbalancepay)data;

        if(StringUtils.isEmpty(nonBalancePayOrder.getOrderNo())) {
            return CommonResult.failed("账务系统订单号为空");
        }
        if(StringUtils.isEmpty(nonBalancePayOrder.getTradeno())) {
            return CommonResult.failed("第三方交易号为空");
        }
        if(StringUtils.isEmpty(nonBalancePayOrder.getTradestatus())) {
            return CommonResult.failed("第三方交易状态为空");
        }
        if(StringUtils.isEmpty(nonBalancePayOrder.getBuyerid())) {
            return CommonResult.failed("购买人第三方id为空");
        }
        if(StringUtils.isEmpty(nonBalancePayOrder.getBuyername())) {
            return CommonResult.failed("购买人银行卡开户名称或第三方账户名称为空");
        }
        if(StringUtils.isEmpty(nonBalancePayOrder.getBuyeraccount())) {
            return CommonResult.failed("购买人第三方账号名为空");
        }
        if (moneyService.doNonBalancePay(nonBalancePayOrder)) {
            result = getPayResult(nonBalancePayOrder);
            return CommonResult.success(result, "支付成功！");
        } else {
            return CommonResult.failed("支付失败！");
        }
    }

    private CommonResult<CustomerPayResult> doCancleBalancePay(Object data) {
        CustomerPayResult result = new CustomerPayResult();
        OrderBalancepay balancePayOrder = (OrderBalancepay)data;

        if (StringUtils.isEmpty(balancePayOrder.getOrderNo())) {
            return CommonResult.failed("账务系统订单号为空");
        }
        if (moneyService.doCancleBalancePay(balancePayOrder.getOrderNo())) {
            result = getPayResult(balancePayOrder);
            return CommonResult.success(result,"支付订单取消成功！");
        } else {
            return CommonResult.failed("支付取消失败！");
        }
    }

    private CommonResult<CustomerPayResult> doCancleNonBalancePay(Object data) {
        CustomerPayResult result = new CustomerPayResult();
        OrderNonbalancepay nonBalancePayOrder = (OrderNonbalancepay)data;

        if (StringUtils.isEmpty(nonBalancePayOrder.getOrderNo())) {
            return CommonResult.failed("账务系统订单号为空");
        }
        if (moneyService.doCancleNonBalancePay(nonBalancePayOrder.getOrderNo())) {
            result = getPayResult(nonBalancePayOrder);
            return CommonResult.success(result, "支付订单取消成功！");
        } else {
            return CommonResult.failed("支付取消失败！");
        }
    }

    private CommonResult<CustomerPayResult> doApplyBalancePayRefund(Object data) {
        CustomerPayResult result = new CustomerPayResult();
        OrderBalancerefund balanceRefundOrder = (OrderBalancerefund)data;

        if(StringUtils.isEmpty(balanceRefundOrder.getOrigorderno())) {
            return CommonResult.failed("原支付订单号为空");
        }
        if(StringUtils.isEmpty(balanceRefundOrder.getBusinesstradeno())) {
            return CommonResult.failed("商户交易号为空");
        }
        int comresult = balanceRefundOrder.getAmont().compareTo(BigDecimal.valueOf(0.00D));
        if(comresult <= 0) {
            return CommonResult.failed("退款金额为零或负数");
        }
        if(StringUtils.isEmpty(balanceRefundOrder.getReason())) {
            return CommonResult.failed("退款原因为空");
        }

        BusinessAccount businessAccount = businessService.findBusinessAccountByBusinessId(balanceRefundOrder.getBusinessid());
        balanceRefundOrder.setBusinessid(businessAccount.getBusinessid());
        if (moneyService.doApplyBalancePayRefund(balanceRefundOrder)) {
            result = getRefundResult(balanceRefundOrder);
            return CommonResult.success(result, "退款申请成功！");
        } else {
            return CommonResult.failed("退款申请失败！");
        }
    }

    private CommonResult<CustomerPayResult> doApplyNonBalancePayRefund(Object data) {
        CustomerPayResult result = new CustomerPayResult();
        OrderNonbalancerefund nonBalanceRefundOrder = (OrderNonbalancerefund)data;

        if(StringUtils.isEmpty(nonBalanceRefundOrder.getOrigorderno())) {
            return CommonResult.failed("原支付订单号为空");
        }
        if(StringUtils.isEmpty(nonBalanceRefundOrder.getOrigtradeno())) {
            return CommonResult.failed("原第三方交易号为空");
        }
        if(StringUtils.isEmpty(nonBalanceRefundOrder.getBusinesstradeno())) {
            return CommonResult.failed("商户交易号为空");
        }
        int comresult = nonBalanceRefundOrder.getAmont().compareTo(BigDecimal.valueOf(0.00D));
        if(comresult <= 0) {
            return CommonResult.failed("退款金额为零或负数");
        }
        if(StringUtils.isEmpty(nonBalanceRefundOrder.getReason())) {
            return CommonResult.failed("退款原因为空");
        }
        BusinessAccount businessAccount = businessService.findBusinessAccountByBusinessId(nonBalanceRefundOrder.getYyBusinessId());
        nonBalanceRefundOrder.setBusinessid(businessAccount.getBusinessid());
        if (moneyService.doApplyNonBalancePayRefund(nonBalanceRefundOrder)) {
            result = getRefundResult(nonBalanceRefundOrder);
            return CommonResult.success(result, "申请退款成功！");
        } else {
            return CommonResult.failed("退款申请失败！");
        }
    }

    private CommonResult<CustomerPayResult> doBalancePayRefund(Object data) {
        CustomerPayResult result = new CustomerPayResult();

        OrderBalancerefund balanceRefundOrder = (OrderBalancerefund)data;
        if(StringUtils.isEmpty(balanceRefundOrder.getOrderNo())) {
            return CommonResult.failed("账务系统订单号为空");
        }
        if (moneyService.doBalancePayRefund(balanceRefundOrder)) {
            result = getRefundResult(balanceRefundOrder);
            result.setBeforebalance(balanceRefundOrder.getBeforebalance());
            result.setAfterbalance(balanceRefundOrder.getAfterbalance());
            result.setFinishdate(balanceRefundOrder.getLastmodifydate());
            return CommonResult.success(result, "退款成功！");
        } else {
            return CommonResult.failed("退款失败！");
        }
    }

    private CommonResult<CustomerPayResult> doNonBalancePayRefund(Object data) {
        CustomerPayResult result = new CustomerPayResult();
        OrderNonbalancerefund nonBalanceRefundOrder = (OrderNonbalancerefund)data;
        if(StringUtils.isEmpty(nonBalanceRefundOrder.getOrderNo())) {
            return CommonResult.failed("账务系统订单号为空");
        }
        if(StringUtils.isEmpty(nonBalanceRefundOrder.getTradeno())) {
            return CommonResult.failed("第三方交易号为空");
        }
        if(StringUtils.isEmpty(nonBalanceRefundOrder.getTradestatus())) {
            return CommonResult.failed("第三方交易状态为空");
        }
        if(StringUtils.isEmpty(nonBalanceRefundOrder.getBuyerid())) {
            return CommonResult.failed("购买人第三方id为空");
        }
        if(StringUtils.isEmpty(nonBalanceRefundOrder.getBuyername())) {
            return CommonResult.failed("购买人银行卡开户名称或第三方账户名称为空");
        }
        if(StringUtils.isEmpty(nonBalanceRefundOrder.getBuyeraccount())) {
            return CommonResult.failed("购买人第三方账号名为空");
        }
        if (moneyService.doNonBalancePayRefund(nonBalanceRefundOrder)) {
            result = getRefundResult(nonBalanceRefundOrder);
            result.setFinishdate(nonBalanceRefundOrder.getLastmodifydate());
            return CommonResult.success(result,"退款成功！");
        } else {
            return CommonResult.failed("退款失败！");
        }
    }

    private CommonResult<CustomerPayResult> doCancleBalancePayRefund(Object  data) {
        CustomerPayResult result = new CustomerPayResult();
        AmsOrderBalancerefund balanceRefundOrder = (AmsOrderBalancerefund)data;

        if(StringUtils.isEmpty(balanceRefundOrder.getOrderNo())) {
            return CommonResult.failed("账务系统订单号为空");
        }
        if(moneyService.doCancleBalancePayRefund(balanceRefundOrder.getOrderNo())) {
            return CommonResult.success(result, "退款订单取消成功！");
        } else {
            return CommonResult.failed("退款取消失败！");
        }
    }

    private CommonResult<CustomerPayResult> doCancleNonBalancePayRefund(Object data) {
        CustomerPayResult result = new CustomerPayResult();
        OrderNonbalancerefund nonBalanceRefundOrder =  (OrderNonbalancerefund)data;

        if(StringUtils.isEmpty(nonBalanceRefundOrder.getOrderNo())) {
            return CommonResult.failed("账务系统订单号为空");
        }
        if (moneyService.doCancleNonBalancePayRefund(nonBalanceRefundOrder.getOrderNo())) {
            result = getRefundResult(nonBalanceRefundOrder);
            return CommonResult.success(result, "退款订单取消成功！");
        } else {
            return CommonResult.failed("退款取消失败！");
        }
    }

    private CommonResult<CustomerPayResult> doApplyBalanceTransfer(Object data) {
        CustomerPayResult result = new CustomerPayResult();
        OrderBalancetransfer balanceTransferOrder = (OrderBalancetransfer)data;

        if(StringUtils.isEmpty(balanceTransferOrder.getBusinesstradeno())) {
            return CommonResult.failed("商户交易号为空");
        }
        if(StringUtils.isEmpty(balanceTransferOrder.getCustid())) {
            return CommonResult.failed("用户客户号为空");
        }
        int comresult = balanceTransferOrder.getAmont().compareTo(BigDecimal.valueOf(0.00D));
        if (comresult <= 0) {
            return CommonResult.failed("转账金额为零或负数");
        }
        if(StringUtils.isEmpty(balanceTransferOrder.getPassword())) {
            return CommonResult.failed("支付密码为空");
        }
        if(StringUtils.isEmpty(balanceTransferOrder.getRececustid())) {
            return CommonResult.failed("收款方客户号为空");
        }

        BusinessAccount businessAccount = businessService.findBusinessAccountByBusinessId(balanceTransferOrder.getYyBusinessId());
        balanceTransferOrder.setBusinessid(businessAccount.getBusinessid());
        balanceTransferOrder.setBusinessname(businessAccount.getBusinessname());
        balanceTransferOrder.setChannel(businessAccount.getChannel());
        balanceTransferOrder.setDescription("余额转账");
        if (moneyService.doApplyBalanceTransfer(balanceTransferOrder)) {
            result.setCustid(balanceTransferOrder.getCustid());
            result.setRececustid(balanceTransferOrder.getRececustid());
            result.setOrderno(balanceTransferOrder.getOrderNo());
            result.setAmont(balanceTransferOrder.getAmont());
            result.setBeforebalance(balanceTransferOrder.getBeforebalance());
            result.setAfterbalance(balanceTransferOrder.getAfterbalance());
            return CommonResult.success(result, "转账申请成功！");
        } else {
            return CommonResult.failed("转账申请失败！");
        }
    }

    private CommonResult<CustomerPayResult> doApplyNonBalanceTransfer(Object data) {
        CustomerPayResult result = new CustomerPayResult();
        OrderNonbalancetransfer nonBalanceTransferOrder = (OrderNonbalancetransfer)data;

        if(StringUtils.isEmpty(nonBalanceTransferOrder.getBusinesstradeno())) {
            return CommonResult.failed("商户交易号为空");
        }
        if(StringUtils.isEmpty(nonBalanceTransferOrder.getCustid())) {
            return CommonResult.failed("用户客户号为空");
        }
        int comresult =  nonBalanceTransferOrder.getAmont().compareTo(BigDecimal.valueOf(0.00D));
        if(comresult <= 0) {
            return CommonResult.failed("转账金额为零或负数");
        }
        if(StringUtils.isEmpty(nonBalanceTransferOrder.getRecetype())) {
            return CommonResult.failed("收款人账户类型为空");
        } else {
            try {
                CBindTypeEnum.getEnum(nonBalanceTransferOrder.getRecetype());
            } catch (Exception e) {
                return CommonResult.failed("收款人账户类型不匹配");
            }
        }
        if(StringUtils.isEmpty(nonBalanceTransferOrder.getRecebankname())) {
            return CommonResult.failed("收款人开户行为空");
        }
        if(StringUtils.isEmpty(nonBalanceTransferOrder.getRecename())) {
            return CommonResult.failed("开户行为空");
        }
        if(StringUtils.isEmpty(nonBalanceTransferOrder.getReceaccount())) {
            return CommonResult.failed("收款人第三方账号名为空");
        }
        BusinessAccount businessAccount = businessService.findBusinessAccountByBusinessId(nonBalanceTransferOrder.getYyBusinessId());
        nonBalanceTransferOrder.setBusinessid(businessAccount.getBusinessid());
        nonBalanceTransferOrder.setBusinessname(businessAccount.getBusinessname());
        nonBalanceTransferOrder.setChannel(businessAccount.getChannel());
        nonBalanceTransferOrder.setDescription("非余额转账");
        if (moneyService.doApplyNonBalanceTransfer(nonBalanceTransferOrder)) {
            result.setCustid(nonBalanceTransferOrder.getCustid());
            result.setOrderno(nonBalanceTransferOrder.getOrderNo());
            result.setAmont(nonBalanceTransferOrder.getAmont());
            return CommonResult.success(result, "转账申请成功！");
        } else {
            return CommonResult.failed("转账申请失败！");
        }
    }

    private CommonResult<CustomerPayResult> doBalanceTransfer(Object data) {
        CustomerPayResult result = new CustomerPayResult();
        OrderBalancetransfer balanceTransferOrder = (OrderBalancetransfer)data;
        if(StringUtils.isEmpty(balanceTransferOrder.getOrderNo())) {
            CommonResult.failed("账务系统订单号为空");
        }
        if(moneyService.doBalanceTransfer(balanceTransferOrder)) {
            result.setCustid(balanceTransferOrder.getCustid());
            result.setRececustid(balanceTransferOrder.getRececustid());
            result.setOrderno(balanceTransferOrder.getOrderNo());
            result.setAmont(balanceTransferOrder.getAmont());
            result.setFinishdate(balanceTransferOrder.getLastmodifydate());
            return CommonResult.success(result, "转账成功！");
        } else {
            return CommonResult.failed("转账失败！");
        }
    }

    private CommonResult<CustomerPayResult> doNonBalanceTransfer(Object data) {
        CustomerPayResult result = new CustomerPayResult();
        OrderNonbalancetransfer nonBalanceTransferOrder = (OrderNonbalancetransfer)data;

        if(StringUtils.isEmpty(nonBalanceTransferOrder.getOrderNo())) {
            return CommonResult.failed("账务系统订单号为空");
        }
        if(StringUtils.isEmpty(nonBalanceTransferOrder.getTradeno())) {
            return CommonResult.failed("第三方交易号为空");
        }
        if(StringUtils.isEmpty(nonBalanceTransferOrder.getTradestatus())) {
            return CommonResult.failed("第三方交易状态为空");
        }
        if(StringUtils.isEmpty(nonBalanceTransferOrder.getBuyerid())) {
            return CommonResult.failed("购买人第三方id为空");
        }
        if(StringUtils.isEmpty(nonBalanceTransferOrder.getBuyername())) {
            return CommonResult.failed("购买人银行卡开户名称或第三方账户名称为空");
        }
        if(StringUtils.isEmpty(nonBalanceTransferOrder.getBuyeraccount())) {
            return CommonResult.failed("购买人第三方账号名为空");
        }
        if (moneyService.doNonBalanceTransfer(nonBalanceTransferOrder)) {
            result.setCustid(nonBalanceTransferOrder.getCustid());
            result.setOrderno(nonBalanceTransferOrder.getOrderNo());
            result.setAmont(nonBalanceTransferOrder.getAmont());
            result.setFinishdate(nonBalanceTransferOrder.getLastmodifydate());
            return CommonResult.success(result, "转账成功！");
        } else {
            return CommonResult.failed("转账失败！");
        }
    }

    private CommonResult<CustomerPayResult> doCancleBalanceTransfer(Object data) {
        CustomerPayResult result = new CustomerPayResult();
        AmsOrderBalancetransfer balanceTransferOrder = (AmsOrderBalancetransfer)data;

        if(StringUtils.isEmpty(balanceTransferOrder.getOrderNo())) {
            return CommonResult.failed("账务系统订单号为空");
        }
        if(moneyService.doCancleBalanceTransfer(balanceTransferOrder.getOrderNo())) {
            return CommonResult.success(result, "转账订单取消成功！");
        } else {
            return CommonResult.failed("转账取消失败！");
        }
    }

    private CommonResult<CustomerPayResult> doCancleNonBalanceTransfer(Object data) {
        CustomerPayResult result = new CustomerPayResult();
        AmsOrderNonbalancetransfer nonBalanceTransferOrder = (AmsOrderNonbalancetransfer)data;

        if(StringUtils.isEmpty(nonBalanceTransferOrder.getOrderNo())) {
            return CommonResult.failed("账务系统订单号为空");
        }
        if (moneyService.doCancleNonBalanceTransfer(nonBalanceTransferOrder.getOrderNo())) {
            return CommonResult.success(result, "转账订单取消成功！");
        } else {
            return CommonResult.failed("转账取消失败！");
        }
    }


    private CustomerPayResult getPayResult(OrderBase payOrder) {
        CustomerPayResult result = new CustomerPayResult();
        result.setCustid(payOrder.getCustid());
        result.setOrderno(payOrder.getOrderNo());
        result.setAmont(payOrder.getAmont());
        result.setFinishdate(payOrder.getLastmodifydate());
        return result;
    }

    private CustomerPayResult getRefundResult(OrderBase refundOrder) {
        CustomerPayResult result = new CustomerPayResult();
        result.setSuramont(refundOrder.getSuramont());
        result.setCustid(refundOrder.getCustid());
        result.setOrderno(refundOrder.getOrderNo());
        result.setAmont(refundOrder.getAmont());
        return result;
    }

}
