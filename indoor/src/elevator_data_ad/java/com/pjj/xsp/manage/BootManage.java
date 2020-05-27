package com.pjj.xsp.manage;

import android.serialport.SerialPort;
import com.pjj.xsp.utils.Log;

import com.pjj.xsp.BuildConfig;
import com.pjj.xsp.PjjApplication;
import com.pjj.xsp.intent.socket.SocketConnectListener;
import com.pjj.xsp.utils.SuperUser;
import com.pjj.xsp.utils.TypeConversion;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Create by xinheng on 2018/10/26。
 * describe：系统 主板操作
 */
public class BootManage {
    private static final String TAG = "BootManage";
    private boolean threadTag = true;
    private static BootManage instance;
    private SerialPort mSerialPort;
    private int isUart = 0;
    private OutputStream mOutputStream = null;
    private InputStream mInputStream = null;
    private int boardThreadMill = 500;
    //private ReceiveBuffOld3188 rb = new ReceiveBuffOld3188();
    //private ReceiveBuff rb = new ReceiveBuff();
    private ReceiveBuffParent rb;
    //    private ReceiveNoCheckBuff rb = new ReceiveNoCheckBuff();
    private ReadThread readThread;
    private SendThread sendThread;
    byte[] m_ComDataBuf = new byte[1024];
    private int size;

    private BootManage() {
        //获得超级用户权限
        SuperUser.upgradeRootPermission(PjjApplication.application.getPackageCodePath());
        if (PjjApplication.OLD_3188) {
            rb = new ReceiveBuffOld3188();
        } else {
            rb = new ReceiveBuff();
        }
        initSerialPort();
    }

    public void setSocketConnectListener(SocketConnectListener socketConnectListener) {
        rb.setSocketConnectListener(socketConnectListener);
    }

    public static BootManage getInstance() {
        if (null == instance) {
            synchronized (BootManage.class) {
                if (null == instance) {
                    instance = new BootManage();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化串口
     */
    public boolean initSerialPort() {
        if (null == mSerialPort)
            mSerialPort = new SerialPort();
        try {
            isUart = mSerialPort.openSerial(new File(AppConfig.SERIAL_PORT_DEVICE), AppConfig.SERIAL_PORT_BAUDRATE, AppConfig.SERIAL_PORT_FLAGS);
            if (isUart == 1) {
                AppConfig.SERIALPORT_IS_ACTIVE = false;
                /*Message tempMsg = mHandler.obtainMessage();
                tempMsg.what = MSG_OPEN_SERIAL_FAILED;
                mHandler.sendMessage(tempMsg);*/
                Log.e(TAG, "initSerialPort: 初始化串口失败");
                return false;
            } else {
                Log.e(TAG, "initSerialPort: 初始化串口成功");
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
        stopClear();
        threadTag = true;
        readThread = new ReadThread();
        readThread.start();
        sendThread = new SendThread();
        return true;
    }

    public void stopClear() {
        if (null != readThread) {
            threadTag = false;
            try {
                readThread.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
            readThread = null;
        }
        if (null != sendThread) {
            threadTag = false;
            try {
                sendThread.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
            sendThread = null;
        }
    }

    public void statReadThread() {
        try {
            sendThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean openSerialPort() {
        return isUart != 1;
    }


    //数据读取线程
    private class ReadThread extends Thread {
        @Override
        public void run() {
            while (threadTag) {
                // 休眠1秒钟后把从板卡返回的数据写入缓存中
                try {
                    size = mInputStream.read(m_ComDataBuf);
                    rb.writeBytes(m_ComDataBuf, size);
                    //Log.i(TAG, "run: ReadThread size=" + size);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(boardThreadMill);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class SendThread extends Thread {
        @Override
        public void run() {
            while (threadTag) {
                sendDataRequest();
                try {
                    Thread.sleep(boardThreadMill);//默认500毫秒获取一次板卡信息
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //板卡数据获取请求
    private void sendDataRequest() {
        String sOut = "021000001003";
//        String sOut = "023200003203";
        sendPortData(sOut);
    }

    /***********************************
     * 从另一个demo拷贝的代码  发送指令给板卡
     ************************************************/

    private void sendPortData(String sOut) {
        sendHex(sOut);
    }

    private void sendHex(String sHex) {
        byte[] bOutArray = TypeConversion.HexToByteArr(sHex);
        send(bOutArray);
    }

    private void send(byte[] bOutArray) {
        try {
            mOutputStream.write(bOutArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
