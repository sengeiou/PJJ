package com.pjj.xsp.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Create by xinheng on 2018/11/17。
 * describe：
 */
@Entity
public class OrderTask {
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String orderId;
    private String peopleInfo;
    private String diyInfo;
    @Generated(hash = 1885040840)
    public OrderTask(Long id, @NotNull String orderId, String peopleInfo,
            String diyInfo) {
        this.id = id;
        this.orderId = orderId;
        this.peopleInfo = peopleInfo;
        this.diyInfo = diyInfo;
    }
    @Generated(hash = 629765490)
    public OrderTask() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getOrderId() {
        return this.orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public String getPeopleInfo() {
        return this.peopleInfo;
    }
    public void setPeopleInfo(String peopleInfo) {
        this.peopleInfo = peopleInfo;
    }
    public String getDiyInfo() {
        return this.diyInfo;
    }
    public void setDiyInfo(String diyInfo) {
        this.diyInfo = diyInfo;
    }

}
