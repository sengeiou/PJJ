package com.pjj.xsp.service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;

import com.pjj.xsp.BuildConfig;
import com.pjj.xsp.utils.Log;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PjjService extends Service {
    private final String TAG = "PjjService_TAG";
    private Timer timer;

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind: action=" + intent.getAction());
        startTimer();
        return new Binder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);
        startForeground(123, new Notification());
        Log.e(TAG, "onStartCommand: ");
        startTimer();
        return START_STICKY;
    }

    private void startTimer() {
        Log.e(TAG, "startTimer: ");
        if (null == timer) {
            startForeground(1011, new Notification());
            Log.e(TAG, "startTimer: 111");
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    checkApp();
                }
            }, 0, 20000);//10s
        }
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy: ");
        if (null != timer) {
            timer.cancel();
            timer = null;
        }
        super.onDestroy();
    }

    private void checkApp() {
        Log.e(TAG, "checkApp: ");
        final String packageName = BuildConfig.APPLICATION_ID;
        if (checkAppIsRunning(packageName)) {
            if (!checkAppIsForeground(packageName)) {
                moveAppToForeground(packageName);
            }
        } else {
            startApp();
        }
    }

    private void startApp() {
        Log.e(TAG, "startApp: ");
        PackageManager packageManager = getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(BuildConfig.APPLICATION_ID);
        startActivity(intent);
    }

    private boolean checkAppIsRunning(String packageName) {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        //mainProcessName=null;
        int myPid = android.os.Process.myPid();
        Log.e(TAG, "checkApp: mainProcessName=" + mainProcessName + ", myPid=" + myPid + ", packageName=" + packageName);
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            Log.e(TAG, "isUIProcess: " + info.processName + ", " + info.pid);
            if (packageName.equals(info.processName)) {
                //app运行中
                return true;
            }
        }
        return false;
    }

    private boolean checkAppIsForeground(String packageName) {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        Log.e(TAG, "checkAppIsForeground: " + currentPackageName);
        if (!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(packageName)) {
            return true;
        }
        return false;
    }

    private void moveAppToForeground(String packageName) {
        startApp();
        /*if (!checkAppIsRunning(packageName)) {
            startApp();
            return;
        }
        Log.e(TAG, "moveAppToForeground: ");
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(3);
        for (ActivityManager.RunningTaskInfo runningTaskInfo : runningTaskInfos) {
            if (packageName.equals(runningTaskInfo.topActivity.getPackageName())) {
                activityManager.moveTaskToFront(runningTaskInfo.id, ActivityManager.MOVE_TASK_WITH_HOME);
                return;
            }
        }*/
    }
}
