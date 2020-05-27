package com.pjj.xsp.module;

import android.os.Handler;
import android.text.TextUtils;

import com.pjj.xsp.intent.http.RetrofitService;
import com.pjj.xsp.present.DownloadPlayTaskParentHelp;
import com.pjj.xsp.utils.Log;

import com.pjj.xsp.BuildConfig;
import com.pjj.xsp.PjjApplication;
import com.pjj.xsp.db.HandleDaoDb;
import com.pjj.xsp.db.TaskText;
import com.pjj.xsp.utils.FileUtils;

import java.util.ArrayList;
import java.util.Iterator;

public class MediaTaskCache {
    private String TAG;
    private PlayTaskParent nextTask;

    MediaTaskCache(String tag) {
        TAG = tag;
    }

    /**
     * 普通任务链表
     */
    ArrayList<PlayTaskParent> mediaTaskList = new ArrayList<>();
    /**
     * 当前播放任务下标
     */
    int indexNow = -1;
    private int nextIndex;

    void addTask(PlayTaskParent playTaskParent) {
        mediaTaskList.add(playTaskParent);
        //Log.e(TAG, "addTask: path=" + playTaskParent.getFilePath() + " ," + playTaskParent.getVideoPath() + ", size=" + mediaTaskList.size());
    }

    void addTaskAndCheckStart(PlayTaskParent playTaskParent) {
        mediaTaskList.add(playTaskParent);
        //Log.e(TAG, "addTaskAndCheckStart: path=" + playTaskParent.getFilePath() + " ," + playTaskParent.getVideoPath() + ", size=" + mediaTaskList.size());
    }


    void clearTask() {
        mediaTaskList.clear();
        indexNow = -1;
        nextTask = null;
    }

    void deleteOrder(String orderId) {
        if (TextUtils.isEmpty(orderId)) {//无效订单
            return;
        }
        //从他当天的任务列表删除
        for (PlayTaskParent next : mediaTaskList) {
            if (orderId.equals(next.getTaskTag())) {
                if (next instanceof TextTask) {
                    ((TextTask) next).setDeleteTag(true);
                }
            }
        }
        //查询数据库 order
        HandleDaoDb.deleteTask(orderId);
    }

    private PlayTaskParent getNextTask() {
        if (mediaTaskList.size() == 0) {
            indexNow = -1;
            return null;
        }
        nextIndex = indexNow + 1;
        if (nextIndex >= mediaTaskList.size()) {
            nextIndex = 0;
        }
        if (nextIndex == indexNow) {
            return null;
        }
        PlayTaskParent playTaskParent = mediaTaskList.get(nextIndex);
        if (playTaskParent.deleteTag()) {
            while (nextIndex != indexNow) {
                PlayTaskParent parent = mediaTaskList.get(nextIndex);
                if (!parent.deleteTag()) {
                    return parent;
                }
                ++nextIndex;
                if (nextIndex >= mediaTaskList.size()) {
                    nextIndex = 0;
                }
            }
            String msg = "getNextTask: nextIndex=" + nextIndex + ", indexNow=" + indexNow;
            Log.e(TAG, msg);
            if (BuildConfig.DEBUG) {
                FileUtils.saveStringFile(PjjApplication.App_Path + "media_task.txt", msg);
            }
            return null;
        } else {
            String msg = "getNextTask: nextIndex=" + nextIndex + ", indexNow=" + indexNow;
            Log.e(TAG, msg);
            if (BuildConfig.DEBUG) {
                FileUtils.saveStringFile(PjjApplication.App_Path + "media_task.txt", msg);
            }
            return playTaskParent;
        }
    }

    boolean hasInit() {
        return indexNow > -1;
    }

    /**
     * 初始化第一个任务
     * 或者接着上次的播放节点
     *
     * @return
     */
    PlayTaskParent initMediaTask() {
        if (mediaTaskList.size() == 0) {
            return null;
        }
        if (indexNow == -1) {
            indexNow = 0;
        }
        Log.e(TAG, "initMediaTask: indexNow=" + indexNow);
        return mediaTaskList.get(indexNow);
    }

    PlayTaskParent getPreMediaTask() {
        nextTask = getNextTask();
        return nextTask;
    }

    /**
     * 获取播放下一个（已预加载）任务
     */
    int getPlayNextTaskTime() {
        //Log.e(TAG, "getPlayNextTaskTime: indexNow=" + indexNow + ", nextIndex=" + nextIndex);
        if (indexNow != nextIndex) {
            indexNow = nextIndex;
        }
        int playTime;
        if (null != nextTask) {
            playTime = nextTask.getPlayTime();
        } else {
            playTime = mediaTaskList.get(indexNow).getPlayTime();
        }
        return playTime;
    }

    public int size() {
        return mediaTaskList.size();
    }
}
