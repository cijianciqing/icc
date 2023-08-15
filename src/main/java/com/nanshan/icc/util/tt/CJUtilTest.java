package com.nanshan.icc.util.tt;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CJUtilTest {

        public static void main(String[] args) {
            // 获取当前时间
            LocalDateTime currentDateTime = LocalDateTime.now();

            // 获取7天前的精确时间
            LocalDateTime sevenDaysAgo = currentDateTime.minusDays(7);

            // 输出7天前的精确时间
            System.out.println("7天前的精确时间是：" + sevenDaysAgo.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
}
