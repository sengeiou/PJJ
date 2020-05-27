package com.pjj.xsp.module;

import android.text.TextUtils;

import com.pjj.xsp.BuildConfig;
import com.pjj.xsp.PjjApplication;
import com.pjj.xsp.db.DateTask;
import com.pjj.xsp.db.DateTaskDao;
import com.pjj.xsp.db.HandleDaoDb;
import com.pjj.xsp.db.OrderTask;
import com.pjj.xsp.db.OrderTaskDao;
import com.pjj.xsp.intent.http.RetrofitService;
import com.pjj.xsp.manage.XSPSystem;
import com.pjj.xsp.module.bean.ScreenTaskBean;
import com.pjj.xsp.utils.DateUtils;
import com.pjj.xsp.utils.FileUtils;
import com.pjj.xsp.utils.Log;
import com.pjj.xsp.utils.TextViewUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Create by xinheng on 2018/11/16。
 * describe：广告屏播放页面操作类
 */
public class XspPlayUI {
    private static XspPlayUI INSTALL;
    private String orderId;

    public static XspPlayUI getInstall() {
        if (null == INSTALL) {
            synchronized (XspPlayUI.class) {
                if (null == INSTALL) {
                    INSTALL = new XspPlayUI();
                }
            }
        }
        return INSTALL;
    }

    private XspPlayUI() {
    }

    /**
     * 更新维保信息
     */
    public void updateWeiBao() {
        if (isNotNull())
            onPlayViewListener.updateWeiBao();
    }

    /**
     * 设置音量
     *
     * @param volume
     */
    public void setVolume(String volume) {
        if (isNotNull()) {
            int volume1 = -1;
            try {
                volume1 = Integer.parseInt(volume);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            if (volume1 > -1)
                onPlayViewListener.setVolume(volume1);
        }
    }

    /**
     * 撤播
     *
     * @param orderId 订单号
     */
    public void stopPlayOrder(String orderId) {
        if (TextUtils.isEmpty(orderId)) {//无效订单
            return;
        }
        if (orderId.equals(this.orderId)) {//已处理或正在处理
            return;
        }
        this.orderId = orderId;
        //查询数据库 order
        List<OrderTask> orderTasks = HandleDaoDb.queryEqBeanByQueryBuilder(OrderTask.class, orderId, OrderTaskDao.Properties.OrderId);
        if (null != orderTasks && orderTasks.size() > 0) {
            HandleDaoDb.deleteBean(orderTasks.get(0));
        }
        //查询数据库 date
        List<DateTask> dateTasks = HandleDaoDb.queryEqBeanByQueryBuilder(DateTask.class, orderId, DateTaskDao.Properties.OrderId);
        if (null != dateTasks && dateTasks.size() > 0) {
            HandleDaoDb.delateListBean(dateTasks);
        }
        if (deleteOrderForMap(orderId, ScreenInfManage.getInstance().getMapNow()) && isNotNull()) {//当前播放任务
            onPlayViewListener.stopPlay();
        }
        deleteOrderForMap(orderId, ScreenInfManage.getInstance().getMap());
    }

    private boolean deleteOrderForMap(String orderId, HashMap map) {
        if (null != map) {
            ScreenTaskBean.DataBean diy = (ScreenTaskBean.DataBean) map.get(XspTaskHandler.SCREEN_CONTENT);
            ScreenTaskBean.DataBean bianmin = (ScreenTaskBean.DataBean) map.get(XspTaskHandler.SCREEN_TITLE);
            boolean change = false;
            if (null != diy && orderId.equals(diy.getOrderId())) {
                map.put(XspTaskHandler.SCREEN_CONTENT, XspTaskHandler.getLocalTaskDataBean());
                change = true;
            }
            if (null != bianmin && orderId.equals(bianmin.getOrderId())) {
                map.put(XspTaskHandler.SCREEN_TITLE, null);
                change = true;
            }
            return change;
        }
        return false;
    }

    /**
     * 重置，未激活状态
     */
    public void reset() {
        if (isNotNull()) {
            onPlayViewListener.reset();
        }
    }

    private OnPlayViewListener onPlayViewListener;

    private boolean isNotNull() {
        return null != onPlayViewListener;
    }

    public void setOnPlayViewListener(OnPlayViewListener onPlayViewListener) {
        this.onPlayViewListener = onPlayViewListener;
    }

    private String orderId_searchTask;

    public void searchTask(String orderId) {
        if (orderId.equals(orderId_searchTask)) {
            return;
        } else {
            orderId_searchTask = orderId;
            if (!TextViewUtils.isEmpty(orderId) && isNotNull()) {
                onPlayViewListener.searchTask(orderId);
            }
        }
    }

    public void reboot() {
        XSPSystem.getInstance().reboot();
    }

    /**
     * 升级
     *
     * @param type    type:1 ->正式版 type:2 ->测试版
     * @param appName
     */
    public void updateApp(String type, String appName) {
        Log.e("TAG", "updateApp: type=" + type + ", " + appName);
        if (("2".equals(type) && BuildConfig.DEBUG) || ("1".equals(type) && !BuildConfig.DEBUG)) {
            String updatePath = ScreenInfManage.filePath + appName;
            RetrofitService.getInstance().downloadFile(updatePath, new FileUtils.OnDownloadListener() {
                @Override
                public void success() {
                    //File file = new File(TlwApplication.App_Path + appName);
                    XSPSystem.getInstance().installApp(PjjApplication.App_Path + appName);
                }

                @Override
                public void fail() {
                    Log.e("TAG", "app download fail: ");
                }
            });
        }
    }

    /**
     * 插播
     *
     * @param fileType
     * @param drumbeatingName
     */
    public void insertAd(String fileType, String drumbeatingName) {
        //下载
        String url = ScreenInfManage.filePathMedia + drumbeatingName;
        Log.e("TAG", "insertAd 插播: " + url);
        RetrofitService.getInstance().downloadFileRe(url, new FileUtils.OnDownloadListener() {
            @Override
            public void success() {
                Log.e("TAG", "success: 下载成功");
                //构建播放数据
                ScreenTaskBean.DataBean dataBean = XspTaskHandler.getLocalTaskDataBean(fileType, PjjApplication.App_Path + drumbeatingName);
                Calendar calendar = Calendar.getInstance(Locale.SIMPLIFIED_CHINESE);
                int min = calendar.get(Calendar.MINUTE);
                HashMap mapNow = ScreenInfManage.getInstance().getMapNow();
                if (null == mapNow) {
                    mapNow = new HashMap<>(1);
                }
                mapNow.put(XspTaskHandler.SCREEN_CONTENT, dataBean);
                if (min >= 50) {//当前播放，且下个小时继续播放
                    HashMap<String, Object> map = ScreenInfManage.getInstance().getMap();
                    if (null == map) {
                        map = new HashMap<>(1);
                    }
                    map.put(XspTaskHandler.SCREEN_CONTENT, dataBean);
                } else {//仅当前播放
                    //无其他意义，仅说明
                }
                if (isNotNull()) {
                    onPlayViewListener.playNow();
                }
            }

            @Override
            public void fail() {
                //TODO 插播下载失败，可能需要通知服务器，暂不处理
                Log.e("TAG", "fail: 下载失败");
            }
        });
    }

    public void updateDefaultBm() {
        if (isNotNull()) {
            onPlayViewListener.updateDefaultBm();
        }
    }

    public void screenshots(String name) {
        if (isNotNull()) {
            onPlayViewListener.screenshots(name);
        }
    }

    public interface OnPlayViewListener {
        /**
         * 更新维保信息
         */
        void updateWeiBao();

        /**
         * 设置音量
         *
         * @param volume
         */
        void setVolume(int volume);

        /**
         * 撤播
         */
        void stopPlay();

        /**
         * 重置，未激活状态
         */
        void reset();

        /**
         * 获取订单任务
         *
         * @param orderId 订单id
         */
        void searchTask(String orderId);

        /**
         * 立刻播放
         */
        void playNow();

        /**
         * 更新默认便民
         */
        void updateDefaultBm();

        /**
         * 截屏
         *
         * @param name 文件名字
         */
        void screenshots(String name);
    }
}
