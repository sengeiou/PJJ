package com.pjj.xsp.module.parameter;

/**
 * Created by XinHeng on 2019/04/08.
 * describe：app升级反馈
 */
public class AppUpload {
    /**
     * 版本号
     */
    private int versionCode;
    /**
     * 版本名称
     */
    private String versionName;
    /**
     * app包名字，如pjj-release-v1.0.1.apk
     * 下载的半路径
     */
    private String appName;
    /**
     * 屏幕唯一标识
     */
    private String screenId;
    /**
     * 屏幕类型，户内indoor 电梯elevator
     */
    private String screenType;
    /**
     * 软件类型  1系统包  2屏幕软件
     */
    private int type;

    /**
     * 目前API的平台-版本-日期信息
     */
    private String sysApiVersion;
    /**
     * 目前设备的型号
     * rk...
     */
    private String sysAndroidModel;
    /**
     * 目前设备的android系统的版本
     */
    private String sysAndroidVersion;
    /**
     * 设备的固件系统版本和编译日期
     */
    private String sysAndroidDisplay;


    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getScreenId() {
        return screenId;
    }

    public void setScreenId(String screenId) {
        this.screenId = screenId;
    }

    public String getScreenType() {
        return screenType;
    }

    public void setScreenType(String screenType) {
        this.screenType = screenType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSysApiVersion() {
        return sysApiVersion;
    }

    public void setSysApiVersion(String sysApiVersion) {
        this.sysApiVersion = sysApiVersion;
    }

    public String getSysAndroidModel() {
        return sysAndroidModel;
    }

    public void setSysAndroidModel(String sysAndroidModel) {
        this.sysAndroidModel = sysAndroidModel;
    }

    public String getSysAndroidVersion() {
        return sysAndroidVersion;
    }

    public void setSysAndroidVersion(String sysAndroidVersion) {
        this.sysAndroidVersion = sysAndroidVersion;
    }

    public String getSysAndroidDisplay() {
        return sysAndroidDisplay;
    }

    public void setSysAndroidDisplay(String sysAndroidDisplay) {
        this.sysAndroidDisplay = sysAndroidDisplay;
    }
}
