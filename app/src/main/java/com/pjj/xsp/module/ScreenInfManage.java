package com.pjj.xsp.module;

import com.pjj.xsp.BuildConfig;
import com.pjj.xsp.manage.XSPSystem;
import com.pjj.xsp.module.bean.ScreenInfBean;
import com.pjj.xsp.module.bean.ScreenTaskBean;
import com.pjj.xsp.module.parameter.ScreenInfTag;
import com.pjj.xsp.module.template.AdvertisingBean;

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
     * 下一时刻的任务 存储
     */
    private HashMap<String, Object> map;
    /**
     * 此时任务
     */
    private HashMap mapNow;
    /**
     * 广告屏唯一标识
     */
    private ScreenInfTag screenInfTag;
    /**
     * 广告屏主题
     */
    private AdvertisingBean advertisingBean;
    /**
     * 屏幕显示样式
     * 全屏、自适应
     */
    private int screenStyle;
    /**
     * App文件下载路径头
     */
    public static final String filePath = "http://pjj-apk-file.oss-cn-beijing.aliyuncs.com/";
    /**
     * 默认便民
     */
    public static String default_bm = "热烈庆祝屏加加app以及电梯物联网系统本月正式上线；屏联你我，加载未来，开启电梯安全新纪元！！！";
    /**
     * 广告屏媒体文件下载路径头
     */
    public static final String filePathMedia = BuildConfig.APP_TYPE ? "http://pjj-liftapp.oss-cn-beijing.aliyuncs.com/" : "http://pjj-liftapp-test.oss-cn-beijing.aliyuncs.com/";

    public HashMap<String, Object> getMap() {
        return map;
    }

    public void setMap(HashMap map) {
        this.map = map;
    }

    public HashMap getMapNow() {
        return mapNow;
    }

    public void setMapNow(HashMap mapNow) {
        this.mapNow = mapNow;
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

    public AdvertisingBean getAdvertisingBean() {
        return advertisingBean;
    }

    public void setAdvertisingBean(AdvertisingBean advertisingBean) {
        this.advertisingBean = advertisingBean;
    }

    public int getScreenStyle() {
        return screenStyle;
    }

    public void setScreenStyle(int screenStyle) {
        this.screenStyle = screenStyle;
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

    public String getCityPinyin(String city) {
        return citymap.get(city);
    }

    public interface OnStartMessageListener {
        /**
         * 收到开始信息
         *
         * @see ADD_START_STATUE
         */
        void receiveStartMessage();

        /**
         * 激活广告屏
         */
        void activateXsp();
    }
}
