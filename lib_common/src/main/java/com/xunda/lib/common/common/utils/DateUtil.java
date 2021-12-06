package com.xunda.lib.common.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日期处理工具类
 *
 * @author ouyang 说明：对日期格式的格式化与转换操作等一系列操作
 */
public class DateUtil {

    public static final String PATTERN_STANDARD16H = "yyyy-MM-dd HH:mm";
    public static final String PATTERN_STANDARD19H = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_FORMAT = "yyyy-MM-dd";
    public static final String Date_String = "MM-dd HH:mm";


    /**
     * 日期转成字符串
     */
    public static String dateToString(String strFormat, Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(strFormat);
        return dateFormat.format(date);
    }


    /**
     * 日期转换成字符串
     */
    public static String DateToStr(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(PATTERN_STANDARD19H);
        String str = format.format(date);
        return str;
    }


    /**
     * 默认的时间戳转成日期
     */
    public static String dateToString(long longDate) {

        return dateToString(longDate, DEFAULT_FORMAT);
    }


    /**
     * 时间戳转成日期
     */
    public static String dateToString(long longDate, String format) {
        if (StringUtil.isBlank(format)) {
            return dateToString(PATTERN_STANDARD19H, new Date(longDate));
        } else {
            return dateToString(format, new Date(longDate));
        }
    }



    /**
     * 时间转化为时间戳
     * @param dateValue 日期对象
     * @return
     */
    public static long stringToLongDate(String strFormat, String dateValue) {
        if (dateValue == null)
            return 0;
        if (strFormat == null)
            strFormat = PATTERN_STANDARD19H;

        SimpleDateFormat dateFormat = new SimpleDateFormat(strFormat);
        Date newDate = null;
        long time = 0;
        try {
            newDate = dateFormat.parse(dateValue);
            time = newDate.getTime() / 1000;// 所以要除以1000
        } catch (ParseException e) {
            newDate = null;
            e.printStackTrace();
        }
        return time;
    }


    /**
     * 日期转化为long时间戳
     */
    public static Long Date2Long(Date date) {

        return date.getTime()/1000;
    }




    /**
     * 获取当前日期，格式：yyyy-MM-dd
     *
     * @return
     */
    public static String getNowDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT);
        return sdf.format(new Date());
    }

    /**
     * 获取当前日期，格式：yyyy-MM-dd HH:mm
     *
     * @return
     */
    public static String getNowDateHourAndMinute() {
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_STANDARD16H);
        return sdf.format(new Date());
    }

    /**
     * 获取当前时间10分钟以后的时间，格式：yyyy-MM-dd HH:mm
     *
     * @return
     */
    public static String getNowDateHourAndMinute10Later() {
        Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE,10);//当前时间往后推10分钟
        Date date = calendar.getTime();
        return dateToString(PATTERN_STANDARD16H,date);
    }

    public static String getDateString(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(Date_String);
        return sdf.format(date);
    }

    public static String getUserDate(String sformat) {
        String dateString = "2017-03-28";
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(sformat);//先转固定格式
            dateString = new SimpleDateFormat("MM-dd HH:mm").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateString;
    }

    /**
     * 计算两个日期间隔天数
     *
     * @param begin
     * @param end
     * @return
     */
    public static int countDays(Date begin, Date end) {
        int days = 0;
        Calendar c_b = Calendar.getInstance();
        Calendar c_e = Calendar.getInstance();
        c_b.setTime(begin);
        c_e.setTime(end);
        while (c_b.before(c_e)) {
            days++;
            c_b.add(Calendar.DAY_OF_YEAR, 1);
        }
        return days;
    }




    /**
     *  获取当前年
     */
    public static String getCurrentYear() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy");// 设置日期格式
        return df.format(new Date());
    }


    /**
     * 计算距离当前日期间隔天数
     */
    public static int countDays(Date date) {
        int days = 0;
        Date now = new Date();
        Calendar c_b = Calendar.getInstance();
        Calendar c_e = Calendar.getInstance();
        c_b.setTime(now);
        c_e.setTime(date);
        if (c_b.before(c_e)) {
            while (c_b.before(c_e)) {
                days++;
                c_b.add(Calendar.DAY_OF_YEAR, 1);
            }
        } else {
            while (c_e.before(c_b)) {
                days++;
                c_e.add(Calendar.DAY_OF_YEAR, 1);
            }
        }

        return days;
    }




    /**
     * 是否是日期，格式：yyyy-MM-dd
     *
     * @return
     */
    public static boolean isDate(String strDate) {
        Pattern pattern = Pattern
                .compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
        Matcher m = pattern.matcher(strDate);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }






    /**
     * 把时间戳转化成多久以前
     * @param millisecond
     * @return
     */
    public static String formatDate(long millisecond) {
        Date startTime = new Date(millisecond);
        Date nowDate = Calendar.getInstance().getTime();
        if (startTime == null || nowDate == null) {
            return null;
        }
        long timeLong = nowDate.getTime() - millisecond;
        if (timeLong <= 20 * 1000) {
            return "刚刚";
        } else if (timeLong < 60 * 1000)
            return timeLong / 1000 + "秒前";
        else if (timeLong < 60 * 60 * 1000) {
            timeLong = timeLong / 1000 / 60;
            return timeLong + "分钟前";
        } else if (timeLong < 60 * 60 * 24 * 1000) {
            timeLong = timeLong / 60 / 60 / 1000;
            return timeLong + "小时前";
        } else if (timeLong < 60 * 60 * 24 * 1000 * 7) {
            timeLong = timeLong / 1000 / 60 / 60 / 24;
            return timeLong + "天前";
        } else if (timeLong < 60 * 60 * 24 * 1000 * 7 * 4) {
            timeLong = timeLong / 1000 / 60 / 60 / 24 / 7;
            return timeLong + "周前";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT,
                    Locale.getDefault());
            return sdf.format(startTime);
        }
    }


    public static int compareDate(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat(PATTERN_STANDARD16H);
        int type = -1;
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                type = 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                type = -1;
            } else {
                type = 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return type;
    }

}
