package com.pjj.xsp.manage;

import com.pjj.xsp.utils.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Administrator on 2017/10/19.
 */
public class BodyRunnable {

    private static final String TAG = "BodyThread";

    public void run() {
        try {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (null != read("/sys/devices/virtual/cx_led/cx_led/cx_usb") && !"".equals(read("/sys/devices/virtual/cx_led/cx_led/cx_usb"))) {
                        String body = read("/sys/devices/virtual/cx_led/cx_led/cx_usb");
                        onHasPeopleListener.hasBody(body);
                    }
                }
            }, 0, 1000);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("BodyRunnable", "[BodyThread][bodyThread interrupted][" + e.getMessage() + "]");
        }
    }

    //sys_path 为节点映射到的实际路径
    public static String read(String sys_path) {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("cat " + sys_path); // 此处进行读操作
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while (null != (line = br.readLine())) {
//                Log.w(TAG, "read data ---> " + line);
                return line;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.w(TAG, "*** ERROR *** Here is what I know: " + e.getMessage());
        }
        return null;
    }

    private OnHasPeopleListener onHasPeopleListener;

    public void setOnHasPeopleListener(OnHasPeopleListener onHasPeopleListener) {
        this.onHasPeopleListener = onHasPeopleListener;
    }

    public interface OnHasPeopleListener {
        void hasBody(String body);
    }
}
