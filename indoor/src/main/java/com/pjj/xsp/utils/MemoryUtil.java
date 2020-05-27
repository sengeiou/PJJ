package com.pjj.xsp.utils;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

public class MemoryUtil {
    public static boolean shouldClean(String path) {
        StatFs statFs = new StatFs(path);
        File storageDirectory = Environment.getExternalStorageDirectory();
        long usableSpace = storageDirectory.getUsableSpace();
        //long freeSpace = storageDirectory.getFreeSpace();
        long totalSpace = storageDirectory.getTotalSpace();
        if (usableSpace / totalSpace > 0.5) {
            return true;
        }
        return false;
    }
}
