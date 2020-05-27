package com.ys.mcu7502;

/**
 * Created by Administrator on 2018/1/27.
 */

public class Mcu7502 {
    public static native int open();//60
    public static native void close();

    public static native int enableWatchdog(); //返回值为7表示命令发送成功
    public static native int disableWatchdog(); //返回值为7表示命令发送成功
    public static native int feetDog(int time); //喂狗的时间，time大于等于10s，小于等于60s  ,返回值为7表示命令发送成功
    public static native int setTime(int time); //设置下一次开机的时间 ，以s为单位。大于等于60s ,返回值为7表示命令发送成功
    public static native int resetMaster();     //返回值为7表示命令发送成功
    public static native int getWatchdogStatus();  //返回值为看门狗状态，1表示已经打开0表示关闭
    public static native int getVersion();     //返回值为int型变量，第一个字节未定义，第二个字节表示版本号第前两位，第三个字节表示版本号中间两位，第四个字节表示版本号的后两位

    static {
        System.loadLibrary("mcu7502");
    }
}
