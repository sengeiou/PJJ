package com.pjj.xsp.module.bean;

/**
 * Create by xinheng on 2018/11/08。
 * describe：
 */
public class ErrorBean {
    /**
     * 获取任务
     */
    public static final String GET_TASK_ERROR="001e";
    /**
     * 下载任务
     */
    public static final String DOWNLOAD_TASK_ERROR="002e";
    private String date;
    private String msg;
    private String code;

    public ErrorBean() {
    }

    public ErrorBean(String date, String msg, String code) {
        this.date = date;
        this.msg = msg;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "{\"date\":\""+date+"\",\"msg\":\""+msg+"\",\"code\":\""+code+"\"}";
    }
}
