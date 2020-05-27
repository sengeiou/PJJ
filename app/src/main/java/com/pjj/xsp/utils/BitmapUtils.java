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

import java.security.MessageDigest;

import static com.bumptech.glide.load.resource.bitmap.VideoDecoder.FRAME_OPTION;

/**
 * Create by xinheng on 2018/11/12。
 * describe：
 */
public class BitmapUtils {

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

}