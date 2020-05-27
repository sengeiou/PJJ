package com.pjj.xsp.intent.socket;

import com.pjj.xsp.module.bean.BoardBean;

/**
 * Create by xinheng on 2018/10/27。
 * describe：
 */
public interface SocketConnectListener {
    void sendMessage(BoardBean s);
}
