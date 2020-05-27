package com.pjj.xsp.view.custom;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import com.pjj.xsp.utils.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Create by xinheng on 2018/11/05。
 * describe：为了适应显示屏系统，很僵。。。
 */
public class LinearForSurface extends LinearLayout {
    private String TAG = "TAG";
    private SurfaceVideoView surfaceVideoView;

    public LinearForSurface(Context context) {
        super(context);
    }

    public LinearForSurface(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LinearForSurface(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void setViedoSize(int mVideoWidth, int mVideoHeight) {
        Log.e("TAG", "setViedoSize: width=" + mVideoWidth + ", height=" + mVideoHeight + ", ");
        //requestLayout();
        if (mVideoWidth > 0 && mVideoHeight > 0) {
            if (mVideoWidth < mVideoHeight) {
                addSurfaceView(getMeasuredWidth(), getMeasuredHeight());
                return;
            }
            int width = getMeasuredWidth();
            int height = getMeasuredHeight();
            // for compatibility, we adjust size based on aspect ratio
            if (mVideoWidth * height < width * mVideoHeight) {
                //Log.i("@@@", "image too wide, correcting");
                width = height * mVideoWidth / mVideoHeight;
            } else if (mVideoWidth * height > width * mVideoHeight) {
                //Log.i("@@@", "image too tall, correcting");
                height = width * mVideoHeight / mVideoWidth;
            }
            Log.e("TAG", "setViedoSize11: width=" + width + ", height=" + height);
            addSurfaceView(getMeasuredWidth(), height);
        } else {
            addSurfaceView(getMeasuredWidth(), getMeasuredHeight());
        }
    }

    private void addSurfaceView(int width, int height) {
        FrameLayout fl = new FrameLayout(getContext());
        LayoutParams params = new LayoutParams(width, height);
        //params.topMargin=-10;
        fl.setLayoutParams(params);
        //fl.setPadding(0,-50,0,0);
        surfaceVideoView = getSurfaceViewView();
        FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        //params1.topMargin=-10;
        //surfaceVideoView.setLayoutParams(params1);
        fl.addView(surfaceVideoView);
        addView(fl);
        surfaceVideoView.setZOrderOnTop(true);
    }

    public void playPath(String videoPath) {
        post(() -> playPath1(videoPath));
    }

    private void playPath1(String videoPath) {
        Log.e(TAG, "playPath: " + videoPath);
        Log.e(TAG, "playPath: " + System.currentTimeMillis());
        MediaMetadataRetriever retr = new MediaMetadataRetriever();
        if (videoPath.contains(""))
            if (videoPath.contains("android.resource:")) {
                retr.setDataSource(getContext(), Uri.parse(videoPath));
            } else {
                retr.setDataSource(videoPath);
            }
        String height1 = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT); // 视频高度
        String width1 = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH); // 视频宽度
        String rotation1 = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION); // 视频旋转方向
        retr.release();
        Log.e(TAG, "playPath: " + System.currentTimeMillis());
        int width = 0;
        int height = 0;
        int rotation = 0;
        try {
            width = (int) Float.parseFloat(width1);
            height = (int) Float.parseFloat(height1);
            rotation = (int) Float.parseFloat(rotation1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.e(TAG, "playPath: " + width1 + ", " + height1 + ", " + rotation);
        if (rotation == 90 || rotation == 270) {
            int a = width;
            width = height;
            height = a;
        }

        setViedoSize(width, height);

        post(() -> addSurfaceView(videoPath));
    }

    private void addSurfaceView(String videoPath) {
        Log.e(TAG, "addSurfaceView: " + getMeasuredWidth() + ", " + getMeasuredHeight());
        surfaceVideoView.playPath(videoPath);
    }

    private SurfaceVideoView getSurfaceViewView() {
        surfaceVideoView = new SurfaceVideoView(getContext());
        return surfaceVideoView;
    }

    public void release() {
        if (null != surfaceVideoView) {
            surfaceVideoView.release();
            removeAllViews();
            surfaceVideoView = null;
        }
    }
}
