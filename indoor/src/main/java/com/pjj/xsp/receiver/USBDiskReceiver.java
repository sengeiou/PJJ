package com.pjj.xsp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.pjj.xsp.manage.XSPSystem;
import com.pjj.xsp.module.XspPlayUI;
import com.pjj.xsp.utils.Log;


public class USBDiskReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        //Log.e("TAG", "onReceive: " + intent);
        Uri data = intent.getData();
        String path = data.getPath();
        //Log.e("TAG", "onReceive: " + data.toString() + ", " + path);
        if (!TextUtils.isEmpty(path)) {
            if (Intent.ACTION_MEDIA_UNMOUNTED.equals(action)) {
                Log.e("usb", "MEDIA_UNMOUNTED");
            }
            if (Intent.ACTION_MEDIA_MOUNTED.equals(action)) {
                String scheme = data.getScheme();
                Log.e("usb", "MEDIA_MOUNTED " + scheme);
                if ("file".equals(scheme)) {
                    XSPSystem.getInstance().showPasswordDialog(path);
                }
            }
        }
    }
}
