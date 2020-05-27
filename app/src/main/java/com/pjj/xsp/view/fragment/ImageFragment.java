package com.pjj.xsp.view.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pjj.xsp.utils.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pjj.xsp.R;
import com.pjj.xsp.utils.FileUtils;

/**
 * Create by xinheng on 2018/10/13 。
 * describe：仅播放视频
 * 仅显示图片
 */
public class ImageFragment extends ABFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "mImgPath";
    private String mImgPath;
    private ImageView iv;

    public ImageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param imgPath 图片地址
     * @return A new instance of fragment ImageFragment.
     */
    public static ImageFragment newInstance(String imgPath) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, imgPath);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImgPath = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        iv = view.findViewById(R.id.iv);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        updateIv();
        super.onViewCreated(view, savedInstanceState);
    }


    private void updateIv() {
        if (null != onActivityListener)
            updateStyle(onActivityListener.getScreenStyle(), onActivityListener.getScreenBgColor());
        Log.e("TAG", "updateIv: " + mImgPath);
        RequestOptions requestOptions = new RequestOptions().error(R.mipmap.ic_launcher);
        Glide.with(this).load(FileUtils.getSDFilePath(mImgPath)).apply(requestOptions).into(iv);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            mImgPath = null;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void updateData(String path) {
        if (!path.equals(mImgPath)) {
            mImgPath = path;
            if (getUserVisibleHint() && null != iv)
                updateIv();
        }
    }

    @Override
    public void updateStyle(boolean tag, int color) {
        if (null == iv) {
            return;
        }
       /* if (tag) {//自适应
            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            iv.setBackground(new ColorDrawable(color));
        } else {
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
        }*/
    }

}
