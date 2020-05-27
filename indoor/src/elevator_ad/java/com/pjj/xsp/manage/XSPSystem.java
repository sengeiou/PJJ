package com.pjj.xsp.manage;

import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import com.pjj.xsp.PjjApplication;
import com.pjj.xsp.module.parameter.AppUpload;
import com.pjj.xsp.utils.Log;

import java.io.File;

/**
 * Create by xinheng on 2018/10/16。
 * describe：系统接口实现
 */
public class XSPSystem extends XspSystemFunction {
    private static XSPSystem instance;


    public static XSPSystem getInstance() {
        if (null == instance) {
            synchronized (XSPSystem.class) {
                if (null == instance) {
                    instance = new XSPSystem();
                }
            }
        }
        return instance;
    }

    private XSPSystem() {
    }

    @Override
    public void recoveryFactoryReset() {

    }

    @Override
    public String getOnlyCode() {
        //onlyCode = "02_00_00_00_00_00_test";
        return super.getOnlyCode();
    }

    @Override
    public void reboot() {
        //RunSystem.run(RunSystem.getReRootArray());
        super.reboot();
    }

    @Override
    public void turnOff() {
        Log.e("TAG", "turnOff: 关机");
        Intent intent = new Intent();
        intent.setAction("android.adw.intent.action.shutdown");
        PjjApplication.application.sendBroadcast(intent);
    }

    @Override
    public void setTurnOffTime(String... date) {

    }

    @Override
    public void cancelTurnOffTime(String date) {

    }

    @Override
    public void setVoicesWitch(boolean tag) {

    }



    @Override
    public void hideNavigationBar() {
        super.hideNavigationBar();
    }

    @Override
    public void installApp(String path) {
        //RunSystem.run(RunSystem.installArray(path));
        Log.e("TAG", "升级 installApp: " + path);
        Intent intent = new Intent();
        intent.putExtra("apk_path", path);
        intent.setAction("android.adw.intent.action.installapp");
        PjjApplication.application.sendBroadcast(intent);
    }

    @Override
    public String screenshots(String file, String name) {
        //Log.e("TAG", "screenshots: 截屏");
        File file1 = new File(PjjApplication.Screenshots_Path);
        File[] files = file1.listFiles();
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            f.getAbsoluteFile().delete();
        }
        Intent intent = new Intent();
        intent.setAction("rk.android.screenshot.action");
        PjjApplication.application.sendBroadcast(intent);
        return "elevator";
    }

    /**
     * 获取设备的固件系统版本和编译日期
     *
     * @return
     */
    public String getAndroidDisplay() {
        return "elevator";
    }

    public AppUpload createAppUpload() {
        AppUpload appUpload = new AppUpload();
        appUpload.setType(1);
        appUpload.setSysAndroidDisplay("elevator");
        appUpload.setSysAndroidModel("elevator");
        appUpload.setSysAndroidVersion("elevator");
        appUpload.setSysApiVersion("elevator");
        return appUpload;
    }
}
