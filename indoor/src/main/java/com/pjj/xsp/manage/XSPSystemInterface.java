package com.pjj.xsp.manage;

import android.app.Activity;
import android.content.Context;

/**
 * Create by xinheng on 2018/10/16。
 * describe：显示屏系统管理
 */
public interface XSPSystemInterface {
    //重启、关机、回复出厂设置、远程升级、设置系统时间、定时开关机、取消定时开关机、开启/关闭/设置声音、 静默安装方法

    /**
     * 回复出厂设置
     */
    void recoveryFactoryReset();

    /**
     * 重启
     */
    void reboot();

    /**
     * 关机
     */
    void turnOff();

    /**
     * 定时开关机
     *
     * @param date
     */
    void setTurnOffTime(String... date);

    /**
     * 取消定时关机
     *
     * @param date
     */
    void cancelTurnOffTime(String date);

    /**
     * 开启/关闭/设置声音
     *
     * @param tag 开关
     */
    void setVoicesWitch(boolean tag);

    /**
     * 设置系统时间
     *
     * @param date 时间
     */
    void setSystemTime(String date);

    /**
     * @return 唯一码
     */
    String getOnlyCode();

    /**
     * 隐藏导航栏
     */
    void hideNavigationBar();

    /**
     * 静默安装app
     *
     * @param path app路径
     */
    void installApp(String path);

    void systemUpdate(String path);

    /**
     * 截屏
     * file 需末尾添加斜杠
     * name 需添加后缀
     *
     * @return 路径
     */
    String screenshots(String file, String name);

    void startWatchDog();

    void closeWatchDog();

    void feedWatchDog();
}
