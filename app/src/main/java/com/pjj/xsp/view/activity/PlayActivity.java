package com.pjj.xsp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import com.pjj.xsp.PjjApplication;
import com.pjj.xsp.intent.http.RetrofitService;
import com.pjj.xsp.module.bean.ScreenPingTaskBean;
import com.pjj.xsp.intent.http.AliFile;
import com.pjj.xsp.module.bean.SpeedScreenBean;
import com.pjj.xsp.module.parameter.ScreenshotsBean;
import com.pjj.xsp.utils.Log;

import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.pjj.xsp.R;
import com.pjj.xsp.contract.MainContract;
import com.pjj.xsp.intent.socket.SocketManage;
import com.pjj.xsp.manage.BootManage;
import com.pjj.xsp.manage.XSPSystem;
import com.pjj.xsp.module.ScreenInfManage;
import com.pjj.xsp.module.XspPlayUI;
import com.pjj.xsp.module.XspTaskHandler;
import com.pjj.xsp.module.bean.ScreenTaskBean;
import com.pjj.xsp.module.bean.WeatherBean;
import com.pjj.xsp.module.template.AdvertisingBean;
import com.pjj.xsp.module.template.AdvertisingFactory;
import com.pjj.xsp.present.MainPresent;
import com.pjj.xsp.utils.SharedUtils;
import com.pjj.xsp.utils.TextJsonUtils;
import com.pjj.xsp.utils.TextViewUtils;
import com.pjj.xsp.view.fragment.ABFragment;
import com.pjj.xsp.view.MainViewHelp;
import com.pjj.xsp.view.fragment.ImageFragment;
import com.pjj.xsp.view.fragment.PingBmFragment;
import com.pjj.xsp.view.fragment.SpeedDiyFragment;
import com.pjj.xsp.view.fragment.VideoViewFragment;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

import static com.pjj.xsp.module.XspTaskHandler.SCREEN_CONTENT;
import static com.pjj.xsp.module.XspTaskHandler.SCREEN_DIY_LIST;
import static com.pjj.xsp.module.XspTaskHandler.SCREEN_TITLE;
import static com.pjj.xsp.module.XspTaskHandler.SCREEN_TITLE_10_LIST;

/**
 * Create by xinheng on 2018-11-15 17:15:49。
 * describe：
 */
public class PlayActivity extends BaseActivity<MainPresent> implements MainContract.View, XspPlayUI.OnPlayViewListener, XspTaskHandler.OnHandleTaskListener, ABFragment.OnActivityListener {
    /**
     * 便民text
     */
    private TextView tv_content;
    private MainViewHelp mainViewHelp;
    private AudioManager mAudioManager;
    private int maxVolume;
    private Fragment fragmentNow;
    private AdvertisingBean advertisingBeanLoop;
    public static final boolean ad = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        mainViewHelp = MainViewHelp.getInstance();
        tv_content = initHeadView();
        mPresent = new MainPresent(this);
        updateWeiBao();
        mainViewHelp.setOnNewDayListener(new MainViewHelp.OnNewDayListener() {
            @Override
            public void newDay(String time) {
                updateNewDayData();
            }

            @Override
            public void updateTheme(boolean tag) {
                if (null != advertisingBeanLoop && null != fragmentNow && fragmentNow instanceof ABFragment) {
                    ((ABFragment) fragmentNow).updateStyle(tag, AdvertisingBean.getColor(advertisingBeanLoop.getDiyBgColor()));
                }
            }
        });
        initFootView();
        initVoiceInf();
        XspPlayUI.getInstall().setOnPlayViewListener(this);
        XspTaskHandler.getInstance().setOnHandleTaskListener(this);
        XspTaskHandler.getInstance().startAlarm();
        //查询存储任务，并播放
        HashMap nowNextHourTask = XspTaskHandler.getInstance().getHourTask(false);
        if (nowNextHourTask != null) {
            XspTaskHandler.getInstance().handleTask(nowNextHourTask);
        } else {
            nowNextHourTask = new HashMap<>(1);
            nowNextHourTask.put(XspTaskHandler.SCREEN_CONTENT, XspTaskHandler.getLocalTaskDataBean());
        }

        ScreenInfManage.getInstance().setMapNow(nowNextHourTask);
        updateUI(nowNextHourTask);
        if (!ad) {
            if (BootManage.getInstance().openSerialPort()) {
                SocketManage.getInstance().createSocket("ws://47.92.50.83:8083/websocket/" + XSPSystem.getInstance().getOnlyCode() + "/1/1");
                BootManage.getInstance().statReadThread();
            }
        }
        /*boolean start = CollectorSession.getInstance().start();
        if (start) {
            //SocketManage.getInstance().createSocket("ws://47.92.50.83:8083/websocket/" + XSPSystem.getInstance().getOnlyCode() + "/1/1");
            AppConfig.BOARD_IS_ACTIVE = true;
            Toast.makeText(this, "串口打开成功...", Toast.LENGTH_SHORT).show();
        }*/
    }

    private void updateNewDayData() {
        mPresent.loadCityName();
    }

    private TextView initHeadView() {
        return mainViewHelp.iniHeadView(this, findViewById(R.id.fl_foot));
    }

    private void initFootView() {
        mainViewHelp.initFootView(this, findViewById(R.id.fl_head));
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainViewHelp.getInstance().initFirstStart();
    }

    @Override
    public void updateLocalCityName(String cityName) {
        mainViewHelp.updateCityName(cityName);
    }

    @Override
    public void updateWeatherView(List<WeatherBean.ResultsBean.WeatherDataBean> list) {
        mainViewHelp.updateWeather(list);
    }

    @Override
    public void updateXianXingNum(String[] arrays) {
        mainViewHelp.updateCarNum(arrays);
    }

    @Override
    public void setUnActivateResult(boolean tag) {
        if (tag) {
            SharedUtils.saveForXml(SharedUtils.ACTIVE_CODE, UnPlayActivity.STATUE_UN_ACTIVATE + "");
            startActivity(new Intent(this, UnPlayActivity.class).putExtra(UnPlayActivity.RESET, true));
            finish();
        }
    }

    @Override
    public void showWaiteStatue() {

    }

    @Override
    public void cancelWaiteStatue() {

    }

    @Override
    public void failStatue(String error) {
        Log.e("TAG", "failStatue: " + error);
    }

    private void initVoiceInf() {
        //获取系统的Audio管理者
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //最大音量
        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //当前音量
        //currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        setVolume(SharedUtils.getXmlForKeyInt(SharedUtils.VOLUME_NUM, 3));
//        setVolume(0);
    }

    @Override
    public void updateWeiBao() {
        //mainViewHelp.updateWeiBao();
        mPresent.loadWeiBao();
    }

    /**
     * 设置音量
     *
     * @param volume
     */
    public void setVolume(int volume) {
        SharedUtils.saveForXml(SharedUtils.VOLUME_NUM, volume);
        volume = (int) (volume / 10f * (maxVolume));
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
    }

    @Override
    public void stopPlay() {
        runOnUiThread(() ->
                updateUI(ScreenInfManage.getInstance().getMapNow())
        );
    }

    @Override
    public void reset() {
        mPresent.loadUnActivateStatue();
    }

    @Override
    public void searchTask(String orderId) {
        mPresent.loadSearchTaskForOrderId(orderId);
    }

    @Override
    public void playNow() {
        runOnUiThread(() -> {
            HashMap mapNow = ScreenInfManage.getInstance().getMapNow();
            updateUI(mapNow);
        });
    }

    @Override
    public void updateDefaultBm() {
        if (tv_content.getText().toString().equals("热烈庆祝屏加加app以及电梯物联网系统本月正式上线；屏联你我，加载未来，开启电梯安全新纪元！！！")) {
            tv_content.setText(ScreenInfManage.default_bm);
        }
    }

    private String orderId;

    @Override
    public void screenshots(String name) {
        //HashMap mapNow = ScreenInfManage.getInstance().getMapNow();
        orderId = name;
        if (TextViewUtils.isEmpty(name)) {
            return;
        }
        File file1 = new File(PjjApplication.Screenshots_Path);
        File[] files = file1.listFiles();
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            f.getAbsoluteFile().delete();
        }
        XSPSystem.getInstance().screenshots();
        tv_content.postDelayed(() -> {
            File file = new File(PjjApplication.Screenshots_Path);
            String[] list = file.list();
            if (null != list && list.length > 0) {
                String string = PjjApplication.Screenshots_Path + list[0];
                //Log.e("TAG", "onClick: " + string + ", " + new File(string).length() / 1204f);
                AliFile.getInstance().uploadFile(string, new AliFile.UploadResult() {
                    @Override
                    protected void success(String result) {
                        super.success(result);
                        /*ScreenshotsBean screenshotsBean = new ScreenshotsBean();
                        //screenshotsBean.setOrderId();
                        screenshotsBean.setScreenId(XSPSystem.getInstance().getOnlyCode());
                        mPresent.uploadScreenshotsName(screenshotsBean);*/
                    }

                    @Override
                    protected void fail(String error) {
                        super.fail(error);
                    }
                });
            }
        }, 7000);
    }

    private void updatePingTitleContent(List<ScreenPingTaskBean.DataBeanPin> list) {
        ScreenPingTaskBean.DataBeanPin dataBeanPin1 = list.get(0);
        //为啥生气，呵呵，
        ScreenTaskBean.DataBean dataBean = new ScreenTaskBean.DataBean();
        dataBean.setOrderId(dataBeanPin1.getOrderId());
        dataBean.setPeopleInfo(dataBeanPin1.getPeopleInfo());
        //updateHeadInf(dataBean);
        updatePingContent(list);
    }

    /**
     * 更新
     * 广告内容区域
     */
    private void updateContent(ScreenTaskBean.DataBean dataBean) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        ScreenTaskBean.DataBean.TempletListBean templetListBean = dataBean.getTempletList().get(0);
        String fragmentTAG = getFragmentTAG(templetListBean.getTempletType());
        Fragment fragment = supportFragmentManager.findFragmentByTag(fragmentTAG);
        if (null != fragment) {
            if (fragment instanceof ABFragment) {
                ((ABFragment) fragment).updateData(templetListBean.getFileList().get(0).getFileUrl());
            }
            if (fragment != fragmentNow) {
                fragmentTransaction.show(fragment);
            } else {
                return;
            }
        } else {
            fragment = gerFragment(dataBean);
            fragmentTransaction.add(R.id.fl_content, fragment, fragmentTAG);
        }
        if (fragment != fragmentNow && null != fragmentNow) {
            fragmentTransaction.hide(fragmentNow);
        }
        if (!isFinishing()) {
            fragmentTransaction.commit();
            fragmentNow = fragment;
        } else {
            Log.e("TAG", "updateContent: isFinishing=" + isFinishing());
        }
    }

    private void updatePingContent(List<ScreenPingTaskBean.DataBeanPin> list) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        String fragmentTAG = getFragmentTAG("100");
        Fragment fragment = supportFragmentManager.findFragmentByTag(fragmentTAG);
        if (null != fragment) {
            if (fragment instanceof PingBmFragment) {
                ((PingBmFragment) fragment).update(list);
            }
            if (fragment != fragmentNow) {
                fragmentTransaction.show(fragment);
            } else {
                return;
            }
        } else {
            fragment = new PingBmFragment();
            ((PingBmFragment) fragment).update(list);
            fragmentTransaction.add(R.id.fl_content, fragment, fragmentTAG);
        }
        if (fragment != fragmentNow && null != fragmentNow) {
            fragmentTransaction.hide(fragmentNow);
        }
        if (!isFinishing()) {
            fragmentTransaction.commit();
            fragmentNow = fragment;
        } else {
            Log.e("TAG", "updateContent: isFinishing=" + isFinishing());
        }
    }

    private void updateSpeedContent(SpeedScreenBean.DataBean data) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        String fragmentTAG = getFragmentTAG("101");
        Fragment fragment = supportFragmentManager.findFragmentByTag(fragmentTAG);
        if (null != fragment) {
            if (fragment instanceof SpeedDiyFragment) {
                ((SpeedDiyFragment) fragment).updateUI(data);
            }
            if (fragment != fragmentNow) {
                fragmentTransaction.show(fragment);
            } else {
                return;
            }
        } else {
            fragment = new SpeedDiyFragment();
            ((SpeedDiyFragment) fragment).updateUI(data);
            fragmentTransaction.add(R.id.fl_content, fragment, fragmentTAG);
        }
        if (fragment != fragmentNow && null != fragmentNow) {
            fragmentTransaction.hide(fragmentNow);
        }
        if (!isFinishing()) {
            fragmentTransaction.commit();
            fragmentNow = fragment;
        } else {
            Log.e("TAG", "updateContent: isFinishing=" + isFinishing());
        }
    }

    private String getFragmentTAG(String fileType) {//1 全图片 2全视频 3上视频下图片
        switch (fileType) {
            case "1"://图片
                return "img";
            case "2":
                return "video";
            case "100":
                return "ping_bian_min";
            case "101":
                return "speed_diy";
            default://视频
                return "video";
        }
    }

    /**
     * 获取fragment
     *
     * @return Fragment
     */
    private Fragment gerFragment(ScreenTaskBean.DataBean dataBean) {
        ScreenTaskBean.DataBean.TempletListBean templetListBean = dataBean.getTempletList().get(0);
        String fileType = templetListBean.getTempletType();
        String fileUrl = templetListBean.getFileList().get(0).getFileUrl();
        Fragment fragment;
        switch (fileType) {
            case "1"://图片
                fragment = ImageFragment.newInstance(fileUrl);
                break;
            case "2"://视频
                fragment = VideoViewFragment.newInstance(fileUrl);
                break;
            default:
                fragment = ImageFragment.newInstance(XspTaskHandler.LOCAL_PATH);
        }
        return fragment;
    }

    @Override
    protected void onDestroy() {
        XspPlayUI.getInstall().setOnPlayViewListener(null);
        XspTaskHandler.getInstance().setOnHandleTaskListener(null);
        super.onDestroy();
    }

    @Override
    public void getXspTask() {
        mPresent.loadXspNextTask();
    }

    @Override
    public void performTask() {
        HashMap<String, Object> map = ScreenInfManage.getInstance().getMap();
        //String s = new Gson().toJson(map);
        //Log.e("TAG", "performTask: " + s);
        if (null != map) {
            updateUI(map);
            mainViewHelp.updateNowTime_(false);
            ScreenInfManage.getInstance().setMapNow(map);
        }
        //ScreenInfManage.getInstance().setMap(null);
    }

    private void updateUI(HashMap map) {
        advertisingBeanLoop = AdvertisingFactory.getAdvertisingBeanLoop();
        //mainViewHelp.setAdvertisingBean(advertisingBeanLoop);
        //Log.e("TAG", "updateUI: " + TextJsonUtils.gson.toJson(map));
        Object title_10_list = map.get(SCREEN_TITLE_10_LIST);
        if (ad) {
            findViewById(R.id.fl_foot).setVisibility(View.GONE);
        } else {
            findViewById(R.id.fl_foot).setVisibility(View.VISIBLE);
        }
        if (null != title_10_list) {
            List<ScreenPingTaskBean.DataBeanPin> title_10_list1 = (List<ScreenPingTaskBean.DataBeanPin>) title_10_list;
            updatePingTitleContent(title_10_list1);
            findViewById(R.id.fl_foot).setVisibility(View.GONE);
            return;
        }
        Object screen_diy_list = map.get(SCREEN_DIY_LIST);
        if (null != screen_diy_list) {
            List<SpeedScreenBean.DataBean> screen_diy_list1 = (List<SpeedScreenBean.DataBean>) screen_diy_list;
            updateSpeedContent(screen_diy_list1.get(0));
            findViewById(R.id.fl_foot).setVisibility(View.GONE);
            return;
        }
        Object title = map.get(SCREEN_TITLE);
        if (null != title) {
            ScreenTaskBean.DataBean dataBeanTitle = (ScreenTaskBean.DataBean) title;
            updateHeadInf(dataBeanTitle);
        } else {
            updateHeadInf(null);
        }
        Object content = map.get(SCREEN_CONTENT);
        if (content != null) {
            ScreenTaskBean.DataBean dataBeanContent = (ScreenTaskBean.DataBean) content;
            if (null != dataBeanContent) {
                updateContent(dataBeanContent);
            }
        }
    }

    /**
     * 更新头部
     * 便民信息
     */
    private void updateHeadInf(ScreenTaskBean.DataBean dataBean) {
        String text;

        String name = null;
        String phone = null;
        if (null != dataBean) {
            text = TextViewUtils.clean(dataBean.getPeopleInfo());
            if ("1".equals(dataBean.getIsShowPhone())) {
                phone = dataBean.getPhoneNumber();
            }
            if ("1".equals(dataBean.getIsShowName())) {
                if ("1".equals(dataBean.getAuthType())) {
                    name = dataBean.getName();
                } else {
                    name = dataBean.getCompanyName();
                }
            }
        } else {
            text = ScreenInfManage.default_bm;
            name = "屏加加科技有限公司";
            phone = "4001251818";
        }
        int length = -1;
        try {
            length = text.getBytes("GBK").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (length > 0 && length <= 37) {
            tv_content.setTextSize(TypedValue.COMPLEX_UNIT_PX, 45);
        } else {
            tv_content.setTextSize(TypedValue.COMPLEX_UNIT_PX, 31);
        }
        tv_content.setText(text);
        mainViewHelp.updateBm(name, phone);
    }

    @Override
    public boolean getScreenStyle() {
        return !mainViewHelp.getScreenTag();
    }

    @Override
    public int getScreenBgColor() {
        return null == advertisingBeanLoop ? Color.BLACK : AdvertisingBean.getColor(advertisingBeanLoop.getDiyBgColor());
    }
}
