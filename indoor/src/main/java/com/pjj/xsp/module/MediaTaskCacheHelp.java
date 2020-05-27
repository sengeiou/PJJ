package com.pjj.xsp.module;

import android.text.TextUtils;

import com.pjj.xsp.utils.Log;

import com.pjj.xsp.PjjApplication;
import com.pjj.xsp.db.DateTask;
import com.pjj.xsp.db.HandleDaoDb;
import com.pjj.xsp.utils.DateUtils;
import com.pjj.xsp.utils.TaskTimerUtils;
import com.pjj.xsp.utils.TextJsonUtils;

public class MediaTaskCacheHelp implements TaskTimerUtils.OnTaskTimerListener {
    private static MediaTaskCacheHelp instance;
    private final String TAG = "TAG_MediaTaskCache";
    private final TaskTimerUtils taskTimerUtils;
    public int mHour = -1;

    public static MediaTaskCacheHelp getInstance() {
        if (null == instance) {
            synchronized (MediaTaskCacheHelp.class) {
                if (null == instance)
                    instance = new MediaTaskCacheHelp();
            }
        }
        return instance;
    }

    /**
     * 普通任务链表
     */
    private MediaTaskCache mediaTaskCache = new MediaTaskCache("TAG_MediaTaskCache");
    /**
     * 定点播放任务链表
     */
    private MediaLimitTaskCache mediaLimitTaskCache = new MediaLimitTaskCache("TAG_MediaLimitTaskCache");
    /**
     * 当前播放任务库
     */
    private MediaTaskCache mediaNowTaskCache;

    private MediaTaskCacheHelp() {
        taskTimerUtils = new TaskTimerUtils();
        taskTimerUtils.setOnTaskTimerListener(this);
        mediaNowTaskCache = mediaTaskCache;
    }

    private void startLimitTask() {
        if (mediaLimitTaskCache == mediaNowTaskCache) {
            mediaLimitTaskCache.clearTask();
        } else {
            mediaNowTaskCache = mediaLimitTaskCache;
        }
        taskTimerUtils.clear();
        if (null != onMediaTaskCacheListener) {
            onMediaTaskCacheListener.addNoTaskView();
        }
        starMediaTask();
    }

    private void initLimitTask() {
        mediaLimitTaskCache.initTodayAllLimitTask();
    }

    /**
     * 检测此小时内的定点任务
     *
     * @param hour
     * @return
     */
    private boolean checkHourLimitTask(int hour) {
        if (this.mHour == -1) {
            return false;
        }
        return mediaLimitTaskCache.hasHourTask(hour);
    }

    public void addLimitTask(DateTask dateTask) {
        boolean b = mediaLimitTaskCache.addLimitListTask(dateTask);
        if (b && !mediaLimitTaskCache.hasInit()) {
            mediaNowTaskCache = mediaLimitTaskCache;
            taskTimerUtils.clear();
            if (null != onMediaTaskCacheListener) {
                onMediaTaskCacheListener.addNoTaskView();
            }
            starMediaTask();
        }
    }

    public void addTask(PlayTaskParent playTaskParent) {
        mediaTaskCache.addTask(playTaskParent);
        //Log.e(TAG, "addTask: path=" + playTaskParent.getFilePath() + " ," + playTaskParent.getVideoPath() + ", size=" + mediaNowTaskCache.size());
    }

    public void addTaskAndCheckStart(PlayTaskParent playTaskParent) {
        mediaTaskCache.addTaskAndCheckStart(playTaskParent);
        //Log.e(TAG, "addTask: path=" + playTaskParent.getFilePath() + " ," + playTaskParent.getVideoPath() + ", size=" + mediaNowTaskCache.size());
        if (mediaNowTaskCache == mediaTaskCache && !mediaTaskCache.hasInit()) {
            starMediaTask();
        }
    }

    private void starMediaTask() {
        if (null != onMediaTaskCacheListener) {
            //Log.e(TAG, "starMediaTask: ");
            onMediaTaskCacheListener.initTaskView();
        }
    }

    public void startTask(boolean hasLimit) {
        if (hasLimit) {
            initLimitTask();
        }
        int nowHour = DateUtils.getNowHour();
        mHour = nowHour;
        if (mediaLimitTaskCache.hasHourTask(nowHour)) {
            startLimitTask();
        } else {
            starMediaTask();
        }
    }

    public void checkNowHour(int hour) {
        if (mHour == -1) {
            return;
        }
        if (mHour == hour) {
            return;
        }
        if (checkHourLimitTask(hour)) {
            mHour = hour;
            startLimitTask();
        } else {
            clearLimitTask();
        }
    }

    private void clearLimitTask() {
        if (mHour != -1) {
            mediaLimitTaskCache.clearTask();
            if (mediaLimitTaskCache == mediaNowTaskCache) {
                taskTimerUtils.clear();
                mediaNowTaskCache = mediaTaskCache;
                if (null != onMediaTaskCacheListener) {
                    onMediaTaskCacheListener.addNoTaskView();
                }
                if (PjjApplication.application.isFlag())
                    starMediaTask();
            }
        }
    }

    public void suspendedClearTask() {
        mediaTaskCache.clearTask();
        if (mediaNowTaskCache != mediaLimitTaskCache) {
            taskTimerUtils.clear();
            if (null != onMediaTaskCacheListener) {
                onMediaTaskCacheListener.addNoTaskView();
            }
        }
    }

    public void deleteOrder(String orderId) {
        if (TextUtils.isEmpty(orderId)) {//无效订单
            return;
        }
        //Log.e(TAG, "deleteOrder: " + orderId);
        mediaNowTaskCache.deleteOrder(orderId);
        if (TextJsonUtils.isNotEmpty(mediaNowTaskCache.mediaTaskList)) {
            //从他当天的任务列表删除
            for (PlayTaskParent next : mediaNowTaskCache.mediaTaskList) {
                if (orderId.equals(next.getTaskTag())) {
                    if (onMediaTaskCacheListener.eqPreTask(next)) {
                        //Log.e(TAG, "deleteOrder: same next");
                        onMediaTaskCacheListener.reSetPreTask(getPreMediaTask());
                    } else if (onMediaTaskCacheListener.eqNowTask(next)) {
                        //Log.e(TAG, "deleteOrder: same now");
                        if (!onMediaTaskCacheListener.preCanUse()) {
                            PlayTaskParent preMediaTask = getPreMediaTask();
                            //Log.e(TAG, "deleteOrder: pre not can use -- " + (null == preMediaTask));
                            if (null == preMediaTask) {
                                if (mediaNowTaskCache == mediaTaskCache) {
                                    clearTask();
                                } else {
                                    clearLimitTask();
                                }
                            } else {
                                onMediaTaskCacheListener.reSetPreTask(preMediaTask);
                                taskTimerUtils.nowComplete();
                            }
                        } else {
                            //Log.e(TAG, "deleteOrder: pre can use");
                            taskTimerUtils.nowComplete();
                        }
                    }
                }
            }
            boolean cleanAll = true;
            for (PlayTaskParent next : mediaNowTaskCache.mediaTaskList) {
                if (!next.deleteTag()) {
                    cleanAll = false;
                }
            }
            if (cleanAll) {//检测 最后
                if (mediaNowTaskCache == mediaTaskCache) {
                    clearTask();
                } else {
                    clearLimitTask();
                }
            }
        }
        //查询数据库 order
        HandleDaoDb.deleteTask(orderId);
    }

    public void clearTask() {
        mediaTaskCache.clearTask();
        mediaLimitTaskCache.clearAll();
        taskTimerUtils.clear();
        mHour = -1;
        if (null != onMediaTaskCacheListener) {
            onMediaTaskCacheListener.addNoTaskView();
        }
    }

    public PlayTaskParent initMediaTask() {
        PlayTaskParent playTaskParent = mediaNowTaskCache.initMediaTask();
        if (playTaskParent != null) {
            taskTimerUtils.setPlayTime(playTaskParent.getPlayTime()).start();
        }
        return playTaskParent;
    }

    public PlayTaskParent getPreMediaTask() {
        return mediaNowTaskCache.getPreMediaTask();
    }

    /**
     * 开始播放下一个（已预加载）任务
     */
    private void startPlayNextTask() {
        int playTime = mediaNowTaskCache.getPlayNextTaskTime();
        //Log.e(TAG, "startPlayNextTask: playTime=" + playTime);
        taskTimerUtils.setPlayTime(playTime).start();
        if (null != onMediaTaskCacheListener) {
            onMediaTaskCacheListener.startPlayPre();
        }
    }


    @Override
    public void taskComplete() {
        startPlayNextTask();
    }

    private OnMediaTaskCacheListener onMediaTaskCacheListener;

    public void setOnMediaTaskCacheListener(OnMediaTaskCacheListener onMediaTaskCacheListener) {
        this.onMediaTaskCacheListener = onMediaTaskCacheListener;
    }

    public interface OnMediaTaskCacheListener {
        void startPlayPre(/*boolean sameIndex*/);

        void addNoTaskView();

        void initTaskView();

        boolean eqNowTask(PlayTaskParent parent);

        boolean eqPreTask(PlayTaskParent parent);

        void reSetPreTask(PlayTaskParent parent);

        boolean preCanUse();
    }

//    public static boolean taskEquals(PlayTaskParent playTaskParent, PlayTaskParent playTaskParentOther) {
//        if (playTaskParent == playTaskParentOther)
//            return true;
//        if (null != playTaskParent && null != playTaskParentOther) {
//            String templetType = playTaskParent.getTempletType();
//            if (templetType.equals(playTaskParentOther.getTempletType())) {
//                if (playTaskParent.getFilePath().equals(playTaskParentOther.getFilePath())) {
//                    if (!templetType.equals("3"))
//                        return true;
//                    else {
//                        return playTaskParent.getVideoPath().equals(playTaskParentOther.getVideoPath());
//                    }
//                }
//            }
//        }
//        return false;
//    }

}
