package com.pjj.xsp.manage;

import android.util.Log;

import com.pjj.xsp.manage.CollectorSession;

public class BoardRunnable implements Runnable {

    private static final String TAG = "BoardRunnable";

    private int boardThreadMill = 500;

    CollectorSession session = CollectorSession.getInstance();

    @Override
    public void run() {
        while (Thread.currentThread() != null && !Thread.currentThread().isInterrupted()) {
            try {
                if (true) {
                    session.sendDataRequest("021000001003");
                }
                Thread.sleep(boardThreadMill);//默认500毫秒获取一次板卡信息
            } catch (InterruptedException e) {
                Log.e(TAG, "[BoardThread][interrupted][" + e.getMessage() + "]");
            }
        }
    }
}

