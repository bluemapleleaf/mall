package com.macro.mall.service.core.amount;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.macro.mall.ams.model.AmsDicOrdertype;
import com.macro.mall.ams.model.AmsDicOrdertypeAmontdetailrule;
import com.macro.mall.ams.service.AmsDicOrdertypeAmontdetailruleRepository;
import com.macro.mall.convert.OrdertypeAmontdetailruleConvert;
import com.macro.mall.domain.DicOrdertypeAmontdetailrule;
import com.macro.mall.domain.order.OrderBase;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发生额明细分解计算
 *
 * @author dongjb
 * @date 2021/01/15
 */
public class AmontDecompose {
    @Autowired
    AmsDicOrdertypeAmontdetailruleRepository amsDicOrdertypeAmontdetailruleRepository;

    /**
     * 获取发生额分解实例
     *
     * @param order             订单对象
     * @param orderTypeObj      订单类型对象
     * @return 分解实例
     */
    public static AmontDecompose getInstance(OrderBase order, AmsDicOrdertype orderTypeObj) {
        return new AmontDecompose(order, orderTypeObj);
    }

    /**
     * 构造函数
     *
     * @param order             订单对象
     * @param orderTypeObj      订单类型对象
     */
    private AmontDecompose(OrderBase order, AmsDicOrdertype orderTypeObj) {
        this.orderTypeObj = orderTypeObj;
        this.order = order;
        doInit();
    }

    /**
     * 初始化分解规则
     */
    private void doInit() {
        LambdaQueryWrapper<AmsDicOrdertypeAmontdetailrule> lambda = new LambdaQueryWrapper<>();
        lambda.eq(AmsDicOrdertypeAmontdetailrule::getOrdertypeid, orderTypeObj.getId())
                .orderByAsc(AmsDicOrdertypeAmontdetailrule::getSort);
        List<AmsDicOrdertypeAmontdetailrule> amsAmontDetailRules = amsDicOrdertypeAmontdetailruleRepository.list(lambda);
        List<DicOrdertypeAmontdetailrule> amontDetailRules = OrdertypeAmontdetailruleConvert.INSTANCE.amsAmontdetailrules2eAmontdetailrules(amsAmontDetailRules);
        AmontCalculation amontCalculation = new AmontCalculation(order, amontDetailRules);
        amontDetailRules = amontCalculation.getAmontDetailRules();
        amontDetailMap = new HashMap<>(8);
        for (DicOrdertypeAmontdetailrule amontDetailRule : amontDetailRules) {
            amontDetailMap.put(amontDetailRule.getId(), amontDetailRule);
        }
        balance = amontCalculation.getBalance();
    }

    /**
     * 通过id获取分解的发生额
     *
     * @param id 发生额明细id
     * @return 发生额明细对象
     */
    public DicOrdertypeAmontdetailrule getAmontDetailById(String id) {
        return amontDetailMap.get(id);
    }

    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * 发生额计算结果
     */
    private BigDecimal balance;

    /**
     * 订单对象
     */
    private final OrderBase order;

    /**
     * 订单类型字典配置
     */
    private final AmsDicOrdertype orderTypeObj;

    /**
     * 发生额明细，id=>AmsDicOrdertypeAmontdetailrule
     */
    private Map<String, DicOrdertypeAmontdetailrule> amontDetailMap;

}
