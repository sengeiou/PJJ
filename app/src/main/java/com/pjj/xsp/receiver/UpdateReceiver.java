package com.pjj.xsp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.pjj.xsp.utils.Log;

import android.widget.Toast;

import com.pjj.xsp.view.activity.UnPlayActivity;

/**
 * Created by XinHeng on 2018/11/26.
 * describe：
 */
public class UpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.e("TAG", "onReceive: " + action);
        if (action.equals("android.intent.action.PACKAGE_REPLACED")) {
            Toast.makeText(context, "升级了一个安装包，重新启动此程序", Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(context, UnPlayActivity.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent2);
        } else if (action.equals("android.intent.action.PACKAGE_ADDED")) {
            //接收安装广播
            String packageName = intent.getDataString();
            Log.e("TAG", "安装了:" + packageName + "-包名的程序");
        } else if (action.equals("android.intent.action.PACKAGE_REMOVED")) {
            //接收卸载广播
            String packageName = intent.getDataString();
            Log.e("TAG", "卸载了:" + packageName + "-包名的程序");
        }
    }
}
