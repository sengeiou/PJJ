package com.pjj.xsp.intent.socket;

import android.support.annotation.Nullable;

import com.pjj.xsp.PjjApplication;
import com.pjj.xsp.utils.FileUtils;
import com.pjj.xsp.utils.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pjj.xsp.module.XspPlayUI;
import com.pjj.xsp.module.bean.BoardBean;
import com.pjj.xsp.module.parameter.SocketXspInf;
import com.pjj.xsp.receiver.JPushReceiver;
import com.pjj.xsp.view.MainViewHelp;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * Create by xinheng on 2018/10/25。
 * describe：
 */
public class XspWebSocketListener extends WebSocketListener implements SocketConnectListener {
    private final String TAG = "Socket";
    private Request request;
    private WebSocket webSocket;
    private boolean isConnect;
    private boolean send;
    private boolean isReConnecting;
    private SocketXspInf socketXspInf = new SocketXspInf();
    private String register;
    private long time;

    public XspWebSocketListener(Request request) {
        this.request = request;
    }

    public Request getRequest() {
        return request;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {//开启长连接成功的回调
        super.onOpen(webSocket, response);
        this.webSocket = webSocket;
        isConnect = true;
        isReConnecting = false;
        Log.e(TAG, "onOpen: 连接成功");
        FileUtils.saveStringFile(PjjApplication.App_Path + "socket.txt", "：onOpen: 连接成功");
        MainViewHelp.getInstance().updateOnlineStatue(true);
        //MainViewHelp.getInstance().updateSocketStatue(true);
    }

    JsonParser jsonParser = new JsonParser();

    @Override
    public void onMessage(WebSocket webSocket, String text) {//接收消息的回调
        super.onMessage(webSocket, text);
        //Log.e(TAG, "onMessage: " + text + ", " + (Looper.myLooper() == Looper.getMainLooper()));
        //{"operateType":"0006","operateName":"开始推送"}
        //007
        FileUtils.saveStringFile(PjjApplication.App_Path + "socket.txt", "：onMessage: " + text);
        try {
            JsonElement parse = jsonParser.parse(text);
            JsonObject asJsonObject = parse.getAsJsonObject();
            dealwithServiceMessage(asJsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        super.onMessage(webSocket, bytes);
        String hex = bytes.hex();
        Log.e(TAG, "onMessage B: " + hex);
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {//正在关闭连接
        super.onClosing(webSocket, code, reason);
        isConnect = false;
        isReConnecting = false;
        FileUtils.saveStringFile(PjjApplication.App_Path + "socket.txt", "：onClosing: " + code + ", " + reason);
        Log.e(TAG, "onClosing: " + code + ", " + reason);
        if (!"正常关闭".equals(reason)) {//非正常关闭 重连
            reConnect();
        }
    }


    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {//关闭连接
        super.onClosed(webSocket, code, reason);
        Log.e(TAG, "onClosed: " + code + ", " + reason);
        isConnect = false;
        isReConnecting = false;
        Log.e(TAG, "onClosed: " + code + ", " + reason);
        if (!"正常关闭".equals(reason)) {//非正常关闭 重连
            reConnect();
        }
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {//长连接连接失败的回调
        super.onFailure(webSocket, t, response);
        isConnect = false;
        isReConnecting = false;
        Log.e(TAG, "onFailure: ");
        FileUtils.saveStringFile(PjjApplication.App_Path + "socket.txt", "：onFailure");
        reConnect();
    }


    private synchronized void reConnect() {
        if (!isConnect && !isReConnecting) {
            isReConnecting = true;
            FileUtils.saveStringFile(PjjApplication.App_Path + "socket.txt", "：reConnect");
            time = System.currentTimeMillis();
            //MainViewHelp.getInstance().updateSocketStatue(false);
            SocketManage.getInstance().reConnect(XspWebSocketListener.this);
        }
    }

    private Gson gson = new Gson();

    /**
     * 发送消息
     *
     * @param message 信息
     */
    public void sendMessage(BoardBean message) {
        //Log.i(TAG, "sendMessage: ..." + isConnect + ", " + send);
        if (isConnect) {
            if (send) {
                BoardBean.DataBean data = message.getData();
                data.setRegister(register);
                socketXspInf.setBoardBean(message);
                socketXspInf.setOperateType("0008");
                String text = gson.toJson(socketXspInf);
                webSocket.send(text);
                //Log.e(TAG, "sendMessage: "+text);
                //Log.e(TAG, "sendMessage1: 发送结果 = " + send + " , message = " + ReceiveBuff1.explain(message));
            }
        } else {
            long time = System.currentTimeMillis();
            if (time - this.time > 5000) {
                isReConnecting = false;
            }
            FileUtils.saveStringFile(PjjApplication.App_Path + "socket.txt", "：reConnect delay>5000");
            reConnect();
        }
    }

    /**
     * 处理服务端发来的信息
     *
     * @param asJsonObject 信息
     */
    private void dealwithServiceMessage(JsonObject asJsonObject) {
        String operateType = asJsonObject.get("operateType").getAsString();
        Log.e(TAG, "onMessage: operateType=" + operateType);
        if ("0006".equals(operateType)) {
            try {
                register = asJsonObject.get("register").getAsString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            send = true;
        } else if ("0007".equals(operateType)) {
            send = false;
        } else if (JPushReceiver.UNDO_ORDER.equals(operateType)) {
            String orderId = asJsonObject.get("orderId").getAsString();
            XspPlayUI.getInstall().stopPlayOrder(orderId);
        } else if (JPushReceiver.SEARCH_TASK.equals(operateType)) {
            String orderId = asJsonObject.get("orderId").getAsString();
            XspPlayUI.getInstall().searchTask(orderId);
        }
    }

    /**
     * 断开socket
     */
    private void cancel() {
        if (null != webSocket) {
            webSocket.close(100, "正常关闭");
        }
    }
}
