package com.pjj.xsp.manage;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.pjj.xsp.utils.Log;

import com.pjj.xsp.PjjApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Create by xinheng on 2018/10/16。
 * describe：系统接口实现
 */
public class XSPSystem implements XSPSystemInterface {
    private static XSPSystem instance;
    private String onlyCode;

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
    public String getOnlyCode() {
        if (null == onlyCode) {
            String localMacAddressFromWifiInfo = getLocalMacAddressFromWifiInfo(PjjApplication.application);
            if (null != localMacAddressFromWifiInfo)
                onlyCode = localMacAddressFromWifiInfo.replace(":", "_");
        }
        return onlyCode;
    }

    @Override
    public void hideNavigationBar() {

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

    /**
     * 根据wifi信息获取本地mac
     *
     * @param context
     * @return
     */
    public static String getLocalMacAddressFromWifiInfo(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo winfo = wifi.getConnectionInfo();
        String mac = winfo.getMacAddress();
        return mac;
    }

    public static void startWifi(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (!wifi.isWifiEnabled()) {
            wifi.setWifiEnabled(true);
        }
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    @Override
    public void screenshots() {
        Intent intent = new Intent();
        intent.setAction("rk.android.screenshot.action");
        PjjApplication.application.sendBroadcast(intent);
        //Log.e("TAG", "screenshots: 截屏");
    }
}
