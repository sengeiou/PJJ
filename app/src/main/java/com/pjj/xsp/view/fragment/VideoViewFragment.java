package com.pjj.xsp.view.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.pjj.xsp.utils.Log;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.pjj.xsp.intent.http.RetrofitService;
import com.pjj.xsp.view.custom.MyVideoView;

import java.io.File;

import static com.pjj.xsp.PjjApplication.App_Path;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoViewFragment extends ABFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "mVideoPath";

    private String mVideoPath;
    private FrameLayout frameLayout;
    private MyVideoView videoView;


    public VideoViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 ParameterUtils 1.
     * @return A new instance of fragment VideoViewFragment.
     */
    public static VideoViewFragment newInstance(String param1) {
        VideoViewFragment fragment = new VideoViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mVideoPath = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        frameLayout = new FrameLayout(getActivity());
        videoView = new MyVideoView(getActivity());
        videoView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER));
        frameLayout.addView(videoView);
        videoView.setZOrderOnTop(true);
        return frameLayout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        videoView.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            int videoWidth = mp.getVideoWidth();
            int videoHeight = mp.getVideoHeight();
            videoView.setVideoSize(videoWidth, videoHeight);
            mp.start();
        });
        videoView.setOnErrorListener((mp, what, extra) -> {
            Log.e("TAG", "onViewCreated: 播放异常" + what + ", " + extra);
            videoView.stopPlayback(); //播放异常，則停止播放，防止彈窗使界面阻塞
            startVideo();
            return true;
        });
        startVideo();
        super.onViewCreated(view, savedInstanceState);
    }

    private void startVideo() {
        Log.e("TAG", "startVideo: " + (null != onActivityListener));
        if (null != onActivityListener)
            updateStyle(onActivityListener.getScreenStyle(), onActivityListener.getScreenBgColor());
    }

    private void startVideo1() {
        videoView.setVisibility(View.VISIBLE);
        //mVideoPath="http://47.92.50.83:8080/video/b430985cbd9b4c11a9b61da2f22b5f1d.mp4";
        //mVideoPath="http://47.92.50.83:8080/video/HEMA6573.MP4";
        if (!TextUtils.isEmpty(mVideoPath)) {
            //mVideoPath="http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";//JieCaoVideoPlayer: onVideoSizeChanged: 640, 360
            //mVideoPath="http://47.92.50.83:8080/video/c0a6a33cfffe4c04b702ba615f541568.mp4";//JieCaoVideoPlayer: onVideoSizeChanged: 320, 568
            File file = new File(App_Path + RetrofitService.getInstance().getFileName(mVideoPath));
            String path;
            if (file.exists()) {
                //Log.e("TAG", "startVideo: size=" + file.length() / 1024);
                path = file.getAbsolutePath();
                Log.e("TAG", "onViewCreated: 本地 = " + path);
            } else {
                path = mVideoPath;
            }
            videoView.setVideoPath(path);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        videoView.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        videoView.pause();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e("TAG", "video onHiddenChanged: " + hidden);
        if (hidden) {
            mVideoPath = null;
            videoView.stopPlayback();
            videoView.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateData(String path) {
        Log.e("TAG", "updateData: 更新了 视频=" + path);
        if (!path.equals(mVideoPath)) {
            Log.e("TAG", "updateData: 更新了 视频111");
            mVideoPath = path;
            if (getUserVisibleHint() && null != videoView) {
                startVideo();
            }
        }
    }

    @Override
    public void updateStyle(boolean tag, int color) {
        Log.e("TAG", "updateStyle: 1");
        if (null == frameLayout) {
            return;
        }
       /* Log.e("TAG", "updateStyle: 2");
        if (tag) {
            frameLayout.setBackgroundColor(color);
            videoView.setTagScreen(true);
        } else {
            videoView.setTagScreen(false);
            frameLayout.setBackgroundColor(Color.BLACK);
        }*/
        startVideo1();
    }

    @Override
    public void onDetach() {
        videoView.stopPlayback();
        super.onDetach();
    }
}
