package com.ascend.wangfeng.inventory;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by fengye on 2017/9/27.
 * email 1040441325@qq.com
 */

public class Convert {
    public static String convertMoney(double price){
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.CHINA);
        // 把转换后的货币String类型返回
        String numString = format.format(price);
        return numString;
    }

}
