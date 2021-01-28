package com.macro.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.macro.mall.ams.model.*;
import com.macro.mall.ams.service.*;
import com.macro.mall.ams.service.impl.AmsOrderBaseRepositoryImpl;
import com.macro.mall.convert.*;
import com.macro.mall.domain.AccountEntrance;
import com.macro.mall.domain.result.AccountResult;
import com.macro.mall.domain.order.*;
import com.macro.mall.enums.*;
import com.macro.mall.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * /**
 * 订单服务
 *
 * @author dongjb
 * @date 2021/01/12
 */

@Slf4j
@Service
public class OrderServiceImpl extends AmsOrderBaseRepositoryImpl implements OrderService {
    @Autowired
    AmsDicOrdertypeRepository amsDicOrdertypeRepository;

    @Autowired
    AmsDicDateparamRepository amsDicDateparamRepository;

    @Autowired
    AmsOrderReviewrecordRepository amsOrderReviewrecordRepository;

    @Autowired
    AmsOrderBalancerechargeRepository amsOrderBalancerechargeRepository;

    @Autowired
    AmsOrderBalancepayRepository amsOrderBalancepayRepository;

    @Autowired
    AmsOrderNonbalancepayRepository amsOrderNonbalancepayRepository;

    @Autowired
    AmsOrderBalancerefundRepository amsOrderBalancerefundRepository;

    @Autowired
    AmsOrderNonbalancerefundRepository amsOrderNonbalancerefundRepository;

    @Autowired
    AmsOrderBalancetransferRepository amsOrderBalancetransferRepository;

    @Autowired
    AmsOrderNonbalancetransferRepository amsOrderNonbalancetransferRepository;

    @Autowired
    AmsOrderBalancecashRepository amsOrderBalancecashRepository;

    @Autowired
    AmsOrderBalancesettlementRepository amsOrderBalancesettlementRepository;

    @Autowired
    AmsOrderShopsettlementRepository amsOrderShopsettlementRepository;

    @Autowired
    AmsOrderInternaltradeRepository amsOrderInternaltradeRepository;

    @Autowired
    AmsOrderIntegralrechargeRepository amsOrderIntegralrechargeRepository;

    @Autowired
    AmsOrderIntegralpayRepository amsOrderIntegralpayRepository;

    @Autowired
    AmsOrderIntegralrefundRepository amsOrderIntegralrefundRepository;

    @Autowired
    AmsOrderIntegralsettlementRepository amsOrderIntegralsettlementRepository;

    @Autowired
    AmsOrderCurrencyrechargeRepository amsOrderCurrencyrechargeRepository;

    @Autowired
    AmsOrderCurrencypayRepository amsOrderCurrencypayRepository;


    @Autowired
    AmsOrderCurrencyrefundRepository amsOrderCurrencyrefundRepository;

    @Autowired
    AmsDicIntergralparamRepository amsDicIntergralparamRepository;



    @Override
    public List<AmsOrderReviewrecord> findReviewRecords(String orderNo) {
        LambdaQueryWrapper<AmsOrderReviewrecord> lambda = new LambdaQueryWrapper<>();
        lambda.eq(AmsOrderReviewrecord::getOrderno, orderNo)
                .orderByDesc(AmsOrderReviewrecord::getReviewdate);
        return amsOrderReviewrecordRepository.list(lambda);
    }

    @Override
    public AmsOrderReviewrecord findReviewRecord(String reviewid) {
        return amsOrderReviewrecordRepository.getById(reviewid );
    }

    @Override
    public boolean doOrderReview(String orderNo, String content, ReviewResultEnum reviewResultEnum, String userid, String username) {

        AmsOrderReviewrecord orderReviewRecord = new AmsOrderReviewrecord();
        orderReviewRecord.setOrderno(orderNo);
        orderReviewRecord.setContent(content);
        orderReviewRecord.setResult(reviewResultEnum.getValue());
        orderReviewRecord.setUserid(userid);
        orderReviewRecord.setUsername(username);
        orderReviewRecord.setAccountdate(getAccountDate("accountdate"));
        orderReviewRecord.setReviewdate(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
        return amsOrderReviewrecordRepository.save(orderReviewRecord);
    }

    @Override
    public boolean doDeleteOrderReview(String reviewid) {
        return amsOrderReviewrecordRepository.removeById(reviewid);
    }

    @Override
    public boolean doDeleteOrderReviews(String orderNo) {
        LambdaQueryWrapper<AmsOrderReviewrecord> lambda = new LambdaQueryWrapper<>();
        lambda.eq(AmsOrderReviewrecord::getOrderno, orderNo);
        return amsOrderReviewrecordRepository.remove(lambda);
    }

    @Override
    public boolean doValidateOrderType(OrderBase orderBase) {
        if (orderBase.getOrdertype() < 100) {
            log.error("订单类型非法：" + orderBase.getOrdertype());
            return false;
        } else {
            LambdaQueryWrapper<AmsDicOrdertype> lambda = new LambdaQueryWrapper<>();
            lambda.eq(AmsDicOrdertype::getStatus, StatusEnum.ACTIVATE.getValue());
            OrderTypeEnum orderTypeEnum = OrderTypeEnum.getEnum(orderBase.getOrdertype());
            //普通交易
            if (orderTypeEnum != null) {
                if (!orderTypeEnum.getOrderClass().equals(orderBase.getClass())) {
                    log.error("订单类型非法：" + orderTypeEnum.getValue() + " => " + this.getClass().getCanonicalName());
                    return false;
                }
                orderBase.setAccountdate(null);
                if (!this.getClass().equals(AmsOrderNormaltrade.class)) {
                    orderBase.setEntryItemParams(null);
                }
                lambda.eq(AmsDicOrdertype::getCode, orderTypeEnum.getValue() + "")
                        .eq(AmsDicOrdertype::getField6, InternalTrade.No.getValue() + "");
                if (orderBase.getOrdertype() < 300 || orderBase.getOrdertype() >= 400) {
                    orderBase.setRatio(BigDecimal.valueOf(1));
                }
            } else {//内部特殊交易
                log.error("不是内部特殊交易订单类型：" + orderBase.getOrdertype() + " => " + this.getClass().getCanonicalName());
                return false;
            }
            AmsDicOrdertype orderTypeObj = amsDicOrdertypeRepository.getOne(lambda);
            if (orderTypeObj == null) {
                log.error("订单类型非法：" + orderBase.getOrdertype());
                return false;
            }
            return true;
        }
    }

    @Override
    public boolean doCreateOrder(OrderBase orderBase) {
        if (!doValidateOrderType(orderBase)) {
            return false;
        }
        try {
            doUpdateAccountDate(orderBase);
        } catch (Exception e) {
            log.error("更新会计相关日期失败：" + e.getMessage(), e);
            return false;
        }
        doUpdateCreateDate(orderBase);
        orderBase.setStatus(OrderStatusEnum.create.getValue());
        orderBase.setActamont(orderBase.getAmont().multiply(orderBase.getRatio().setScale(2, DecimalProcessModeEnum.Half_UP.getMode())));
        AccountEntrance accountEntrance = AccountEntrance.getInstance(orderBase);
        if (accountEntrance != null) {
            orderBase.setAccountResult(accountEntrance.doProcess(orderBase));
            if(orderBase.getAccountResult().getStatus().equals(ResultStatusEnum.success.getCode())) {
                save(OrderBaseConvert.INSTANCE.do2po(orderBase));
                switch (OrderTypeEnum.getEnum(orderBase.getOrdertype())) {
                    case BalanceRecharge:
                        amsOrderBalancerechargeRepository.save(OrderBalancerechargeConvert.INSTANCE.do2po((OrderBalancerecharge)orderBase));
                        break;
                    case BalancePay:
                        amsOrderBalancepayRepository.save(OrderBalancepayConvert.INSTANCE.do2po((OrderBalancepay)orderBase));
                        break;
                    case NonBalancePay:
                        amsOrderNonbalancepayRepository.save(OrderNonBalancepayConvert.INSTANCE.do2po((OrderNonbalancepay)orderBase));
                        break;
                    case BalanceRefund:
                        amsOrderBalancerefundRepository.save(OrderBalancerefundConvert.INSTANCE.do2po((OrderBalancerefund)orderBase));
                        break;
                    case NonBalanceRefund:
                        amsOrderNonbalancerefundRepository.save(OrderNonBalancerefundConvert.INSTANCE.do2po((OrderNonbalancerefund)orderBase));
                        break;
                    case BalanceTransfer:
                        amsOrderBalancetransferRepository.save(OrderBalancetransferConvert.INSTANCE.do2po((OrderBalancetransfer)orderBase));
                        break;
                    case BalanceCash:
                        amsOrderBalancecashRepository.save(OrderBalancecashConvert.INSTANCE.do2po((OrderBalancecash) orderBase));
                        break;
                    case BalanceSettlement:
                        amsOrderBalancesettlementRepository.save(OrderBalancesettlementConvert.INSTANCE.do2po((OrderBalancesettlement) orderBase));
                        break;
                    case NonBalanceTransfer:
                        amsOrderNonbalancetransferRepository.save(OrderNonbalancetransferConvert.INSTANCE.do2po((OrderNonbalancetransfer) orderBase));
                        break;
                    case ShopSettlement:
                        amsOrderShopsettlementRepository.save(OrderShopsettlementConvert.INSTANCE.do2po((OrderShopsettlement) orderBase));
                        break;
                    case InternalTrade:
                        amsOrderInternaltradeRepository.save(OrderInternaltradeConvert.INSTANCE.do2po((OrderInternaltrade) orderBase));
                        break;
                    case Reviewrecord:
                        amsOrderReviewrecordRepository.save(OrderReviewrecordConvert.INSTANCE.do2po((OrderReviewrecord) orderBase));
                        break;
                    case IntegralRecharge:
                        amsOrderIntegralrechargeRepository.save(OrderIntegralrechargeConvert.INSTANCE.do2po((OrderIntegralrecharge) orderBase));
                        break;
                    case IntegralPay:
                        amsOrderIntegralpayRepository.save(OrderIntegralpayConvert.INSTANCE.do2po((OrderIntegralpay) orderBase));
                        break;
                    case IntegralRefund:
                        amsOrderIntegralrefundRepository.save(OrderIntegralrefundConvert.INSTANCE.do2po((OrderIntegralrefund) orderBase));
                        break;
                    case CustomCurrencyPay:
                        amsOrderCurrencypayRepository.save(OrderCurrencypayConvert.INSTANCE.do2po((OrderCurrencypay) orderBase));
                        break;
                    case CustomCurrencyRefund:
                        amsOrderCurrencyrefundRepository.save(OrderCurrencyrefundConvert.INSTANCE.do2po((OrderCurrencyrefund) orderBase));
                        break;
                    case CustomCurrencyRecharge:
                        amsOrderCurrencyrechargeRepository.save(OrderCurrencyrechargeConvert.INSTANCE.do2po((OrderCurrencyrecharge) orderBase));
                        break;


                    default:
                        log.info("默认路径");
                        break;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public boolean doUpdateOrder(OrderBase orderBase) {
        if (!doValidateOrderType(orderBase)) {
            return false;
        }
        try {
            doUpdateAccountDate(orderBase);
        } catch (Exception e) {
            log.error("更新会计相关日期失败：" + e.getMessage(), e);
            return false;
        }
        try {
            if (isEnableUpdateOrder(orderBase)) {
                AccountEntrance accountEntrance = AccountEntrance.getInstance(orderBase);
                if (accountEntrance != null) {
                    orderBase.setAccountResult(accountEntrance.doProcess(orderBase));
                    return orderBase.getAccountResult().getStatus().equals(ResultStatusEnum.success.getCode());
                } else {
                    return false;
                }
            } else {
                log.error("订单信息不允许修改");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public void doUpdateAccountDate(OrderBase orderBase) {
        if (StringUtils.isEmpty(orderBase.getAccountdate())) {
            orderBase.setAccountdate(getAccountDate("accountdate"));
        }

        int statementdateno = Integer.parseInt(getAccountDate("statementdateno"));
        int bindaccountdateno = Integer.parseInt(getAccountDate("bindaccountdateno"));

        LocalDate localDate = LocalDate.parse(orderBase.getAccountdate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        int dayno =localDate.getDayOfMonth();

        int offermonthStat = 0;
        int offermonthBind;
        if (dayno >= statementdateno) {
            offermonthStat = 1;
        }
        if (statementdateno > bindaccountdateno) {
            offermonthBind = offermonthStat + 1;
        } else {
            offermonthBind = offermonthStat;
        }

        LocalDate statment = LocalDate.now();
        statment.plusMonths(offermonthStat);
        statment.withDayOfMonth(statementdateno);
        orderBase.setStatementdate(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(statment));
        LocalDate binddate = LocalDate.now();
        binddate.plusMonths(offermonthBind);
        binddate.withDayOfMonth(bindaccountdateno);
        orderBase.setBindaccountdate(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(binddate));
    }

    @Override
    public OrderStatusEnum findCurrOrderStatus(OrderBase orderBase) {
        LambdaQueryWrapper<AmsOrderBase> lambda = new LambdaQueryWrapper<>();
        lambda.eq(AmsOrderBase::getOrderNo, orderBase.getOrderNo());
        AmsOrderBase amsOrderBase = getOne(lambda);
        if (amsOrderBase == null) {
            return OrderStatusEnum.other;
        } else {
            return OrderStatusEnum.getEnum(amsOrderBase.getStatus());
        }
    }

    @Override
    public void doUpdateCreateDate(OrderBase orderBase) {
        String now = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
        orderBase.setCreatedate(now);
        orderBase.setLastmodifydate(now);
    }

    @Override
    public boolean doChangeOrderSatus(OrderBase orderBase, OrderStatusEnum orderStatusEnum) {
        try {
            if (orderBase != null) {
                LambdaUpdateWrapper<AmsOrderBase> lambda = new LambdaUpdateWrapper<>();
                lambda.eq(AmsOrderBase::getOrderNo, orderBase.getOrderNo())
                        .set(AmsOrderBase::getStatus, orderStatusEnum.getValue());
                return update(lambda);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), "订单状态修改失败！");
        }
        return false;
    }

    @Override
    public OrderBase findOrderByOrderNo(String orderNo) {
        return OrderBaseConvert.INSTANCE.po2do(getById(orderNo));
    }

    @Override
    public AmsOrderBalancerecharge findOrderBalancerecharge(String orderNo) {
        return amsOrderBalancerechargeRepository.getById(orderNo);
    }

    @Override
    public AmsOrderNonbalancepay findOrderNonbalancepay(String orderNo) {
        return amsOrderNonbalancepayRepository.getById(orderNo);
    }

    @Override
    public AmsOrderBalancerefund findOrderBalanceRefund(String orderNo) {
        return amsOrderBalancerefundRepository.getById(orderNo);
    }

    @Override
    public AmsOrderNonbalancerefund findOrderNonBalanceRefund(String orderNo) {
        return amsOrderNonbalancerefundRepository.getById(orderNo);
    }

    @Override
    public AmsOrderBalancetransfer findOrderBalancetransfer(String orderNo) {
        return amsOrderBalancetransferRepository.getById(orderNo);
    }

    @Override
    public AmsOrderNonbalancetransfer findOrderNonBalancetransfer(String orderNo) {
        return amsOrderNonbalancetransferRepository.getById(orderNo);
    }

    @Override
    public AmsOrderShopsettlement findOrderShopsettlement(String orderNo) {
        return amsOrderShopsettlementRepository.getById(orderNo);
    }

    @Override
    public AmsOrderIntegralrecharge findOrderIntegralrecharge(String orderNo) {
        return amsOrderIntegralrechargeRepository.getById(orderNo);
    }

    @Override
    public AmsOrderIntegralpay findOrderIntegralpay(String orderNo) {
        return amsOrderIntegralpayRepository.getById(orderNo);
    }

    @Override
    public AmsOrderIntegralrefund findOrderIntegralRefund(String orderNo) {
        return amsOrderIntegralrefundRepository.getById(orderNo);
    }

    @Override
    public AmsOrderCurrencyrecharge findOrderCurrencyrecharge(String orderNo) {
        return amsOrderCurrencyrechargeRepository.getById(orderNo);
    }

    @Override
    public AmsOrderCurrencypay findOrderCurrencypay(String orderNo) {
        return amsOrderCurrencypayRepository.getById(orderNo);
    }

    @Override
    public AmsOrderCurrencyrefund findOrderCurrencyrefund(String orderNo) {
        return amsOrderCurrencyrefundRepository.getById(orderNo);
    }

    @Override
    public boolean doValidateOrder(OrderBase orderBase) {
        AccountResult accountResult = orderBase.getAccountResult();
        if (accountResult.getStatus().equals(ResultStatusEnum.success.getCode())) {
            AccountEntrance accountEntrance = AccountEntrance.getInstance(orderBase);
            if (accountEntrance != null) {
                accountResult = accountEntrance.doValidate();
                if (accountResult.getStatus().equals(ResultStatusEnum.success.getCode())) {
                    return true;
                } else {
                    log.error(accountResult.getMessage());
                    return false;
                }
            } else {
                accountResult.setStatus(ResultStatusEnum.failed);
                accountResult.setMessage("核心记账规则出错");
                log.error("核心记账规则出错");
                return false;
            }
        } else {
            log.error(accountResult.getMessage());
            return false;
        }
    }

    @Override
    public boolean validateRefundPayTypeAndStatus(OrderBase payOrder, OrderBase refundOrder, int flag) {
        try {
            if (!OrderTypeEnum.BalancePay.equals(payOrder.getOrdertype()) && !OrderTypeEnum.NonBalancePay.equals(payOrder.getOrdertype())
                    && !OrderTypeEnum.IntegralPay.equals(payOrder.getOrdertype()) && !OrderTypeEnum.CustomCurrencyPay.equals(payOrder.getOrdertype())) {
                return false;
            }
            PayTypeEnum payTypeEnum = PayTypeEnum.getEnum(refundOrder.getPaytype());
            refundOrder.setPaytypename(payTypeEnum.getDescription());
            if (!payOrder.getPaytype().equals(payTypeEnum.getName())) {
                return false;
            }
            if (flag == 0) {
                if (!payOrder.getStatus().equals(OrderStatusEnum.paySuccess.getValue()) && !payOrder.getStatus().equals(OrderStatusEnum.reviewing.getValue())
                        && !payOrder.getStatus().equals(OrderStatusEnum.success.getValue()) && !payOrder.getStatus().equals(OrderStatusEnum.refundSuccess.getValue())
                        && !payOrder.getStatus().equals(OrderStatusEnum.refundFail.getValue()) && !payOrder.getStatus().equals(OrderStatusEnum.refundCancle.getValue())
                        && !payOrder.getStatus().equals(OrderStatusEnum.adjustFail.getValue()) && !payOrder.getStatus().equals(OrderStatusEnum.adjustCancle.getValue())) {
                    return false;
                }
            } else {
                if (!payOrder.getStatus().equals(OrderStatusEnum.applyRefundSuccess.getValue()) && !payOrder.getStatus().equals(OrderStatusEnum.refunding.getValue())) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    @Override
    public String getAccountDate(String dateType) {
        LambdaQueryWrapper<AmsDicDateparam> lambda = new LambdaQueryWrapper<>();
        lambda.eq(AmsDicDateparam::getName, dateType)
                .eq(AmsDicDateparam::getStatus, StatusEnum.ACTIVATE.getValue());
        AmsDicDateparam dateParam = amsDicDateparamRepository.getOne(lambda);
        return dateParam.getCode().trim();
    }

    /**
     * 获取积分兑换比例
     *
     * @return 积分兑换比例
     */
    @Override
    public BigDecimal getIntegralRatio() {
        BigDecimal ratio = BigDecimal.ONE;

       LambdaQueryWrapper<AmsDicIntergralparam> lambda = new LambdaQueryWrapper<>();
       lambda.eq(AmsDicIntergralparam::getType, IntergralParamTypeEnum.ratio.getValue() + "")
               .eq(AmsDicIntergralparam::getStatus, StatusEnum.ACTIVATE.getValue())
               .orderByDesc(AmsDicIntergralparam::getField2);

        List<AmsDicIntergralparam> intergralParamList = amsDicIntergralparamRepository.list(lambda);


        String sysdate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
        String accdate = getAccountDate("accountdate");
        String value = "";
        for (AmsDicIntergralparam intergralParam : intergralParamList) {
            if ((ValidityDateTypeEnum.accountdate.getValue() + "").equals(intergralParam.getField1())) {
                if (intergralParam.getField2().trim().compareTo(accdate) <= 0) {
                    value = intergralParam.getCode().trim();
                    break;
                }
            }
            if ((ValidityDateTypeEnum.sysdate.getValue() + "").equals(intergralParam.getField1())) {
                if (intergralParam.getField2().trim().compareTo(sysdate) <= 0) {
                    value = intergralParam.getCode().trim();
                    break;
                }
            }
        }

        if (!StringUtils.isEmpty(value)) {
            try {
                ratio = new BigDecimal(value);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return ratio;
    }








    //---------------私有方法
    /**
     * 是否允许更新订单信息
     *
     * @return true|false
     */
    private boolean isEnableUpdateOrder(OrderBase orderBase) {
        OrderStatusEnum currOrderStatusEnum = findCurrOrderStatus(orderBase);
        OrderStatusEnum newOrderStatusEnum = OrderStatusEnum.getEnum(orderBase.getStatus());
        //订单状态相等时
        if (currOrderStatusEnum.equals(newOrderStatusEnum.getValue())) {
            return !(currOrderStatusEnum.equals(OrderStatusEnum.success.getValue()) || currOrderStatusEnum.equals(OrderStatusEnum.failed.getValue()) || currOrderStatusEnum.equals(OrderStatusEnum.cancle.getValue())
                    || currOrderStatusEnum.equals(OrderStatusEnum.refundSuccess.getValue()) || currOrderStatusEnum.equals(OrderStatusEnum.refundFail.getValue()) || currOrderStatusEnum.equals(OrderStatusEnum.refundCancle.getValue())
                    || currOrderStatusEnum.equals(OrderStatusEnum.adjustSuccess.getValue()) || currOrderStatusEnum.equals(OrderStatusEnum.adjustFail.getValue()) || currOrderStatusEnum.equals(OrderStatusEnum.adjustCancle.getValue()));
        } else {//订单状态不相等时
            if (currOrderStatusEnum.equals(OrderStatusEnum.create.getValue())) {
                return newOrderStatusEnum.equals(OrderStatusEnum.paySuccess.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.reviewing.getValue());
            }
            if (currOrderStatusEnum.equals(OrderStatusEnum.paySuccess.getValue())) {
                return newOrderStatusEnum.equals(OrderStatusEnum.applyRefund.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.reviewing.getValue())
                        || newOrderStatusEnum.equals(OrderStatusEnum.success.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.failed.getValue())
                        || newOrderStatusEnum.equals(OrderStatusEnum.cancle.getValue());
            }
            if (currOrderStatusEnum.equals(OrderStatusEnum.reviewing.getValue())) {
                return newOrderStatusEnum.equals(OrderStatusEnum.applyRefund.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.success.getValue())
                        || newOrderStatusEnum.equals(OrderStatusEnum.failed.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.cancle.getValue());
            }
            if (currOrderStatusEnum.equals(OrderStatusEnum.success.getValue())) {
                return newOrderStatusEnum.equals(OrderStatusEnum.applyRefund.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.applyAdjust.getValue());
            }
            if (currOrderStatusEnum.equals(OrderStatusEnum.applyRefund.getValue())) {
                return newOrderStatusEnum.equals(OrderStatusEnum.applyRefundSuccess.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.refunding.getValue());
            }
            if (currOrderStatusEnum.equals(OrderStatusEnum.applyRefundSuccess.getValue())) {
                return newOrderStatusEnum.equals(OrderStatusEnum.refunding.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.refundSuccess.getValue())
                        || newOrderStatusEnum.equals(OrderStatusEnum.refundFail.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.refundCancle.getValue());
            }
            if (currOrderStatusEnum.equals(OrderStatusEnum.refunding.getValue())) {
                return newOrderStatusEnum.equals(OrderStatusEnum.paySuccess.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.reviewing.getValue())
                        || newOrderStatusEnum.equals(OrderStatusEnum.refundSuccess.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.refundFail.getValue())
                        || newOrderStatusEnum.equals(OrderStatusEnum.refundCancle.getValue());
            }
            if (currOrderStatusEnum.equals(OrderStatusEnum.refundSuccess.getValue())) {
                return newOrderStatusEnum.equals(OrderStatusEnum.applyRefund.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.applyAdjust.getValue());
            }
            if (currOrderStatusEnum.equals(OrderStatusEnum.refundFail.getValue())) {
                return newOrderStatusEnum.equals(OrderStatusEnum.applyRefund.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.applyAdjust.getValue());
            }
            if (currOrderStatusEnum.equals(OrderStatusEnum.refundCancle.getValue())) {
                return newOrderStatusEnum.equals(OrderStatusEnum.applyRefund.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.applyAdjust.getValue());
            }
            if (currOrderStatusEnum.equals(OrderStatusEnum.applyAdjust.getValue())) {
                return newOrderStatusEnum.equals(OrderStatusEnum.applyAdjustSuccess.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.adjusting.getValue());
            }
            if (currOrderStatusEnum.equals(OrderStatusEnum.applyAdjustSuccess.getValue())) {
                return newOrderStatusEnum.equals(OrderStatusEnum.adjusting.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.adjustSuccess.getValue())
                        || newOrderStatusEnum.equals(OrderStatusEnum.adjustFail.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.adjustCancle.getValue());
            }
            if (currOrderStatusEnum.equals(OrderStatusEnum.adjusting.getValue())) {
                return newOrderStatusEnum.equals(OrderStatusEnum.adjustSuccess.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.adjustFail.getValue())
                        || newOrderStatusEnum.equals(OrderStatusEnum.adjustCancle.getValue());
            }
            if (currOrderStatusEnum.equals(OrderStatusEnum.adjustFail.getValue())) {
                return newOrderStatusEnum.equals(OrderStatusEnum.applyRefund.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.applyAdjust.getValue());
            }
            return currOrderStatusEnum.equals(OrderStatusEnum.adjustCancle.getValue()) && (
                    newOrderStatusEnum.equals(OrderStatusEnum.applyRefund.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.applyAdjust.getValue()));
        }
    }

}
