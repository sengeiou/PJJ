package com.pjj.xsp.view.custom;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.pjj.xsp.PjjApplication;
import com.pjj.xsp.intent.http.RetrofitService;
import com.pjj.xsp.module.ScreenInfManage;
import com.pjj.xsp.module.XspTaskHandler;
import com.pjj.xsp.module.bean.SpeedScreenBean;
import com.pjj.xsp.utils.TextJsonUtils;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.pjj.xsp.PjjApplication.App_Path;

/**
 * Created by XinHeng on 2019/03/18.
 * describe：
 */
public class SpeedViewGroup extends ViewGroup {
    private LinkedList<VideoView> videoViews = new LinkedList<>();

    public SpeedViewGroup(Context context) {
        super(context);
    }

    public SpeedViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpeedViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private List<SpeedScreenBean.DataBean.ViewSizeBeanListBean> list;
    private int proportionX;
    private int proportionY;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        Log.e("TAG", "onMeasure: " + getMeasuredWidth() + ", " + getMeasuredHeight() + ", " + getChildCount());
    }

    public void setData(List<SpeedScreenBean.DataBean.ViewSizeBeanListBean> list, int prox, int proy) {
        stopVideoPlayback();
        videoViews.clear();
        removeAllViews();
        post(() -> {
            this.list = list;
            proportionX = prox;
            proportionY = proy;
            if (TextJsonUtils.isNotEmpty(list)) {
                for (int i = 0; i < list.size(); i++) {
                    View childView = getChildView(list.get(i));
                    if (null != childView)
                        addView(childView);
                }
            }
        });
    }

    private View getChildView(SpeedScreenBean.DataBean.ViewSizeBeanListBean dataBean) {
        if (null != dataBean) {
            int type = dataBean.getType();
            String path = dataBean.getFileName();
            View view;
            Log.e("TAG", "getChildView: type=" + type);
            switch (type) {
                case 2:
                    FrameLayout frameLayout = new FrameLayout(getContext());
                    MyVideoView viewVideo = new MyVideoView(getContext());
                    viewVideo.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER));
                    frameLayout.addView(viewVideo);
                    videoViews.add(viewVideo);
                    viewVideo.setZOrderOnTop(true);
                    viewVideo.post(() -> {
                        //viewVideo.setVideoPath(getSdcardPath("ba84b55df52ffaa3853b04d1f1dfbeaa.mp4"));
                        viewVideo.setVideoPath(getSdcardPath(path));
                        viewVideo.setOnPreparedListener(mp -> {
                            mp.setLooping(true);
                            int videoWidth = mp.getVideoWidth();
                            int videoHeight = mp.getVideoHeight();
                            viewVideo.setVideoSize(videoWidth, videoHeight);
                            mp.start();
                        });
                    });
                    view = frameLayout;
                    break;
                default:
                    view = new ImageView(getContext());
                    view.post(() -> Glide.with(this).load(getSdcardPath(path)).into((ImageView) view));
            }
            view.setLayoutParams(new ViewGroup.LayoutParams(getMeasuredWidth() * dataBean.getWidth() / proportionX, getMeasuredHeight() * dataBean.getHeight() / proportionY));
            return view;
        }
        return null;
    }

    private String getSdcardPath(String path) {
        if (!TextUtils.isEmpty(path)) {
            File file = new File(App_Path + RetrofitService.getInstance().getFileName(path));
            if (file.exists()) {
                path = file.getAbsolutePath();
                Log.e("TAG", "onViewCreated: 本地 = " + path);
            } else {
                path = ScreenInfManage.filePathMedia + path;
            }
        } else {
            path = "";
        }
        //Log.e("TAG", "getSdcardPath: " + path);
        return path;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() > 0) {
            int top;
            int left;
            for (int i = 0; i < list.size(); i++) {
                View childAt = getChildAt(i);
                SpeedScreenBean.DataBean.ViewSizeBeanListBean bean = list.get(i);
                left = getMeasuredWidth() * bean.getX() / proportionX;
                top = getMeasuredHeight() * bean.getY() / proportionY;
                Log.e("TAG", "onLayout: left=" + left + ", top=" + top + ", " + (left + childAt.getMeasuredWidth()) + ", " + (top + childAt.getMeasuredHeight()));
                childAt.layout(left, top, left + childAt.getMeasuredWidth(), top + childAt.getMeasuredHeight());
            }
        }
    }

    public void stopVideoPlayback() {
        Iterator<VideoView> iterator = videoViews.iterator();
        while (iterator.hasNext()) {
            VideoView next = iterator.next();
            next.stopPlayback();
            next.setVisibility(View.GONE);
        }
    }
}
