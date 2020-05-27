package com.pjj.xsp.manage;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.pjj.xsp.PjjApplication;
import com.pjj.xsp.intent.socket.SocketConnectListener;
import com.pjj.xsp.module.bean.BoardBean;
import com.pjj.xsp.utils.FileUtils;
import com.pjj.xsp.utils.TypeConversion;
import com.pjj.xsp.view.MainViewHelp;

public class ReceiveBuffOld3188 extends ReceiveBuffParent {

    /********************************* 之前写的 *******************************************/
    public byte[] comBytesBuf = new byte[1024];
    public int lastPos = 0;
    private int dataLength = 0;
    private int sum;
    byte[] needData = new byte[1024];

    public synchronized void writeBytes(byte[] b, int size) {
        String jsonStr = "";
        if ((size + lastPos) >= 2048) {
            comBytesBuf = new byte[1024];
            lastPos = 0;
        }
        for (int i = 0; i < b.length; i++) {

            if (b[i] == 0x02) {

                System.arraycopy(b, i, comBytesBuf, lastPos, size);// b:表示复制内容的源数组 0:表示内容的起始索引 comBytesBuf:表示将数据复制到目标数组 lastPos:表示复制内容的起始索引 size:表示要复制的元素数目

                dataLength = Integer.parseInt(Byte2Hex(comBytesBuf[i + 3]), 16); // 102
                Log.e("cjg", "获取的校验码===" + comBytesBuf[dataLength + 4] + ", " + dataLength); // 获取该条完整数据的校验码
                if (dataLength > 0) {
                    for (int j = 1; j < dataLength + 2; j++) {
                        sum ^= comBytesBuf[j];
                    }
                    String chStringHex = TypeConversion.toChStringHex(TypeConversion.bytes2HexString(needData));
                    Log.e("cjg", "sum==计算出的校验码=" + sum+", "+chStringHex);

                    if (sum != comBytesBuf[dataLength + 4]) {
//                        Log.e("cjg", "校验码不对");
//                        Log.e("cjg", "==获取的16进制数==" + TypeConversion.bytes2HexString(comBytesBuf));
                        int a = chStringHex.indexOf("{");
                        int c = chStringHex.indexOf("}");
                        if (a != -1 && c != -1) {
                            try {
                                jsonStr = chStringHex.substring(a, c - a + 2);
                            } catch (StringIndexOutOfBoundsException e) {
                                e.printStackTrace();
                                Log.e("cjg", "writeBytes: 2222---" + chStringHex);
                            }
                        }
                        Log.i("cjg", "======jsonStr.length::22222-----11---" + jsonStr);
                    } else {
                        System.arraycopy(comBytesBuf, i + 4, needData, 0, dataLength); //从BUFF中复制数据至校验数组
                        int a = chStringHex.indexOf("{");
//                        Log.v("cjg","============a:"+a);
                        int c = chStringHex.indexOf("}");
//                        Log.v("cjg","============带符号16进制数:"+TypeConversion.bytes2HexString(needData).substring(a, c - a + 2));
                        if (a != -1 && c != -1) {
                            try {
//                                Log.e("cjg", "======needData:" + needData);
                                jsonStr = chStringHex.substring(a, c - a + 2);
                            } catch (StringIndexOutOfBoundsException e) {
                                e.printStackTrace();
                                Log.e("cjg", "writeBytes: 1111---" + chStringHex);
                            }
                        }
                        Log.i("cjg", "======jsonStr.length::11111--------" + jsonStr + "\r\n" + "System.currentTimeMillis():" + System.currentTimeMillis());
//                        Log.v("cjg", "======jsonStr.length::" + jsonStr.length());
                        if (jsonStr.length() > 100) {
                            sendElevatorBroadcast(jsonStr);
                        }
                    }
                    sum = 0;
                }
            } else {
                //Log.e("cjg", "writeBytes: 0x02!=" + i+", "+new String(b));
            }
        }
    }

    public String Byte2Hex(Byte inByte) {
        return String.format("%02x", inByte).toUpperCase();
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

    public static String explain(BoardBean boardBean) {
        StringBuffer buffer = new StringBuffer();
        int ret = boardBean.getRet();
        BoardBean.DataBean data = boardBean.getData();
        String statue;
        String faultReport;
        String upDown = data.getUpDown();
        if (upDown.equals("FF")) {//0F：电梯上行 10：电梯下行 FF：停梯
            statue = "停梯";
        } else if (upDown.equals("0F")) {
            statue = "上行";
        } else {
            statue = "下行";
        }
        String faultReportB = data.getFaultReportB();
        if ("0B".equals(faultReportB)) {
            faultReport = "蹲底";
        } else if ("0C".equals(faultReportB)) {
            faultReport = "冲顶";
        } else {
            faultReport = "";
        }
        String faultReportA = data.getFaultReportA();
        if (!"FF".equals(faultReportA)) {
            faultReportA = "A：" + faultReportA;
        }
        String doorState = data.getDoorState();
        String doorStatue;
        if ("0D".equals(doorState)) {
            doorStatue = "开门";
        } else if ("FF".equals(doorState)) {
            doorStatue = "门FF";
        } else {
            doorStatue = "关门";
        }
        //buffer.append("当前");
        buffer.append(data.getFloor());
        buffer.append("层-");
        buffer.append(statue);
        buffer.append("-");
        buffer.append(doorStatue);
        buffer.append("-");
        buffer.append(faultReport);
        buffer.append("-");
        buffer.append(faultReportA);
        return buffer.toString();
    }
}

