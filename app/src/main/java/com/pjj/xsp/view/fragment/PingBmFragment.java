package com.pjj.xsp.view.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pjj.xsp.R;
import com.pjj.xsp.module.bean.ScreenPingTaskBean;
import com.pjj.xsp.utils.ColorUtils;
import com.pjj.xsp.utils.TextViewUtils;
import com.pjj.xsp.utils.ViewUtils;
import com.pjj.xsp.view.custom.ScreenLeftView;
import com.pjj.xsp.view.custom.ScreenRightView;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PingBmFragment extends ABFragment {
    private ViewGroup parent;
    private List<ScreenPingTaskBean.DataBeanPin> list;

    public PingBmFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ping_bm, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parent = view.findViewById(R.id.parent);
        if (null != list) {
            updateUI(list);
        }
    }

    @Override
    public void updateData(String path) {

    }

    @Override
    public void updateStyle(boolean tag, int color) {

    }

    public void update(List<ScreenPingTaskBean.DataBeanPin> list) {
        if (null == parent) {
            this.list = list;
        } else {
            updateUI(list);
        }
    }

    private void updateUI(List<ScreenPingTaskBean.DataBeanPin> list) {
        int childCount = parent.getChildCount();
        if (childCount == 0) {
            for (int i = 0; i < list.size(); i++) {
                parent.addView(createChildView(list.get(i)));
            }
            return;
        }
        for (int i = 0; i < childCount; i++) {
            View childAt = parent.getChildAt(i);
            setInf(list.get(i), childAt);
        }
    }

    private View createChildView(ScreenPingTaskBean.DataBeanPin screenPingTaskBean) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_play_head_ad, parent, false);
        setInf(screenPingTaskBean, view);
        return view;
    }

    private void setInf(ScreenPingTaskBean.DataBeanPin screenPingTaskBean, View view) {
        View view_bg = view.findViewById(R.id.view_bg);
        View ll_person = view.findViewById(R.id.ll_person);
        ImageView iv_left = view.findViewById(R.id.iv_left);
        ImageView iv_right = view.findViewById(R.id.iv_right);

        TextView tv_bianmin_name = view.findViewById(R.id.tv_bianmin_name);
        TextView tv_bianmin = view.findViewById(R.id.tv_bianmin);
        TextView tv_ad_person_name = view.findViewById(R.id.tv_ad_person_name);
        TextView tv_ad_person = view.findViewById(R.id.tv_ad_person);
        TextView tv_ad_phone_name = view.findViewById(R.id.tv_ad_phone_name);
        TextView tv_ad_phone = view.findViewById(R.id.tv_ad_phone);
        tv_bianmin_name.setText(screenPingTaskBean.getPieceTitle());
        String peopleInfo = TextViewUtils.clean(screenPingTaskBean.getPeopleInfo());
        int length = -1;
        try {
            length = peopleInfo.getBytes("GBK").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (length > 0 && length <= 37) {
            tv_bianmin.setTextSize(TypedValue.COMPLEX_UNIT_PX, 45);
        } else {
            tv_bianmin.setTextSize(TypedValue.COMPLEX_UNIT_PX, 31);
        }
        tv_bianmin.setText(peopleInfo);
        ScreenLeftView screen_left=view.findViewById(R.id.screen_left);
        ScreenRightView screen_right=view.findViewById(R.id.screen_right);
        //颜色更改
        String pieceColour = screenPingTaskBean.getPieceColour();
        int bgColor = ColorUtils.getBgColor(pieceColour);
        screen_left.setColorBg(bgColor);
        screen_right.setColorBg(bgColor);
        view_bg.setBackgroundColor(bgColor);
        //iv_left.setImageResource(ColorUtils.getLeftResource(pieceColour));
        //iv_right.setImageResource(ColorUtils.getRightResource(pieceColour));
        tv_ad_person_name.setBackgroundColor(bgColor);
        tv_ad_phone_name.setBackgroundColor(bgColor);
        tv_ad_person.setTextColor(bgColor);
        tv_ad_phone.setTextColor(bgColor);
        String authType = screenPingTaskBean.getAuthType();//订单用途 authType: 1个人 2商家
        boolean shangjia = "2".equals(authType);
        ll_person.setBackground(ViewUtils.getBgDrawable(Color.WHITE, 2));
        boolean showButtom = false;
        if ("1".equals(screenPingTaskBean.getIsShowName())) {
            tv_ad_person_name.setVisibility(View.VISIBLE);
            tv_ad_person.setVisibility(View.VISIBLE);
            tv_ad_person_name.setBackground(ViewUtils.getBgDrawable(bgColor, 2));
            tv_ad_person.setText(shangjia ? screenPingTaskBean.getCompanyName() : screenPingTaskBean.getName());
            showButtom = true;
        } else {
            tv_ad_person_name.setVisibility(View.GONE);
            tv_ad_person.setVisibility(View.GONE);
        }
        if ("1".equals(screenPingTaskBean.getIsShowPhone())) {
            tv_ad_phone_name.setVisibility(View.VISIBLE);
            tv_ad_phone.setVisibility(View.VISIBLE);
            tv_ad_phone_name.setBackground(ViewUtils.getBgDrawable(bgColor, 2));
            tv_ad_phone.setText(screenPingTaskBean.getPhoneNumber());
            showButtom = true;
        } else {
            tv_ad_phone_name.setVisibility(View.GONE);
            tv_ad_phone.setVisibility(View.GONE);
        }
        if (!showButtom) {
            float dimension = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 68, getResources().getDisplayMetrics());
            //int top = (int) ((dimension - tv_bianmin.getTextSize()) * 0.5f);
            tv_bianmin.setLineSpacing(dimension, 0f);
            tv_bianmin.setIncludeFontPadding(false);
            //tv_bianmin.setPadding(tv_bianmin.getPaddingLeft(), top, tv_bianmin.getPaddingRight(), -top);
            ll_person.setVisibility(View.GONE);
        }
    }
}
