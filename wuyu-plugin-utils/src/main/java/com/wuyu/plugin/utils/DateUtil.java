/*--------------------------------------------------------------------------
 *  Copyright (c) 2009-2020, www.wuyushuo.com All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the yinyuetai developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: git_wuyu@163.com (tencent qq: 2094998377)
 *--------------------------------------------------------------------------
*/
package com.wuyu.plugin.utils;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class DateUtil {

    public static final String[] ASTROLOGY_NAMES = {
            "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座", "水瓶座", "双鱼座"
    };

    private static String[] ZODIAC_NAMES = {
            "鼠", "猪", "狗", "鸡", "猴", "羊", "马", "蛇", "龙", "兔", "虎", "牛"
    };

    public static final long MILLIS_IN_ONE_MINUTE = 1000 * 60;
    public static final long MILLIS_IN_ONE_HOUR = MILLIS_IN_ONE_MINUTE * 60;
    public static final long MILLIS_IN_ONE_DAY = MILLIS_IN_ONE_HOUR * 24;
    public static final long MILLIS_IN_ONE_YEAR = MILLIS_IN_ONE_DAY * 365;

    public static final FastDateFormat JAVA_DATE_TIME_FORMAT_FULL = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
    public static final FastDateFormat JAVA_DATE_TIME_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm");
    public static final FastDateFormat JAVA_DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd");


    /**
     * 获取当前时间
     * @return
     */
    public static String getNowTime() {
        return getNowTime(JAVA_DATE_TIME_FORMAT);
    }

    /**
     * 获取当前时间
     * @return
     */
    public static String getNowTime(FastDateFormat fastDateFormat) {
        return fastDateFormat.format(new Date());
    }

    /**
     * 获取当前时间
     * @param pattern
     * @return
     */
    public static String getNowTime(String pattern) {
        return FastDateFormat.getInstance(pattern).format(new Date());
    }

    /**
     * 构造日期
     * @param year
     * @param month
     * @return
     */
    public static Date toDate(int year, int month) {
        return (new DateTime(year, month, 1, 0, 0, 0, 0)).toDate();
    }

    /**
     * 构造日期
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static Date toDate(int year, int month, int day) {
        try {
            LocalDate ex = new LocalDate(year, month, day);
            return ex.toDate();
        } catch (Exception var5) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(1, year);
            calendar.set(2, month - 1);
            calendar.set(5, day);
            calendar.set(5, day);
            calendar.set(11, 0);
            calendar.set(12, 0);
            calendar.set(13, 0);
            calendar.set(14, 0);
            return new Date(calendar.getTimeInMillis());
        }
    }

    /**
     * 构造日期
     * @param year
     * @param month
     * @param day
     * @param hour
     * @return
     */
    public static Date toDate(int year, int month, int day, int hour) {
        try {
            return (new DateTime(year, month, day, hour, 0, 0, 0)).toDate();
        } catch (Exception var6) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(1, year);
            calendar.set(2, month - 1);
            calendar.set(5, day);
            calendar.set(5, day);
            calendar.set(11, hour);
            calendar.set(12, 0);
            calendar.set(13, 0);
            calendar.set(14, 0);
            return new Date(calendar.getTimeInMillis());
        }
    }

    /**
     * 构造日期
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @return
     */
    public static Date toDate(int year, int month, int day, int hour, int minute) {
        return (new DateTime(year, month, day, hour, minute, 0)).toDate();
    }

    /**
     * 日期格式化
     * @param time
     * @return
     */
    public static String formatDate(long time){
        return formatDateTime(time, JAVA_DATE_FORMAT);
    }

    /**
     * 日期格式化
     * @param time
     * @return
     */
    public static String formatDateTime(long time){
        return formatDateTime(time, JAVA_DATE_TIME_FORMAT);
    }

    /**
     * 日期格式化
     * @param time
     * @param format
     * @return
     */
    public static String formatDateTime(long time, String format){
        return formatDateTime(time, FastDateFormat.getInstance(format));
    }

    /**
     * 日期格式化
     * @param time
     * @param format
     * @return
     */
    public static String formatDateTime(long time, FastDateFormat format){
        return format.format(time);
    }

    /**
     * 日期格式化
     * @param date
     * @return
     */
    public static String formatDateTime(Date date){
        return formatDateTime(date, JAVA_DATE_TIME_FORMAT);
    }

    /**
     * 日期格式化
     * @param date
     * @return
     */
    public static String formatDate(Date date){
        return formatDateTime(date, JAVA_DATE_FORMAT);
    }

    /**
     * 日期格式化
     * @param date
     * @param format
     * @return
     */
    public static String formatDateTime(Date date, String format){
        if(null == date){
            return "";
        }
        return formatDateTime(date, FastDateFormat.getInstance(format));
    }

    /**
     * 日期格式化
     * @param date
     * @param format
     * @return
     */
    public static String formatDateTime(Date date, FastDateFormat format){
        if(null == date){
            return "";
        }
        return format.format(date);
    }

    /**
     * 格式化日期：17分钟前, 26小时前, 3天前 ,6年前
     * @param date
     * @return
     */
    public static String fuzzyDateTime(Date date) {
        if (null == date) {
            return "";
        }
        // 超过30天的时间 按照默认日期格式化
        return fuzzyDateTime(date.getTime(), MILLIS_IN_ONE_DAY * 30);
    }

    /**
     * 格式化日期：17分钟前, 26小时前, 3天前 ,6年前
     * @param time
     * @return
     */
    public static String fuzzyDateTime(long time) {
        long beforeNow = System.currentTimeMillis() - time;
        if (beforeNow < 1000) {
            return "1秒前";
        }
        // 超过30天的时间 按照默认日期格式化
        return fuzzyDateTime(beforeNow, MILLIS_IN_ONE_DAY * 30);
    }

    /**
     * 格式化日期：17分钟前, 26小时前, 3天前 ,6年前, 当时间间隔大于等于formatThreshold时,使用格式化的日期
     * @param date
     * @param formatThreshold 格式化时间的间隔时间短
     * @return
     */
    public static String fuzzyDateTime(Date date, long formatThreshold) {
        return fuzzyDateTime(date, formatThreshold, JAVA_DATE_TIME_FORMAT);
    }

    /**
     * 格式化日期：17分钟前, 26小时前, 3天前 ,6年前, 当时间间隔大于等于formatThreshold时,使用格式化的日期
     * @param date
     * @param formatThreshold 格式化时间的间隔时间短
     * @param format
     * @return
     */
    public static String fuzzyDateTime(Date date, long formatThreshold, FastDateFormat format) {
        if (null == date) {
            return "";
        }
        long beforeNow = System.currentTimeMillis() - date.getTime();
        if (beforeNow < formatThreshold) {
            return fuzzyMillisTime(beforeNow);
        } else {
            return format.format(date);
        }
    }

    /**
     * 格式化日期：17分钟前, 26小时前, 3天前 ,6年前, 当时间间隔大于等于formatThreshold时,使用格式化的日期
     * @param time
     * @param formatThreshold 格式化时间的间隔时间短
     * @return
     */
    public static String fuzzyDateTime(long time, long formatThreshold) {
        return fuzzyDateTime(time, formatThreshold, JAVA_DATE_TIME_FORMAT);
    }

    /**
     * 格式化日期：17分钟前, 26小时前, 3天前 ,6年前, 当时间间隔大于等于formatThreshold时,使用格式化的日期
     * @param time
     * @param formatThreshold 格式化时间的间隔时间短
     * @param format
     * @return
     */
    public static String fuzzyDateTime(long time, long formatThreshold, FastDateFormat format) {
        long beforeNow = System.currentTimeMillis() - time;
        if (beforeNow < formatThreshold) {
            return fuzzyMillisTime(beforeNow);
        } else {
            return format.format(new Date(time));
        }
    }

    /**
     * 原实现做的乘除法过多,影响效率,且几个方法的代码重复
     * @param intervalMillis 据现在的时间间隔
     * @return
     */
    private static String fuzzyMillisTime(long intervalMillis) {
        if (intervalMillis <= 0) {
            return "1秒前";
        } else if (intervalMillis < MILLIS_IN_ONE_MINUTE) {
            return intervalMillis / 1000 + "秒前";
        } else if (intervalMillis < MILLIS_IN_ONE_HOUR) {
            return intervalMillis / MILLIS_IN_ONE_MINUTE + "分钟前";
        } else if (intervalMillis < MILLIS_IN_ONE_DAY) {
            return intervalMillis / MILLIS_IN_ONE_HOUR + "小时前";
        } else if (intervalMillis < MILLIS_IN_ONE_YEAR){
            return intervalMillis / MILLIS_IN_ONE_DAY + "天前";
        } else{
            return intervalMillis / MILLIS_IN_ONE_YEAR + "年前";
        }
    }

    /**
     * 获取星座信息
     * @param date
     * @return
     */
    public static String getAstrologyName(Date date) {
        int index = getAstrologyByDate(date);
        if (index < 0 || index > 11) {
            return "未知";
        } else {
            return ASTROLOGY_NAMES[index];
        }
    }

    /**
     * 根据日期返回所属星座的 index
     * 0  - 白羊座
     * 1  - 金牛座
     * 2  - 双子座
     * 3  - 巨蟹座
     * 4  - 狮子座
     * 5  - 处女座
     * 6  - 天秤座
     * 7  - 天蝎座
     * 8  - 射手座
     * 9  - 摩羯座
     * 10 - 水瓶座
     * 11 - 双鱼座
     * -1 - 不知道啥星座（不应当发生）
     *
     * @param date
     * @return
     */
    private static int getAstrologyByDate(Date date) {
        DateTime dateTime = new DateTime(date);
        int month = dateTime.getMonthOfYear();
        int day = dateTime.getDayOfMonth();
        if (month == 3 && day >= 21 || month == 4 && day <= 20) {
            return 0;
        } else if (month == 4 && day >= 21 || month == 5 && day <= 21) {
            return 1;
        } else if (month == 5 && day >= 22 || month == 6 && day <= 21) {
            return 2;
        } else if (month == 6 && day >= 22 || month == 7 && day <= 22) {
            return 3;
        } else if (month == 7 && day >= 23 || month == 8 && day <= 23) {
            return 4;
        } else if (month == 8 && day >= 24 || month == 9 && day <= 23) {
            return 5;
        } else if (month == 9 && day >= 24 || month == 10 && day <= 23) {
            return 6;
        } else if (month == 10 && day >= 24 || month == 11 && day <= 22) {
            return 7;
        } else if (month == 11 && day >= 23 || month == 12 && day <= 21) {
            return 8;
        } else if (month == 12 && day >= 22 || month == 1 && day <= 20) {
            return 9;
        } else if (month == 1 && day >= 21 || month == 2 && day <= 19) {
            return 10;
        } else if (month == 2 && day >= 20 || month == 3 && day <= 20) {
            return 11;
        } else {
            return -1;
        }
    }

    /**
     * 获取生肖
     * @param year
     * @return
     */
    public static String getChineseZodiac(int year) {
        Calendar ca = Calendar.getInstance();
        int now = ca.get(Calendar.YEAR);
        int index = (now - year - 1) % 12 - 1;
        if (index > 0) {
            return ZODIAC_NAMES[index];
        } else if (index == 0) {
            return ZODIAC_NAMES[11];
        } else {
            return ZODIAC_NAMES[12 - (-index)];
        }
    }

    /**
     * 获取星期
     * @param calendar
     * @return
     */
    public static String getDayOfWeek(Calendar calendar) {
        int dayth = calendar.get(Calendar.DAY_OF_WEEK);
        String dayOfWeek = "";
        switch (dayth) {
            case 1:
                dayOfWeek = "星期天";
                break;
            case 2:
                dayOfWeek = "星期一";
                break;
            case 3:
                dayOfWeek = "星期二";
                break;
            case 4:
                dayOfWeek = "星期三";
                break;
            case 5:
                dayOfWeek = "星期四";
                break;
            case 6:
                dayOfWeek = "星期五";
                break;
            case 7:
                dayOfWeek = "星期六";
                break;
        }

        return dayOfWeek;
    }

    public static Date parseDate(String str, String... parsePatterns) throws ParseException {
        return DateUtils.parseDate(str, parsePatterns);
    }

    /**
     * 获取随机日期
     * @param beginDate 起始日期，格式为：yyyy-MM-dd
     * @param endDate 结束日期，格式为：yyyy-MM-dd
     * @return
     */

    public static Date randomDate(String beginDate, String endDate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date start = format.parse(beginDate);// 构造开始日期
            Date end = format.parse(endDate);// 构造结束日期
            // getTime()表示返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。
            if (start.getTime() >= end.getTime()) {
                return null;
            }
            long date = random(start.getTime(), end.getTime());

            return new Date(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static long random(long begin, long end) {
        long rtn = begin + (long) (Math.random() * (end - begin));
        // 如果返回的是开始时间和结束时间，则递归调用本函数查找随机值
        if (rtn == begin || rtn == end) {
            return random(begin, end);
        }
        return rtn;
    }

}
