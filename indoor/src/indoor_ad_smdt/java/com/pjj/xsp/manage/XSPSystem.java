package com.pjj.xsp.manage;

import android.app.smdt.SmdtManager;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.pjj.xsp.PjjApplication;
import com.pjj.xsp.module.parameter.AppUpload;

import com.pjj.xsp.utils.FileUtils;
import com.pjj.xsp.utils.SharedUtils;
import com.pjj.xsp.view.PasswordDialog;
import com.smdt.demo.ApiManager;

import java.io.File;
import java.io.IOException;

import javax.security.auth.login.LoginException;


/**
 * Create by xinheng on 2018/10/16。
 * describe：系统接口实现
 */
public class XSPSystem extends XspSystemFunction {
    private static XSPSystem instance;
    private SmdtManager smdt;
    private static final String password = "pjj-smdt";

    public static XSPSystem getInstance() {
        if (null == instance) {
            synchronized (XSPSystem.class) {
                if (null == instance) {
                    instance = new XSPSystem();
                }
            }
        }
        return instance;
    }

    @Override
    public String getPassword() {
        return password;
    }

    private XSPSystem() {
        smdt = SmdtManager.create(PjjApplication.application);
        //String s = smdt.smdtGetAPIVersion();
        //Log.e("TAG", "主板信息" + s);
    }

    @Override
    public void reboot() {
        smdt.smdtReboot("reboot");
    }

    /**
     * 每天9:50关机，20:10开机
     * 1 代表使用，0 代表不使用
     *
     * @param date
     */
    @Override
    public void setTurnOffTime(String... date) {
        smdt.smdtSetTimingSwitchMachine(date[0], date[1], "1");
    }


    @Override
    public void installApp(String path) {
        Log.e("TAG", "installApp: 升级=" + path);
        SharedUtils.saveForXml(SharedUtils.NEW_APP_PATH, path);
        smdt.smdtSilentInstall(path, PjjApplication.application);
        //ApiManager manager = ApiManager.getInstance(PjjApplication.application);
        //Log.e("TAG", "namager=" + (manager == null));
        //manager.silentInstall(path, true);//静默安装apkPath路径的apk，isRuning是安装后是否立马运行
        //manager.silentDelete(path);//静默卸载包名为packageName的apk
    }


    @Override
    public String screenshots(String file, String name) {
        smdt.smdtTakeScreenshot(file, name, PjjApplication.application);
        //String screenshot = BitmapUtils.getScreenshot();
        //return file + name;
        return file + name;
    }

    final String pathname = File.separator + "pjj" + File.separator + "pjj_smdt_update.zip";

    /**
     * u盘升级系统
     *
     * @param path
     */
    @Override
    public void systemUploadU(String path) {
        Log.e("TAG", "systemUploadU: " + path);
        File systemFile = new File(path + pathname);
        if (systemFile.exists() && systemFile.isFile()) {
            Log.e("TAG", "systemUploadU: 升级");
            String newPath$Name = PjjApplication.App_Path + "/update.zip";
            Toast.makeText(PjjApplication.application, "拷贝中，勿动U盘", Toast.LENGTH_LONG).show();
            FileUtils.copyFile(systemFile, newPath$Name, new FileUtils.OnDownloadListener() {
                @Override
                public void success() {
                    systemUpdate(newPath$Name);
                }

                @Override
                public void fail() {

                }
            });
        } else {
            Toast.makeText(PjjApplication.application, "U盘路径错误", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showPasswordDialog(String path) {
        File file = new File(path);
        Log.e("TAG", "showPasswordDialog: " + file.isDirectory());
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                Log.e("TAG", "showPasswordDialog: " + f.getAbsolutePath());
                if (f.isDirectory()) {
                    File[] files1 = f.listFiles();
                    for (File f1 : files1) {
                        Log.e("TAG", "showPasswordDialog1: " + f1.getAbsolutePath());
                    }
                }
            }
            File systemFile = new File(path + pathname);
            if (systemFile.exists() && systemFile.isFile()) {
                Log.e("TAG", "showPasswordDialog: 密码弹窗");
                PjjApplication.application.showPasswordDialogMainThread(path);
            }
        }
    }


    @Override
    public void systemUpdate(String path) {
        File packageFile = new File(path);
        if (!packageFile.exists()) {
            Log.e("TAG", "升级包不存在");
            return;
        }
        Log.e("TAG", "升级包=" + path);
        try {
            smdt.smdtInstallPackage(PjjApplication.application, packageFile);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("TAG", "升级异常");
        }
    }

    @Override
    public String getScreenType() {
        return "indoor";
    }

    /**
     * 获取设备的固件系统版本和编译日期
     *
     * @return
     */
    public String getAndroidDisplay() {
        try {
            return smdt.getAndroidDisplay();
        } catch (Exception e) {
            e.printStackTrace();
            return "unknow";
        }
    }

    public AppUpload createAppUpload() {
        AppUpload appUpload = new AppUpload();
        appUpload.setType(1);
        appUpload.setSysAndroidDisplay(smdt.getAndroidDisplay());
        appUpload.setSysAndroidModel(smdt.getAndroidModel());
        appUpload.setSysAndroidVersion(smdt.getAndroidVersion());
        appUpload.setSysApiVersion(smdt.smdtGetAPIVersion());
        return appUpload;
    }
    @Override
    public void hideNavigationBar() {
        smdt.smdtSetStatusBar(PjjApplication.application, false);
    }
    @Override
    public String getScreenSize() {
//        smdt.execSuCmd("getprop persist.sys.screensize");
        String read = read("getprop persist.sys.screensize");
        Log.e("TAG", "getScreenSize: read=" + read);
        if (read == null) {
            read = "unknown";
        }
        return read;
    }

    @Override
    public void closeWatchDog() {
        smdt.smdtWatchDogEnable((char) 0);
    }

    @Override
    public void startWatchDog() {
        smdt.smdtWatchDogEnable((char) 1);
    }

    @Override
    public void feedWatchDog() {
        //Log.e("TAG", "feedWatchDog: " );
        smdt.smdtWatchDogFeed();
    }
}
