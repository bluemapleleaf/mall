package com.macro.mall.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.base.Strings;
import com.macro.mall.ams.model.AmsCustomeridSerial;
import com.macro.mall.ams.service.AmsCustomeridSerialRepository;
import com.macro.mall.enums.AccountTypeEnum;
import com.macro.mall.enums.CustomerType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 编码工厂
 *
 * @author dongjb
 * @date 2021/01/05
 */
@Slf4j
@Service
public class CodeFactory {

    @Autowired
    static AmsCustomeridSerialRepository amsCustomeridSerialRepository;
    /**
     * 生成C户客户号
     *
     * @return C户客户号
     */
    public static String generateCustId(String regioncode) {
        LocalDateTime localDateTime = LocalDateTime.now();
        String timestamp = localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));

        LambdaQueryWrapper<AmsCustomeridSerial> lambda = new LambdaQueryWrapper<>();
        lambda.eq(AmsCustomeridSerial::getRegioncode, regioncode)
                .eq(AmsCustomeridSerial::getCusttype, CustomerType.CUSTOMER.getValue())
                .eq(AmsCustomeridSerial::getTimestamp, timestamp);
        AmsCustomeridSerial customerIdSerial = amsCustomeridSerialRepository.getOne(lambda);

        int number = 1;
        if (customerIdSerial != null) {
            number = customerIdSerial.getNumber();
            customerIdSerial.setNumber(number + 1);
        } else {
            customerIdSerial = new AmsCustomeridSerial();
            customerIdSerial.setRegioncode(regioncode);
            customerIdSerial.setCusttype(CustomerType.CUSTOMER.getValue());
            customerIdSerial.setTimestamp(timestamp);
            customerIdSerial.setNumber(number + 1);
            customerIdSerial.setCreatedate(timestamp);
            customerIdSerial.setModifydate(timestamp);
        }
        if (!amsCustomeridSerialRepository.save(customerIdSerial)){
            log.error("生成客户号失败！");
            return null;
        }
        return regioncode + CustomerType.CUSTOMER.getValue() + timestamp + Strings.padStart(number + "", 3, '0');
    }

    /**
     * 生成C户虚拟账户编码
     *
     * @param custid      C户客户号
     * @param accountType 虚拟账户类型
     * @return 虚拟账户编码
     */
    public static String generateCustSubCode(String custid, AccountTypeEnum accountType) {
        return custid + accountType.getCode() + (int) (Math.random() * 900 + 100);
    }

    /**
     * 生成订单号
     *
     * @param ordertype 订单类型
     * @return 订单号
     */
    public static String generateOrderNo(int ordertype) {
        String ordertypeStr = Strings.padEnd(ordertype + "", 4, '0');
        LocalDateTime localDateTime = LocalDateTime.now();
        String randomStr = (int)((Math.random()*9+1)*100000) + "0";
        String timeStamp = localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        return ordertypeStr + timeStamp + randomStr;
    }

    /**
     * 生成B户内部账号
     *
     * @param channel              B户渠道号
     * @param accountitemfirstcode 一级科目编码
     * @param accountitemsecdcode  二级科目编码
     * @param accountitemthirdcode 三级科目编码
     * @param accountsubitemcode   科目下立子账户编码
     * @return B户内部账号
     */
    public static String generateBusinessInnerId(String channel, String accountitemfirstcode, String accountitemsecdcode, String accountitemthirdcode, String accountsubitemcode) {
        if (!accountitemsecdcode.startsWith(accountitemfirstcode)) {
            log.error("科目编码不符合规范：二级科目编号前缀必须是一级科目编号");
        }
        if (!accountitemthirdcode.startsWith(accountitemsecdcode)) {
            log.error("科目编码不符合规范：三级科目编号前缀必须是二级科目编号");
        }
        return channel + accountitemthirdcode + accountsubitemcode;
    }

}
