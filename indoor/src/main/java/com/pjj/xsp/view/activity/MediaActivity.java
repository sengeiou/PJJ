package com.pjj.xsp.view.activity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pjj.xsp.BuildConfig;
import com.pjj.xsp.PjjApplication;
import com.pjj.xsp.R;
import com.pjj.xsp.contract.MainContract;
import com.pjj.xsp.db.HandleDaoDb;
import com.pjj.xsp.db.MinePlayTask;
import com.pjj.xsp.intent.http.AliFile;
import com.pjj.xsp.intent.http.RetrofitService;
import com.pjj.xsp.manage.XSPSystem;
import com.pjj.xsp.module.MediaTaskCacheHelp;
import com.pjj.xsp.module.ProductionConsumptionModule;
import com.pjj.xsp.module.ScreenInfManage;
import com.pjj.xsp.module.XspPlayUI;
import com.pjj.xsp.module.XspTaskHandler;
import com.pjj.xsp.module.bean.WeatherBean;
import com.pjj.xsp.module.parameter.ScreenshotsBean;
import com.pjj.xsp.present.MainPresent;
import com.pjj.xsp.utils.BitmapUtils;
import com.pjj.xsp.utils.DateUtils;
import com.pjj.xsp.utils.Log;
import com.pjj.xsp.utils.SharedUtils;
import com.pjj.xsp.utils.TextViewUtils;
import com.pjj.xsp.view.MainViewHelp;
import com.pjj.xsp.view.custom.MediaSlideViewGroup;

import java.io.File;
import java.util.List;

public class MediaActivity extends BaseActivity<MainPresent> implements MainContract.View, XspPlayUI.OnPlayViewListener, XspTaskHandler.OnHandleTaskListener {
    private MainViewHelp mainViewHelp;
    private AudioManager mAudioManager;
    private int maxVolume;
    final Handler mHandler = new Handler();

    public static boolean ad = true;
    private ProductionConsumptionModule<String> module;
    private boolean reSetUnPlay = false;
    //处于后台标志
    private boolean InBackgroundStatue = false;
    private MediaSlideViewGroup mediaSlideViewGroup;
    //private ServiceConnection serviceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        mainViewHelp = MainViewHelp.getInstance();
        mainViewHelp.initFootView(findViewById(R.id.fl_foot));
        XSPSystem.getInstance().startWatchDog();
        //Intent service = new Intent(this, PjjService.class);
        //service.setAction("com.pjj.xsp.guard.start");
//        service.setPackage(BuildConfig.APPLICATION_ID);
        /*serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.e("TAG", "onServiceConnected: " + name.getPackageName());
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.e("TAG", "onServiceDisconnected: ");
            }
        };*/
        //startService(service);
        //bindService(service, serviceConnection, BIND_AUTO_CREATE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (BuildConfig.DEBUG) {
            Log.e("TAG", "onCreate: pid=" + Process.myPid() + " progressName=" + getPackageName());
        } else {
            //Log.e("TAG", "adfa测试啥啥啥");
        }
        mPresent = new MainPresent(this);
        mediaSlideViewGroup = findViewById(R.id.mediaSlideViewGroup);
        mainViewHelp.setOnNewDayListener(time -> {
            updateNewDayData();
            String cooperationMode = SharedUtils.getXmlForKey("cooperationMode");
            if (TextUtils.isEmpty(cooperationMode)) {
                cooperationMode = "1";
            }
            searchDataAndPlay(cooperationMode);
        });
        initHeadView();
        mediaSlideViewGroup.postDelayed(this::updateWeiBao, 500);
        initVoiceInf();
        XspPlayUI.getInstall().setOnPlayViewListener(this);
        XspTaskHandler.getInstance().setOnHandleTaskListener(this);
        //维保信息获取后在调用，所以注调
        //HandleDaoDb.queryTaskAndPerform(DateUtils.getNowDate(), null);
        XSPSystem.getInstance().bindService(this);
        startThreadTakeScreen();
        controllerHeadViewShowOrHidden(XspPlayUI.getTagBoolean(SharedUtils.getXmlForKey(SharedUtils.HEAD_VIEW_SHOW_TAG)));
    }

    @Override
    protected void onResume() {
        InBackgroundStatue = false;
        //Log.e("TAG", "onResume: InBackgroundStatue="+InBackgroundStatue );
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Log.e("TAG", "onPause111");
        if (!reSetUnPlay) {
            //Log.e("TAG", "onPause222");
            InBackgroundStatue = true;
            mHandler.postDelayed(() -> {
                PackageManager packageManager = getPackageManager();
                Intent intent = packageManager.getLaunchIntentForPackage("com.pjj.xsp");
                startActivity(intent);
            }, 5000);
        }
    }

    @Override
    public void checkOnLine() {
        if (InBackgroundStatue && !reSetUnPlay) {
            //Log.e("TAG", "checkOnLine111");
            mHandler.post(() -> {
                PackageManager packageManager = getPackageManager();
                Intent intent = packageManager.getLaunchIntentForPackage("com.pjj.xsp");
                startActivity(intent);
            });
        }
    }

    private void startThreadTakeScreen() {
        module = new ProductionConsumptionModule<>();
        module.setOnProductuinConsumptionListener(new ProductionConsumptionModule.OnProductuinConsumptionListener<String>() {
            @Override
            public void performTask(String t) {
                makeScreenshots(t);
            }

            @Override
            public void startRun() {
                mHandler.post(module.changePerform());
            }
        });
        module.startSelf();
    }

    private void updateNewDayData() {
        Log.e("TAG", "updateNewDayData: ");
        mPresent.loadCityName();
    }

    private void initHeadView() {
        mainViewHelp.initView(this, findViewById(R.id.fl_head));
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
            reSetUnPlay = true;
            XSPSystem.getInstance().closeWatchDog();
            MediaTaskCacheHelp.getInstance().clearTask();
            SharedUtils.saveForXml(SharedUtils.ACTIVE_CODE, UnPlayActivity.STATUE_UN_ACTIVATE + "");
            //unbindService(serviceConnection);
            //stopService(new Intent(this,PjjService.class));
            startActivity(new Intent(this, UnPlayActivity.class).putExtra(UnPlayActivity.RESET, true));
            finish();
        }
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
        setVolume(SharedUtils.getXmlForKeyInt(SharedUtils.VOLUME_NUM, 0));
//        setVolume(5);
    }

    @Override
    public void updateWeiBao() {
        //mainViewHelp.updateWeiBao();
        Log.e("TAG", "updateWeiBao: ");
        mPresent.loadWeiBao();
    }

    @Override
    public void updateLocalCityName(String cityName) {
        mainViewHelp.updateCityName(cityName);
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
    }

    @Override
    public void reset() {
        mPresent.loadUnActivateStatue();
    }

    @Override
    public void searchTask(String orderId) {
        mPresent.loadLunBoTaskForOrderId(orderId);
    }

    @Override
    public void playNow() {
        mHandler.post(() -> {
            String cooperationMode = SharedUtils.getXmlForKey("cooperationMode");
            if (TextUtils.isEmpty(cooperationMode)) {
                cooperationMode = "1";
            }
            searchDataAndPlay(cooperationMode);
        });
    }

    @Override
    public void controllerHeadViewShowOrHidden(boolean flag) {
        mHandler.post(() -> findViewById(R.id.fl_head).setVisibility(flag ? View.VISIBLE : View.GONE));
    }

    @Override
    public void reStart(String cooperationMode) {
//        XspPlayUI.getInstall().clearNowLink();
        MediaTaskCacheHelp.getInstance().clearTask();
        SharedUtils.saveForXml("cooperationMode", cooperationMode);
        searchDataAndPlay(cooperationMode);
    }

    /**
     * 问题：
     * 1.自用切换，定点任务是否清除；以及定点任务是否适应自用屏
     * 2.定点任务与暂停播放的优先级
     *
     * @param cooperationMode
     */
    private void searchDataAndPlay(String cooperationMode) {
        MediaTaskCacheHelp.getInstance().clearTask();
        if (PjjApplication.application.isFlag()) {
            if ("4".equals(cooperationMode)) {
                HandleDaoDb.queryTaskAndPerform();
            } else {
                HandleDaoDb.queryTaskAndPerform(DateUtils.getNowDate(), null);
            }
        }
    }

    @Override
    public void screenshots(String name) {
        //orderId = name;
        if (TextViewUtils.isEmpty(name)) {
            return;
        }
        mPresent.loadTakeScreenResult(name, "收到截屏命令");
        //Log.e("TAG", "screenshots: 收到截屏命令");
        module.addQueue(name);
        //makeScreenshots(name);
    }

    private void makeScreenshots(String orderId) {
        Log.e("TAG", "makeScreenshots: ");
        String fileName = "screen_" + System.currentTimeMillis() + ".png";
        String screenshots;
        int delayMillis = 4000;
        if (BuildConfig.USE_TYPE.contains("elevator")) {
            delayMillis += 3000;
        } else if (BuildConfig.USE_TYPE.contains("m10")) {
            delayMillis += 4000;
        }
        if (BuildConfig.USE_TYPE.contains("m10")) {
            File file1 = new File(PjjApplication.Screenshots_Path);
            File[] files = file1.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                f.getAbsoluteFile().delete();
            }
            Intent intent = new Intent("rk.android.screenshot.ACTION");
            MediaActivity.this.sendBroadcast(intent);
            screenshots = "elevator";
        } else {
            screenshots = XSPSystem.getInstance().screenshots(PjjApplication.App_Path + "screenshots/", fileName);
        }
        mHandler.postDelayed(() -> {
            String path = screenshots;
            if ("elevator".equals(path)) {
                path = elevatorScreenshots();
            }
            android.util.Log.e("TAG", "makeScreenshots: path=" + path);
            if (null == path) {
                return;
            }
            File file = new File(path);
            if (!file.exists()) {
                path = BitmapUtils.getScreenshot();
                android.util.Log.e("TAG", "makeScreenshots: null next=" + path);
                if (null != path) {
                    file = new File(path);
                    if (!file.exists()) {
                        mPresent.loadTakeScreenResult(orderId, "生成图片失败-1");
                        android.util.Log.e("TAG", "makeScreenshots: null");
                        module.notifyThread();
                        return;
                    }
                } else {
                    mPresent.loadTakeScreenResult(orderId, "生成图片失败-0");
                    android.util.Log.e("TAG", "makeScreenshots: null");
                    module.notifyThread();
                    return;
                }
            }
            final File file1 = file;
            Log.e("TAG", "makeScreenshots: " + file.length());
            AliFile.getInstance().uploadFile(path, new AliFile.UploadResult() {
                @Override
                protected void success(String result) {
                    android.util.Log.e("TAG", "success: " + result);
                    ScreenshotsBean screenshotsBean = new ScreenshotsBean();
                    screenshotsBean.setScreenId(XSPSystem.getInstance().getOnlyCode());
                    screenshotsBean.setOrderId(orderId);
                    screenshotsBean.setPrintScreen(result);
                    RetrofitService.getInstance().uploadScreenshotsName(screenshotsBean, new RetrofitService.MyCallback() {
                        @Override
                        protected void success(String s) {
                            //file1.getAbsoluteFile().delete();
                            android.util.Log.e("TAG", "success: 通知成功 size=" + file1.length());
                            module.notifyThread();
                        }

                        @Override
                        protected void fail(String error) {
                            android.util.Log.e("TAG", "error=" + error);
                            mPresent.loadTakeScreenResult(orderId, "图片上传成功但，" + error);
                            file1.getAbsoluteFile().delete();
                            module.notifyThread();
                        }
                    });
                }

                @Override
                protected void fail(String error) {
                    mPresent.loadTakeScreenResult(orderId, "上传图片失败，" + error);
                    super.fail(error);
                    file1.getAbsoluteFile().delete();
                    module.notifyThread();
                }
            });
        }, delayMillis);

    }

    private String elevatorScreenshots() {
        File file = new File(PjjApplication.Screenshots_Path);
        String[] list = file.list();
        if (null != list && list.length > 0) {
            return PjjApplication.Screenshots_Path + list[0];
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        XspPlayUI.getInstall().setOnPlayViewListener(null);
        XspTaskHandler.getInstance().setOnHandleTaskListener(null);
        XSPSystem.getInstance().unBindService(this);
        //XSPSystem.getInstance().closeWatchDog();
        //unbindService(serviceConnection);
        ScreenInfManage.getInstance().setScreenInfDataBean(null);
        //BootManage.getInstance().stopClear();
        if (null != mediaSlideViewGroup)
            mediaSlideViewGroup.recycle();
        module.recycle();
        module = null;
        super.onDestroy();
    }

    @Override
    public void getXspTask() {
        mPresent.loadXspNextTask();
    }

    @Override
    public void performTask() {

    }

    @Override
    public void addMineTask(MinePlayTask taskText) {
        mPresent.addMineTask(taskText);
    }

    @Override
    public void downloadFile(String path, String fileName, String code) {
        mPresent.downloadFile(path, fileName, code);
    }

    @Override
    public void insertJson(String json) {
        mPresent.insertJson(json);
    }

    @Override
    public void uploadFile() {
        String filePath = PjjApplication.App_Path + "restart.txt";
        String msg = "重启记录";
        mPresent.uploadFile(filePath, msg);
        mPresent.uploadFile(PjjApplication.App_Path + "media_task.txt", "播放记录");
    }
}
