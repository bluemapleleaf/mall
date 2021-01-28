package com.macro.mall.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.macro.mall.ams.model.AmsBusinessAccount;
import com.macro.mall.ams.model.AmsBusinessBindinfo;
import com.macro.mall.ams.model.AmsBusinessSubaccount;
import com.macro.mall.ams.model.AmsDicBBindtype;
import com.macro.mall.ams.service.AmsBusinessBindinfoRepository;
import com.macro.mall.ams.service.AmsBusinessSubaccountRepository;
import com.macro.mall.ams.service.AmsDicBBindtypeRepository;
import com.macro.mall.ams.service.impl.AmsBusinessAccountRepositoryImpl;
import com.macro.mall.convert.BusinessAccountConvert;
import com.macro.mall.domain.BusinessAccount;
import com.macro.mall.enums.DefaultEnum;
import com.macro.mall.enums.StatusEnum;
import com.macro.mall.service.BusinessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * B户服务
 *
 * @author dongjb
 * @date 2021/01/06
 */
@Slf4j
@Service
public class BusinessServiceImpl extends AmsBusinessAccountRepositoryImpl implements BusinessService {
    @Autowired
    AmsBusinessSubaccountRepository amsBusinessSubaccountRepository;

    @Autowired
    AmsBusinessBindinfoRepository amsBusinessBindinfoRepository;

    @Autowired
    AmsDicBBindtypeRepository amsDicBBindtypeRepository;

    @Override
    public String getBusinessKeyById(String id) {
        AmsBusinessAccount businessAccount = getById(id);
        if (businessAccount != null) {
            if (businessAccount.getStatus().equals(StatusEnum.ACTIVATE.getValue())) {
                return businessAccount.getBusinesskey();
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    @Override
    public List<BusinessAccount> findBusinessAccount() {
        List<AmsBusinessAccount> amsBusinessAccountList =  list();
        return amsBusinessAccountList.stream().map(amsBusinessAccount -> {
            BusinessAccount businessAccount = BusinessAccountConvert.INSTANCE.amsBusinessAccount2BusinessAccount(amsBusinessAccount);
            businessAccount.setSubAccounts(findBusinessSubaccountByBusinessid(amsBusinessAccount.getBusinessid()));
            businessAccount.setBindInfos(getBusinessBindinfoByBusinessid(amsBusinessAccount.getBusinessid()));
            return businessAccount;
        }).collect(Collectors.toList());
    }

    @Override
    public BusinessAccount findBusinessAccountById(String id) {
        AmsBusinessAccount amsBusinessAccount = getById(id);
        BusinessAccount businessAccount = BusinessAccountConvert.INSTANCE.amsBusinessAccount2BusinessAccount(amsBusinessAccount);
        businessAccount.setSubAccounts(findBusinessSubaccountByBusinessid(businessAccount.getBusinessid()));
        businessAccount.setBindInfos(getBusinessBindinfoByBusinessid(businessAccount.getBusinessid()));
        return businessAccount;
    }

    @Override
    public BusinessAccount findBusinessAccountByBusinessId(String businessId) {
        LambdaQueryWrapper<AmsBusinessAccount> lambda = new LambdaQueryWrapper<>();
        lambda.eq(AmsBusinessAccount::getBusinessid, businessId);
        AmsBusinessAccount amsBusinessAccount = getOne(lambda);
        BusinessAccount businessAccount = BusinessAccountConvert.INSTANCE.amsBusinessAccount2BusinessAccount(amsBusinessAccount);
        businessAccount.setSubAccounts(findBusinessSubaccountByBusinessid(businessAccount.getBusinessid()));
        businessAccount.setBindInfos(getBusinessBindinfoByBusinessid(businessAccount.getBusinessid()));
        return businessAccount;
    }

    @Override
    public AmsBusinessBindinfo findBusinessDefaultBindInfoByBusinessId(String businessid) {
        BusinessAccount businessAccount = findBusinessAccountByBusinessId(businessid);
        List<AmsBusinessBindinfo> businessBindInfos = businessAccount.getBindInfos();
        return businessBindInfos.stream().filter(businessBindInfo ->
            businessBindInfo.getType().equals(businessAccount.getSettlementtype()) &&
            businessBindInfo.getStatus().equals(StatusEnum.ACTIVATE.getValue())
        ).findFirst().get();
    }

    @Override
    public AmsBusinessBindinfo findBusinessBindInfoById(String bindInfoId) {
        return amsBusinessBindinfoRepository.getById(bindInfoId);
    }

    @Override
    public BusinessAccount findOperBusinessAccount() {
        LambdaQueryWrapper<AmsBusinessAccount> lambda = new LambdaQueryWrapper<>();
        lambda.eq(AmsBusinessAccount::getIsdefault, DefaultEnum.ISDEFAULT.getValue())
                .eq(AmsBusinessAccount::getStatus, StatusEnum.ACTIVATE.getValue());
        AmsBusinessAccount amsBusinessAccount = getOne(lambda);
        BusinessAccount businessAccount = BusinessAccountConvert.INSTANCE.amsBusinessAccount2BusinessAccount(amsBusinessAccount);
        businessAccount.setSubAccounts(findBusinessSubaccountByBusinessid(businessAccount.getBusinessid()));
        businessAccount.setBindInfos(getBusinessBindinfoByBusinessid(businessAccount.getBusinessid()));
        return businessAccount;
    }

    @Override
    public List<AmsDicBBindtype> findOperBusinessBindInfoType() {
        BusinessAccount businessAccount = findOperBusinessAccount();
        if (businessAccount != null) {
            List<AmsBusinessBindinfo> businessBindInfos = businessAccount.getBindInfos();
            StringBuilder codesB = new StringBuilder();
            businessBindInfos.stream().filter(businessBindInfo -> businessBindInfo.getStatus().equals(StatusEnum.ACTIVATE.getValue()))
                    .forEach(businessBindInfo -> codesB.append("'").append( businessBindInfo.getType()).append("',"));
            String codes = codesB.substring(0, codesB.length() - 1);


            LambdaQueryWrapper<AmsDicBBindtype> lambda = new LambdaQueryWrapper<>();
            lambda.eq(AmsDicBBindtype::getStatus, StatusEnum.ACTIVATE.getValue())
                    .in(AmsDicBBindtype::getCode, codes);
            return amsDicBBindtypeRepository.list(lambda);
        } else {
            return null;
        }
    }

    @Override
    public AmsBusinessBindinfo findOperBusinessBindInfo(String typecode) {
        BusinessAccount businessAccount = findOperBusinessAccount();
        if (businessAccount != null) {
            List<AmsBusinessBindinfo> businessBindInfos = businessAccount.getBindInfos();
            return businessBindInfos.stream().filter(businessBindInfo ->
                    businessBindInfo.getStatus().equals(StatusEnum.ACTIVATE.getValue())
                            && businessBindInfo.getType().equals(typecode))
                    .findFirst().get();
        } else {
            return null;
        }
    }

    @Override
    public List<AmsBusinessSubaccount> findBusinessSubaccountByBusinessid(String businessid) {
        LambdaQueryWrapper<AmsBusinessSubaccount> lambdaSubAccount = new LambdaQueryWrapper<>();
        lambdaSubAccount.eq(AmsBusinessSubaccount::getBusinessid, businessid)
                .eq(AmsBusinessSubaccount::getStatus, StatusEnum.ACTIVATE.getValue());
        return amsBusinessSubaccountRepository.list(lambdaSubAccount);
    }

    public List<AmsBusinessBindinfo> getBusinessBindinfoByBusinessid(String businessid) {
        LambdaQueryWrapper<AmsBusinessBindinfo> lambdaBindinfo = new LambdaQueryWrapper<>();
        lambdaBindinfo.eq(AmsBusinessBindinfo::getBusinessid, businessid);
        return amsBusinessBindinfoRepository.list(lambdaBindinfo);
    }

    @Override
    public boolean updateBusinessSubAccount(AmsBusinessSubaccount subaccount) {
        return amsBusinessSubaccountRepository.updateById(subaccount);
    }


}
