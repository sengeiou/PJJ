package com.pjj.xsp.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogcatWrite {
    public static int TIME_ACTION = 0;
    public static String FOLDER_ACTION = "";
    boolean logcatThread = false;

    private MyThreadlogcatV mdl = null;

    public LogcatWrite() {
        android.util.Log.e("TAG", "LogcatWrite: ");
    }

    public void setupTimer(String path) {
        FOLDER_ACTION = path;
        this.mdl = new MyThreadlogcatV();
        //this.mdl.start();
        new Thread() {
            @Override
            public void run() {
                String pathname = FOLDER_ACTION + getCurTime() + "/";
                File file = new File(pathname);
                if (!file.exists()) {
                    file.mkdir();
                }
                String filePath = pathname + "logcat1.txt";
                Log.e("TAG", "testDate1: " + filePath);
                file = new File(filePath);
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                String array = "logcat -f " + filePath + " -n 50 -r 20480";
//                String array = "logcat -f " + filePath ;
                Runtime runtime = Runtime.getRuntime();
                Process process = null;
                InputStream is = null;
                InputStreamReader isr = null;
                BufferedReader br = null;
                try {
                    process = runtime.exec(array); // 此处进行读操作
                    if (null != process) {
                        process.waitFor();
                    }
                    is = process.getInputStream();
                    isr = new InputStreamReader(is);
                    br = new BufferedReader(isr);
//                    String line;
//                    StringBuilder buffer = new StringBuilder();
//                    while (null != (line = br.readLine())) {
//                        Log.e("TAG", "read data ---> " + line);
//                        buffer.append(line);
//                    }
//                    return buffer.toString().trim();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.w("TAG-XSP", "*** ERROR *** Here is what I know: " + e.getMessage());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (null != br)
                            br.close();
                        if (null != isr)
                            isr.close();
                        if (null != is)
                            is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (null != process)
                        process.destroy();
                }
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                String pathname = FOLDER_ACTION + getCurTime() + "/";
                File file = new File(pathname);
                if (!file.exists()) {
                    file.mkdir();
                }
                String filePath = pathname + "dmesg.txt";
                Log.e("TAG", "dmesg1: " + filePath);
                file = new File(filePath);
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                String array = "dmesg > " + filePath ;//+ " -n 50 -r 20480";
//                String array = "logcat -f " + filePath ;
                Runtime runtime = Runtime.getRuntime();
                Process process = null;
                InputStream is = null;
                InputStreamReader isr = null;
                BufferedReader br = null;
                try {
                    process = runtime.exec(array); // 此处进行读操作
                    if (null != process) {
                        process.waitFor();
                    }
                    is = process.getInputStream();
                    isr = new InputStreamReader(is);
                    br = new BufferedReader(isr);
//                    String line;
//                    StringBuilder buffer = new StringBuilder();
//                    while (null != (line = br.readLine())) {
//                        Log.e("TAG", "read data ---> " + line);
//                        buffer.append(line);
//                    }
//                    return buffer.toString().trim();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.w("TAG-XSP", "*** ERROR *** Here is what I know: " + e.getMessage());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (null != br)
                            br.close();
                        if (null != isr)
                            isr.close();
                        if (null != is)
                            is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (null != process)
                        process.destroy();
                }
            }
        }.start();
    }

    private String getCurTime() {
        String time = "";
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        time = format.format(date);
        return time;
    }

    private String nowPath;

    private String getFolderFilePath() {
        int tag = 1;
        if (null == nowPath) {
            nowPath = FOLDER_ACTION + getCurTime() + "/log-1.txt";
        } else {
            int index = nowPath.lastIndexOf("/");
            int indexEnd = nowPath.lastIndexOf(".txt");
            String substring = nowPath.substring(index + 1, indexEnd);
            String[] split = substring.split("-");
            tag = Integer.parseInt(split[1]);
        }
        File file = new File(nowPath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (file.length() / 1204 / 1024 > 50) {
                String filePath = FOLDER_ACTION + getCurTime() + "/log-" + tag + ".txt";

            }
        }
        return "";
    }

    private void testDate(String folder) {
        String filePath = folder + "logcat.txt";
        Log.e("TAG", "testDate: " + filePath);
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Process process = null;
        DataOutputStream os = null;
        DataInputStream is = null;
        try {
            //String s = "logcat -v time> " + folder + "logcat.txt \n";
            List<String> commandList = new ArrayList<String>();
            commandList.add("logcat");
            commandList.add("-f");
            commandList.add(filePath);
            commandList.add("-v");
            commandList.add("time");
            process = Runtime.getRuntime().exec(commandList.toArray(new String[commandList.size()]));
            os = new DataOutputStream(process.getOutputStream());
//            os.writeBytes(s);
            os.writeBytes("exit\n");
            os.flush();
            int aa = process.waitFor();
            //is = new DataInputStream(process.getInputStream());
//            byte[] buffer = new byte[is.available()];
//            is.read(buffer);
//            String out = new String(buffer);
//            Log.e("TAG_LOG", out + aa);
        } catch (Exception var16) {
            var16.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }

                if (is != null) {
                    is.close();
                }
                process.destroy();
            } catch (Exception var15) {
                var15.printStackTrace();
            }

        }

    }

    private static String loadFileAsString(String filePath) throws IOException {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        boolean var4 = false;

        int numRead;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }

        reader.close();
        return fileData.toString();
    }

    public void close() {
        this.logcatThread = false;
        this.mdl = null;
    }

    public class MyThreadlogcatV extends Thread {
        public MyThreadlogcatV() {
        }

        public void run() {
            this.logcatV();
        }

        private void logcatV() {
            while (true) {
                try {
                    sleep(3000L);
                    testDate(FOLDER_ACTION);
                    //adw_write_file(FOLDER_ACTION);

                    sleep(9000L);
                } catch (InterruptedException var2) {
                    var2.printStackTrace();
                }
            }
        }
    }
}
