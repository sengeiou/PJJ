package com.pjj.xsp.present;

import com.pjj.xsp.contract.BaseView;
import com.pjj.xsp.intent.http.RetrofitService;

/**
 * Create by xinheng on 2018/10/16。
 * describe：回收
 */
public class BasePresent<V extends BaseView> {
    protected V mView;

    public BasePresent(V view) {
        mView = view;
    }

    public boolean isViewNotNull() {
        return null != mView;
    }

    public void recycle() {
        mView = null;
    }


    protected RetrofitService getRetrofitService() {
        return RetrofitService.getInstance();
    }
}
