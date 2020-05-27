package com.pjj.xsp.module.parameter;

/**
 * Create by xinheng on 2018/11/17。
 * describe：广告屏根据订单号查询 参数
 */
public class ScreenOrderTask {
    private String screenId;
    private String orderId;

    public String getScreenId() {
        return screenId;
    }

    public void setScreenId(String screenId) {
        this.screenId = screenId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
