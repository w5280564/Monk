package com.xunda.lib.common.common.utils;

import java.math.BigDecimal;


/**
 * double float相加减不丢失精度的工具类
 */
public class ArithUtil {
    public static final int DEF_DIV_SCALE=10;

    private ArithUtil(){}


    /**
     * 加
     * @param d1
     * @param d2
     * @return
     */
    public static double add(double d1,double d2){
        BigDecimal b1=new BigDecimal(Double.toString(d1));
        BigDecimal b2=new BigDecimal(Double.toString(d2));
        return b1.add(b2).doubleValue();

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
    public static double mul(double d1,double d2){
        BigDecimal b1=new BigDecimal(Double.toString(d1));
        BigDecimal b2=new BigDecimal(Double.toString(d2));
        return b1.multiply(b2).doubleValue();

    }


    public static double mul_String(String d1,String d2){
        BigDecimal b1=new BigDecimal(d1);
        BigDecimal b2=new BigDecimal(d2);
        return b1.multiply(b2).floatValue();
    }


    public static double div(double d1,double d2){

        return div(d1,d2,DEF_DIV_SCALE);

    }


    /**
     * 除
     * @param d1
     * @param d2
     * @param scale
     * @return
     */
    public static double div(double d1,double d2,int scale){
        if(scale<0){
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1=new BigDecimal(Double.toString(d1));
        BigDecimal b2=new BigDecimal(Double.toString(d2));
        return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();

    }


    /**
     * 除
     * @param d1
     * @param d2
     * @param scale
     * @return
     */
    public static double div_string(String d1,String d2,int scale){
        if(scale<0){
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1=new BigDecimal(d1);
        BigDecimal b2=new BigDecimal(d2);
        return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).floatValue();

    }
}
