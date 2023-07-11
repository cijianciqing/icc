package com.nanshan.icc;

import java.math.BigDecimal;

public class Tt {

    public static void main(String[] args) {
        int a = 0;
        int b = 10;

        BigDecimal result = new BigDecimal(a).divide(new BigDecimal(b),BigDecimal.ROUND_UP);
        int roundedValue = result.intValue();

        System.out.println(roundedValue); // 输出结果为 3
    }

}
