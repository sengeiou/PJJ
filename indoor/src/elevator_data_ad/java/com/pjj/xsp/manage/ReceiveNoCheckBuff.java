package com.pjj.xsp.manage;

import com.pjj.xsp.utils.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.pjj.xsp.PjjApplication;
import com.pjj.xsp.intent.socket.SocketConnectListener;
import com.pjj.xsp.module.bean.BoardBean;
import com.pjj.xsp.utils.FileUtils;
import com.pjj.xsp.view.MainViewHelp;

public class ReceiveNoCheckBuff {
    private StringBuffer buffer = new StringBuffer();

    public synchronized void writeBytes(byte[] b, int size) {
        byte[] bytes = new byte[size];
        for (int i = 0; i < size; i++) {
            bytes[i] = b[i];
        }
        String s = new String(bytes);
        buffer.append(s);
        int start_index = buffer.indexOf("v");
        if (start_index > -1) {
            int end_index = buffer.indexOf("}a");
            int end_index_ = buffer.indexOf("}l");
            if (end_index == -1 || (end_index_ > -1 && end_index > end_index_)) {
                end_index = end_index_;
            }
            if (end_index > -1 && end_index > start_index) {
                end_index = end_index + 1;
                Log.e("TAG", "DispRecData s: " + buffer.toString());
                Log.e("TAG", "DispRecData s: " + start_index + ", " + end_index);
                String substring = buffer.substring(start_index + 1, end_index);
                sendElevatorBroadcast(substring);
                Log.e("TAG", "substring: " + substring);
                buffer.delete(0, end_index + 1);
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

    private SocketConnectListener socketConnectListener;

    public void setSocketConnectListener(SocketConnectListener socketConnectListener) {
        this.socketConnectListener = socketConnectListener;
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

