package com.pjj.xsp.view.custom;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;

import com.pjj.xsp.utils.Log;

import java.io.File;
import java.io.IOException;

public class TextureVideoView extends TextureView implements TextureView.SurfaceTextureListener {
    private final String TAG = "TAG_TextureVideoView";
    private Surface surface;
    private MediaPlayer mMediaPlayer;
    private MediaPlayer.OnPreparedListener onPreparedListener;
    private int mVideoWidth;
    private int mVideoHeight;
    private String mVideoPath;
    //播放加载状态 0初始 1预加载完成 2有播放请求
    private int playStatue;
    private boolean destroyedTag;
    /**
     * 适应屏幕
     */
    private boolean tagScreen = true;

    public TextureVideoView(Context context) {
        super(context);
        init();
    }

    public TextureVideoView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init();
    }

    public TextureVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            int mVideoWidth = mp.getVideoWidth();
            int mVideoHeight = mp.getVideoHeight();
            setVideoSize(mVideoWidth, mVideoHeight);
            //Log.e(TAG, "init: start play " + playStatue);
            if (playStatue == 2 && null != surface) {
                //Log.e("TAG", "setOnPreparedListener start111" + ", " + mVideoPath);
                mp.start();
            } else {
                playStatue = 1;
            }
            if (null != onPreparedListener) {
                onPreparedListener.onPrepared(mp);
            }
        });
        mMediaPlayer.setOnErrorListener((mp, what, extra) -> {
            //Log.e(TAG, "what=" + what + ", extra=" + extra + ", " + mVideoPath);
            mp.reset();
            if (!TextUtils.isEmpty(mVideoPath)) {
                //setVideoPath_(mVideoPath);
            }
            return true;
        });
        setSurfaceTextureListener(this);
        if (!TextUtils.isEmpty(mVideoPath)) {
            setVideoPath_(mVideoPath);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(mVideoWidth, widthMeasureSpec);
        int height = getDefaultSize(mVideoHeight, heightMeasureSpec);
        if (mVideoWidth > 0 && mVideoHeight > 0) {
            int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
            int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
            int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

            if (widthSpecMode == MeasureSpec.EXACTLY && heightSpecMode == MeasureSpec.EXACTLY) {
                // the size is fixed
                width = widthSpecSize;
                height = heightSpecSize;

                // for compatibility, we adjust size based on aspect ratio
                if (mVideoWidth * height < width * mVideoHeight) {
                    //Log.i("@@@", "image too wide, correcting");
                    width = height * mVideoWidth / mVideoHeight;
                } else if (mVideoWidth * height > width * mVideoHeight) {
                    //Log.i("@@@", "image too tall, correcting");
                    height = width * mVideoHeight / mVideoWidth;
                }
            } else if (widthSpecMode == MeasureSpec.EXACTLY) {
                // only the width is fixed, adjust the height to match aspect ratio if possible
                width = widthSpecSize;
                height = width * mVideoHeight / mVideoWidth;
                if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                    // couldn't match aspect ratio within the constraints
                    height = heightSpecSize;
                }
            } else if (heightSpecMode == MeasureSpec.EXACTLY) {
                // only the height is fixed, adjust the width to match aspect ratio if possible
                height = heightSpecSize;
                width = height * mVideoWidth / mVideoHeight;
                if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                    // couldn't match aspect ratio within the constraints
                    width = widthSpecSize;
                }
            } else {
                // neither the width nor the height are fixed, try to use actual video size
                width = mVideoWidth;
                height = mVideoHeight;
                if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                    // too tall, decrease both width and height
                    height = heightSpecSize;
                    width = height * mVideoWidth / mVideoHeight;
                }
                if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                    // too wide, decrease both width and height
                    width = widthSpecSize;
                    height = width * mVideoHeight / mVideoWidth;
                }
            }
        } else {
            // no size yet, just adopt the given spec sizes
        }
        //Log.e("TAG", "onMeasure: video" + width + ", " + height);
        setMeasuredDimension(width, height);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        //Log.e(TAG, "onSurfaceTextureAvailable: width=" + width + ", height=" + height);
        if (null == surface) {
            surface = new Surface(surfaceTexture);
            if (null != mMediaPlayer) {
                mMediaPlayer.setSurface(surface);
                if (null != mVideoPath && destroyedTag && playStatue == 2 && !mMediaPlayer.isPlaying()) {
                    destroyedTag = false;
                    //Log.e("TAG", "onSurfaceTextureAvailable start222" + ", " + mVideoPath);
                    mMediaPlayer.start();
                }
            }
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        //Log.e(TAG, "onSurfaceTextureSizeChanged:  width=" + width + ", height=" + height + ", " + mVideoPath);
        //getBitmap()
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        //Log.e(TAG, "onSurfaceTextureDestroyed: " + ", " + mVideoPath);
        if (null != mMediaPlayer && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
        surfaceTexture.release();
        if (null != surface) {
            surface.release();
            surface = null;
        }
        //stopPlayback();
        destroyedTag = true;
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        //Log.e(TAG, "onSurfaceTextureUpdated: ");
    }

    public void setOnPreparedListener(MediaPlayer.OnPreparedListener listener) {
        this.onPreparedListener = listener;
    }

    public void setVideoPath(String path) {
        mVideoPath = path;
        playStatue = 0;
        if (null != mMediaPlayer) {
            setVideoPath_(path);
        }
    }

    private void setVideoPath_(String path) {
        try {
            //mMediaPlayer.setDataSource(path);
            Uri parse = Uri.fromFile(new File(path));
//            Uri parse = Uri.parse(path);
            //Log.e(TAG, "setVideoPath_: " + parse.getScheme() + ", path=" + path);
            mMediaPlayer.setDataSource(getContext(), parse, null);
            mMediaPlayer.prepareAsync();
//            mMediaPlayer.prepare();
//            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        if (null != mMediaPlayer && playStatue == 1 && null != surface) {
            Log.e("TAG", "start start333" + ", " + mVideoPath);
            mMediaPlayer.start();
        }
        playStatue = 2;
    }

    public void pause() {
        if (null != mMediaPlayer && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }

    public void stopPlayback() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void rePlay() {
        if (null != mMediaPlayer && mMediaPlayer.isPlaying()) {
            mMediaPlayer.seekTo(0);
        }
    }

    public void setVideoSize(int width, int height) {
        // Log.e("TAG", "setVideoSize: width=" + width + ", height=" + height);
        if (width >= height || tagScreen) {
            mVideoWidth = width;
            mVideoHeight = height;
        } else {
            mVideoWidth = 0;
            mVideoHeight = 0;
        }
        requestLayout();
    }
}
