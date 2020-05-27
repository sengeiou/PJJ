package com.pjj.xsp.manage;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.pjj.xsp.PjjApplication;
import com.pjj.xsp.module.parameter.AppUpload;

import com.pjj.xsp.utils.SharedUtils;
import com.ys.mcu7502.Mcu7502;
import com.ys.rkapi.MyManager;

import java.io.File;


/**
 * Create by xinheng on 2018/10/16。
 * describe：系统接口实现
 */
public class XSPSystem extends XspSystemFunction {
    private static XSPSystem instance;
    private MyManager rk;

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
        rk = MyManager.getInstance(PjjApplication.application);
        //rk.bindAIDLService(PjjApplication.application);
    }

    @Override
    public void bindService(Context context) {
        rk.bindAIDLService(context);
    }

    @Override
    public void unBindService(Context context) {
        rk.unBindAIDLService(context);
    }

    @Override
    public void reboot() {
        rk.reboot();
    }

/*    @Override
    public void setTurnOffTime(String... date) {
        rk.smdtSetTimingSwitchMachine(date[0], da
        te[1], "1");
    }*/

    @Override
    public void installApp(String path) {
        Log.e("TAG", "installApp: 升级=" + path);
        //rk.silentInstallApk(path);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(path));
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        PjjApplication.application.startActivity(intent);
        SharedUtils.saveForXml(SharedUtils.NEW_APP_PATH, path);
    }

    @Override
    public String screenshots(String file, String name) {
        boolean screenshot = rk.takeScreenshot(file + name);
        android.util.Log.e("TAG", "screenshots: screenshots result=" + screenshot);
        return file + name;
    }

    /**
     * 文件名一定为update.img
     *
     * @param path
     */
    @Override
    public void systemUpdate(String path) {
        File packageFile = new File(path);
        if (!packageFile.exists()) {
            Log.e("TAG", "升级包不存在");
            return;
        }
        Log.e("TAG", "systemUpdate: " + path);
        rk.upgradeSystem(path);
    }

    @Override
    public String getScreenType() {
        return "indoor";
    }

    /**
     * 获取设备的固件系统版本和编译日期
     *
     * @return
     */
    public String getAndroidDisplay() {

        try {//getApiVersion
            return rk.getAndroidDisplay();
        } catch (Exception e) {
            e.printStackTrace();
            return "unknow1111";
        }
    }

    public AppUpload createAppUpload() {
        AppUpload appUpload = new AppUpload();
        appUpload.setType(1);
        appUpload.setSysAndroidDisplay(rk.getAndroidDisplay());
        appUpload.setSysAndroidModel(rk.getAndroidModle());
        appUpload.setSysAndroidVersion(rk.getAndroidVersion());
        appUpload.setSysApiVersion(rk.getApiVersion());
        return appUpload;
    }

    @Override
    public void startWatchDog() {
        //打开看门狗功能
        int open = Mcu7502.open();
        int i = Mcu7502.enableWatchdog();
        Log.e("XSP_SYS_TAG", "startWatchDog: open=" + open + ", " + i);
    }

    @Override
    public void closeWatchDog() {
        int i = Mcu7502.disableWatchdog();
        Mcu7502.close();
        Log.e("XSP_SYS_TAG", "closeWatchDog: " + i);
    }

    @Override
    public void feedWatchDog() {
        int a = Mcu7502.feetDog(30); //单位是秒
        Log.e("XSP_SYS_TAG", "feedWatchDog: " + a);
    }

    @Override
    public String getSystemUpdateFileName() {
        return "update.img";
    }
}
