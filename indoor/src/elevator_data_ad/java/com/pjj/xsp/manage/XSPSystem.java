package com.pjj.xsp.manage;

import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import com.pjj.xsp.PjjApplication;
import com.pjj.xsp.module.parameter.AppUpload;
import com.pjj.xsp.utils.BitmapUtils;
import com.pjj.xsp.utils.Log;
import com.pjj.xsp.utils.SharedUtils;

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
    public void reboot() {
        //RunSystem.run(RunSystem.getReRootArray());
        if(PjjApplication.OLD_3188){
            super.reboot();
            return;
        }
        Intent intent = new Intent();
        intent.setAction("android.adw.intent.action.reboot");
        PjjApplication.application.sendBroadcast(intent);
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
    public void setSystemTime(String date) {

    }


    @Override
    public void hideNavigationBar() {

    }

    @Override
    public void installApp(String path) {
        //RunSystem.run(RunSystem.installArray(path));
        Log.e("TAG", "升级 installApp: " + path);
        SharedUtils.saveForXml(SharedUtils.NEW_APP_PATH, path);
        Intent intent = new Intent();
        intent.putExtra("apk_path", path);
        intent.setAction("android.adw.intent.action.installapp");
        PjjApplication.application.sendBroadcast(intent);
    }

    @Override
    public String screenshots(String file, String name) {
        //Log.e("TAG", "screenshots: 截屏");
        if (PjjApplication.OLD_3188) {
            return BitmapUtils.getScreenshot();
        }
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
