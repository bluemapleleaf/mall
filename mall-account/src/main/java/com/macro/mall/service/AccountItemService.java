package com.macro.mall.service;

import com.macro.mall.ams.model.AmsDicAccountItem;
import com.macro.mall.ams.model.AmsEntryItemflow;
import com.macro.mall.ams.service.AmsDicAccountItemRepository;
import com.macro.mall.enums.BalanceDirectEnum;

import java.math.BigDecimal;

public interface AccountItemService extends AmsDicAccountItemRepository {

    /**
     * 获取可用的会计科目
     *
     * @param parentcode 第三级科目编码
     * @param code       第四级序号
     * @return 会计科目子账户对象
     */
    AmsDicAccountItem getSubAccountItem(String parentcode, String code);

    /**
     * 获取可用的会计科目
     *
     * @param code 第四级科目编码
     * @return 会计科目子账户对象
     */
    AmsDicAccountItem getAccountItem(String code);

    /**
     * 获取可用的会计科目
     *
     * @param item 任意级别科目编码
     * @return 会计科目子账户对象
     */
    AmsDicAccountItem getParentAccountItem(AmsDicAccountItem item);

    /**
     * 获取会计科目流水
     *
     * @param flow 会计科目流水
     * @return 是否成功
     */
    boolean creatEntryItemFlow(AmsEntryItemflow flow);

    /**
     * 创建会计分录流水
     *
     * @param orderno           交易订单号
     * @param businessid        会计主体B户id
     * @param amont             发生额
     * @param accountdate       会计日期
     * @param statementdate     会计出账日期
     * @param bindaccountdate   会计扎账日期
     * @param accountsubitem    第四级科目对象
     * @param balanceDirectEnum 余额方向
     */
    void doRecord(String orderno, String businessid, BigDecimal amont, String accountdate, String statementdate, String bindaccountdate, AmsDicAccountItem accountsubitem, BalanceDirectEnum balanceDirectEnum);


    }
