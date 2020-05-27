package com.pjj.xsp.view.activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.pjj.xsp.present.BasePresent;
import com.zhy.autolayout.AutoLayoutActivity;

/**
 * Create by xinheng on 2018/10/16。
 *
 * describe：
 */
public class BaseActivity<P extends BasePresent> extends AutoLayoutActivity {
    protected P mPresent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    protected void onDestroy() {
        if(null!=mPresent) {
            mPresent.recycle();
            mPresent = null;
        }
        super.onDestroy();
    }
}
