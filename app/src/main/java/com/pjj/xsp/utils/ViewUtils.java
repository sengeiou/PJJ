package com.pjj.xsp.utils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;

import com.pjj.xsp.PjjApplication;

/**
 * Created by XinHeng on 2019/01/23.
 * describe：
 */
public class ViewUtils {
    public static Drawable getBgDrawable(int color, int radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(radius);
        return drawable;
    }
    public static int getColor(int resource){
        return ContextCompat.getColor(PjjApplication.application,resource);
    }
}
