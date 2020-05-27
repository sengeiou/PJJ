package com.pjj.xsp.intent.socket;

import android.os.Handler;
import android.os.Looper;

import com.pjj.xsp.utils.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;


/**
 * Create by xinheng on 2018/10/25。
 * describe：Socket 创建 重连 单例
 */
public class SocketManage {

    private static SocketManage INSTANCE;

    public static SocketManage getInstance() {
        if (null == INSTANCE) {
            synchronized (SocketManage.class) {
                if (null == INSTANCE) {
                    INSTANCE = new SocketManage();
                }
            }
        }
        return INSTANCE;
    }

    private OkHttpClient client;
    private Handler mainHandler;

    private SocketManage() {
        client = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS)/*.readTimeout(0, TimeUnit.MILLISECONDS)*/.build();
        mainHandler = new Handler(Looper.getMainLooper());
    }


    /**
     * 创建socket连接
     *
     * @param url 连接地址
     */
    public void createSocket(String url) {//"ws://192.168.1.124:8083/websocket/"+XSPSystem.getInstance().getOnlyCode()
        Request request = new Request.Builder().url(url).build();
        XspWebSocketListener xspWebSocketListener = new XspWebSocketListener(request);
        client.newWebSocket(request, xspWebSocketListener);
        //BootManage.getInstance().setSocketConnectListener(xspWebSocketListener);
        //xspWebSocketListener.setSocketConnectListener(socketConnectListener);
        //String s = request.url().toString();
        //Log.e("TAG", "createSocket: " + s);

    }

    /**
     * 重连
     *
     * @param xspWebSocketListener socket连接状态以及请求信息
     */
    public void reConnect(XspWebSocketListener xspWebSocketListener) {
        Log.e("TAG", "重连 reConnect: ");
        Request request = xspWebSocketListener.getRequest();
        client.newWebSocket(request, xspWebSocketListener);
    }
}
