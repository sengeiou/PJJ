package com.pjj.xsp.module.bean;

import java.io.Serializable;

/**
 * Create by xinheng on 2018/10/12 0012。
 * describe：接口返回的信息
 */
public class ResultBean implements Serializable {
    public static final String SUCCESS_CODE="1";
    private String flag;//结果码 String 0：失败 1：成功
    private String msg;//结果信息 String 返回结果信息描述

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public boolean isSuccess(){
        return SUCCESS_CODE.equals(flag);
    }
}
