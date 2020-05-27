package android.serialport;


import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialPort {

    private static final String TAG = "SerialPort";

    private FileDescriptor mFd;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;

    public SerialPort() {

    }

    public int openSerial(File device, int baudrate, int flags) {

        int ret = 0;

        mFd = open(device.getAbsolutePath(), baudrate, flags);

        if (mFd == null) {
            ret = 1;
            Log.d(TAG, "native open returns null");
            return ret;
        }
        mFileInputStream = new FileInputStream(mFd);
        mFileOutputStream = new FileOutputStream(mFd);
        return ret;
    }

    // Getters and setters
    public InputStream getInputStream() {
        return mFileInputStream;
    }

    public OutputStream getOutputStream() {
        return mFileOutputStream;
    }

    // JNI
    private native static FileDescriptor open(String path, int baudrate, int flags);

    private native void close();

    static {
//        Log.i(TAG, "loadLibrary......serial_port........");
        System.loadLibrary("serial_port");
    }
}
