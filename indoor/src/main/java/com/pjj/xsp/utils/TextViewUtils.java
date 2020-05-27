package com.pjj.xsp.utils;

import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Create by xinheng on 2018/10/13。
 * describe：文本工具
 */
public class TextViewUtils {
    public static boolean isEmpty(String s) {
        return android.text.TextUtils.isEmpty(s);
    }

    /**
     * 清除null
     *
     * @param s 数据
     * @return 字符串 不为null
     */
    public static String clean(String s) {
        if (null == s) {
            return "";
        }
        return s;
    }



    private static boolean containsHour(String playTime, int nowHour) {
        String[] split = playTime.split(",");
        if (null != split) {
            for (int i = 0; i < split.length; i++) {
                if (String.valueOf(nowHour).equals(split[i])) {
                    return true;
                }
            }
        }
        return false;
    }



    public static float getBaseLine(int height, Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return height / 2f - fontMetrics.top / 2 - fontMetrics.bottom / 2;
    }

    public static int getTextHeight(String text, Paint paint) {
        return getRectText(text, paint).height();
    }

    public static Rect getRectText(String text, Paint paint) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds;
    }
}
