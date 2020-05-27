package com.pjj.xsp.manage;

import android.serialport.SerialPort;
import android.util.Log;

import com.pjj.xsp.utils.TypeConversion;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

public class CollectorSession {
    private static final String TAG = "CollectorSession";

    private SerialPort mSerialPort = null;
    private int isUart = 0;
    private OutputStream mOutputStream = null;
    private InputStream mInputStream = null;

    private ReadThread mReadThread;

    byte[] m_ComDataBuf = new byte[1024];
    private int size;
    private ReceiveBuff rb = new ReceiveBuff();

    private static CollectorSession session = new CollectorSession();

    private CollectorSession() {

    }

    public static CollectorSession getInstance() {
        return session;
    }

    /**
     * 重新加载串口
     *
     * @return
     */
    public boolean reStart() {
        mSerialPort = null;
        return start();
    }

    /**
     * 加载
     * 驱动，绑定数据流
     *
     * @return
     */
    public boolean start() {
        if (mSerialPort == null) {
            try {
                mSerialPort = new SerialPort();
                //isUart = mSerialPort.openSerial(new File(AppConfig.SERIAL_PORT_DEVICE_S4), AppConfig.SERIAL_PORT_BAUDRATE, AppConfig.SERIAL_PORT_FLAGS);
                isUart = mSerialPort.openSerial(new File(AppConfig.SERIAL_PORT_DEVICE_S3), AppConfig.SERIAL_PORT_BAUDRATE_T, AppConfig.SERIAL_PORT_FLAGS);
                Log.e("cjg", "======isUart:" + isUart + ", " + AppConfig.SERIAL_PORT_DEVICE_S3);
                if (isUart == 1) {
                    AppConfig.SERIALPORT_IS_ACTIVE = false;
                    return false;
                } else {
                    AppConfig.SERIALPORT_IS_ACTIVE = true;
                    if (mInputStream != null) {
                        try {
                            mInputStream.close();
                            mInputStream = null;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (mOutputStream != null) {
                        try {
                            mOutputStream.close();
                            mOutputStream = null;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    mInputStream = mSerialPort.getInputStream();
                    mOutputStream = mSerialPort.getOutputStream();
                }
            } catch (SecurityException e) {
                AppConfig.SERIALPORT_IS_ACTIVE = false;
                e.printStackTrace();
            }
        }
        if (mReadThread == null) {
            mReadThread = new ReadThread();
            mReadThread.start();
        }
        return true;
    }

    public void stop() {
        if (mReadThread != null) {
            mReadThread.interrupt();
            mReadThread = null;
        }
        mSerialPort = null;
    }

    //发送数据至板卡
    public void sendDataToUart(final byte[] buffer, final int size) {
        try {
            if (mOutputStream != null) {
                mOutputStream.write(buffer);// 把指令通过串口写入板卡
            }
        } catch (IOException e) {
            Log.d(TAG, "mOutputStream.write failed!");
        }
    }

    //板卡数据获取请求
    public void sendDataRequest(String sOut) {
        if (isUart == 1) {
            this.start();
        }
        //sendPortData(sOut);
    }

    //数据读取线程
    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (mInputStream == null) {
                        return;
                    }
                    try {
                        // 休眠1秒钟后把从板卡返回的数据写入缓存中
                        size = mInputStream.read(m_ComDataBuf);
                        rb.writeBytes(m_ComDataBuf, size);
                        //Log.e("TAG", "run: " + size);
//                    Log.v("cjg","TypeConversion.bytes2HexString(bOutArray)="+ TypeConversion.bytes2HexString(m_ComDataBuf));
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }, 0, 500);
        }
    }

    private void sendPortData(String sOut) {
        sendHex(sOut);
    }

    public void sendHex(String sHex) {
        byte[] bOutArray = TypeConversion.HexToByteArr(sHex);
        send(bOutArray);
    }

    public void send(byte[] bOutArray) {
        try {
            mOutputStream.write(bOutArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
