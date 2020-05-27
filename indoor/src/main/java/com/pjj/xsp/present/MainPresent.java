package com.pjj.xsp.present;

import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;

import com.pjj.xsp.BuildConfig;
import com.pjj.xsp.PjjApplication;
import com.pjj.xsp.contract.MainContract;

import com.pjj.xsp.db.DaoManager;
import com.pjj.xsp.db.DateTask;
import com.pjj.xsp.db.DateTaskDao;
import com.pjj.xsp.db.HandleDaoDb;
import com.pjj.xsp.db.MinePlayTask;
import com.pjj.xsp.db.MinePlayTaskDao;
import com.pjj.xsp.db.TaskText;
import com.pjj.xsp.db.TaskTextDao;
import com.pjj.xsp.intent.http.AliFile;
import com.pjj.xsp.intent.http.AppendUrlParamInterceptor;
import com.pjj.xsp.intent.http.RetrofitService;

import com.pjj.xsp.manage.XSPSystem;
import com.pjj.xsp.module.JsonObjectBean;
import com.pjj.xsp.module.MediaTaskCacheHelp;
import com.pjj.xsp.module.PlayTaskParent;
import com.pjj.xsp.module.ScreenInfManage;

import com.pjj.xsp.module.XspPlayUI;
import com.pjj.xsp.module.bean.HeartTaskBean;
import com.pjj.xsp.module.bean.ResultBean;
import com.pjj.xsp.module.bean.ScreenInfBean;

import com.pjj.xsp.module.bean.WeatherBean;
import com.pjj.xsp.module.parameter.ActivateStatue;

import com.pjj.xsp.module.parameter.ScreenOrderTask;
import com.pjj.xsp.module.parameter.ScreenshotsBean;

import com.pjj.xsp.module.parameter.UploadTakeScreen;
import com.pjj.xsp.receiver.JPushReceiver;
import com.pjj.xsp.utils.FileUtils;
import com.pjj.xsp.utils.Log;
import com.pjj.xsp.utils.SharedUtils;
import com.pjj.xsp.utils.TextJsonUtils;
import com.pjj.xsp.utils.TextViewUtils;
import com.pjj.xsp.view.MainViewHelp;

import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Create by xinheng on 2018/11/01。
 * describe：
 */
public class MainPresent extends BasePresent<MainContract.View> implements MainContract.Present {
    private Handler mainHandler;
    private SaveCodeTaskThread childHandler;
    private String pingYin;
    private String cityName;
    private Timer timer;
    private String taskJson;

    public MainPresent(MainContract.View view) {
        super(view);
        mainHandler = new Handler(Looper.getMainLooper());
        childHandler = new SaveCodeTaskThread("performTask");
        childHandler.start();
        childHandler.getHandler();
    }

    @Override
    public void initHeartJson() {
        taskJson = "{\"screenId\":\"" + XSPSystem.getInstance().getOnlyCode() + "\",\"screenType\":\"" + (AppendUrlParamInterceptor.elevatorTag ? "elevScreen" : "mediaScreen") + "\"}";
    }

    @Override
    public void loadCityName() {
        //getCityName();
        if (/*TextUtils.isEmpty(pingYin) || */TextUtils.isEmpty(cityName)) {
            loadWeiBao();
        } else {
            loadXianXingNum(pingYin);
            loadCityWeather(cityName);
        }
    }

    @Override
    public void loadCityWeather(String cityName) {
        getRetrofitService().getDayWeather(cityName, new RetrofitService.CallbackClass<WeatherBean>(WeatherBean.class) {

            @Override
            protected void result(WeatherBean weatherBean) {
                String status = weatherBean.getStatus();
//                try {
//                    FileUtils.saveStringFile(PjjApplication.App_Path + "weather.txt", TextJsonUtils.toJsonString(weatherBean));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                if ("success".equals(status)) {
                    List<WeatherBean.ResultsBean> results = weatherBean.getResults();
                    if (null != results && results.size() > 0) {
                        WeatherBean.ResultsBean resultsBean = results.get(0);
                        if (null != resultsBean) {
                            List<WeatherBean.ResultsBean.WeatherDataBean> weather_data = resultsBean.getWeather_data();
                            if (null != weather_data && weather_data.size() > 0) {
                                mView.updateWeatherView(weather_data);
                            }
                        }
                    }
                }
            }

            @Override
            protected void fail(String error) {
                Log.e("TAG", "天气 fail: " + error);
                FileUtils.saveStringFile(PjjApplication.App_Path + "weather.txt", "天气 fail: " + error);
                childHandler.postDelayed(() -> loadCityWeather(cityName), 2000);
            }
        });
    }

    @Override
    public void loadXianXingNum(String localName) {
        if (TextViewUtils.isEmpty(localName)) {
            mView.updateXianXingNum(null);
            return;
        }
        /*getRetrofitService().getCarXianXingNum(localName, new RetrofitService.MyCallback() {
            @Override
            protected void success(String s) {
                if (isViewNotNull()) {
                    int i1 = s.indexOf("今天(");
                    int i2 = s.indexOf("明天(");
                    String substring = s.substring(i1, i2);
                    Log.e("TAG", "success: " + substring);
                    FileUtils.saveStringFile(PjjApplication.App_Path + "carNum.txt", substring);
                    int i = substring.indexOf("\">") + 2;
                    int i3 = substring.lastIndexOf("</div>");
                    String substring1 = substring.substring(i, i3);
                    String replace = substring1.replace("<span>", "").replace("</span>", "").replace(" ", "");
                    String[] nums = replace.split("和");
                    if (nums != null && nums.length == 2) {
                        mView.updateXianXingNum(nums);
                    } else {
                        mView.updateXianXingNum(null);
                    }
                }
            }

            @Override
            protected void fail(String error) {
                //loadXianXingNum(localName);
                FileUtils.saveStringFile(PjjApplication.App_Path + "carNum.txt", "weather-" + error);
                Log.e("TAG", "限号 fail: " + error);
                if ("错误".equals(error)) {
                    childHandler.postDelayed(() -> loadXianXingNum(localName), 2000);
                }
                if (isViewNotNull())
                    mView.updateXianXingNum(null);
            }
        });*/
    }

    @Override
    public void loadXspNextTask() {

    }

    @Override
    public void loadWeiBao() {
        startTask();
        getRetrofitService().getScreenInf(ScreenInfManage.getInstance().getScreenInfTag(), new RetrofitService.CallbackClassResult<ScreenInfBean>(ScreenInfBean.class) {
            @Override
            protected void resultSuccess(ScreenInfBean screenInfBean) {
                List<ScreenInfBean.DataBean> data = screenInfBean.getData();
                if (null != data && data.size() > 0) {
                    ScreenInfBean.DataBean dataBean = data.get(0);
                    if (!dataBean.isActivate()) {//未激活
                        mView.setUnActivateResult(true);
                        return;
                    }
                    if (TextUtils.isEmpty(cityName) || TextUtils.isEmpty(pingYin)) {
                        pingYin = dataBean.getAreaCode();
                        cityName = dataBean.getAreaName();
                        //if (!BuildConfig.DEBUG)
                        mView.updateLocalCityName(cityName);
                        loadCityName();
                    }
                    String cooperationMode = dataBean.getCooperationMode();
                    ScreenInfBean.DataBean screenInfDataBean = ScreenInfManage.getInstance().getScreenInfDataBean();
                    if (null == screenInfDataBean) {
                        screenInfDataBean = new ScreenInfBean.DataBean();
                    }
                    boolean same = equalsString(cooperationMode, screenInfDataBean.getCooperationMode());
                    ScreenInfManage.getInstance().setScreenInfDataBean(dataBean);
                    Log.e("TAG", "same=" + same);
                    if (!same) {
                        Database db = DaoManager.getInstance().getDaoMaster().getDatabase();
                        DaoManager.getInstance().getDaoSession();
                        if ("4".equals(cooperationMode)) {//自用
                            DateTaskDao.dropTable(db, true);
                            TaskTextDao.dropTable(db, true);
                            MinePlayTaskDao.createTable(db, true);
                        } else {
                            MinePlayTaskDao.dropTable(db, true);
                            TaskTextDao.createTable(db, true);
                            DateTaskDao.createTable(db, true);
                        }
                        mView.reStart(cooperationMode);
                    }

                    MainViewHelp.getInstance().updateWeiBao();
                    if (TextUtils.isEmpty(taskJson)) {
                        initHeartJson();
                        heartInternet();
                    }
                    //startTask();
                }
            }

            @Override
            protected void fail(String error) {
                Log.e("TAG", "维保 fail: " + error);
                if (null != childHandler) {
                    childHandler.postDelayed(() -> loadWeiBao(), 3000);
                }
            }
        });
    }

    private boolean equalsString(String s, String s1) {
        Log.e("TAG", "equalsString: s=" + s + ", s1=" + s1);
        if (null == s && null == s1) {
            return true;
        }
        if (null != s && s.equals(s1)) {
            return true;
        }
        return false;
    }

    @Override
    public void loadUnActivateStatue() {
        ActivateStatue.setActivateScreenStatue(ActivateStatue.createUnActivateBean(), new ActivateStatue.OnActivateScreenStatueListener() {
            @Override
            public void success() {
                if (isViewNotNull()) {
                    mView.setUnActivateResult(true);
                }
            }

            @Override
            public void fail(String error) {
                Log.e("TAG", "loadUnActivateStatue-fail: " + error);
                if (isViewNotNull()) {
                    childHandler.postDelayed(() -> loadUnActivateStatue(), 1000);
                }
            }
        });
    }

    @Override
    public void loadSearchTaskForOrderId(String orderId) {

    }

    private void getTaskFail(String error) {

    }

    @Override
    public void uploadScreenshotsName(ScreenshotsBean screenshotsBean) {
        getRetrofitService().uploadScreenshotsName(screenshotsBean, new RetrofitService.CallbackClassResult<ResultBean>(ResultBean.class) {
            @Override
            protected void resultSuccess(ResultBean resultBean) {
                Log.e("TAG", "resultSuccess截屏 : 成功");
            }

            @Override
            protected void fail(String error) {
                android.util.Log.e("TAG", "fail 截屏: " + error);
            }
        });
    }

    @Override
    public void loadLunBoTaskForOrderId(String order) {
        ScreenOrderTask screenOrderTask = new ScreenOrderTask();
        screenOrderTask.setScreenId(XSPSystem.getInstance().getOnlyCode());
        screenOrderTask.setOrderId(order);
        getRetrofitService().loadSearchTaskForOrderId(screenOrderTask, new RetrofitService.MyCallback() {
            @Override
            protected void success(String s) {
                loadOrderResult(order, "1");
                dealData(s);
            }

            @Override
            protected void fail(String error) {
                childHandler.postDelayed(() -> loadLunBoTaskForOrderId(order), 2000);
            }
        });
    }

    @Override
    public void codeTaskFeedback(String json) {
        getRetrofitService().updateScreenCommandStatus(json, new RetrofitService.CallbackClass<ResultBean>(ResultBean.class) {
            @Override
            protected void result(ResultBean resultBean) {

            }

            @Override
            protected void fail(String error) {

            }
        });
    }

    private void loadOrderResult(String orderId, String putStatus) {
        getRetrofitService().ackOrderScreen(orderId, XSPSystem.getInstance().getOnlyCode(), putStatus, new RetrofitService.MyCallback() {
            @Override
            protected void success(String s) {
                Log.e("TAG", "success: " + s);
            }

            @Override
            protected void fail(String error) {
                Log.e("TAG", "fail: " + error);
            }
        });
    }

    private void dealData(String s) {
        JsonObjectBean objectBean = new JsonObjectBean(s);
        TaskText taskText = objectBean.getTaskText();
        boolean limitTag = objectBean.isLimitTag();
        if (null != taskText) {
            String text = taskText.getText();
            if (TextUtils.isEmpty(text)) {
                return;
            }
            downloadTask(objectBean.isNowDay(), taskText, limitTag);
        }
    }

    @Override
    public void addMineTask(MinePlayTask task) {
        PlayTaskParent playTaskParent = task.getPlayTaskParent();
        if (null != playTaskParent) {
            loadOrderResult(task.getOrderId(), "1");
            if (!checkRepeat(task.getOrderId(), MinePlayTask.class, MinePlayTaskDao.Properties.OrderId)) {
                HandleDaoDb.insertBean(task, DaoManager.getInstance().getDaoSession().getMinePlayTaskDao());
                downloadFile(playTaskParent, playTaskParent.getFilePath(), true);
            }
        }
    }

    private <T> boolean checkRepeat(String order, Class<T> cls, Property property) {
        List taskTexts = null;
        try {
            taskTexts = HandleDaoDb.queryEqBeanByQueryBuilder(cls, order, property);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (TextJsonUtils.isNotEmpty(taskTexts)) {
            return true;
        }
        return false;
    }

    private void downloadTask(boolean tag, TaskText taskText, boolean limitTag) {
        Log.e("TAG", "downloadTask: isNowDay=" + tag);
        PlayTaskParent playTaskParent = taskText.getPlayTaskParent();
        if (null != playTaskParent) {
            List<TaskText> taskTexts = HandleDaoDb.queryEqBeanByQueryBuilder(TaskText.class, taskText.getOrderId(), TaskTextDao.Properties.OrderId);
            Long raw;
            if (TextJsonUtils.isNotEmpty(taskTexts)) {
                raw = taskTexts.get(0).getId();
            } else {
                raw = HandleDaoDb.insertBean(taskText, DaoManager.getInstance().getDaoSession().getTaskTextDao());
            }
            List<DateTask> dateTaskList = taskText.getDateTaskList();
            List<DateTask> dateTaskListSave = new ArrayList<>();
            if (TextJsonUtils.isNotEmpty(dateTaskList)) {
                Iterator<DateTask> iterator = dateTaskList.iterator();
                while (iterator.hasNext()) {
                    DateTask dateTask = iterator.next();
                    dateTask.setId(raw);
                    dateTaskListSave.add(dateTask);
                    if (HandleDaoDb.checkDateTask(dateTask.getDate(), raw.toString())) {
                        iterator.remove();
                    }
                }
            }
            if (TextJsonUtils.isNotEmpty(dateTaskList)) {
                HandleDaoDb.insertListBean(dateTaskList);
            }
            if (limitTag) {
                for (int i = 0; i < dateTaskListSave.size(); i++) {
                    DateTask dateTask = dateTaskListSave.get(i);
                    //下载标识
                    MediaTaskCacheHelp.getInstance().addLimitTask(dateTask);
                }
            }

            new DownloadPlayTaskParentHelp(mainHandler, playTaskParent, () -> {
                loadOrderResult(playTaskParent.getTaskTag(), "2");
                if (tag) {
                    if (limitTag) {
                        for (int i = 0; i < dateTaskListSave.size(); i++) {
                            DateTask dateTask = dateTaskListSave.get(i);
                            MediaTaskCacheHelp.getInstance().addLimitTask(dateTask);
                        }
                    } else {
                        if (PjjApplication.application.isFlag())
                            MediaTaskCacheHelp.getInstance().addTaskAndCheckStart(playTaskParent);
                    }
                }
            }).downloadFile();
        }
    }

    private void downloadFile(PlayTaskParent taskParent, String path, boolean addTag) {
        //Log.e("TAG", "downloadFile: addTag=" + addTag + ", flag=" + PjjApplication.application.isFlag());
        FileUtils.OnDownloadListener onDownloadListener = new FileUtils.OnDownloadListener() {
            @Override
            public void success() {
                if (addTag && PjjApplication.application.isFlag()) {
                    loadOrderResult(taskParent.getTaskTag(), "2");
                    childHandler.post(() -> MediaTaskCacheHelp.getInstance().addTaskAndCheckStart(taskParent));
                }
            }

            @Override
            public void fail() {
                //2秒后重新下载
                Log.e("TAG", "redownload path=" + path + ", addTag=" + addTag);
                childHandler.postDelayed(() -> downloadFile(taskParent, path, addTag), 2000);
            }
        };
        getRetrofitService().downloadFile(ScreenInfManage.filePathMedia + path, onDownloadListener);
    }

    @Override
    public void loadTakeScreenResult(String orderId, String error) {
        UploadTakeScreen screen = new UploadTakeScreen();
        screen.setOrderId(orderId);
        screen.setMsg(error);
        screen.setScreenId(XSPSystem.getInstance().getOnlyCode());
        getRetrofitService().receiveScreenImgInfo(screen, new RetrofitService.MyCallback() {
            @Override
            protected void success(String s) {
                Log.e("TAG", "success :" + s);
            }

            @Override
            protected void fail(String error) {
                Log.e("TAG", "fail :" + error);
            }
        });
    }

    private void startTask() {
        if (null == timer) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    heartbeat();
                }
            }, 100, 10000);
        }
    }

    private int count;

    private void heartbeat() {
        if (count % 2 == 0) {
            mView.checkOnLine();
        }
        //Log.e("TAG", "heartbeat");
        if (count >= 60 || count == 0) {//10min
            count = 0;
            heartInternet();
        }
        ++count;
        XSPSystem.getInstance().feedWatchDog();
    }

    private void heartInternet() {
        if (!TextUtils.isEmpty(taskJson)) {
            getRetrofitService().receiveScreenImgInfo(taskJson, new RetrofitService.CallbackClassResult<HeartTaskBean>(HeartTaskBean.class) {
                @Override
                protected void resultSuccess(HeartTaskBean heartTaskBean) {
                    if (TextJsonUtils.isNotEmpty(heartTaskBean.getScreenCommandList())) {
                        for (int i = 0; i < heartTaskBean.getScreenCommandList().size(); i++) {
                            String json = heartTaskBean.getScreenCommandList().get(i);
                            childHandler.post(json);
                        }
                    }
                    if (TextJsonUtils.isNotEmpty(heartTaskBean.getOrderIdList())) {
                        List<String> strings = HandleDaoDb.checkTask(heartTaskBean.getOrderIdList());
                        if (TextJsonUtils.isNotEmpty(strings)) {
                            for (int i = 0; i < strings.size(); i++) {
                                String s = strings.get(i);
                                loadLunBoTaskForOrderId(s);
                            }
                        }
                    }
                }

                @Override
                protected void fail(String error) {
                    Log.e("TAG", "fail: " + error);
                }
            });
        }
    }

    @Override
    public void downloadFile(String path, String fileName, String code) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        String downloadPath;
        if (!path.startsWith("http://") && !path.contains("https://")) {
            downloadPath = ScreenInfManage.filePath + path;
        } else {
            downloadPath = path;
        }
        String filePath;
        if (XSPSystem.getInstance().getSystemUpdateFileName().equals(fileName) && (BuildConfig.USE_TYPE.contains("ad_sh_ys_m1") || BuildConfig.USE_TYPE.contains("ad_m10_3128"))) {
            filePath = Environment.getExternalStorageDirectory().toString() + "/" + fileName;
        } else {
            filePath = PjjApplication.App_Path + fileName;
        }
        Runnable runnable = null;
        if (code.equals(JPushReceiver.UPDATE_APP)) {
            SharedUtils.saveForXml(SharedUtils.VERSION_UPDATE, "下载");
            runnable = () -> XSPSystem.getInstance().installApp(filePath);
        } else if (code.equals(JPushReceiver.UPDATE_SYSTEM)) {
            File file = new File(filePath);
            if (file.exists()) {
                file.getAbsoluteFile().delete();
            }
            runnable = () -> XSPSystem.getInstance().systemUpdate(filePath);
        }
        if (null == runnable) {
            return;
        }
        final Runnable runnableMain = runnable;
        getRetrofitService().downloadBigFile(downloadPath, filePath, new FileUtils.OnDownloadListener() {
            @Override
            public void success() {
                mainHandler.post(runnableMain);
            }

            @Override
            public void fail() {

            }
        });
    }

    @Override
    public void insertJson(String json) {
        childHandler.post(json);
    }

    @Override
    public void uploadFile(String filePath, String msg) {
        if (!new File(filePath).exists()) {
            return;
        }
        AliFile.getInstance().uploadFile(filePath, new AliFile.UploadResult() {
            @Override
            protected void success(String result) {
                super.success(result);
                insertScreenErrorFile(result, msg, filePath);
            }

            @Override
            protected void fail(String error) {
                super.fail(error);
            }
        }, "1");
    }

    private void insertScreenErrorFile(String fileName, String fileNature, String filePath) {
        String screenId = XSPSystem.getInstance().getOnlyCode();
        if (TextUtils.isEmpty(screenId)) {
            return;
        }
        getRetrofitService().insertScreenErrorFile(screenId, fileName, fileNature, new RetrofitService.MyCallback() {
            @Override
            protected void success(String s) {
                Log.e("TAG", "insertScreenErrorFile success: " + s);
            }

            @Override
            protected void fail(String error) {
                Log.e("TAG", "insertScreenErrorFile fail: " + error);
            }
        });
    }

    @Override
    public void recycle() {
        if (null != timer) {
            timer.cancel();
        }
        childHandler.quit();
        childHandler = null;
        super.recycle();
    }
}
