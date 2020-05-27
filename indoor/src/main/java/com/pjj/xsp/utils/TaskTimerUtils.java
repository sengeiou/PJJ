package com.pjj.xsp.utils;


import android.os.Handler;
import android.os.Looper;
import com.pjj.xsp.utils.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

/**
 * Created by XinHeng on 2019/03/26.
 * describe：
 */
public class TaskTimerUtils {
    /**
     * 播放时长
     */
    private int playTime;
    private final Handler mHandler;
    private OnTaskTimerListener onTaskTimerListener;
    private TaskTimerRunnable timerRunnable;

    public TaskTimerUtils() {
        mHandler = new Handler(Looper.getMainLooper());
        timerRunnable = new TaskTimerRunnable();
    }

    public TaskTimerUtils setPlayTime(int playTime) {
        this.playTime = playTime;
        return this;
    }

    public TaskTimerUtils setOnTaskTimerListener(OnTaskTimerListener onTaskTimerListener) {
        this.onTaskTimerListener = onTaskTimerListener;
        return this;
    }

    public void start() {
        if (playTime > 0)
            mHandler.postDelayed(timerRunnable, playTime * 1000 + 1000);
    }

    public void clear() {
        Log.e("TAG", "clear: playTime= " + playTime);
        if (playTime > 0)
            mHandler.removeCallbacks(timerRunnable);
    }

    public void nowComplete() {
        Log.e("TAG", "clear: nowComplete= " + playTime);
        if (playTime > 0) {
            mHandler.removeCallbacks(timerRunnable);
            if (Looper.myLooper() != Looper.getMainLooper()) {
                mHandler.post(timerRunnable);
            } else {
                timerRunnable.run();
            }
        }
    }

    private class TaskTimerRunnable implements Runnable {

        @Override
        public void run() {
            onTaskTimerListener.taskComplete();
        }
    }

    public interface OnTaskTimerListener {
        void taskComplete();
    }

}
