package com.pjj.xsp.manage;


import android.content.Context;
import android.util.Log;

import com.pjj.xsp.PjjApplication;
import com.pjj.xsp.module.parameter.AppUpload;

import com.pjj.xsp.utils.SharedUtils;
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
        rk.smdtSetTimingSwitchMachine(date[0], date[1], "1");
    }*/


    @Override
    public void installApp(String path) {
        Log.e("TAG", "installApp: 升级=" + path);
        rk.silentInstallApk(path);
        SharedUtils.saveForXml(SharedUtils.NEW_APP_PATH, path);
        // ApiManager manager = ApiManager.getInstance(PjjApplication.application);
        //Log.e("TAG", "namager=" + (manager == null));
        //manager.silentInstall(path, true);//静默安装apkPath路径的apk，isRuning是安装后是否立马运行
        //manager.silentDelete(path);//静默卸载包名为packageName的apk
    }

    @Override
    public String screenshots(String file, String name) {
        boolean result = rk.takeScreenshot(file + name);
        Log.e("TAG", "screenshots: screenshots result=" + result);
        //String screenshot = BitmapUtils.getScreenshot();
        //return file + name;
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
            return "unknow";
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
}
