package com.pjj.xsp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.pjj.xsp.manage.XSPSystem;
import com.pjj.xsp.module.XspPlayUI;
import com.pjj.xsp.present.BasePresent;
import com.zhy.autolayout.AutoLayoutActivity;

/**
 * Create by xinheng on 2018/10/16。
 * <p>
 * describe：
 */
public class BaseActivity<P extends BasePresent> extends AutoLayoutActivity {
    protected P mPresent;
    private Toast mToast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉Activity上面的状态栏
        XSPSystem.getInstance().setActivity(this);
        XSPSystem.getInstance().hideNavigationBar();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    public void showWaiteStatue() {

    }

    public void cancelWaiteStatue() {

    }

    public void failStatue(String error) {
        if (error == null) {
            error = "error-";
        }
        if (null == mToast) {
            mToast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(error);
        }
        mToast.show();
    }

    @Override
    protected void onDestroy() {
        if (null != mPresent) {
            mPresent.recycle();
            mPresent = null;
        }
        XSPSystem.getInstance().clean(this);
        super.onDestroy();
    }
}
