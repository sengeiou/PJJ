package com.pjj.xsp;

import android.app.Application;
import android.os.Environment;

import com.pjj.xsp.utils.Log;
import com.pjj.xsp.crash.CrashHandlerUtil;
import com.pjj.xsp.manage.XSPSystem;
import com.pjj.xsp.module.parameter.ScreenInfTag;
import com.pjj.xsp.module.ScreenInfManage;

import java.io.File;

import cn.jpush.android.api.JPushInterface;

/**
 * Create by xinheng on 2018/11/15。
 * describe：
 */
public class PjjApplication extends Application {
    public static PjjApplication application;
    public static final String App_Path = Environment.getExternalStorageDirectory().toString() + "/pjj/";
    public static final String Screenshots_Path = Environment.getExternalStorageDirectory().toString() + "/Screenshots/";
    public static final boolean OLD_3188 = BuildConfig.OLD_3188;//OLD_3188 小屏 旧

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        String onlyCode = XSPSystem.getInstance().getOnlyCode();
        Log.e("TAG", "onCreate: onlyCode=" + onlyCode);
        ScreenInfTag screenInfTag = new ScreenInfTag();
        screenInfTag.setScreenId(onlyCode);
        screenInfTag.setScreenSize("");
        ScreenInfManage.getInstance().setScreenInfTag(screenInfTag);
        initJPush();
        File file = new File(App_Path);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(App_Path + "screenshots/");
        if (!file.exists()) {
            file.mkdirs();
        }
        //if (!BuildConfig.DEBUG)
        //CrashHandlerUtil.getInstance().init(this);
    }

    /**
     * 极光推送初始化
     */
    private void initJPush() {
        JPushInterface.setDebugMode(false);  // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);  // 初始化 JPush
    }
}
