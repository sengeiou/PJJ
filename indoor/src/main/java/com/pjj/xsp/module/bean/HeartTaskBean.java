package com.pjj.xsp.module.bean;

import java.util.List;

public class HeartTaskBean extends ResultBean {
    private List<String> orderIdList;
    private List<String> screenCommandList;

    public List<String> getOrderIdList() {
        return orderIdList;
    }

    public void setOrderIdList(List<String> orderIdList) {
        this.orderIdList = orderIdList;
    }

    public List<String> getScreenCommandList() {
        return screenCommandList;
    }

    public void setScreenCommandList(List<String> screenCommandList) {
        this.screenCommandList = screenCommandList;
    }
}
