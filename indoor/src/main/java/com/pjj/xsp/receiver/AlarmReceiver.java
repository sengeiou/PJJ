package com.pjj.xsp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.pjj.xsp.manage.XSPSystem;
import com.pjj.xsp.module.XspTaskHandler;
import com.pjj.xsp.utils.Log;


/**
 * Create by xinheng on 2018/10/12 0012。
 * describe：App定时器
 */
public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "TAG-AlarmReceiver";
    /**
     * 开机
     */
    public static final int BOOT_TYPE = 1;
    /**
     * 关机
     */
    public static final int OFF_TYPE = 2;
    /**
     * 任务 播放视频或图片
     */
    public static final int TASK_TYPE = 0;
    /**
     * 获取网络任务
     */
    public static final int GET_TASK_TYPE = 3;

    public static final String TASK_TYPE_NAME = "task_type";

    @Override
    public void onReceive(Context context, Intent intent) {
        int type = intent.getIntExtra(TASK_TYPE_NAME, 0);
        if (type == TASK_TYPE || type == GET_TASK_TYPE) {
            XspTaskHandler.getInstance().setNextAlarm(intent);
        }
        switch (type) {
            case TASK_TYPE:
                Log.e(TAG, "onReceive: 执行任务");
                XspTaskHandler.getInstance().performTask();
                break;
            case GET_TASK_TYPE:
                Log.e(TAG, "onReceive: 获取任务");
                XspTaskHandler.getInstance().getNextXspTask();
                break;
            case OFF_TYPE://关机
                XSPSystem.getInstance().turnOff();
            case BOOT_TYPE://开机
                XSPSystem.getInstance().reboot();
            default:
        }
    }
}
