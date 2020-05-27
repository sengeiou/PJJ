package com.pjj.xsp.module.bean;

import android.text.TextUtils;

import com.pjj.xsp.module.ScreenInfManage;

import java.io.Serializable;
import java.util.List;

/**
 * Create by xinheng on 2018/10/12 0012。
 * describe：任务信息
 */
public class ScreenParentBean extends ResultBean implements Serializable {
    private String hour;
    private String curDate;
    private int orderType;
    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getCurDate() {
        return curDate;
    }

    public void setCurDate(String curDate) {
        this.curDate = curDate;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }
}
