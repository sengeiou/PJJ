package com.pjj.xsp.manage;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.pjj.xsp.PjjApplication;
import com.pjj.xsp.module.bean.BoardBean;
import com.pjj.xsp.utils.FileUtils;
import com.pjj.xsp.utils.Log;
import com.pjj.xsp.utils.TypeConversion;
import com.pjj.xsp.view.MainViewHelp;

public class ReceiveBuff extends ReceiveBuffParent {

    public byte[] comBytesBuf = new byte[1024];
    public int lastPos = 0;
    private int dataLength = 0;
    private int sum;
    byte[] needData = new byte[1024];

    /*public synchronized void writeBytes(byte[] b, int size) {
        //Log.e("TAG", "writeBytes 探针 : " + new String(b));
        String jsonStr = "";
        if ((size + lastPos) >= 2048) {
            comBytesBuf = new byte[2048];
            lastPos = 0;
        }
        for (int i = 0; i < b.length; i++) {
            if (b[i] == 0x02) {
                System.arraycopy(b, i, comBytesBuf, lastPos, size);// b:表示复制内容的源数组 0:表示内容的起始索引 comBytesBuf:表示将数据复制到目标数组 lastPos:表示复制内容的起始索引 size:表示要复制的元素数目
                dataLength = Integer.parseInt(TypeConversion.Byte2Hex(comBytesBuf[i + 3]), 16); // 102
                if (dataLength > 0) {
                    for (int j = 1; j < dataLength + 2; j++) {
                        sum ^= comBytesBuf[j];
                    }
                    if (sum != comBytesBuf[dataLength + 4]) {
                        String toChStringHex = TypeConversion.toChStringHex(TypeConversion.bytes2HexString(needData));
                        //Log.e("TAG", "writeBytes: toChStringHex1=" + toChStringHex);
                        int a = toChStringHex.indexOf("{");
                        int c = toChStringHex.indexOf("}");
                        if (a != -1 && c != -1) {
                            jsonStr = toChStringHex.substring(a, c - a + 2);
                        }
                    } else {
                        System.arraycopy(comBytesBuf, i + 4, needData, 0, dataLength); //从BUFF中复制数据至校验数组
                        String toChStringHex = TypeConversion.toChStringHex(TypeConversion.bytes2HexString(needData));
                        //Log.e("TAG", "writeBytes: toChStringHex2=" + toChStringHex);
                        int a = toChStringHex.indexOf("{");
                        int c = toChStringHex.indexOf("}");
                        if (a != -1 && c != -1) {
                            try {
                                jsonStr = toChStringHex.substring(a, c - a + 2);
                            } catch (StringIndexOutOfBoundsException e) {
                                e.printStackTrace();
                            }
                        }
                        if (jsonStr.length() > 100) {
                            sendElevatorBroadcast(jsonStr);
                        }
                    }
                    sum = 0;
                }
            }
        }
    }*/
    public synchronized void writeBytes(byte[] b, int size) {
        String jsonStr = "";
        if ((size + lastPos) >= 2048) {
            comBytesBuf = new byte[2048];
            lastPos = 0;
        }

        for (int i = 0; i < b.length; i++) {
            if (b[i] == 0x02) {
                //Log.e("ReceiveBuff", "====new String(b):" + new String(b));
                System.arraycopy(b, i, comBytesBuf, lastPos, size);// b:表示复制内容的源数组 0:表示内容的起始索引 comBytesBuf:表示将数据复制到目标数组 lastPos:表示复制内容的起始索引 size:表示要复制的元素数目
                if (comBytesBuf.length - i > 120){
                    //Log.e("ReceiveBuff", "==2222222");
                    dataLength = Integer.parseInt(TypeConversion.Byte2Hex(comBytesBuf[i + 3]), 16); // 102
                    for (int j = 1; j < dataLength + 2; j++) {
                        sum ^= comBytesBuf[j];
                    }
                    //Log.e("ReceiveBuff", "==3333333333");
                    //Log.e("ReceiveBuff", "====new String(comBytesBuf):" + new String(comBytesBuf));
                    if (sum != comBytesBuf[dataLength + 4]) {
                        //Log.e("cjg", "==44444444444");
                        int a = TypeConversion.toChStringHex(TypeConversion.bytes2HexString(needData)).indexOf("{");
                        int c = TypeConversion.toChStringHex(TypeConversion.bytes2HexString(needData)).indexOf("}");
                        if (a != -1 && c != -1) {
                            try{
                                jsonStr = TypeConversion.toChStringHex(TypeConversion.bytes2HexString(needData)).substring(a, c - a + 2);
                            }catch (StringIndexOutOfBoundsException e){
                                e.printStackTrace();
                            }
                        }
                    } else {
                        //Log.e("ReceiveBuff", "==5555555555555");
                        //Log.e("ReceiveBuff", "====new String(needData):" + new String(needData));
                        System.arraycopy(comBytesBuf, i + 4, needData, 0, dataLength); //从BUFF中复制数据至校验数组
                        int a = TypeConversion.toChStringHex(TypeConversion.bytes2HexString(needData)).indexOf("{");
                        int c = TypeConversion.toChStringHex(TypeConversion.bytes2HexString(needData)).indexOf("}");
                        if (a != -1 && c!= -1) {
                            try{
                                jsonStr = TypeConversion.toChStringHex(TypeConversion.bytes2HexString(needData)).substring(a, c - a + 2);
                                //Log.e("ReceiveBuff", "======data:" + jsonStr);
                            }catch (StringIndexOutOfBoundsException e){
                                e.printStackTrace();
                            }
                        }
                    }
                    if (jsonStr.length() > 100) {
                        sendElevatorBroadcast(jsonStr);
                        //Log.e("cjg", "======data:" + jsonStr);
                    }
                    sum = 0;
                }
            }
        }
    }
    private Gson gson = new Gson();

    // 发送一条广播把获取到的电梯运行数据给AdvertiseActivity
    public void sendElevatorBroadcast(String data) {
        BoardBean bean = null;
        try {
            bean = gson.fromJson(data, BoardBean.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        if (null != bean) {
            if (bean.getRet() == 0) {//获取失败
                Log.e("TAG", "ret==0-获取数据失败");
                boolean b = BootManage.getInstance().initSerialPort();
                FileUtils.saveStringFile(PjjApplication.App_Path + "chuankou.txt", "ret==0-获取数据失败,信息：" + data + "\n-重新初始化串口结果" + b);
                return;
            }
            MainViewHelp.getInstance().setElevatorText(bean);
            if (null != socketConnectListener) {
                socketConnectListener.sendMessage(bean);
            }
        }
    }


}

