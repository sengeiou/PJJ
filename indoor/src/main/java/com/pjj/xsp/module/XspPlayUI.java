package com.pjj.xsp.module;

import android.text.TextUtils;

import com.pjj.xsp.BuildConfig;
import com.pjj.xsp.PjjApplication;

import com.pjj.xsp.db.DateTask;
import com.pjj.xsp.db.HandleDaoDb;
import com.pjj.xsp.db.MinePlayTask;
import com.pjj.xsp.intent.http.RetrofitService;
import com.pjj.xsp.manage.XSPSystem;
import com.pjj.xsp.module.bean.ScreenInfBean;
import com.pjj.xsp.utils.DateUtils;
import com.pjj.xsp.utils.FileUtils;
import com.pjj.xsp.utils.Log;
import com.pjj.xsp.utils.SharedUtils;
import com.pjj.xsp.utils.TaskTimerUtils;
import com.pjj.xsp.utils.TextViewUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Create by xinheng on 2018/11/16。
 * describe：广告屏播放页面操作类，大部分与极光相关
 */
public class XspPlayUI {
    private static XspPlayUI INSTALL;
    private List<PlayTaskParent> playTaskParents = new ArrayList<>(30);
    private int taskIndex = -1;
    private TaskTimerUtils taskTimerUtils = new TaskTimerUtils();
    public PlayTaskParent defaultTask = new PlayTaskParent() {
        @Override
        public String getTempletType() {
            return "1";
        }

        @Override
        public String getVideoPath() {
            return null;
        }

        @Override
        public String getFilePath() {
            return XspTaskHandler.LOCAL_PATH;
        }

        @Override
        public String getFileType() {
            return "1";
        }

        @Override
        public int getPlayTime() {
            return -1;
        }

        @Override
        public String getTaskTag() {
            return "local";
        }

        @Override
        public boolean deleteTag() {
            return false;
        }
    };

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
        taskTimerUtils.setOnTaskTimerListener(() -> startNextTask());
    }

    /**
     * 查询并设置下一个播放任务
     */
    public void startNextTask() {
        taskIndex++;
        PlayTaskParent playTaskParent;
        if (playTaskParents.size() == 0) {
            playTaskParent = defaultTask;
            //恢复最初标识
            taskIndex = -1;
        } else {
            if (taskIndex >= playTaskParents.size()) {
                taskIndex = 0;
            }
            //Log.e("TAG", "startNextTask taskIndex=" + taskIndex);
            playTaskParent = playTaskParents.get(taskIndex);
            if (taskIndex == 0) {
                checkDownload(playTaskParent);
            }
            checkDownload(getNextTask());
        }
        //设置当前任务
        ScreenInfManage.getInstance().setNowTask(playTaskParent);
        //播放当前任务
        XspTaskHandler.getInstance().performTask();
        //一定时间后播放下一个任务
        taskTimerUtils.setPlayTime(playTaskParent.getPlayTime()).start();
    }

    private void checkDownload(PlayTaskParent playTaskParent) {
        if (null == playTaskParent) {
            return;
        }
        if (!TextUtils.isEmpty(playTaskParent.getVideoPath())) {
            RetrofitService.getInstance().downloadFile(ScreenInfManage.filePathMedia + playTaskParent.getVideoPath());
        }
        RetrofitService.getInstance().downloadFile(ScreenInfManage.filePathMedia + playTaskParent.getFilePath());
    }

    /**
     * 必须是视频才返回
     * 为了预加载图片
     *
     * @return
     */
    public PlayTaskParent getNextTask() {
        if (taskIndex == -1) {
            return null;
        }
        int index = taskIndex + 1;
        if (index >= playTaskParents.size()) {
            index = 0;
        }
        if (taskIndex == index) {
            return null;
        }
        PlayTaskParent playTaskParent = playTaskParents.get(index);
        /*if ("2".equals(playTaskParent.getFileType())) {
            return playTaskParent;
        }*/
        return playTaskParent;
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
        MediaTaskCacheHelp.getInstance().deleteOrder(orderId);
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

    public void downloadFile(String path, String fileName, String code) {
        if (isNotNull()) {
            onPlayViewListener.downloadFile(path, fileName, code);
        }
    }

    public void searchTask(String orderId) {
        ScreenInfBean.DataBean screenInfDataBean = ScreenInfManage.getInstance().getScreenInfDataBean();
        if (null != screenInfDataBean) {
            String cooperationMode = screenInfDataBean.getCooperationMode();
            if ("4".equals(cooperationMode)) {
                return;
            }
        }
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
        if (TextUtils.isEmpty(type)) {
            type = "1";
        }
        Log.e("TAG", "updateApp: type=" + type + ", " + appName);
//        if (("2".equals(type) && BuildConfig.DEBUG) || ("1".equals(type) && !BuildConfig.DEBUG)) {
        if ("2".equals(type) || "1".equals(type)) {
            String updatePath = ScreenInfManage.filePath + appName;
            SharedUtils.saveForXml(SharedUtils.VERSION_UPDATE, "下载");
            RetrofitService.getInstance().downloadFile(updatePath, new FileUtils.OnDownloadListener() {
                @Override
                public void success() {
                    //File file = new File(TlwApplication.App_Path + appName);
                    try {
                        //SharedUtils.saveForXml(SharedUtils.VERSION_UPDATE, "开始升级");
                        SharedUtils.saveForXml(SharedUtils.VERSION_UPDATE, appName);
                        XSPSystem.getInstance().installApp(PjjApplication.App_Path + appName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
                //ScreenTaskBean.DataBean dataBean = XspTaskHandler.getLocalTaskDataBean(fileType, PjjApplication.App_Path + drumbeatingName);
                Calendar calendar = Calendar.getInstance(Locale.SIMPLIFIED_CHINESE);
                int min = calendar.get(Calendar.MINUTE);

                //mapNow.put(XspTaskHandler.SCREEN_CONTENT, dataBean);
                if (min >= 50) {//当前播放，且下个小时继续播放
                    //map.put(XspTaskHandler.SCREEN_CONTENT, dataBean);
                } else {//仅当前播放
                    //无其他意义，仅说明
                }
                if (isNotNull()) {
                    //onPlayViewListener.playNow();
                }
            }

            @Override
            public void fail() {
                //TODO 插播下载失败，可能需要通知服务器，暂不处理
                Log.e("TAG", "fail: 下载失败");
            }
        });
    }

    public void screenshots(String name) {
        if (isNotNull()) {
            onPlayViewListener.screenshots(name);
        }
    }

    /**
     * 添加任务
     * 注：文件下载好后，添加
     *
     * @param taskParent
     */
    public void addLinkListTask(PlayTaskParent taskParent) {
        Log.e("TAG", "addLinkListTask: add=" + taskParent.getFilePath());
        //int size = playTaskParents.size();
        playTaskParents.add(taskParent);
        if (taskIndex == -1) {
            startNextTask();
        }/* else if (taskIndex == size - 1) {
            taskIndex = playTaskParents.size() - 1;
        }*/
    }

    public void clearNowLink() {
        taskTimerUtils.clear();
        playTaskParents.clear();
        taskIndex = -1;
        //startNextTask();
    }

    public void addMineTask(MinePlayTask taskText) {
        ScreenInfBean.DataBean screenInfDataBean = ScreenInfManage.getInstance().getScreenInfDataBean();
        if (null != screenInfDataBean) {
            String cooperationMode = screenInfDataBean.getCooperationMode();
            if (!"4".equals(cooperationMode)) {
                return;
            }
        }
        if (isNotNull()) {
            onPlayViewListener.addMineTask(taskText);
        }
    }

    public void stopOrRecoverStatue(boolean turnOff) {
        if (PjjApplication.application.isFlag() != turnOff) {
            SharedUtils.saveForXml(SharedUtils.STOP_RECOVER, turnOff ? "1" : "0");
            PjjApplication.application.setFlag(turnOff);
            if (turnOff) {
                if (null != onPlayViewListener) {
                    onPlayViewListener.playNow();
                }
            } else {
                MediaTaskCacheHelp.getInstance().suspendedClearTask();
            }
        }

    }

    public void controllerHeadViewShowOrHidden(boolean flag) {
        if (isNotNull()) {
            String tag = SharedUtils.getXmlForKey(SharedUtils.HEAD_VIEW_SHOW_TAG);
            if (flag == getTagBoolean(tag)) {
                return;
            }
            SharedUtils.saveForXml(SharedUtils.HEAD_VIEW_SHOW_TAG, flag ? "1" : "0");
            onPlayViewListener.controllerHeadViewShowOrHidden(flag);
        }
    }

    public static boolean getTagBoolean(String tag) {
        if (null == tag || "1".equals(tag))
            return true;
        return false;
    }

    public void insertJson(String json) {
        if (isNotNull()) {
            onPlayViewListener.insertJson(json);
        }
    }

    public void uploadFile() {
        if (isNotNull()) {
            onPlayViewListener.uploadFile();
        }
    }

    public interface OnPlayViewListener {
        void controllerHeadViewShowOrHidden(boolean flag);

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
         * 截屏
         *
         * @param name
         */
        void screenshots(String name);

        void addMineTask(MinePlayTask taskText);

        /**
         * 下载文件
         *
         * @param path     路径
         * @param fileName 文件名
         * @param code     用途
         */
        void downloadFile(String path, String fileName, String code);

        void insertJson(String json);

        void uploadFile();
    }
}
