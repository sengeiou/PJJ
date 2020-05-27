package com.pjj.xsp.view.custom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import com.pjj.xsp.utils.Log;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import com.pjj.xsp.module.MediaTaskCacheHelp;
import com.pjj.xsp.module.PlayTaskParent;
import com.pjj.xsp.module.ScreenInfManage;
import com.pjj.xsp.module.XspPlayUI;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.pjj.xsp.PjjApplication.App_Path;

public class MediaSlideViewGroup extends FrameLayout implements MediaTaskCacheHelp.OnMediaTaskCacheListener {
    private final String TAG = "TAG";
    private int ScreenWidth;
    private boolean firstInitTag = false;
    private ObjectAnimator objectAnimator;
    private PlayTaskParent nowTask;
    private PlayTaskParent preTask;

    /**
     * view持有者
     */
    private List<OnPlayParent> viewHolders;
    private MediaRunnable addViewRunnable;

    public MediaSlideViewGroup(Context context) {
        this(context, null);
    }

    public MediaSlideViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MediaSlideViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //setOrientation(HORIZONTAL);
        //绑定监听
        MediaTaskCacheHelp.getInstance().setOnMediaTaskCacheListener(this);
        viewHolders = new ArrayList<>();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        ScreenWidth = displayMetrics.widthPixels;
        objectAnimator = new ObjectAnimator();
        objectAnimator.setFloatValues(ScreenWidth, 0);
        objectAnimator.setDuration(1000);
        objectAnimator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            if (animation instanceof ObjectAnimator) {
                Object target = ((ObjectAnimator) animation).getTarget();
                if (target instanceof View) {
                    //((View) target).setX(value);
                    ((View) target).setAlpha(1 - value / ScreenWidth);
                }
            }
        });
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (animation instanceof ObjectAnimator) {
                    Object target = ((ObjectAnimator) animation).getTarget();
                    if (target instanceof View) {
                        ((View) target).setX(0);
                    }
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                removeChildView(0);
                runAddViewRunnable();
            }
        });
        addOnlyOne(XspPlayUI.getInstall().defaultTask);
    }

    @Override
    public void initTaskView() {
        post(() -> {
            PlayTaskParent playTaskParent = MediaTaskCacheHelp.getInstance().initMediaTask();
            viewHolders.clear();
            if (null != playTaskParent) {
                firstInitTag = true;
                //removeAllViews();
                addContentViewMainThread(playTaskParent);
                addViewRunnable.run();
                nowTask = playTaskParent;
                addPreTaskView();
                viewHolders.get(0).startPlay();
                startAnimator(getChildAt(1));
            /*if (null != addViewRunnable)//不一定有下一个任务
                addViewRunnable.run();*/
            }
        });
    }

    private void clearRunnableTask() {
        //清除初始化或预加载任务
        addViewRunnable = null;
    }

    private void addPreTaskView() {
        PlayTaskParent preMediaTask = MediaTaskCacheHelp.getInstance().getPreMediaTask();
        if (null != preMediaTask) {
            addContentViewMainThread(preMediaTask);
        } else {
            //Log.e(TAG, "addPreTaskView: pre=null");
        }
    }

    private void addContentViewMainThread(PlayTaskParent parent) {
        String templetType = parent.getTempletType();
        ////Log.e(TAG, "addContentViewMainThread: " + parent.getFilePath() + ", " + parent.getVideoPath());
        switch (templetType) {
            case "3"://两个
                addViewRunnable = new MediaRunnable() {
                    @Override
                    void mediaRun() {
                        preTask = parent;
                        addTowMedia(parent);
                    }
                };
                break;
            default://一个
                addViewRunnable = new MediaRunnable() {
                    @Override
                    void mediaRun() {
                        preTask = parent;
                        addOnlyOne(parent);
                    }
                };
        }
    }

    private void removeChildView(int index) {
        if (viewHolders.size() > 1) {
            OnPlayParent deleteOnPlayParent = getNowOnPlayParent(index);
            viewHolders.remove(deleteOnPlayParent);
            deleteOnPlayParent.recycle();
        }
        removeViewsInLayout(index, 1);
    }

    private OnPlayParent getNowOnPlayParent(int index, View deleteView) {
        OnPlayParent deleteOnPlayParent = viewHolders.get(index);
        if (deleteView != deleteOnPlayParent.getView()) {
            for (int i = 0; i < viewHolders.size(); i++) {
                if (deleteView == viewHolders.get(i).getView()) {
                    deleteOnPlayParent = viewHolders.get(i);
                    break;
                }
            }
        }
        return deleteOnPlayParent;
    }

    private OnPlayParent getNowOnPlayParent(int index) {
        return getNowOnPlayParent(index, getChildAt(index));
    }

    private void addOnlyOne(PlayTaskParent parent) {
        String filePath = getPath(parent.getFilePath());
        String fileType = parent.getFileType();
        OnPlayParent onPlayParent;
        if ("1".equals(fileType)) {//图片
            onPlayParent = new ImageContract();
        } else {
            onPlayParent = new VideoContract();
        }
        onPlayParent.setMediaPath(filePath);
        viewHolders.add(onPlayParent);
        //onPlayParent.startPlay();
        addContentView(onPlayParent.getView(), getMatchLayoutParams());
    }

    private void addTowMedia(PlayTaskParent parent) {
        String imagePath = getPath(parent.getFilePath());
        String videoPath = getPath(parent.getVideoPath());
        ImageContract imageContract = new ImageContract();
        imageContract.setMediaPath(imagePath);
        VideoContract videoContract = new VideoContract();
        videoContract.setMediaPath(videoPath);
        TowViewContract towViewContract = new TowViewContract(videoContract, imageContract);
        //towViewContract.startPlay();
        viewHolders.add(towViewContract);
        addContentView(towViewContract.getView(), getMatchLayoutParams());
    }

    private void addContentView(View view, FrameLayout.LayoutParams params) {
        //addView(view, params);
        if (firstInitTag) {
            view.setAlpha(0);
            view.setX(ScreenWidth);
        }
        addViewInLayout(view, -1, params, true);
        //view.requestLayout();
    }


    private void startAnimator(View view) {
        nowTask = preTask;
        objectAnimator.setTarget(view);
        objectAnimator.start();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    private LayoutParams getMatchLayoutParams() {
        return new LayoutParams(ScreenWidth, LayoutParams.MATCH_PARENT);
    }

    private LinearLayout.LayoutParams getMatchHalfLayoutParams() {
        return new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 1);
    }

    private String getPath(String filePath) {
        ////Log.e("TAG", "getPath: filePath="+filePath);
        if (filePath.startsWith("file:///")) {
            return filePath;
        }
        File file = new File(App_Path + filePath);
        if (file.exists()) {
            filePath = file.getAbsolutePath();
            // //Log.e("TAG", "onViewCreated: 本地 = " + filePath);
        } else {
            filePath = ScreenInfManage.filePathMedia + filePath;
        }
        return filePath;
    }

    @Override
    public void startPlayPre() {
        //Log.e(TAG, "startPlayPre: childCount=" + getChildCount());
        addPreTaskView();
        if (getChildCount() > 1) {
            if (viewHolders.size() > 1)
                viewHolders.get(viewHolders.size() - 2).pause();
            OnPlayParent onPlayParent = viewHolders.get(viewHolders.size() - 1);
            //Log.e(TAG, "start play path=" + onPlayParent.getMediaPath());
            onPlayParent.startPlay();
            startAnimator(getChildAt(1));
        } else {
            runAddViewRunnable();
            viewHolders.get(0).rePlay();
        }
    }

    @Override
    public boolean eqNowTask(PlayTaskParent parent) {
        return parent == nowTask;
    }

    @Override
    public boolean eqPreTask(PlayTaskParent parent) {
        return parent == preTask;
    }

    @Override
    public void reSetPreTask(PlayTaskParent parent) {
        preTask = null;
        if (null != parent) {
            addContentViewMainThread(parent);
        }
        removeChildView(1);
        runAddViewRunnable();
    }

    @Override
    public boolean preCanUse() {
        if (null == preTask) {
            return false;
        }
        return !preTask.deleteTag();
    }

    private void runAddViewRunnable() {
        if (null != addViewRunnable) {
            addViewRunnable.run();
        } else {
            preTask = null;
        }
    }

    @Override
    public void addNoTaskView() {
        post(() -> {
            removeAllViews();
            firstInitTag = false;
            addOnlyOne(XspPlayUI.getInstall().defaultTask);
            requestLayout();
        });
    }

    private class VideoContract implements OnPlayParent {
        private TextureVideoView videoView;
        //        private MyVideoView videoView
        private FrameLayout fl;
        private String path;

        VideoContract() {
            videoView = new TextureVideoView(getContext());
            //videoView.setZOrderOnTop(true);
            //videoView.setZOrderMediaOverlay(true);
            fl = new FrameLayout(getContext());
            fl.setBackgroundColor(Color.BLACK);
            fl.addView(videoView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER));
        }

        public View getView() {
            return fl;
        }

        public TextureVideoView getVideoView() {
            return videoView;
        }

        @Override
        public void setMediaPath(String path) {
            this.path = path;
            videoView.setVideoPath(path);
        }

        @Override
        public String getMediaPath() {
            return path;
        }

        @Override
        public void startPlay() {
            videoView.start();
        }

        @Override
        public void rePlay() {
            videoView.rePlay();
        }

        @Override
        public void pause() {
            videoView.pause();
        }

        @Override
        public void recycle() {
            videoView.stopPlayback();
            videoView = null;
        }
    }

    private class ImageContract implements OnPlayParent {
        private ImageView imageView;
        private String path;

        public ImageContract() {
            imageView = new ImageView(getContext());
        }

        public View getView() {
            return imageView;
        }

        @Override
        public void setMediaPath(String path) {
            this.path = path;
            ////Log.e(TAG, "setMediaPath: path=" + path );
            if (path.contains("android_asset")) {
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            }
            Glide.with(MediaSlideViewGroup.this).load(path).into(imageView);
        }

        @Override
        public String getMediaPath() {
            return path;
        }

        @Override
        public void startPlay() {
            //imageView.
        }

        @Override
        public void rePlay() {

        }

        @Override
        public void pause() {

        }

        @Override
        public void recycle() {
            imageView.setImageDrawable(null);
            imageView = null;
        }
    }

    private abstract class MoreViewContract<T extends ViewGroup> implements OnPlayParent {
        private OnPlayParent[] onPlayParents;
        private T parent;

        @Override
        public void pause() {
            for (int i = 0; i < onPlayParents.length; i++) {
                onPlayParents[i].pause();
            }
        }

        @Override
        public void rePlay() {
            for (int i = 0; i < onPlayParents.length; i++) {
                onPlayParents[i].rePlay();
            }
        }

        public MoreViewContract(T parent, OnPlayParent... onPlayParents) {
            this.parent = parent;
            this.onPlayParents = onPlayParents;
            fillParent(parent, onPlayParents);
        }

        protected abstract void fillParent(T parent, OnPlayParent[] onPlayParents);

        @Override
        public void startPlay() {
            for (int i = 0; i < onPlayParents.length; i++) {
                onPlayParents[i].startPlay();
            }
        }

        @Override
        public void setMediaPath(String path) {
        }

        @Override
        public String getMediaPath() {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < onPlayParents.length; i++) {
                buffer.append(onPlayParents[i].getMediaPath());
                buffer.append(", ");
            }
            return buffer.toString();
        }

        @Override
        public View getView() {
            return parent;
        }

        @Override
        public void recycle() {
            for (int i = 0; i < onPlayParents.length; i++) {
                onPlayParents[i].recycle();
            }
        }
    }

    private class TowViewContract extends MoreViewContract<LinearLayout> {

        public TowViewContract(OnPlayParent... onPlayParents) {
            super(new LinearLayout(getContext()), onPlayParents);
        }

        @Override
        protected void fillParent(LinearLayout parent, OnPlayParent[] onPlayParents) {
            parent.setOrientation(LinearLayout.VERTICAL);
            parent.setBackgroundColor(Color.BLACK);
            for (int i = 0; i < onPlayParents.length; i++) {
                View view = onPlayParents[i].getView();
                parent.addView(view, getMatchHalfLayoutParams());
                //view.post(() -> //Log.e(TAG, "fillParent: height=" + view.getMeasuredHeight()));
            }
        }
    }

    private abstract class MediaRunnable implements Runnable {
        @Override
        public void run() {
            mediaRun();
            requestLayout();
            ////Log.e(TAG, "run: count=" + getChildCount());
            clearRunnableTask();
        }

        abstract void mediaRun();
    }

    interface OnPlayParent {
        void startPlay();

        void pause();

        void setMediaPath(String path);

        String getMediaPath();

        View getView();

        void rePlay();

        void recycle();
    }

    public void recycle() {
        MediaTaskCacheHelp.getInstance().setOnMediaTaskCacheListener(null);
    }

    public void onResume() {

    }

    public void onPause() {

    }
}
