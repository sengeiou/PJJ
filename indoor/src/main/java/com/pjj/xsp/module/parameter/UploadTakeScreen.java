package com.pjj.xsp.module.parameter;

public class UploadTakeScreen {
    private String orderId;//-- 订单id
    private String screenId; // -- 屏幕id
    private String msg;//  -- 截屏反馈

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getScreenId() {
        return screenId;
    }

    public void setScreenId(String screenId) {
        this.screenId = screenId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
