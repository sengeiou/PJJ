package com.pjj.xsp.utils;

import com.google.gson.Gson;
import com.pjj.xsp.intent.http.RetrofitService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SignatureException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

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

    static ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static void saveStringFile(String filePath, String text) {
        //File file =new File(filePath);
        executorService.execute(() -> {
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
        });

    }

    public static String getSDFilePath(String path) {
        File file = new File(App_Path + RetrofitService.getInstance().getFileName(path));
        if (file.exists()) {
            path = file.getAbsolutePath();
            //Log.e("TAG", "onViewCreated: 本地 = " + file.getPath());
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

    public static void copyFile(File oldFile, String newPath$Name, OnDownloadListener downloadListener) {
        new Thread() {
            @Override
            public void run() {
                File file = new File(newPath$Name);
                if (file.exists()) {
                    String fileMD5 = getFileMD5(file);
                    String fileMD5Old = getFileMD5(oldFile);
                    Log.e("TAG", "run: " + fileMD5Old + ", " + fileMD5);
                    if (fileMD5 != null && fileMD5.equals(fileMD5Old)) {
                        downloadListener.success();
                        return;
                    } else {
                        file.getAbsoluteFile().delete();
                    }
                }
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                boolean copyFile = copyFile(oldFile, newPath$Name);
                if (copyFile) {
                    downloadListener.success();
                } else {
                    downloadListener.fail();
                }
            }
        }.start();
    }

    private static boolean copyFile(File oldFile, String newPath$Name) {
        try {
            if (!oldFile.exists()) {
                Log.e("--Method--", "copyFile:  oldFile not exist.");
                return false;
            } else if (!oldFile.isFile()) {
                Log.e("--Method--", "copyFile:  oldFile not file.");
                return false;
            } else if (!oldFile.canRead()) {
                Log.e("--Method--", "copyFile:  oldFile cannot read.");
                return false;
            }

            /* 如果不需要打log，可以使用下面的语句
            if (!oldFile.exists() || !oldFile.isFile() || !oldFile.canRead()) {
                return false;
            }
            */

            FileInputStream fileInputStream = new FileInputStream(oldFile);
            FileOutputStream fileOutputStream = new FileOutputStream(newPath$Name);
            byte[] buffer = new byte[1024];
            int byteRead;
            while (-1 != (byteRead = fileInputStream.read(buffer))) {
                fileOutputStream.write(buffer, 0, byteRead);
            }
            fileInputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String readTxt(File file) {
        if (null == file || !file.isFile() || !file.exists()) {
            return null;
        }
        String content = "";
        try {
            InputStream instream = new FileInputStream(file);
            if (instream != null) {
                InputStreamReader inputreader
                        = new InputStreamReader(instream, "UTF-8");
                BufferedReader buffreader = new BufferedReader(inputreader);
                String line;
                //分行读取
                while ((line = buffreader.readLine()) != null) {
                    content += line;
                }
                instream.close();//关闭输入流
            }
        } catch (java.io.FileNotFoundException e) {
            Log.e("TestFile", "The File doesn't not exist.");
            return null;
        } catch (IOException e) {
            Log.e("TestFile", e.getMessage());
            return null;
        }
        return content;
    }

}
