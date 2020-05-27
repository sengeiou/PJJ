package com.pjj.xsp.manage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.admin.SystemUpdatePolicy;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.view.View;
import android.view.WindowManager;

import com.pjj.xsp.BuildConfig;
import com.pjj.xsp.PjjApplication;
import com.pjj.xsp.utils.Log;
import com.pjj.xsp.utils.SuperUser;
import com.pjj.xsp.view.activity.BaseActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by XinHeng on 2019/04/03.
 * describe：
 */
public class XspSystemFunction implements XSPSystemInterface {
    protected String onlyCode;

    protected Activity activity;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void clean(Activity activity) {
        if (this.activity == activity) {
            this.activity = null;
        }
    }

    public String getPassword() {
        return "pjj";
    }

    public void systemUploadU(String path) {
    }

    @Override
    public void recoveryFactoryReset() {

    }

    public void showPasswordDialog(String path) {

    }

    @Override
    public void reboot() {
        boolean b = SuperUser.checkGetRootAuth();
        if (b) {
            RunSystem.run(RunSystem.getReRootArray());
        } else {
            Intent intent = PjjApplication.application.getBaseContext().getPackageManager().getLaunchIntentForPackage(PjjApplication.application.getBaseContext().getPackageName());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //与正常页面跳转一样可传递序列化数据,在Launch页面内获得
            intent.putExtra("REBOOT", "reboot");
            PjjApplication.application.startActivity(intent);
        }
    }

    @Override
    public void turnOff() {
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
            if (null == localMacAddressFromWifiInfo) {
                localMacAddressFromWifiInfo = read("cat /sys/class/net/wlan0/address");
                Log.e("TAG", "getOnlyCode: " + localMacAddressFromWifiInfo);
            }
            if (null != localMacAddressFromWifiInfo)
                onlyCode = localMacAddressFromWifiInfo.replace(":", "_") + (BuildConfig.APP_TYPE ? "" : "_test");
        }

        return onlyCode;
    }

    @Override
    public void hideNavigationBar() {
        if (activity != null) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }

    @Override
    public void installApp(String path) {

    }

    @Override
    public void systemUpdate(String path) {

    }

    /**
     * 根据wifi信息获取本地mac
     *
     * @param context
     * @return
     */
    public static String getLocalMacAddressFromWifiInfo(Context context) {
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo winfo = wifi.getConnectionInfo();
        @SuppressLint("HardwareIds") String mac = winfo.getMacAddress();
        return mac;
    }

    public static void startWifi(Context context) {
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
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
    public String screenshots(String file, String name) {
        return null;
    }

    @Override
    public void startWatchDog() {

    }

    @Override
    public void closeWatchDog() {

    }

    @Override
    public void feedWatchDog() {

    }

    /**
     * 屏幕使用性质
     *
     * @return
     */
    public String getScreenType() {
        return "elevator";
    }

    public void bindService(Context context) {

    }

    public void unBindService(Context context) {

    }

    public String getScreenSize() {
        return null;
    }

    public static String read(String sys_path) {
        Process process = null;
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        Runtime runtime = Runtime.getRuntime();
        try {
            process = runtime.exec(sys_path); // 此处进行读操作
            is = process.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            String line;
            StringBuilder buffer = new StringBuilder();
            while (null != (line = br.readLine())) {
                Log.e("TAG", "read data ---> " + line);
                buffer.append(line);
            }
            return buffer.toString().trim();
        } catch (IOException e) {
            e.printStackTrace();
            Log.w("TAG-XSP", "*** ERROR *** Here is what I know: " + e.getMessage());
        } finally {
            try {
                if (null != br)
                    br.close();
                if (null != isr)
                    isr.close();
                if (null != is)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (null != process)
                process.destroy();
        }
        return null;
    }

    public String getSystemUpdateFileName() {
        return "update.zip";
    }
}
