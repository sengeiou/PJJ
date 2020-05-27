package com.pjj.xsp.manage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;

import com.pjj.xsp.PjjApplication;
import com.pjj.xsp.module.parameter.AppUpload;
import com.pjj.xsp.utils.DateUtils;
import com.pjj.xsp.utils.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Locale;

/**
 * Create by xinheng on 2018/10/16。
 * describe：系统接口实现
 */
public class XSPSystem extends XspSystemFunction {
    private static XSPSystem instance;


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

    private XSPSystem() {
    }

    @Override
    public void recoveryFactoryReset() {

    }

    @Override
    public void reboot() {
        closeWatchDog();
        PowerManager pManager = (PowerManager) PjjApplication.application.getSystemService(Context.POWER_SERVICE);
        pManager.reboot("");
    }

    @Override
    public void turnOff() {
        Log.e("TAG", "turnOff: 关机");
        Calendar shutdp = Calendar.getInstance(Locale.SIMPLIFIED_CHINESE);
        int[] timeoff = {shutdp.get(Calendar.YEAR), shutdp.get(Calendar.MONTH) + 1, shutdp.get(Calendar.DATE),
                shutdp.get(Calendar.HOUR_OF_DAY), shutdp.get(Calendar.MINUTE)};    //开机时间
//        int[] timeon = {opendp.getYear(), opendp.getMonth() + 1, opendp.getDayOfMonth(),
//                opentp.getCurrentHour(), opentp.getCurrentMinu te()};
        for (int i = 0; i < timeoff.length; i++) {
            Log.e("TAG", "turnOff: " + timeoff[i]);
            //timeon[i] = Integer.parseInt(timeons[i]);
        }
        Intent intent = new Intent();
        intent.putExtra("timeoff", timeoff);
        //intent.putExtra("timeon", timeon);
        // enable=true时 定时开关机生效  为false时 取消定 时开关机
        intent.putExtra("enable", true);
        intent.setAction("android.56iq.intent.action. setpoweronoff");
        PjjApplication.application.sendBroadcast(intent);
    }

    @Override
    public void setTurnOffTime(String... timeons) {
        /*Calendar shutdp=Calendar.getInstance(Locale.SIMPLIFIED_CHINESE);
        int[] timeon = {shutdp.get(Calendar.YEAR), shutdp.get(Calendar.MONTH)+1, shutdp.get(Calendar.DATE),
                shutdp.get(Calendar.HOUR_OF_DAY), shutdp.get(Calendar.MINUTE)};    //开机时间*/
        int[] timeon = new int[timeons.length];
        for (int i = 0; i < timeons.length; i++) {
            Log.e("TAG", "setTurnOffTime: " + timeons[i]);
            timeon[i] = Integer.parseInt(timeons[i]);
        }
        Intent intent = new Intent();
        intent.putExtra("timeon", timeon);
        //intent.putExtra("timeon", timeon);
        // enable=true时 定时开关机生效  为false时 取消定 时开关机
        intent.putExtra("enable", true);
        intent.setAction("android.56iq.intent.action. setpoweronoff");
        PjjApplication.application.sendBroadcast(intent);
    }

    @Override
    public void cancelTurnOffTime(String date) {

    }

    @Override
    public void setVoicesWitch(boolean tag) {

    }

    @Override
    public void setSystemTime(String date) {

    }

    @Override
    public String getSystemUpdateFileName() {
        return "update.img";
    }

    @Override
    public void hideNavigationBar() {
        if (activity != null) {
            Intent in = new Intent();
            in.setAction("com.outform.hidebar");
            activity.sendBroadcast(in);
        }
    }

    @Override
    public void installApp(String path) {
        //RunSystem.run(RunSystem.installArray(path));
        Log.e("TAG", "升级 installApp: " + path);
        //installSilent(path);
        Intent it = new Intent(Intent.ACTION_VIEW);
        //it.setDataAndType(Uri.parse(path), "application/vnd.android.package-archive");
        it.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PjjApplication.application.startActivity(it);
    }

    @Override
    public void systemUpdate(String path) {
        //super.systemUpdate(path);
        //下载完固件需要重启
        reboot();
    }

    @Override
    public String screenshots(String file, String name) {
        Log.e("TAG", "screenshots: 截屏");
        File file1 = new File(PjjApplication.Screenshots_Path);
        File[] files = file1.listFiles();
        for (File f : files) {
            f.getAbsoluteFile().delete();
        }
        if (activity != null) {
            Intent intent = new Intent("rk.android.screenshot.action");
            activity.sendBroadcast(intent);
        }
        return "elevator";
    }

    /**
     * 获取设备的固件系统版本和编译日期
     *
     * @return
     */
    public String getAndroidDisplay() {
        return android.os.Build.DISPLAY;
    }

    @Override
    public String getScreenType() {
        return "indoor";
    }

    @Override
    public String getScreenSize() {
        String androidDisplay = getAndroidDisplay();
        String[] split = androidDisplay.split("-");
        if (split.length > 2) {
            return split[2];
        }
        return null;
    }

    @Override
    public void startWatchDog() {
        String read = read("ls -l /sys/devices/20072000.i2c/i2c-0/0-003c/adwdog");
        Log.e("TAG", "startWatchDog: " + read);
        //喂狗，并自定开启
        feedWatchDog();
    }

    @Override
    public void feedWatchDog() {
        writeDogCode("echo d >");
    }

    @Override
    public void closeWatchDog() {
        writeDogCode("echo c >");
    }

    public AppUpload createAppUpload() {
        AppUpload appUpload = new AppUpload();
        appUpload.setType(1);
        appUpload.setScreenId(getOnlyCode());
        appUpload.setScreenType(getScreenType());
        String androidDisplay = getAndroidDisplay();
        appUpload.setSysAndroidDisplay(androidDisplay);
        int index = androidDisplay.indexOf("-");
        if (index > -1) {
            appUpload.setSysAndroidModel(androidDisplay.substring(0, index));
        }
        appUpload.setSysAndroidVersion(String.valueOf(Build.VERSION.SDK_INT));
        appUpload.setSysApiVersion(androidDisplay);
        return appUpload;
    }

    /**
     * 静默安装
     */
    private void installSilent(String path) {
//        String cmd = "pm install -r /mnt/sdcard/Demo.apk";
        String cmd = "pm install -r " + path;
        Process process = null;
        DataOutputStream os = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = null;
        StringBuilder errorMsg = null;
        try {
            // 静默安装需要root权限
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.write(cmd.getBytes());
            os.writeBytes("\n");
            os.writeBytes("exit\n");
            os.flush();
            // 执行命令
            process.waitFor();
            // 获取返回结果
            successMsg = new StringBuilder();
            errorMsg = new StringBuilder();
            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s;
            while ((s = successResult.readLine()) != null) {
                successMsg.append(s);
            }
            while ((s = errorResult.readLine()) != null) {
                errorMsg.append(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (process != null) {
                    process.destroy();
                }
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void writeDogCode(String commnd) {
        adw_write_file(commnd, "/sys/devices/20072000.i2c/i2c-0/0-003c/adwdog");
    }

    /**
     * echo  d > xx/adwdog
     * method:  向指定文件写入对应指令
     */
    private static void adw_write_file(String commnd, String path) {
        File file = new File(path);
        if (file.exists()) {
            try {
                if (file.canWrite()) {
                    FileOutputStream fout = new FileOutputStream(file);
                    byte[] bytes = commnd.getBytes();
                    fout.write(bytes);
                    fout.close();
                } else {
                    Log.e("TAG", "adw_write_file: not write");
                }
            } catch (Exception e) {
                Log.e("TAG", file.toString() + "can not write");
            }
        }
    }
}
