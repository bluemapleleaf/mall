package com.macro.mall.util;

import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

/**
 * 工具类
 *
 * @author dongjb
 * @date 2021/01/13
 */
public class Utility {
    /**
     * 合并数组
     *
     * @param first  第一个数组
     * @param second 第二个数组
     * @return 合并后的目标数组
     */
    private static Field[] contact(Field[] first, Field[] second) {
        Field[] fields = new Field[first.length + second.length];
        System.arraycopy(first, 0, fields, 0, first.length);
        System.arraycopy(second, 0, fields, first.length, second.length);
        return fields;
    }

    /**
     * 判断金额是否合法
     * 金额规则：1、全数字；2、非负数；3、小数点后最多两位
     *
     * @param amont 金额字符串
     * @return true|false
     */
    public static boolean isAvailableAmont(String amont) {
        if (StringUtils.isEmpty(amont)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^(([1-9][0-9]+)|[0-9])(\\.[0-9]{1,2})?$");
        return pattern.matcher(amont).matches();
    }

}
