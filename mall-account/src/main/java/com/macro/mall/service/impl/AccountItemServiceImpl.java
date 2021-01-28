package com.macro.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.macro.mall.ams.model.AmsDicAccountItem;
import com.macro.mall.ams.model.AmsEntryItemflow;
import com.macro.mall.ams.service.AmsEntryItemflowRepository;
import com.macro.mall.ams.service.impl.AmsDicAccountItemRepositoryImpl;
import com.macro.mall.enums.BalanceDirectEnum;
import com.macro.mall.enums.StatusEnum;
import com.macro.mall.service.AccountItemService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AccountItemServiceImpl extends AmsDicAccountItemRepositoryImpl implements AccountItemService {
    @Autowired
    AmsEntryItemflowRepository amsEntryItemflowRepository;

    @Override
    public AmsDicAccountItem getSubAccountItem(String parentcode, String code) {

        LambdaQueryWrapper<AmsDicAccountItem> lambda = new LambdaQueryWrapper<>();
        lambda.eq(AmsDicAccountItem::getCode, parentcode)
                .eq(AmsDicAccountItem::getStatus, StatusEnum.ACTIVATE.getValue());
        AmsDicAccountItem  accountItem = getOne(lambda);


        LambdaQueryWrapper<AmsDicAccountItem> lambdasub = new LambdaQueryWrapper<>();
        lambdasub.eq(AmsDicAccountItem::getCode, code)
                .eq(AmsDicAccountItem::getParentid, accountItem.getId())
                .eq(AmsDicAccountItem::getStatus, StatusEnum.ACTIVATE.getValue());

        return getOne(lambdasub);
    }


    @Override
    public AmsDicAccountItem getAccountItem(String code) {

        LambdaQueryWrapper<AmsDicAccountItem> lambda = new LambdaQueryWrapper<>();
        lambda.eq(AmsDicAccountItem::getCode, code)
                .eq(AmsDicAccountItem::getStatus, StatusEnum.ACTIVATE.getValue());
        return getOne(lambda);
    }

    @Override
    public AmsDicAccountItem getParentAccountItem(AmsDicAccountItem item) {
        return getById(getById(item.getId()).getParentid());
    }

    @Override
    public boolean creatEntryItemFlow(AmsEntryItemflow flow) {
        return amsEntryItemflowRepository.save(flow);
    }

    @Override
    public void doRecord(String orderno, String businessid, BigDecimal amont, String accountdate, String statementdate, String bindaccountdate, AmsDicAccountItem accountsubitem, BalanceDirectEnum balanceDirectEnum) {
        AmsDicAccountItem accountitemthird = getParentAccountItem(accountsubitem);
        AmsDicAccountItem accountitemsecd = getParentAccountItem(accountitemthird);
        AmsDicAccountItem accountitemfirst = getParentAccountItem(accountitemsecd);
        AmsEntryItemflow entryItemFlow = new AmsEntryItemflow();
        entryItemFlow.setOrderno(orderno);
        entryItemFlow.setBusinessid(businessid);
        entryItemFlow.setAccountitemfirstname(accountitemfirst.getName());
        entryItemFlow.setAccountitemfirstcode(accountitemfirst.getCode());
        entryItemFlow.setAccountitemsecdname(accountitemsecd.getName());
        entryItemFlow.setAccountitemsecdcode(accountitemsecd.getCode());
        entryItemFlow.setAccountitemthirdname(accountitemthird.getName());
        entryItemFlow.setAccountitemthirdcode(accountitemthird.getCode());
        entryItemFlow.setAccountsubitemname(accountsubitem.getName());
        entryItemFlow.setAccountsubitemcode(accountsubitem.getCode());
        entryItemFlow.setType(Integer.valueOf(accountsubitem.getField1()));
        entryItemFlow.setBalancedirect(balanceDirectEnum.getValue());
        entryItemFlow.setAmont(amont);
        entryItemFlow.setAccountdate(accountdate);
        entryItemFlow.setStatementdate(statementdate);
        entryItemFlow.setBindaccountdate(bindaccountdate);
        entryItemFlow.setCreatedate( DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
        if (!creatEntryItemFlow(entryItemFlow)) {
            log.error("创建内部科目流水失败");
        }
    }

}
