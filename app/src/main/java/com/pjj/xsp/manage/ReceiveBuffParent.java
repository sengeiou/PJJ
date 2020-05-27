package com.pjj.xsp.manage;

import com.pjj.xsp.intent.socket.SocketConnectListener;

/**
 * Created by XinHeng on 2019/01/15.
 * describe：
 */
public abstract class ReceiveBuffParent {
    public abstract void writeBytes(byte[] b, int size);

    protected SocketConnectListener socketConnectListener;

    public void setSocketConnectListener(SocketConnectListener socketConnectListener) {
        this.socketConnectListener = socketConnectListener;
    }
}
