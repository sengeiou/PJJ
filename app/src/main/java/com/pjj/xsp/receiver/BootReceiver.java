package com.pjj.xsp.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;

import com.pjj.xsp.PjjApplication;
import com.pjj.xsp.utils.FileUtils;
import com.pjj.xsp.utils.Log;

/**
 * Create by xinheng on 2018/10/20。
 * describe：开机广播
 */
public class BootReceiver extends BroadcastReceiver {
    /**
     * 重启广播
     */
    public static final String BOOT_ACTION = "android.intent.action.BOOT_COMPLETED";
    /**
     * 呼救按钮点击广播
     */
    public static final String HELP_KEY_ACTION = "com.android.adw.helpbutton";
    public static final String ELEVATOR_STATUE_ACTION = "com.android.hlc.elevator";
    private static final String TAG = "TAG";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (null == action) {
            return;
        }
        switch (action) {
            case BOOT_ACTION:
                startApp(context);
                break;
            case HELP_KEY_ACTION:
                //helpKeyResponse();
                break;
            case ELEVATOR_STATUE_ACTION:
                //String jsonStr = intent.getStringExtra("data");
                //dealElevatorStatue(jsonStr);
                break;
            default:
        }
    }


    /**
     * 启动 pjj app
     *
     * @param context
     */
    private void startApp(Context context) {
        Log.e("TAG", "onReceive: 重启广播");
        /*Intent i = new Intent();
        ComponentName com = new ComponentName("com.tlw.xsp", "com.tlw.xsp.view.activity.InactiveActivity");
        i.setComponent(com);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);*/
        FileUtils.saveStringFile(PjjApplication.App_Path + "restart.txt", "重启");
        new Handler().postDelayed(() -> {
            PackageManager packageManager = context.getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage("com.pjj.xsp");
            context.startActivity(intent);
        }, 2000);

        /*Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.pjj.xsp");
        PendingIntent restartIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 3000, restartIntent); // 1秒钟后重启应用*/
        //System.exit(0);
    }

    public static int i;

    /**
     * 呼救按钮响应
     */
    private void helpKeyResponse() {
        Log.e("TAG", "helpKeyResponse: 呼救");
        /*VoipManage instance = VoipManage.getInstance();
        if(instance.isLoginStatue()){//登录成功
            instance.callPhone("18201538182");
        }*/
        ++i;
        /*if (i == 1) {
            if (TlwApplication.application != null && TlwApplication.application.getActivityNow() != null && !(TlwApplication.application.getActivityNow() instanceof RescueActivity)) {
                Toast.makeText(TlwApplication.application, "呼救中心。", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(TlwApplication.application, RescueActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                TlwApplication.application.startActivity(i);
            }
        }
        if (i == 2) {
            i = 0;
        }*/
        /*VoipCall.getInstance().makeCall("18201538182");*/
        if (i > 2) {//1 2
            //XSPSystem.getInstance().reboot();
            //MainViewHelp.getInstance().setTest();
            i = 0;
        }
    }
}
