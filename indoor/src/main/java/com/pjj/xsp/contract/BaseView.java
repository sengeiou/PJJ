package com.pjj.xsp.contract;

/**
 * Create by xinheng on 2018/10/16。
 * describe：view 基础协议
 */
public interface BaseView {
    /**
     * 展示等待状态
     */
    void showWaiteStatue();

    /**
     * 取消等待状态
     */
    void cancelWaiteStatue();

    /**
     * 失败状态
     * @param error 原因
     */
    void failStatue(String error);
}
