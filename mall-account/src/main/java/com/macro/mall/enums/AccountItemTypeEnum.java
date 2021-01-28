package com.macro.mall.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 会计科目类型
 *
 * @author dongjb
 * @date 2021/01/17
 */
public enum AccountItemTypeEnum {

    /**
     * 会计科目类型
     */

    //借
    debits(1),

    //贷
    credits(2),

    //共同
    common(3);

    private final Integer value;

    private final static Map<Integer, AccountItemTypeEnum> ENUM_MAP;

    static {
        ENUM_MAP = new HashMap<>();
        for (AccountItemTypeEnum type : values()) {
            ENUM_MAP.put(type.getValue(), type);
        }
    }

    AccountItemTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public Boolean equals(Integer value) {
        return this.value.equals(value);
    }

    public static AccountItemTypeEnum getEnum(Integer value){
        return ENUM_MAP.getOrDefault(value, null);
    }

}
