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
public class DateTask {
    @Id(autoincrement = true)
    private Long id_date;
    @NotNull
    private String date;
    @NotNull
    private String hour;
    @NotNull
    private String orderId;

    @Generated(hash = 1820539509)
    public DateTask(Long id_date, @NotNull String date, @NotNull String hour,
                    @NotNull String orderId) {
        this.id_date = id_date;
        this.date = date;
        this.hour = hour;
        this.orderId = orderId;
    }

    @Generated(hash = 1681919144)
    public DateTask() {
    }

    public Long getId_date() {
        return this.id_date;
    }

    public void setId_date(Long id_date) {
        this.id_date = id_date;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return this.hour;
    }

    public void setHour(String hour) {
        if (hour.startsWith(",") && hour.endsWith(",")) {

        } else {
            hour = "," + hour + ",";
        }
        this.hour = hour;
    }

    public String getOrderId() {
        return this.orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
