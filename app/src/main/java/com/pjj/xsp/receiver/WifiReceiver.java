package com.pjj.xsp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import com.pjj.xsp.utils.Log;

import com.pjj.xsp.view.MainViewHelp;

/**
 * Create by xinheng on 2018/11/08。
 * describe：
 */
public class WifiReceiver extends BroadcastReceiver {
    private static final String TAG = "wifiReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(WifiManager.RSSI_CHANGED_ACTION)) {
            //Log.i(TAG, "wifi信号强度变化");
        } else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action) || ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {//wifi连接上与否
            ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ethNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
            NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (ethNetInfo != null && ethNetInfo.isConnected()) {//有线
                Log.e(TAG, "onReceive: 有线");
            } else if (wifiNetInfo != null && wifiNetInfo.isConnected()) {//无线
                Log.e(TAG, "onReceive: 无线");
            } else {//无网络
                Log.e(TAG, "onReceive: 当前网络不可用");
                MainViewHelp.getInstance().updateOnlineStatue(false);
                return;
            }
            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            NetworkInfo.State state = info.getState();
            Log.e(TAG, "onReceive: "+state);
            if (state.equals(NetworkInfo.State.DISCONNECTED)) {
                Log.e(TAG, "断开");
            } else if (state.equals(NetworkInfo.State.CONNECTED)) {
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                //获取当前wifi名称
                Log.e(TAG, "连接到网络 " + wifiInfo.getSSID());
                MainViewHelp.getInstance().updateOnlineStatue(true);
            }
        } else if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {        //wifi打开与否
            int wifistate = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);
            if (wifistate == WifiManager.WIFI_STATE_DISABLED) {
                Log.e(TAG, "系统关闭网络");
            } else if (wifistate == WifiManager.WIFI_STATE_ENABLED) {
                Log.e(TAG, "系统开启网络");
            }
        }
    }

}
