package com.pjj.xsp.manage;

import java.io.IOException;

/**
 * Create by xinheng on 2018/10/20。
 * describe：系统命令
 */
public class RunSystem {
    public static void run(String... array) {
        //String[] arrayRestart = {"su", "-c", "reboot"};
        try {
            Runtime.getRuntime().exec(array);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重启
     *
     * @return
     */
    public static String[] getReRootArray() {
        String[] arrayRestart = {"su", "-c", "reboot"};
        return arrayRestart;
    }

    /**
     * wifi开关指令
     *
     * @param tag true 开启,
     * @return
     */
    public static String[] getCloseWifiArray(boolean tag) {//svc wifi disable svc wifi disable
        if (tag) {//svc wifi enable
            return new String[]{"svc", "wifi", "enable"};
        } else {//svc wifi disable
            return new String[]{"svc", "wifi", "disable"};
        }
    }

    public static String[] installArray(String apkPath) {//su pm install -r
        return new String[]{"pm", "install", "-r", apkPath};
    }
}
