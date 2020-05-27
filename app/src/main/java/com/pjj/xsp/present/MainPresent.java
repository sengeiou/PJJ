package com.pjj.xsp.present;

import android.os.Handler;
import android.text.TextUtils;

import com.google.gson.JsonSyntaxException;
import com.pjj.xsp.module.bean.ResultBean;
import com.pjj.xsp.module.bean.ScreenParentBean;
import com.pjj.xsp.module.bean.ScreenPingTaskBean;
import com.pjj.xsp.module.bean.SpeedScreenBean;
import com.pjj.xsp.module.parameter.ScreenInfTag;
import com.pjj.xsp.module.parameter.ScreenshotsBean;
import com.pjj.xsp.utils.Log;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.pjj.xsp.PjjApplication;
import com.pjj.xsp.contract.MainContract;
import com.pjj.xsp.db.DaoManager;
import com.pjj.xsp.db.DateTask;
import com.pjj.xsp.db.HandleDaoDb;
import com.pjj.xsp.db.OrderTask;
import com.pjj.xsp.intent.http.RetrofitService;
import com.pjj.xsp.manage.XSPSystem;
import com.pjj.xsp.module.ScreenInfManage;
import com.pjj.xsp.module.XspTaskHandler;
import com.pjj.xsp.module.bean.ScreenInfBean;
import com.pjj.xsp.module.bean.ScreenOrderTaskBean;
import com.pjj.xsp.module.bean.ScreenTaskBean;
import com.pjj.xsp.module.bean.WeatherBean;
import com.pjj.xsp.module.parameter.ActivateStatue;
import com.pjj.xsp.module.parameter.ScreenOrderTask;
import com.pjj.xsp.utils.DateUtils;
import com.pjj.xsp.utils.FileUtils;
import com.pjj.xsp.utils.TextJsonUtils;
import com.pjj.xsp.utils.TextViewUtils;
import com.pjj.xsp.view.MainViewHelp;

import java.util.HashMap;
import java.util.List;

import static com.pjj.xsp.module.XspTaskHandler.SCREEN_CONTENT;
import static com.pjj.xsp.module.XspTaskHandler.downloadTask;
import static com.pjj.xsp.module.XspTaskHandler.getLocalTaskDataBean;

/**
 * Create by xinheng on 2018/11/01。
 * describe：
 */
public class MainPresent extends BasePresent<MainContract.View> implements MainContract.Present {
    private Handler mainHandler;
    private String pingYin;
    private String cityName;
    /**
     * 获取任务，45min截止
     */
    private int GET_TASK_LESS_MIN = 45;

    public MainPresent(MainContract.View view) {
        super(view);
        mainHandler = new Handler();
    }

    @Override
    public void loadCityName() {
        //getCityName();
        if (TextUtils.isEmpty(pingYin) || TextUtils.isEmpty(cityName)) {
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
                try {
                    FileUtils.saveStringFile(PjjApplication.App_Path + "weather.txt", TextJsonUtils.toJsonString(weatherBean));
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                mainHandler.postDelayed(() -> loadCityWeather(cityName), 2000);
            }
        });
    }

    @Override
    public void loadXianXingNum(String localName) {
        if (TextViewUtils.isEmpty(localName)) {
            mView.updateXianXingNum(null);
            return;
        }
        getRetrofitService().getCarXianXingNum(localName, new RetrofitService.MyCallback() {
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
                    mainHandler.postDelayed(() -> loadXianXingNum(localName), 2000);
                }
                if (isViewNotNull())
                    mView.updateXianXingNum(null);
            }
        });
    }

    @Override
    public void loadXspNextTask() {
        //服务器下一个时间点
        //本地找不到此时间点的任务，才会执行此方法
        ScreenInfTag screenInfTag = ScreenInfManage.getInstance().getScreenInfTag();
        getRetrofitService().getScreenTaskInf(screenInfTag, new RetrofitService.MyCallback() {
            @Override
            protected void success(String s) {
                //s="{\"msg\":\"查询成功\",\"orderType\":5,\"flag\":\"1\",\"data\":[{\"viewSizeBeanList\":[{\"randomFlag\":\"1\",\"fileName\":\"d939386464e0c15f3d91ac630632e96a.mp4\",\"orderId\":\"20190316112423155270666328605029\",\"x\":0,\"width\":1,\"y\":0,\"type\":2,\"height\":1},{\"fileName\":\"cdd8b5fb2f7e30e512e45d7b028cc512.jpg\",\"orderId\":\"20190316113607155270736787687940\",\"x\":0,\"width\":1,\"y\":1,\"height\":1}],\"IdentificationId\":\"658448e350b646c7a45edca8a1dc6910\",\"size\":2,\"proportionX\":1,\"proportionY\":2}],\"hour\":\"16\",\"curDate\":\"2019-03-18\"}";
                //Log.e("TAG", "loadXspNextTask success: " + s);
                ScreenParentBean screenParentBean = TextJsonUtils.gson.fromJson(s, ScreenParentBean.class);
                if (screenParentBean == null) {
                    fail("数据错误");
                    return;
                }
                if(!screenParentBean.isSuccess()){
                    fail(screenParentBean.getMsg());
                    return;
                }
                if (screenParentBean.getOrderType() == 5) {
                    SpeedScreenBean speedScreenBean = null;
                    try {
                        speedScreenBean = TextJsonUtils.gson.fromJson(s, SpeedScreenBean.class);
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                    if (null == speedScreenBean) {
                        fail("parse-error");
                        return;
                    }
                    XspTaskHandler.dealWithSpeedScreenTaskData(speedScreenBean);
                    return;
                }
                ScreenTaskBean screenTaskBean = null;
                try {
                    screenTaskBean = TextJsonUtils.gson.fromJson(s, ScreenTaskBean.class);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                ScreenPingTaskBean screenPingTaskBean = null;
                try {
                    screenPingTaskBean = TextJsonUtils.gson.fromJson(s, ScreenPingTaskBean.class);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                if (null == screenPingTaskBean && null == screenTaskBean) {
                    fail("parse-error");
                    return;
                }
                if (null != screenPingTaskBean && null != screenPingTaskBean.getData() && screenPingTaskBean.getData().size() > 0 && screenPingTaskBean.getData().get(0).getPieceColour() != null) {
                    if (screenPingTaskBean.isSuccess()) {
                        XspTaskHandler.dealWithBMPingScreenTaskData(screenPingTaskBean);
                    } else {
                        fail(screenPingTaskBean.getMsg());
                    }
                } else {
                    if (screenTaskBean.isSuccess()) {
                        //数据处理 并存储 下载
                        XspTaskHandler.dealWithScreenTaskData(screenTaskBean);
                    } else {
                        fail(screenTaskBean.getMsg());
                    }
                }

            }

            @Override
            protected void fail(String error) {
                Log.e("TAG", "loadXspNextTask-fail: " + error);
                if (null != error && error.startsWith("parse-error", 0)) {//json串错误
                    getTaskFail(error);
                } else {
                    if (DateUtils.lessTheMin(GET_TASK_LESS_MIN)) {
                        mainHandler.postDelayed(xspTaskRunnable, 1000);
                    } else {
                        getTaskFail(error);
                    }
                }
            }
        });
    }

    @Override
    public void loadWeiBao() {
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
                        loadCityName();
                    }
                    ScreenInfManage.getInstance().setScreenInfDataBean(dataBean);
                    MainViewHelp.getInstance().updateWeiBao();
                }
            }

            @Override
            protected void fail(String error) {
                Log.e("TAG", "维保 fail: " + error);
                mainHandler.postDelayed(() -> loadWeiBao(), 2000);
            }
        });
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
                    mainHandler.postDelayed(() -> loadUnActivateStatue(), 1000);
                }
            }
        });
    }

    @Override
    public void loadSearchTaskForOrderId(String orderId) {
        ScreenOrderTask screenOrderTask = new ScreenOrderTask();
        screenOrderTask.setOrderId(orderId);
        screenOrderTask.setScreenId(XSPSystem.getInstance().getOnlyCode());
        getRetrofitService().loadSearchTaskForOrderId(screenOrderTask, new RetrofitService.CallbackClassResult<ScreenOrderTaskBean>(ScreenOrderTaskBean.class) {

            @Override
            protected void resultSuccess(ScreenOrderTaskBean screenOrderTaskBean) {
                ScreenOrderTaskBean.DataBean data = screenOrderTaskBean.getData();
                ScreenTaskBean.DataBean order = data.getOrder();
                if (null != order && !TextViewUtils.isEmpty(order.getOrderId()) && !TextViewUtils.isEmpty(order.getOrderType())) {
                    String orderId = order.getOrderId();
                    //HandleDaoDb.queryEqBeanByQueryBuilder()
                    String orderType = order.getOrderType();//1 DIY类型 2便民信息 3填空传媒
                    String bmjson = null;
                    if ("2".equals(orderType)) {//便民
                        data.setTempletList(null);
                        bmjson = order.getBmInfJson();
                    } else {
                        order.setPeopleInfo(null);
                    }
                    List<ScreenOrderTaskBean.DataBean.OrderTimeBean> orderTime = data.getOrderTime();
                    if (null != orderTime && orderTime.size() > 0) {
                        List<DateTask> dateTasks = ScreenOrderTaskBean.changeDateTaskList(orderId, orderTime);
                        if (null != dateTasks) {
                            HandleDaoDb.insertListBean(dateTasks);
                            List<ScreenTaskBean.DataBean.TempletListBean> templetList = data.getTempletList();
                            downloadTask(templetList);//下载
                            OrderTask orderTask = ScreenOrderTaskBean.changeOrderTask(orderId, bmjson, templetList);
                            if (null != orderTask) {
                                HandleDaoDb.insertBean(orderTask, DaoManager.getInstance().getDaoSession().getOrderTaskDao());
                            }
                        }
                    }


                }

            }

            @Override
            protected void fail(String error) {
                Log.e("TAG", "loadSearchTaskForOrderId-fail: " + error);
                mainHandler.postDelayed(() -> loadSearchTaskForOrderId(orderId), 1000);
            }
        });
    }

    private void getTaskFail(String error) {
        HashMap<String, ScreenTaskBean.DataBean> map = new HashMap<>(1);
        map.put(SCREEN_CONTENT, getLocalTaskDataBean());
        ScreenInfManage.getInstance().setMap(map);
        FileUtils.saveStringFile(PjjApplication.App_Path + "task.log", "获取广告屏播放任务失败-" + error);
    }

    private Runnable xspTaskRunnable = () -> loadXspNextTask();

    /**
     * 高德地图
     */
    private void getCityName() {
        //声明mlocationClient对象
        AMapLocationClient mlocationClient;
        //声明mLocationOption对象
        AMapLocationClientOption mLocationOption = null;
        mlocationClient = new AMapLocationClient(PjjApplication.application);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(amapLocation -> {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    String s = amapLocation.toString();
                    Log.e("TAG", "onLocationChanged: " + s);
                    String city = amapLocation.getCity();
                    //String locationDetail = amapLocation.getLocationDetail();
                    if (!TextViewUtils.isEmpty(city)) {
                        mlocationClient.stopLocation();
                        String replace = city.replace("市", "");
                        mView.updateLocalCityName(replace);
                        //loadXianXingNum(ScreenInfManage.getInstance().getCityPinyin(replace));
                        //loadCityWeather(city);
                        mlocationClient.onDestroy();
                    }
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                    loadCityWeather("北京市");
                    mlocationClient.stopLocation();
                    mlocationClient.onDestroy();
                }
            }
        });
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();

    }

    @Override
    public void uploadScreenshotsName(ScreenshotsBean screenshotsBean) {
        getRetrofitService().uploadScreenshotsName(screenshotsBean, new RetrofitService.CallbackClassResult<ResultBean>(ResultBean.class) {
            @Override
            protected void resultSuccess(ResultBean resultBean) {
                android.util.Log.e("TAG", "resultSuccess截屏 : 成功");
            }

            @Override
            protected void fail(String error) {
                android.util.Log.e("TAG", "fail 截屏: " + error);
            }
        });
    }
}
