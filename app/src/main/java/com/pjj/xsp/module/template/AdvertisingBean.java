package com.pjj.xsp.module.template;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.pjj.xsp.PjjApplication;

/**
 * Create by xinheng on 2018/11/14。
 * describe：
 */
public class AdvertisingBean {

    private int imageResource = -1;
    private String imagePath;
    private String topTitleColor;
    private String topTitleTextColor;
    private String topTitleTextLineColor;
    private String topAdContentColor;

    private String diyBgColor;

    //电梯数据
    private String bottomElevatorBgColor;
    private String bottomElevatorTextColor;
    private String bottomImaBgColor;
    private String bottomImaColor;
    //日期以及汽车限号
    private String bottomMidBgColor;
    private String bottomDateTextColor;
    private String bottomWeekTextColor;
    private String bottomTimeTextColor;
    private String bottomCarTextColor;
    private String bottomCarNumColor;
    //在线、城市、天气
    private String bottomOnlineCityBgColor;
    private String bottomOnlineTagColor;
    private String bottomOnlineTextColor;
    private String bottomCityColor;
    private String bottomDayTemperatureBgColor;
    private String bottomDayColor;
    private String bottomTemperatureColor;
    private String bottomVLineColor;

    public AdvertisingBean() {
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setImagePathClear(String imagePath) {
        imageResource = -1;
        this.imagePath = imagePath;
    }

    public String getTopTitleColor() {
        return topTitleColor;
    }

    public void setTopTitleColor(String topTitleColor) {
        this.topTitleColor = topTitleColor;
    }

    public String getTopTitleTextColor() {
        return topTitleTextColor;
    }

    public void setTopTitleTextColor(String topTitleTextColor) {
        this.topTitleTextColor = topTitleTextColor;
    }

    public String getTopTitleTextLineColor() {
        return topTitleTextLineColor;
    }

    public void setTopTitleTextLineColor(String topTitleTextLineColor) {
        this.topTitleTextLineColor = topTitleTextLineColor;
    }

    public String getTopAdContentColor() {
        return topAdContentColor;
    }

    public void setTopAdContentColor(String topAdContentColor) {
        this.topAdContentColor = topAdContentColor;
    }

    public String getDiyBgColor() {
        return diyBgColor;
    }

    public void setDiyBgColor(String diyBgColor) {
        this.diyBgColor = diyBgColor;
    }

    public String getBottomElevatorBgColor() {
        return bottomElevatorBgColor;
    }

    public void setBottomElevatorBgColor(String bottomElevatorBgColor) {
        this.bottomElevatorBgColor = bottomElevatorBgColor;
    }

    public String getBottomElevatorTextColor() {
        return bottomElevatorTextColor;
    }

    public void setBottomElevatorTextColor(String bottomElevatorTextColor) {
        this.bottomElevatorTextColor = bottomElevatorTextColor;
    }

    public String getBottomImaBgColor() {
        return bottomImaBgColor;
    }

    public void setBottomImaBgColor(String bottomImaBgColor) {
        this.bottomImaBgColor = bottomImaBgColor;
    }

    public String getBottomImaColor() {
        return bottomImaColor;
    }

    public void setBottomImaColor(String bottomImaColor) {
        this.bottomImaColor = bottomImaColor;
    }

    public String getBottomMidBgColor() {
        return bottomMidBgColor;
    }

    public void setBottomMidBgColor(String bottomMidBgColor) {
        this.bottomMidBgColor = bottomMidBgColor;
    }

    public String getBottomDateTextColor() {
        return bottomDateTextColor;
    }

    public void setBottomDateTextColor(String bottomDateTextColor) {
        this.bottomDateTextColor = bottomDateTextColor;
    }

    public String getBottomWeekTextColor() {
        return bottomWeekTextColor;
    }

    public void setBottomWeekTextColor(String bottomWeekTextColor) {
        this.bottomWeekTextColor = bottomWeekTextColor;
    }

    public String getBottomTimeTextColor() {
        return bottomTimeTextColor;
    }

    public void setBottomTimeTextColor(String bottomTimeTextColor) {
        this.bottomTimeTextColor = bottomTimeTextColor;
    }

    public String getBottomCarTextColor() {
        return bottomCarTextColor;
    }

    public void setBottomCarTextColor(String bottomCarTextColor) {
        this.bottomCarTextColor = bottomCarTextColor;
    }

    public String getBottomCarNumColor() {
        return bottomCarNumColor;
    }

    public void setBottomCarNumColor(String bottomCarNumColor) {
        this.bottomCarNumColor = bottomCarNumColor;
    }

    public String getBottomOnlineCityBgColor() {
        return bottomOnlineCityBgColor;
    }

    public void setBottomOnlineCityBgColor(String bottomOnlineCityBgColor) {
        this.bottomOnlineCityBgColor = bottomOnlineCityBgColor;
    }

    public String getBottomDayTemperatureBgColor() {
        return bottomDayTemperatureBgColor;
    }

    public void setBottomDayTemperatureBgColor(String bottomDayTemperatureBgColor) {
        this.bottomDayTemperatureBgColor = bottomDayTemperatureBgColor;
    }

    public String getBottomOnlineTagColor() {
        return bottomOnlineTagColor;
    }

    public void setBottomOnlineTagColor(String bottomOnlineTagColor) {
        this.bottomOnlineTagColor = bottomOnlineTagColor;
    }

    public String getBottomOnlineTextColor() {
        return bottomOnlineTextColor;
    }

    public void setBottomOnlineTextColor(String bottomOnlineTextColor) {
        this.bottomOnlineTextColor = bottomOnlineTextColor;
    }

    public String getBottomCityColor() {
        return bottomCityColor;
    }

    public void setBottomCityColor(String bottomCityColor) {
        this.bottomCityColor = bottomCityColor;
    }

    public String getBottomDayColor() {
        return bottomDayColor;
    }

    public void setBottomDayColor(String bottomDayColor) {
        this.bottomDayColor = bottomDayColor;
    }

    public String getBottomTemperatureColor() {
        return bottomTemperatureColor;
    }

    public void setBottomTemperatureColor(String bottomTemperatureColor) {
        this.bottomTemperatureColor = bottomTemperatureColor;
    }

    public String getBottomVLineColor() {
        return bottomVLineColor;
    }

    public void setBottomVLineColor(String bottomVLineColor) {
        this.bottomVLineColor = bottomVLineColor;
    }

    public void setImageDrawable(View view) {
        if (imageResource != -1) {
            Drawable drawable = ContextCompat.getDrawable(PjjApplication.application, imageResource);
            setViewDrawable(view, drawable);
        } else {
            if (!TextUtils.isEmpty(imagePath)) {
                //使用 application ，因只有一个主页面，影响不大
                Glide.with(PjjApplication.application).load(imagePath).into(new CustomViewTarget<View, Drawable>(view) {
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {

                    }

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        setViewDrawable(this.view, resource);
                    }

                    @Override
                    protected void onResourceCleared(@Nullable Drawable placeholder) {
                    }
                });
            }
        }
    }

    private void setViewDrawable(View view, Drawable drawable) {
        if (view instanceof ImageView) {
            ((ImageView) view).setImageDrawable(drawable);
        } else {
            view.setBackground(drawable);
        }
    }

    private void setViewBgColor(String color, View view) {
        int i = getColor(color);
        view.setBackground(new ColorDrawable(i));
    }

    public static int getColor(String color) {
        int i = Color.BLACK;
        try {
            i = Color.parseColor(color);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    public void setBackground(View view, String color) {
        view.setBackground(new ColorDrawable(getColor(color)));
    }

    public void setTextColor(View view, String color) {
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(getColor(color));
        }
    }

}
