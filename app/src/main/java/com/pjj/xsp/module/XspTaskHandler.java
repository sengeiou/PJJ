package com.pjj.xsp.module;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.pjj.xsp.db.DateTask;
import com.pjj.xsp.module.bean.ScreenPingTaskBean;
import com.pjj.xsp.module.bean.SpeedScreenBean;
import com.pjj.xsp.utils.Log;

import com.pjj.xsp.PjjApplication;
import com.pjj.xsp.db.HandleDaoDb;
import com.pjj.xsp.intent.http.RetrofitService;
import com.pjj.xsp.module.bean.ScreenTaskBean;
import com.pjj.xsp.receiver.AlarmReceiver;
import com.pjj.xsp.utils.DateUtils;
import com.pjj.xsp.utils.TextJsonUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

/**
 * Create by xinheng on 2018/11/16。
 * describe：广告屏任务处理
 */
public class XspTaskHandler {
    /**
     * 闹铃间隔时间
     * 1 小时
     */
    public static final int ALARM_DELAY = 3600 * 1000;
    //屏幕位置标识
    public static final String SCREEN_TITLE = "title";
    public static final String SCREEN_TITLE_10_LIST = "title_10_list";
    public static final String SCREEN_DIY_LIST = "screen_diy_list";
    public static final String SCREEN_CONTENT = "content";
    /**
     * 本地广告路径
     */
    public static final String LOCAL_PATH = "file:///android_asset/local3.jpg";
    //public static final String LOCAL_PATH = "android.resource://" + "com.tlw.xsp" + "/" + R.raw.localvideo;
    private static final String TAG = "TAG";

    private static XspTaskHandler INSTANCE;

    public static XspTaskHandler getInstance() {
        if (null == INSTANCE) {
            synchronized (XspTaskHandler.class) {
                if (null == INSTANCE) {
                    INSTANCE = new XspTaskHandler();
                }
            }
        }
        return INSTANCE;
    }

    private XspTaskHandler() {
    }


    public void getNextHourXspTask() {
        HashMap<String, ScreenTaskBean.DataBean> map = getHourTask(true);
        if (null != map) {
            handleTask(map);
            ScreenInfManage.getInstance().setMap(map);
        } else {
            if (null != onHandleTaskListener) {
                onHandleTaskListener.getXspTask();
            }
        }
    }

    /**
     * 处理获取的任务
     *
     * @param map
     */
    public void handleTask(HashMap<String, ScreenTaskBean.DataBean> map) {
        ScreenTaskBean.DataBean dataBean = map.get(SCREEN_CONTENT);
        if (null == dataBean) {
            dataBean = getLocalTaskDataBean();
            map.put(SCREEN_CONTENT, dataBean);
        } else {
            //TODO 再次下载，验证本地文件是在存在
            downloadTask(dataBean.getTempletList());
        }
    }

    /**
     * 获取现在时间点的任务
     *
     * @param next 是否是下一个时间点 true 是；false 不是，此时
     * @return
     */
    public HashMap<String, ScreenTaskBean.DataBean> getHourTask(boolean next) {
        String date = DateUtils.getNowDate();
        String hour = String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + (next ? 1 : 0));
        Log.e(TAG, "getHourTask: " + date + ", " + hour);
        //查询数据库
        return HandleDaoDb.queryEqLikeBeanByQueryBuilder(date, hour);
    }

    public void performTask() {
        if (null != onHandleTaskListener) {
            onHandleTaskListener.performTask();
        }
    }

    /**
     * 开启闹铃任务
     */
    public void startAlarm() {
        Calendar calendar = Calendar.getInstance();
        int nextHour = calendar.get(Calendar.HOUR_OF_DAY) + 1;
        int min = calendar.get(Calendar.MINUTE);
        int tag;
        if (min >= 40) {
            //获取下个时间节点的
            tag = 0;
            getNextHourXspTask();
        } else {
            tag = -60;
        }
        Log.e("TAG", "startTaskTime: " + nextHour);
        calendar.set(Calendar.HOUR_OF_DAY, nextHour);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long timeInMillis = calendar.getTimeInMillis();
        //执行任务闹铃
        startAlarm(timeInMillis, AlarmReceiver.TASK_TYPE);
        //40min以后 获取任务闹铃
        startAlarm(timeInMillis + (40L + tag) * 60 * 1000, AlarmReceiver.GET_TASK_TYPE);
    }

    private void startAlarm(long time, int type) {
        Intent intent = new Intent(PjjApplication.application, AlarmReceiver.class);
        intent.putExtra(AlarmReceiver.TASK_TYPE_NAME, type);
        intent.putExtra("time", time - ALARM_DELAY);
        intent.setAction(type == 0 ? "get_a_task" : "run_a_task");
        setNextAlarm(intent);
    }

    /**
     * 下一个时间节点闹铃
     *
     * @param intent
     */
    public void setNextAlarm(Intent intent) {
        long time = intent.getLongExtra("time", 0) + ALARM_DELAY;
        intent.putExtra("time", time);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(PjjApplication.application, 0, intent, FLAG_UPDATE_CURRENT);//可修改intent参数
        AlarmManager alarmManager = (AlarmManager) PjjApplication.application.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }

    public static void dealWithBMPingScreenTaskData(ScreenPingTaskBean data1) {
        List<ScreenPingTaskBean.DataBeanPin> data = data1.getData();
        HashMap<String, Object> map = new HashMap<>(1);
        map.put(SCREEN_TITLE_10_LIST, data);
        ScreenInfManage.getInstance().setMap(map);
        DateTask dateTask = data1.changeDateTask();
        HandleDaoDb.insertDateTask(dateTask);
    }

    public static void dealWithSpeedScreenTaskData(SpeedScreenBean data1) {
        if (data1 != null && TextJsonUtils.isNotEmpty(data1.getData())) {
            List<SpeedScreenBean.DataBean> data = data1.getData();
            if (TextJsonUtils.isNotEmpty(data)) {
                SpeedScreenBean.DataBean dataBean = data.get(0);
                if (dataBean != null) {
                    List<SpeedScreenBean.DataBean.ViewSizeBeanListBean> viewSizeBeanList = dataBean.getViewSizeBeanList();
                    if (TextJsonUtils.isNotEmpty(viewSizeBeanList)) {
                        for (int i = 0; i < viewSizeBeanList.size(); i++) {
                            SpeedScreenBean.DataBean.ViewSizeBeanListBean bean = viewSizeBeanList.get(i);
                            if (null != bean) {
                                String fileName = bean.getFileName();
                                RetrofitService.getInstance().downloadFile(ScreenInfManage.filePathMedia + fileName);
                            }
                        }
                        HashMap<String, Object> map = new HashMap<>(1);
                        map.put(SCREEN_DIY_LIST, data);
                        ScreenInfManage.getInstance().setMap(map);
                        DateTask dateTask = data1.changeDateTask();
                        HandleDaoDb.insertDateTask(dateTask);
                    }
                }
            }
        }
    }

    /**
     * 数据处理
     * 消除重复数据
     *
     * @param data1
     */
    public static void dealWithScreenTaskData(ScreenTaskBean data1) {
        List<ScreenTaskBean.DataBean> data = data1.getData();
        HashMap<String, Object> map = new HashMap<>(2);
        String orderId = null;
        if (null != data && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                ScreenTaskBean.DataBean dataBean = data.get(i);
                String orderType = dataBean.getOrderType();//订单类型
                if (null == orderType) {
                    continue;
                }
                if (TextUtils.isEmpty(orderId) && null != dataBean) {
                    orderId = dataBean.getOrderId();
                }
                switch (orderType) {
                    case "1"://DIY类型
                    case "3"://随机diy
                        map.put(SCREEN_CONTENT, dataBean);
                        break;
                    case "2"://便民信息
                        map.put(SCREEN_TITLE, dataBean);
                        break;
                    default:
                        //map.put(TlwApplication.SCREEN_CONTENT, getLocalTaskDataBean());
                        Log.e(TAG, "dealWithScreenTaskData: 无效信息--" + dataBean.toString());
                        continue;
                }
            }
            //数据库存储 2018年11月17日09:48:11
            ScreenTaskBean.DataBean dataBean = (ScreenTaskBean.DataBean) map.get(SCREEN_CONTENT);
            if (null == dataBean) {
                dataBean = getLocalTaskDataBean();
                map.put(SCREEN_CONTENT, dataBean);
            } else {
                if (!TextUtils.isEmpty(orderId)) {//订单有效
                    //数据库查询此订单，若没有则插入
                    ScreenTaskBean.DataBean bianmin = (ScreenTaskBean.DataBean) map.get(SCREEN_TITLE);
                    HandleDaoDb.insertOrder(bianmin.getOrderId(), bianmin, null, data1.getCurDate(), data1.getHour());
                    HandleDaoDb.insertOrder(dataBean.getOrderId(), null, dataBean.getTempletList(), data1.getCurDate(), data1.getHour());
                }
                downloadTask(dataBean.getTempletList());
            }
        } else {
            ScreenTaskBean.DataBean dataBean = getLocalTaskDataBean();
            map.put(SCREEN_CONTENT, dataBean);
        }
        ScreenInfManage.getInstance().setMap(map);
    }

    /**
     * 下载任务
     * diy
     *
     * @param templetList
     */
    public static void downloadTask(List<ScreenTaskBean.DataBean.TempletListBean> templetList) {
        if (null != templetList && templetList.size() > 0) {
            ScreenTaskBean.DataBean.TempletListBean templetListBean;
            List<ScreenTaskBean.DataBean.TempletListBean.FileListBean> fileList;
            ScreenTaskBean.DataBean.TempletListBean.FileListBean fileListBean;
            for (int i = 0; i < templetList.size(); i++) {
                templetListBean = templetList.get(i);
                if (null != templetListBean) {
                    fileList = templetListBean.getFileList();
                    if (null != fileList && fileList.size() > 0) {
                        for (int j = 0; j < fileList.size(); j++) {
                            fileListBean = fileList.get(j);
                            if (null != fileListBean) {
                                RetrofitService.getInstance().downloadFile(fileListBean.getFileUrl());
                            }
                        }
                    }
                }
            }
        }
    }

    private OnHandleTaskListener onHandleTaskListener;

    public void setOnHandleTaskListener(OnHandleTaskListener onHandleTaskListener) {
        this.onHandleTaskListener = onHandleTaskListener;
    }

    /**
     * 获取本地广告任务
     *
     * @return
     */
    public static ScreenTaskBean.DataBean getLocalTaskDataBean() {
        /*ScreenTaskBean.DataBean dataBean = new ScreenTaskBean.DataBean();
        dataBean.setOrderType("1");
        ScreenTaskBean.DataBean.TempletListBean templetListBean = new ScreenTaskBean.DataBean.TempletListBean();
        ScreenTaskBean.DataBean.TempletListBean.FileListBean fileListBean = new ScreenTaskBean.DataBean.TempletListBean.FileListBean();
        fileListBean.setFileUrl(LOCAL_PATH);
        fileListBean.setType("1");
        fileListBean.setFilePlace("1");
        fileListBean.setFileName("本地宣传片");

        ArrayList fileList = new ArrayList(1);
        fileList.add(fileListBean);
        templetListBean.setFileList(fileList);
        templetListBean.setTempletType("1");
        ArrayList templetList = new ArrayList(1);
        templetList.add(templetListBean);
        dataBean.setTempletList(templetList);*/
        return getLocalTaskDataBean("1", LOCAL_PATH);
    }

    public static ScreenTaskBean.DataBean getLocalTaskDataBean(String fileType, String fileUrl) {
        ScreenTaskBean.DataBean dataBean = new ScreenTaskBean.DataBean();
        dataBean.setOrderType("1");
        ScreenTaskBean.DataBean.TempletListBean templetListBean = new ScreenTaskBean.DataBean.TempletListBean();
        ScreenTaskBean.DataBean.TempletListBean.FileListBean fileListBean = new ScreenTaskBean.DataBean.TempletListBean.FileListBean();
        fileListBean.setFileUrl(fileUrl);
        //fileListBean.setType("1");图片
        fileListBean.setType(fileType);
        fileListBean.setFilePlace("1");
        String name = "插播";
        if (fileUrl.contains("file")) {
            name = "本地宣传片";
        }
        fileListBean.setFileName(name);
        ArrayList fileList = new ArrayList(1);
        fileList.add(fileListBean);
        templetListBean.setFileList(fileList);
        //templetListBean.setTempletType("1");
        templetListBean.setTempletType(fileType);
        ArrayList templetList = new ArrayList(1);
        templetList.add(templetListBean);
        dataBean.setTempletList(templetList);
        return dataBean;
    }

    public interface OnHandleTaskListener {
        /**
         * 获取任务
         */
        void getXspTask();

        /**
         * 执行任务
         */
        void performTask();
    }
}
