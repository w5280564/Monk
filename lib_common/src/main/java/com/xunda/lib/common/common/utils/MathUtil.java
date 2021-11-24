package com.xunda.lib.common.common.utils;

import java.math.BigDecimal;

public class MathUtil {


    public static double div(Double d1, long d2) {

        BigDecimal b1=new BigDecimal(Double.toString(d1));
        BigDecimal b2=new BigDecimal(Long.toString(d2));

        return b1.divide(b2,2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }



    /**
     * 减
     * @param d1
     * @param d2
     * @return
     */
    public static double sub(double d1,double d2){
        BigDecimal b1=new BigDecimal(Double.toString(d1));
        BigDecimal b2=new BigDecimal(Double.toString(d2));
        return b1.subtract(b2).doubleValue();

    }


    /**
     * 乘
     * @param d1
     * @param d2
     * @return
     */
    public static double mul(long d1,double d2){
        BigDecimal b1=new BigDecimal(Long.toString(d1));
        BigDecimal b2=new BigDecimal(Double.toString(d2));
        return b1.multiply(b2).doubleValue();

    }
}
