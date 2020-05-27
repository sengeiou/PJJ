package com.pjj.xsp.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.pjj.xsp.PjjApplication;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.security.MessageDigest;

import static com.bumptech.glide.load.resource.bitmap.VideoDecoder.FRAME_OPTION;

/**
 * Create by xinheng on 2018/11/12。
 * describe：
 */
public class BitmapUtils {
    public static void saveBitmapThread(Bitmap bitmap, Bitmap.CompressFormat format, String path, int quality, OnSaveListener onSaveListener) {
        new Thread() {
            @Override
            public void run() {
                File file = new File(path);
                createFolder(file.getParentFile());
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (null != out)
                    bitmap.compress(format, quality, out);//peg支持透明
                if (file.length() > 1) {
                    onSaveListener.saveResult(true);
                } else {
                    onSaveListener.saveResult(false);
                }
            }
        }.start();
    }

    public static void createFolder(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 获取视频其中一帧的图片
     *
     * @param frameTimeMicros 获取某一时间帧
     */
    @SuppressLint("CheckResult")
    public static RequestOptions getGlideRequestOptionsForVideo(long frameTimeMicros) {
        RequestOptions requestOptions = RequestOptions.frameOf(frameTimeMicros);
        requestOptions.set(FRAME_OPTION, MediaMetadataRetriever.OPTION_CLOSEST);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.transform(new BitmapTransformation() {
            @Override
            protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
                return toTransform;
            }

            @Override
            public void updateDiskCacheKey(MessageDigest messageDigest) {
                try {
                    messageDigest.update((PjjApplication.application.getPackageName() + "RotateTransform").getBytes("utf-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return requestOptions;
    }

    /**
     * 加载视频第一帧图片
     *
     * @param requestManager
     * @param path           路径
     * @param imageView
     */
    public static void loadFirstImageForVideo(RequestManager requestManager, String path, ImageView imageView) {
        requestManager.load(path).apply(getGlideRequestOptionsForVideo(1)).into(imageView);
    }

    public interface OnSaveListener {
        void saveResult(boolean tag);
    }

    /**
     * 手机截图
     *
     * @return 返回截图的路径
     */
    public static String getScreenshot() {
        Process process = null;
        String filePath = PjjApplication.App_Path + "screenshots/screen_" + System.currentTimeMillis() + ".png";
        try {
            process = Runtime.getRuntime().exec("su");
            PrintStream outputStream = null;
            outputStream = new PrintStream(new BufferedOutputStream(process.getOutputStream(), 8192));
            outputStream.println("screencap -p " + filePath);
            outputStream.flush();
            outputStream.close();
            process.waitFor();
            //InputStream inputStream = process.getInputStream();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        if (new File(filePath).exists()) {
            return filePath;
        } else {
            return null;
        }
    }
}