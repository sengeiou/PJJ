package com.pjj.xsp.module;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.pjj.xsp.PjjApplication;
import com.pjj.xsp.receiver.AlarmReceiver;
import com.pjj.xsp.utils.Log;
import java.util.Calendar;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

/**
 * Create by xinheng on 2018/11/16。
 * describe：广告屏任务处理
 */
public class XspTaskHandler {
    /**
     * 闹铃间隔时间
     * 1 小时
     */
    public static final int ALARM_DELAY = 3600 * 1000;
    //屏幕位置标识
    public static final String SCREEN_TITLE = "title";
    public static final String SCREEN_TITLE_10_LIST = "title_10_list";
    public static final String SCREEN_DIY_LIST = "screen_diy_list";
    public static final String SCREEN_CONTENT = "content";
    public static final String SCREEN_ROTATION_CHART = "rotation_chart";
    /**
     * 本地广告路径
     */
    public static final String LOCAL_PATH = "file:///android_asset/local1.jpg";
    //public static final String LOCAL_PATH = "android.resource://" + "com.tlw.xsp" + "/" + R.raw.localvideo;
    private static final String TAG = "TAG";

    private static XspTaskHandler INSTANCE;

    public static XspTaskHandler getInstance() {
        if (null == INSTANCE) {
            synchronized (XspTaskHandler.class) {
                if (null == INSTANCE) {
                    INSTANCE = new XspTaskHandler();
                }
            }
        }
        return INSTANCE;
    }

    private XspTaskHandler() {
    }


    public void getNextXspTask() {

    }


    public void performTask() {
        if (null != onHandleTaskListener) {
            onHandleTaskListener.performTask();
        }
    }

    /**
     * 开启闹铃任务
     */
    public void startAlarm() {
        Calendar calendar = Calendar.getInstance();
        int nextHour = calendar.get(Calendar.HOUR_OF_DAY) + 1;
        int min = calendar.get(Calendar.MINUTE);
        int tag;
        if (min >= 40) {
            //获取下个时间节点的
            tag = 0;
            getNextXspTask();
        } else {
            tag = -60;
        }
        Log.e("TAG", "startTaskTime: " + nextHour);
        calendar.set(Calendar.HOUR_OF_DAY, nextHour);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long timeInMillis = calendar.getTimeInMillis();
        //执行任务闹铃
        startAlarm(timeInMillis, AlarmReceiver.TASK_TYPE);
        //40min以后 获取任务闹铃
        startAlarm(timeInMillis + (40L + tag) * 60 * 1000, AlarmReceiver.GET_TASK_TYPE);
    }

    private void startAlarm(long time, int type) {
        Intent intent = new Intent(PjjApplication.application, AlarmReceiver.class);
        intent.putExtra(AlarmReceiver.TASK_TYPE_NAME, type);
        intent.putExtra("time", time - ALARM_DELAY);
        intent.setAction(type == 0 ? "get_a_task" : "run_a_task");
        setNextAlarm(intent);
    }

    /**
     * 下一个时间节点闹铃
     *
     * @param intent
     */
    public void setNextAlarm(Intent intent) {
        long time = intent.getLongExtra("time", 0) + ALARM_DELAY;
        intent.putExtra("time", time);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(PjjApplication.application, 0, intent, FLAG_UPDATE_CURRENT);//可修改intent参数
        AlarmManager alarmManager = (AlarmManager) PjjApplication.application.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }


    private OnHandleTaskListener onHandleTaskListener;

    public void setOnHandleTaskListener(OnHandleTaskListener onHandleTaskListener) {
        this.onHandleTaskListener = onHandleTaskListener;
    }

    public interface OnHandleTaskListener {
        /**
         * 获取任务
         */
        void getXspTask();

        /**
         * 执行任务
         */
        void performTask();
    }
}
