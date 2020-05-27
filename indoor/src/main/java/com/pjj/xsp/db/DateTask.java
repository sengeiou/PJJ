package com.pjj.xsp.db;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by XinHeng on 2019/03/26.
 * describe：
 */
@Entity
public class DateTask {
    @SerializedName("orderTime")
    @NotNull
    private String date;
    @SerializedName("timeSection")
    private String hour;
    @NotNull
    private Long id;
    @Id(autoincrement = true)
    private Long _id;
    @NotNull
    private String limitTag = "0";


    @Generated(hash = 1330318407)
    public DateTask(@NotNull String date, String hour, @NotNull Long id, Long _id,
            @NotNull String limitTag) {
        this.date = date;
        this.hour = hour;
        this.id = id;
        this._id = _id;
        this.limitTag = limitTag;
    }

    @Generated(hash = 1681919144)
    public DateTask() {
    }
    

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getLimitTag() {
        return this.limitTag;
    }

    public void setLimitTag(String limitTag) {
        this.limitTag = limitTag;
    }


}
