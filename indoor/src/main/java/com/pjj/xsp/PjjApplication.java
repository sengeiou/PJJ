package com.pjj.xsp;

import android.app.Application;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;

import com.pjj.xsp.crash.CrashHandlerUtil;
import com.pjj.xsp.manage.XSPSystem;
import com.pjj.xsp.module.ScreenInfManage;
import com.pjj.xsp.module.parameter.ScreenInfTag;
import com.pjj.xsp.utils.Log;
import com.pjj.xsp.utils.LogcatWrite;
import com.pjj.xsp.view.PasswordDialog;
import com.tencent.bugly.crashreport.CrashReport;

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
    public static final boolean OLD_3188 = false;
    private boolean flag = true;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    private Handler mainHandler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        String onlyCode = XSPSystem.getInstance().getOnlyCode();
        Log.e("TAG", "Application onCreate: onlyCode=" + onlyCode);
        ScreenInfTag screenInfTag = new ScreenInfTag();
        screenInfTag.setScreenId(onlyCode);
        screenInfTag.setScreenSize("");
        ScreenInfManage.getInstance().setScreenInfTag(screenInfTag);
        initJPush();
        LogcatWrite logcatWrite = new LogcatWrite();
        File file = new File(App_Path);
        if (!file.exists()) {
            file.mkdirs();
        }
        if (BuildConfig.USE_TYPE.contains("elevator") || BuildConfig.USE_TYPE.contains("m10")) {
            file = new File(Screenshots_Path);
            if (!file.exists()) {
                file.mkdirs();
            }
        } else {
            file = new File(App_Path + "screenshots/");
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        if (!BuildConfig.DEBUG)
            CrashHandlerUtil.getInstance().init(this);
        //测试阶段建议设置成true，发布时设置为false
        CrashReport.initCrashReport(getApplicationContext(), "c9df16a4fa", BuildConfig.DEBUG);
        //WifiLinkHelp wifiLinkHelp = new WifiLinkHelp();
        //wifiLinkHelp.linkWifi(this);
        file = new File(App_Path + "log/");
        if (!file.exists()) {
            file.mkdirs();
        }
        //if (BuildConfig.USE_TYPE.contains("m10_3128"))
            //logcatWrite.setupTimer(App_Path + "log/");
    }

    /**
     * 极光推送初始化
     */
    private void initJPush() {
        JPushInterface.setDebugMode(false);  // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);  // 初始化 JPush
    }

    private PasswordDialog passwordDialog;

    public void showPasswordDialogMainThread(String path) {
        mainHandler.post(() -> showPasswordDialog(path));
    }

    private void showPasswordDialog(String path) {
        if (passwordDialog != null && passwordDialog.isShowing()) {
            return;
        }
        if (null == passwordDialog) {
            passwordDialog = new PasswordDialog(PjjApplication.application);
            passwordDialog.setOnPasswordDialogListener(new PasswordDialog.OnPasswordDialogListener() {
                @Override
                public void sureClick(String text) {
                    if (XSPSystem.getInstance().getPassword().equals(text)) {
                        Toast.makeText(getApplicationContext(), "稍等。。。", Toast.LENGTH_SHORT).show();
                        XSPSystem.getInstance().systemUploadU(path);
                        passwordDialog.dismiss();
                    }
                }
            });
        }
        mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                passwordDialog.dismiss();
            }
        }, 30000);
        passwordDialog.show();
    }
}
