package com.pjj.xsp.view.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Create by xinheng on 2018/10/13。
 * describe：
 */
public abstract class ABFragment extends Fragment {
    protected OnActivityListener onActivityListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnActivityListener) {
            onActivityListener = (OnActivityListener) context;
        }
    }

    public abstract void updateData(String path);

    /**
     * 更新适应样式
     *
     * @param tag
     */
    public abstract void updateStyle(boolean tag, int color);

    public interface OnActivityListener {
        boolean getScreenStyle();

        int getScreenBgColor();
    }
}
