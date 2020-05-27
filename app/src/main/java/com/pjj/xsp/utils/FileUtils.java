package com.pjj.xsp.utils;

import com.google.gson.Gson;
import com.pjj.xsp.intent.http.RetrofitService;
import com.pjj.xsp.module.bean.ErrorBean;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

import static com.pjj.xsp.PjjApplication.App_Path;

/**
 * Create by xinheng on 2018/10/15。
 * describe：
 */
public class FileUtils {
    public static void saveFile(String filePath, InputStream inputStream, OnDownloadListener onDownloadListener) {
        File fileComplete = new File(filePath);
        if (fileComplete.exists()) {
            Log.e("TAG", "saveFile: 已存在");
            onDownloadListener.success();
            return;
        }
        File file = new File(filePath + "1");
        if (file.exists()) {
            file.getAbsoluteFile().delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
            //BufferedInputStream bis = new BufferedInputStream(inputStream);
            byte[] b = new byte[1024];
            int len;
            while ((len = inputStream.read(b)) != -1) {
                fos.write(b, 0, len);
            }
            inputStream.close();
            fos.close();
            String absolutePath = file.getAbsolutePath();
            renameFile(absolutePath, absolutePath.substring(0, absolutePath.length() - 1));
            Log.e("TAG", "saveFile: 完成");
            onDownloadListener.success();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            onDownloadListener.fail();
        } catch (IOException e) {
            e.printStackTrace();
            onDownloadListener.fail();
        }
    }

    /**
     * 重命名文件
     *
     * @param oldPath 原来的文件地址
     * @param newPath 新的文件地址
     */
    public static void renameFile(String oldPath, String newPath) {
        File oleFile = new File(oldPath);
        File newFile = new File(newPath);
        //执行重命名
        oleFile.renameTo(newFile);
    }

    public static void saveStringFile(String filePath, ErrorBean errorBean) {
        new Thread() {
            @Override
            public void run() {
                Gson gson = new Gson();
                gson.toJson(errorBean);
            }
        }.start();
    }

    public static void saveStringFile(String filePath, String text) {
        //File file =new File(filePath);
        new Thread() {
            @Override
            public void run() {
                try {
                    String writeString = DateUtils.getNowTimeS() + "-" + text;
                    BufferedWriter bfw = new BufferedWriter(new FileWriter(filePath, true));
                    bfw.write(writeString);
                    bfw.newLine();
                    bfw.flush();
                    bfw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static String getSDFilePath(String path) {
        File file = new File(App_Path + RetrofitService.getInstance().getFileName(path));
        if (file.exists()) {
            path = file.getAbsolutePath();
            Log.e("TAG", "onViewCreated: 本地 = " + file.getPath());
        }
        return path;
    }

    public interface OnDownloadListener {
        void success();

        void fail();
    }
    /**
     * 获取单个文件的MD5值！
     *
     * @param path
     * @return
     */
    public static String getFileMD5(String path) {
        return getFileMD5(new File(path));
    }

    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }
}
