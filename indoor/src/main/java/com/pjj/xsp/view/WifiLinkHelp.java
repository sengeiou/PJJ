package com.pjj.xsp.view;

import android.content.Context;
import android.net.wifi.ScanResult;


import com.pjj.xsp.utils.Log;
import com.pjj.xsp.utils.WifiUtils;

import java.util.List;

public class WifiLinkHelp {
    private final String TAG = "WifiLinkHelp_TAG";

    public void linkWifi(Context context) {
        boolean wifiConnect = WifiUtils.isWifiConnect(context);
        if (wifiConnect) {
            Log.e(TAG, "linkWifi: wifi已连接");
            return;
        } else {
            Log.e(TAG, "linkWifi: wifi未连接");
        }
        /*WifiUtils instance = WifiUtils.getInstance(context);
        new Thread() {
            @Override
            public void run() {
                try {
                    linkWifi(instance);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();*/
    }

    private void linkWifi(WifiUtils instance) throws InterruptedException {
        if (!instance.isWifiEnable()) {
            instance.openWifi();
        }
        Thread.sleep(2000);
        if (!instance.isWifiEnable()) {
            Log.e(TAG, "linkWifi: wifi打开失败");
            return;
        }
        List<ScanResult> wifiList = instance.getWifiList();
        if (wifiList.size() == 0) {
            return;
        }
        for (ScanResult scanResult : wifiList) {
            String ssid = scanResult.SSID;
            //Log.e(TAG, "linkWifi:ssid=" + ssid);
            Log.e(TAG, "linkWifi:" + scanResult);
            if ("TLW-KF".equals(ssid)) {
                boolean b = instance.connectWifiPws(ssid, "13366003002");
                Log.e(TAG, "linkWifi: result=" + b);
                return;
            }
        }
    }
}
