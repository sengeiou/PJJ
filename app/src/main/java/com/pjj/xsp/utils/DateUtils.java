package com.pjj.xsp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Create by xinheng on 2018/11/16。
 * describe：
 */
public class DateUtils {
    public static final SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    public static final SimpleDateFormat sfDay = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

    public static long getNowTimeL() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }

    public static String getNowTimeS() {
        return sf.format(new Date());
    }

    public static String getNowDate() {
        return sfDay.format(new Date());
    }

    /**
     * @param date 格式yyyy-MM-dd
     * @return 是否为当天
     */
    public static boolean isNowDate(String date) {
        if (TextViewUtils.isEmpty(date)) {
            return false;
        }
        Calendar calendar = Calendar.getInstance();
        String[] dates = date.split("-");
        if (dates != null && dates.length == 3) {
            if (calendar.get(Calendar.YEAR) == changeInt(dates[0]) &&
                    (calendar.get(Calendar.MONTH) + 1) == changeInt(dates[1]) &&
                    calendar.get(Calendar.DATE) == changeInt(dates[2])) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNowDate(String date, String hour) {
        if (TextViewUtils.isEmpty(date)) {
            return false;
        }
        Calendar calendar = Calendar.getInstance();
        String[] dates = date.split("-");
        if (dates != null && dates.length == 3) {
            if (calendar.get(Calendar.YEAR) == changeInt(dates[0]) &&
                    (calendar.get(Calendar.MONTH) + 1) == changeInt(dates[1]) &&
                    calendar.get(Calendar.DATE) == changeInt(dates[2]) &&
                    calendar.get(Calendar.HOUR_OF_DAY) == changeInt(hour)) {
                return true;
            }
        }
        return false;
    }

    public static int changeInt(String num) {
        try {
            return Integer.parseInt(num);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static boolean lessTheMin(int theMin) {
        int min = Calendar.getInstance().get(Calendar.MINUTE);
        return min < theMin;
    }

    public static boolean lessNowDate(String date) {
        return isDate1less2(date, sfDay.format(new Date()));
    }

    /**
     * 日期比较
     * 格式 yyyy-MM-dd
     *
     * @param date1
     * @param date2
     * @return date1 < date2 ->true
     */
    public static boolean isDate1less2(String date1, String date2) {
        Date parse1 = null;
        try {
            parse1 = sfDay.parse(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date parse2 = null;
        try {
            parse2 = sfDay.parse(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (null == parse1 || null == parse2) {
            //删除
            return true;
        }
        return parse1.before(parse2);
    }
}
