package com.pjj.xsp.view.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pjj.xsp.R;
import com.pjj.xsp.module.bean.SpeedScreenBean;
import com.pjj.xsp.view.custom.SpeedViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpeedDiyFragment extends ABFragment {


    private SpeedViewGroup sg_speed;
    private SpeedScreenBean.DataBean dataBean;

    public SpeedDiyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_speed_diy, container, false);
    }

    public void updateUI(SpeedScreenBean.DataBean dataBean) {
        if (null == sg_speed) {
            this.dataBean = dataBean;
        } else {
            sg_speed.setData(dataBean.getViewSizeBeanList(), dataBean.getProportionX(), dataBean.getProportionY());
        }
    }

    @Override
    public void updateData(String path) {

    }

    @Override
    public void updateStyle(boolean tag, int color) {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sg_speed = view.findViewById(R.id.sg_speed);
        if(null!=dataBean){
            sg_speed.setData(dataBean.getViewSizeBeanList(), dataBean.getProportionX(), dataBean.getProportionY());
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(hidden){
            sg_speed.stopVideoPlayback();
        }
        super.onHiddenChanged(hidden);
    }
}
