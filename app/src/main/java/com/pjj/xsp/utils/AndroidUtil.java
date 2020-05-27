package com.pjj.xsp.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Base64;

import com.pjj.xsp.PjjApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Created by 22 on 2018/4/3.
 */

public class AndroidUtil {
    private static final String TAG = "AndroidUtil";
    private Context mContext;

    public AndroidUtil(Context paramContext) {
        this.mContext = paramContext;
    }


    //将 s 进行 BASE64 编码
    public static String getBASE64(String s) {
        if (s == null) return null;
        return  Base64.encodeToString(s.getBytes(), Base64.NO_WRAP);

    }

    public String getClientID() {
        return MD5Util.encrypt16(getWlanMac1());
    }



    //将 BASE64 编码的字符串 s 进行解码
    public static String getFromBASE64(String s) {
        if (s == null) return null;
        try {
            byte[] b = Base64.decode(s,Base64.NO_WRAP);
            return new String(b);
        } catch (Exception e) {
            return null;
        }
    }
    public static String getLogJson() {
        String s =  "{\"code\":\"10002\",\"id\":\""+getWlanMac()+"\"}";
        return  s;
    }

    public static String sendMsg(String msg ) {
        String s =  "{\"userId\":\""+getWlanMac()+"\",\"originCode\":\"10002\",\"content\":\"+"+msg+"\",\"recipients\":\"\"}";
        return  s;
    }

    //获取以太网地址
    private static String getEthMac() {

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("sys/class/net/eth0/address"));
            return reader.readLine();
        } catch (Exception e) {
            return "";
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
            }
        }
    }

    public static String getWlanMac() {
        String macString = "";
        WifiManager wifimsg = (WifiManager) PjjApplication.application.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifimsg != null) {
            if (wifimsg.getConnectionInfo() != null) {
                if (wifimsg.getConnectionInfo().getMacAddress() != null) {
                    macString = wifimsg.getConnectionInfo().getMacAddress().replaceAll(":", "");
                }
            }
        }
//        Log.d("picher_log",macString);
        return macString;
    }

    public static String getWlanMac1() {
        String macString = "";
        WifiManager wifimsg = (WifiManager) PjjApplication.application.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifimsg != null) {
            if (wifimsg.getConnectionInfo() != null) {
                if (wifimsg.getConnectionInfo().getMacAddress() != null) {
                    macString = wifimsg.getConnectionInfo().getMacAddress();
                }
            }
        }
//        Log.d("picher_log",macString);
        return macString;
    }

    /*
    *TF卡路径
     */
    @SuppressLint("SdCardPath")
    public static String getPath2() {
        String sdcard_path = null;
        String sd_default = Environment.getExternalStorageDirectory()
                .getAbsolutePath();
//        Log.d("text", sd_default);
        if (sd_default.endsWith("/")) {
            sd_default = sd_default.substring(0, sd_default.length() - 1);
        }
        // 得到路径
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            String line;
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                if (line.contains("secure"))
                    continue;
                if (line.contains("asec"))
                    continue;
                if (line.contains("fat") && line.contains("/mnt/")) {
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        if (sd_default.trim().equals(columns[1].trim())) {
                            continue;
                        }
                        sdcard_path = columns[1];
                    }
                } else if (line.contains("fuse") && line.contains("/mnt/")) {
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        if (sd_default.trim().equals(columns[1].trim())) {
                            continue;
                        }
                        sdcard_path = columns[1];
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("text", sdcard_path);
        return sdcard_path;
    }

    /**
     * 判断SD卡是否可用
     *
     * @return true : 可用<br>false : 不可用
     */
    public static boolean isSDCardEnable() {
        Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取手机外部总空间大小
     *
     * @return 总大小，字节为单位
     */
    static public long getTotalExternalMemorySize() {
        if (isSDCardEnable()) {
            //获取SDCard根目录
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize;
        } else {
            return -1;
        }
    }

    /**
     * 获取SD卡剩余空间
     *
     * @return SD卡剩余空间
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getFreeSpace() {
        if (!isSDCardEnable()) return "sdcard unable!";
        StatFs stat = new StatFs(getPath2());
        long blockSize, availableBlocks;
        availableBlocks = stat.getAvailableBlocksLong();

        blockSize = stat.getBlockSizeLong();
        long size = availableBlocks * blockSize / 1024L / 1024L;
        return String.valueOf(size);
    }


}
