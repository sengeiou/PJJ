package com.pjj.xsp.module;

import com.pjj.xsp.BuildConfig;
import com.pjj.xsp.db.DateTask;
import com.pjj.xsp.manage.XSPSystem;
import com.pjj.xsp.module.bean.ScreenInfBean;
import com.pjj.xsp.module.parameter.ScreenInfTag;

import java.util.HashMap;

/**
 * Create by xinheng on 2018/10/25。
 * describe：广告屏信息管理
 */
public class ScreenInfManage {
    /**
     * 添加开始状态
     */
    public static final boolean ADD_START_STATUE = false;
    private static final HashMap<String, String> citymap = new HashMap<>();

    static {
        citymap.put("北京", "beijing");
        citymap.put("天津", "tianjin");
    }

    private static ScreenInfManage instance;
    /**
     * 广告屏信息
     * 维保信息
     */
    private ScreenInfBean.DataBean dataBean;

    private ScreenInfManage() {
    }

    public static ScreenInfManage getInstance() {
        if (null == instance) {
            synchronized (ScreenInfManage.class) {
                if (null == instance) {
                    instance = new ScreenInfManage();
                }
            }
        }
        return instance;
    }

    /**
     * 此时任务
     */
    private PlayTaskParent nowTask;
    private PlayTaskParent nextTask;
    /**
     * 广告屏唯一标识
     */
    private ScreenInfTag screenInfTag;

    /**
     * App文件下载路径头
     */
    public static final String filePath = "http://pjj-apk-file.oss-cn-beijing.aliyuncs.com/";
    /**
     * 广告屏媒体文件下载路径头
     */
    public static final String filePathMedia = BuildConfig.APP_TYPE ? "http://pjj-liftapp.oss-cn-beijing.aliyuncs.com/" : "http://pjj-liftapp-test.oss-cn-beijing.aliyuncs.com/";

    public PlayTaskParent getNowTask() {
        return nowTask;
    }

    public void setNowTask(PlayTaskParent nowTask) {
        this.nowTask = nowTask;
    }

    public PlayTaskParent getNextTask() {
        return nextTask;
    }

    public void setNextTask(PlayTaskParent nextTask) {
        this.nextTask = nextTask;
    }

    public ScreenInfTag getScreenInfTag() {
        if (null == screenInfTag.getScreenId()) {
            screenInfTag.setScreenId(XSPSystem.getInstance().getOnlyCode());
        }
        return screenInfTag;
    }

    public void setScreenInfTag(ScreenInfTag screenInfTag) {
        this.screenInfTag = screenInfTag;
    }

    public ScreenInfBean.DataBean getScreenInfDataBean() {
        return dataBean;
    }

    public void setScreenInfDataBean(ScreenInfBean.DataBean dataBean) {
        this.dataBean = dataBean;
    }

    private OnStartMessageListener onStartMessageListener;

    public void setOnStartMessageListener(OnStartMessageListener onStartMessageListener) {
        this.onStartMessageListener = onStartMessageListener;
    }

    public void startPlayView() {
        if (ADD_START_STATUE && null != onStartMessageListener) {
            onStartMessageListener.receiveStartMessage();
        }
    }

    public void activateXsp() {
        if (null != onStartMessageListener) {
            onStartMessageListener.activateXsp();
        }
    }

    public interface OnStartMessageListener {
        /**
         * 收到开始信息
         *
         * @see { ADD_START_STATUE }
         */
        void receiveStartMessage();

        /**
         * 激活广告屏
         */
        void activateXsp();
    }
}
