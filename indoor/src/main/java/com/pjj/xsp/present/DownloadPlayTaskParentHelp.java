package com.pjj.xsp.present;

import android.os.Handler;

import com.pjj.xsp.intent.http.RetrofitService;
import com.pjj.xsp.module.PlayTaskParent;
import com.pjj.xsp.module.ScreenInfManage;
import com.pjj.xsp.utils.FileUtils;

public class DownloadPlayTaskParentHelp {
    private int count;
    private Handler mHandler;
    private PlayTaskParent mPlayTaskParent;
    private int mMaxCount;
    private OnDownloadPlayTaskHelpListener mOnDownloadPlayTaskHelpListener;

    public DownloadPlayTaskParentHelp(Handler handler, PlayTaskParent playTaskParent, OnDownloadPlayTaskHelpListener onDownloadPlayTaskHelpListener) {
        mHandler = handler;
        mPlayTaskParent = playTaskParent;
        mOnDownloadPlayTaskHelpListener = onDownloadPlayTaskHelpListener;
    }

    public void downloadFile() {
        String filePath = mPlayTaskParent.getFilePath();
        String videoPath = null;
        mMaxCount = 1;
        if ("3".equals(mPlayTaskParent.getTempletType())) {
            videoPath = mPlayTaskParent.getVideoPath();
            mMaxCount = 2;
        }
        download(ScreenInfManage.filePathMedia + filePath);
        if (mMaxCount == 2) {
            download(ScreenInfManage.filePathMedia + videoPath);
        }
    }

    private void download(String path) {
        RetrofitService.getInstance().downloadFile(path, new FileUtils.OnDownloadListener() {
            @Override
            public void success() {
                updateCount();
            }

            @Override
            public void fail() {
                mHandler.postDelayed(() -> download(path), 2000);
            }
        });
    }

    private synchronized void updateCount() {
        ++count;
        if (count == mMaxCount) {
            mHandler.post(() -> mOnDownloadPlayTaskHelpListener.success());
        }
    }

    public interface OnDownloadPlayTaskHelpListener {
        void success();
    }
}
