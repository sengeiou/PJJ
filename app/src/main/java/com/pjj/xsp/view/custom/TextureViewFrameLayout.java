package com.pjj.xsp.view.custom;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Create by xinheng on 2018/10/25。
 * describe：包含textureview的framelayout的控件
 */
public class TextureViewFrameLayout extends FrameLayout {
    private VideoTextureView textureView;
    /**
     * 播放地址
     */
    private String mVideoPath;
    private View view;
    private TextView textView;


    public TextureViewFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public TextureViewFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextureViewFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createTextureView();
    }

    private void createTextureView() {
        //ExPlayerManage.getInstance().setTextureViewFrameLayout(this);
        textureView = new VideoTextureView(getContext());
        textureView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER));
        //textureView.setSurfaceTextureListener(ExPlayerManage.getInstance());
        addView(textureView);

        textView = new TextView(getContext());
        textView.setGravity(Gravity.LEFT);
        textView.setTextSize(10);
        textView.setTextColor(Color.BLACK);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.BOTTOM | Gravity.RIGHT);
        textView.setLayoutParams(params);
        addView(textView);
        textView.setVisibility(GONE);
        /*view=new View(getContext());
        view.setBackgroundColor(Color.RED);
        view.setLayoutParams(new FrameLayout.LayoutParams(0, 1, Gravity.BOTTOM));
        addView(view);*/

    }


    /*public void playPath(String path) {
        mVideoPath = path;
        if (null != ExPlayerManage.getInstance().getSurfaceTexture())
            ExPlayerManage.getInstance().createPlayer(path, getContext());
    }*/

    public String getVideoPath() {
        return mVideoPath;
    }

    public void setVideoSize(int width, int height, int rotation) {
        textureView.setVideoSize(width, height, rotation);
    }

//    public void release() {
//        ExPlayerManage.getInstance().release();
//    }

    public void setProgress(float rate,String s) {
        post(() -> {
            float width = getMeasuredWidth() * rate;
            FrameLayout.LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            layoutParams.width = (int) width;
            view.setLayoutParams(layoutParams);
            textView.setText(s);
        });
    }
    public void onLinePlayer(boolean flag){
        if(flag) {
            textView.setVisibility(VISIBLE);
            textView.setText("在线");
        }else{
            //textView.setVisibility(VISIBLE);
            textView.setVisibility(GONE);
            //textView.setText("在线1");
        }
    }
}
