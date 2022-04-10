package org.oss.focussnip.utils;

import java.util.Random;

/**
 * 生成订单 id 的类
 */
public class OrderIdUtil {
    private static final int LENGHT = 10; // 订单id长度为10
    public static String getOrderId() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < LENGHT; i++) {
            stringBuilder.append(new Random().nextInt(10));
        }
        return stringBuilder.toString();
    }
}
