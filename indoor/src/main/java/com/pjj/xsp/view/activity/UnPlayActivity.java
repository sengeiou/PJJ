package com.pjj.xsp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pjj.xsp.BuildConfig;
import com.pjj.xsp.PjjApplication;
import com.pjj.xsp.R;
import com.pjj.xsp.contract.InactiveContract;
import com.pjj.xsp.manage.XSPSystem;
import com.pjj.xsp.module.ScreenInfManage;
import com.pjj.xsp.module.parameter.ActivateStatue;
import com.pjj.xsp.module.parameter.AppUpload;
import com.pjj.xsp.present.InactivePresent;
import com.pjj.xsp.utils.Base64Utils;
import com.pjj.xsp.utils.DataCleanManager;
import com.pjj.xsp.utils.Log;
import com.pjj.xsp.utils.QrCodeUtil;
import com.pjj.xsp.utils.SharedUtils;
import com.pjj.xsp.utils.SuperUser;
import com.pjj.xsp.utils.TagAliasOperatorHelper;
import com.pjj.xsp.utils.TextJsonUtils;
import com.pjj.xsp.view.WifiLinkHelp;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.math.BigDecimal;
import java.util.HashMap;

import static com.pjj.xsp.utils.TagAliasOperatorHelper.ACTION_SET;
import static com.pjj.xsp.utils.TagAliasOperatorHelper.sequence;

/**
 * Create by xinheng on 2018-11-15 17:14:53。
 * describe：
 */
public class UnPlayActivity extends BaseActivity<InactivePresent> implements InactiveContract.View, TagAliasOperatorHelper.OnAliasListener {
    public static final int STATUE_ACTIVATE = 0;
    public static final int STATUE_UN_ACTIVATE = 1;
    //    public static final int STATUE_START = 2;
    //    public static final int STATUE_UN_START = 3;
    public static final int STATUE_REGISTER = 4;//注册
    public static final int STATUE_UN_REGISTER = 5;
    public static final int STATUE_JPUSH_ALIAS = 6;//极光已设置ALIAS
    public static final int STATUE_UN_JPUSH_ALIAS = 7;//极光未设置ALIAS
    public static String RESET = "reset";
    private boolean reset;
    private static final int DELAYED_TIME = 2000;


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STATUE_ACTIVATE, STATUE_UN_ACTIVATE})
    @interface XspActivateStatue {
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STATUE_JPUSH_ALIAS, STATUE_UN_JPUSH_ALIAS})
    @interface XspJpushAliasStatue {
    }

    private TextView textView;
    private ImageView iv_qr;
    private Handler mainHandler = new Handler();
    private static final boolean cleanDb = false;
    private static final boolean cleanRegisterTag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_un_play);

        //为了防止重复启动多个闪屏页面
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) > 0) {
            finish();
            return;
        }
        new WifiLinkHelp().linkWifi(this);
        textView = findViewById(R.id.tv);
        iv_qr = findViewById(R.id.iv_qr);
        String screenSize = XSPSystem.getInstance().getScreenSize();
        if (TextUtils.isEmpty(screenSize) || screenSize.equals("unknown")) {
            screenSize = getScreenSizeOfDevice();
        }
        ScreenInfManage.getInstance().getScreenInfTag().setScreenSize(screenSize);
        mPresent = new InactivePresent(this);
        XSPSystem.startWifi(PjjApplication.application);
        reset = getIntent().getBooleanExtra(RESET, false);
        String xmlForKey = SharedUtils.getXmlForKey(SharedUtils.STOP_RECOVER);
        boolean equals;
        if (TextUtils.isEmpty(xmlForKey)) {
            equals = true;
        } else {
            equals = "1".equals(xmlForKey);
        }
        PjjApplication.application.setFlag(equals);
        if (BuildConfig.DEBUG) {
            boolean b = SuperUser.checkGetRootAuth();
            Log.e("TAG", "root检测 root = " + b);
        }
        if (cleanRegisterTag) {
            SharedUtils.cleanXml();
        }
        if (cleanDb) {
            try {
                DataCleanManager.cleanDatabases(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                DataCleanManager.deleteSdcard(PjjApplication.App_Path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ScreenInfManage.getInstance().setOnStartMessageListener(new ScreenInfManage.OnStartMessageListener() {
            @Override
            public void receiveStartMessage() {
                handleStartMessage();
            }

            @Override
            public void activateXsp() {
                //startActivateNext();
                mPresent.setActivateScreenStatue(ActivateStatue.createActivateBean());
            }
        });
        String onlyCode = XSPSystem.getInstance().getOnlyCode();
        textView.setText(onlyCode + "\n获取数据中。。。");

        if (reset) {//重置
            initQr(onlyCode);
            //mPresent.setActivateScreenStatue(ActivateStatue.createUnActivateBean());
            textView.setText(onlyCode + "\n等待激活。。。");
        } else {
            setJpushAlias();
        }
    }

    private void initQr(String onlyCode) {
        iv_qr.setVisibility(View.VISIBLE);
        textView.post(() -> {
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            int measuredWidth = (int) (0.87 * displayMetrics.widthPixels);
            if (measuredWidth < 100) {
                measuredWidth = (int) (textView.getMeasuredWidth() * 3.5);
            }
            ViewGroup.LayoutParams params = iv_qr.getLayoutParams();
            params.width = measuredWidth;
            params.height = measuredWidth;
            iv_qr.setLayoutParams(params);
            HashMap<String, String> stringStringHashMap = new HashMap<>();
            stringStringHashMap.put("screenId", onlyCode);
            stringStringHashMap.put("screenType", XSPSystem.getInstance().getScreenType());
            stringStringHashMap.put("appType", BuildConfig.APP_TYPE ? "formal" : "test");
            String json = TextJsonUtils.toJsonString(stringStringHashMap);
            //Log.e("TAG", "onCreate: json=" + json);
            String content = "http://protal.test.pingjiajia.cn/screenAdministration/#/registrationScreen?content=" + Base64Utils.encode(json.getBytes());
            //Bitmap bitmap = QrCodeUtil.makeQr(content, measuredWidth);
            //Log.e("TAG", "onCreate: content=" + content);
            Bitmap bitmap = null;
           /* try {
                bitmap = BitmapFactory.decodeStream(getResources().getAssets().open("ic_laun.png"));
                bitmap.setHasAlpha(true);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            bitmap = QrCodeUtil.addLogo(QrCodeUtil.makeQr(content, measuredWidth), ContextCompat.getDrawable(this, R.mipmap.ic_launcher));
            if (null != bitmap) {
                //Bitmap bitmap1 = QrCodeUtil.createQRCodeWithLogo(content, measuredWidth, bitmap);
                iv_qr.setImageBitmap(bitmap);
            }
        });
    }

    private String getScreenSizeOfDevice() {
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(point);
        Log.e("TAG", "getScreenSizeOfDevice: " + point.toString());
        DisplayMetrics dm = getResources().getDisplayMetrics();
        Log.e("TAG", "getScreenSizeOfDevice: " + dm.toString());
        double x = Math.pow(point.x / dm.xdpi, 2);
        double y = Math.pow(point.y / dm.ydpi, 2);
        Log.e("TAG", "getScreenSizeOfDevice: x=" + x + ", y=" + y);
        double screenInches = Math.sqrt(x + y);
        Log.e("TAG", "getScreenSizeOfDevice: " + screenInches);
        BigDecimal bg = new BigDecimal(screenInches);
        float pingMuSize = getPingMuSize(this);
        Log.e("TAG", "getScreenSizeOfDevice: pingMuSize=" + pingMuSize);
        return bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "";
    }

    /**
     * @ 获取当前手机屏幕尺寸
     */
    public static float getPingMuSize(Context mContext) {
        //f6 zuida
        String screenSize = XSPSystem.getInstance().getScreenSize();
        int densityDpi = mContext.getResources().getDisplayMetrics().densityDpi;
        float scaledDensity = mContext.getResources().getDisplayMetrics().scaledDensity;
        float density = mContext.getResources().getDisplayMetrics().density;
        float xdpi = mContext.getResources().getDisplayMetrics().xdpi;
        float ydpi = mContext.getResources().getDisplayMetrics().ydpi;
        int width = mContext.getResources().getDisplayMetrics().widthPixels;
        int height = mContext.getResources().getDisplayMetrics().heightPixels;

        // 这样可以计算屏幕的物理尺寸
        float width2 = (width / xdpi) * (width / xdpi);
        float height2 = (height / ydpi) * (width / xdpi);

        Log.e("TAG", "getPingMuSize: width2=" + width2 + ", height2" + height2);

        return (float) Math.sqrt(width2 + height2);
    }

    private void setJpushAlias() {
        String aliasStatue = SharedUtils.getXmlForKey(SharedUtils.ALIAS_CODE);
        TagAliasOperatorHelper.getInstance().setOnAliasListener(this);
        if (null == aliasStatue) {
            setJPushAlias();
            return;
        }
        switch (aliasStatue) {
            case STATUE_JPUSH_ALIAS + "":
                handlerActiveCode(SharedUtils.getActiveCode());
                break;
            default:
                setJPushAlias();
                break;
        }
    }

    private void handleStartMessage() {
        if ((STATUE_ACTIVATE + "").equals(SharedUtils.getActiveCode())) {
            startCheckUpdateNotes();
        }
    }

    @Override
    public void handlerActiveCode(String activeCode) {
        Log.e("TAG", "handlerActiveCode:xml= " + activeCode);
        if (null == activeCode) {
            mPresent.sendOnlyTagForRegistered(ScreenInfManage.getInstance().getScreenInfTag());
            return;
        }
        switch (activeCode) {
            case STATUE_ACTIVATE + "":
                startActivateNext();
                break;
            case STATUE_REGISTER + "":
                //等待后台激活
                String onlyCode = XSPSystem.getInstance().getOnlyCode();
                initQr(onlyCode);
                textView.setText(onlyCode + "\n等待激活。。。");
                break;
            default:
                mPresent.sendOnlyTagForRegistered(ScreenInfManage.getInstance().getScreenInfTag());
        }
    }

    @Override
    public void updateText(String text) {
        textView.setText(text);
    }

    @Override
    public void setActivateStatueResultSuccess() {
        //textView.setText(XSPSystem.getInstance().getOnlyCode() + ",等待启动。。。");
        updateActivateStatueXml(STATUE_ACTIVATE);
        startActivateNext();
    }

    private void updateActivateStatueXml(@XspActivateStatue int statue) {
        SharedUtils.saveForXml(SharedUtils.ACTIVE_CODE, Integer.toString(statue));
    }

    private void updateJpushAliasStatueXml(@XspJpushAliasStatue int statue) {
        SharedUtils.saveForXml(SharedUtils.ALIAS_CODE, Integer.toString(statue));
    }

    @Override
    public void setActivateStatueResultFail(String error) {
        Log.e("TAG", "setActivateStatueResultFail: " + error);
        mainHandler.postDelayed(() -> mPresent.setActivateScreenStatue(ActivateStatue.createActivateBean()), DELAYED_TIME);
    }

    @Override
    public void failStatue(String error) {
        super.failStatue(error);
        Log.e("TAG", "failStatue: " + error);
    }

    @Override
    public void registerFail(String msg) {

    }

    private void startActivateNext() {
        if (ScreenInfManage.ADD_START_STATUE) {
            textView.setText(XSPSystem.getInstance().getOnlyCode() + ",等待开启");
        } else {
            startCheckUpdateNotes();
        }
    }

    private void startPlayActivity() {
        Log.e("TAG", "startPlayActivity: ");
        startActivity(new Intent(this, MediaActivity.class));
        finish();
    }

    private void startCheckUpdateNotes() {
        String path = SharedUtils.getXmlForKey(SharedUtils.NEW_APP_PATH);
        if (!TextUtils.isEmpty(path)) {
            try {
                new File(path).getAbsoluteFile().delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
            SharedUtils.saveForXml(SharedUtils.NEW_APP_PATH, "");
        }
        String date = SharedUtils.getXmlForKey(SharedUtils.VERSION_UPDATE);
        Log.e("TAG", "startCheckUpdateNotes: " + date);
        if (!TextUtils.isEmpty(date) && !"下载".equals(date)) {
            textView.setText("发送升级反馈中。。。");
            SharedUtils.saveForXml(SharedUtils.VERSION_UPDATE, "");
            AppUpload appUpload = new AppUpload();
            appUpload.setType(2);
            appUpload.setAppName(date);
            appUpload.setScreenId(XSPSystem.getInstance().getOnlyCode());
            appUpload.setScreenType(XSPSystem.getInstance().getScreenType());
            appUpload.setVersionCode(BuildConfig.VERSION_CODE);
            appUpload.setVersionName(BuildConfig.VERSION_NAME);
            android.util.Log.e("TAG", "startCheckUpdateNotes=" + date);
            mPresent.uploadUpdateAppSuccessInf(appUpload);
        } else {
            sendAppUpdateSuccess();
        }
    }

    private void startCheckSystemUpdateNotes() {
        String systemVersion = SharedUtils.getXmlForKey(SharedUtils.SYSTEM_API_VERSION);
        String androidDisplay = XSPSystem.getInstance().getAndroidDisplay();
        android.util.Log.e("TAG", "startCheckSystemUpdateNotes=" + systemVersion + ", " + androidDisplay);
        if (TextUtils.isEmpty(systemVersion)) {
            SharedUtils.saveForXml(SharedUtils.SYSTEM_API_VERSION, androidDisplay);
            sendSystemUpdateSuccess();
        } else {
            if (systemVersion.equals(androidDisplay)) {
                sendSystemUpdateSuccess();
            } else {
                sendSystemUpdateInf();
            }
        }
    }

    private void sendSystemUpdateInf() {
        AppUpload appUpload = XSPSystem.getInstance().createAppUpload();
        String onlyCode = XSPSystem.getInstance().getOnlyCode();
        appUpload.setScreenType(XSPSystem.getInstance().getScreenType());
        if (XSPSystem.getInstance().getSystemUpdateFileName().equals("update.img")) {
            File file = new File(Environment.getExternalStorageDirectory().toString() + "/update.img");
            if (file.exists()) {
                file.getAbsoluteFile().delete();
            }
        }
        if (TextUtils.isEmpty(onlyCode)) {
            mainHandler.postDelayed(() -> sendSystemUpdateInf(), 3000);
        } else {
            appUpload.setScreenId(onlyCode);
            mPresent.uploadUpdateSystemSuccessInf(appUpload);
        }
    }

    @Override
    public void sendAppUpdateSuccess() {
        Log.e("TAG", "sendAppUpdateSuccess: ");
        startCheckSystemUpdateNotes();
    }

    @Override
    public void sendSystemUpdateSuccess() {
        Log.e("TAG", "sendSystemUpdateSuccess: ");
        startPlayActivity();
    }

    @Override
    public void result(boolean tag) {
        runOnUiThread(() -> {
            if (tag) {
                updateJpushAliasStatueXml(STATUE_JPUSH_ALIAS);
                handlerActiveCode(SharedUtils.getActiveCode());
            } else {
                String msg = "";
                if (sequence > 3) {
                    msg = "\n请检查网络";
                }
                textView.setText(XSPSystem.getInstance().getOnlyCode() + "\n获取数据中。。。" + "\n设置标识失败，重试中-" + (sequence - 1) + msg);
                mainHandler.postDelayed(() -> setJPushAlias(), 2000);
            }
        });
    }

    /**
     * 设置极光推送设备别名
     */
    private void setJPushAlias() {
        String alias = XSPSystem.getInstance().getOnlyCode();
        int action = ACTION_SET;
        TagAliasOperatorHelper.TagAliasBean tagAliasBeanAlias = new TagAliasOperatorHelper.TagAliasBean();
        tagAliasBeanAlias.action = action;
        sequence++;
        tagAliasBeanAlias.alias = alias;
        tagAliasBeanAlias.isAliasAction = true;
        TagAliasOperatorHelper.getInstance().handleAction(getApplicationContext(), sequence, tagAliasBeanAlias);
    }

    @Override
    protected void onDestroy() {
        TagAliasOperatorHelper.getInstance().setOnAliasListener(null);
        ScreenInfManage.getInstance().setOnStartMessageListener(null);
        super.onDestroy();
    }
}
