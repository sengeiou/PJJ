package com.pjj.xsp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IntDef;

import com.pjj.xsp.utils.Log;

import android.util.DisplayMetrics;
import android.widget.TextView;

import com.pjj.xsp.R;
import com.pjj.xsp.PjjApplication;
import com.pjj.xsp.contract.InactiveContract;
import com.pjj.xsp.manage.XSPSystem;
import com.pjj.xsp.module.ScreenInfManage;
import com.pjj.xsp.module.parameter.ActivateStatue;
import com.pjj.xsp.present.InactivePresent;
import com.pjj.xsp.utils.SharedUtils;
import com.pjj.xsp.utils.TagAliasOperatorHelper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.math.BigDecimal;

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
    private static final int DELAYED_TIME = 200;


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STATUE_ACTIVATE, STATUE_UN_ACTIVATE})
    @interface XspActivateStatue {
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STATUE_JPUSH_ALIAS, STATUE_UN_JPUSH_ALIAS})
    @interface XspJpushAliasStatue {
    }

    private TextView textView;
    private Handler mainHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_un_play);
        textView = findViewById(R.id.tv);
        ScreenInfManage.getInstance().getScreenInfTag().setScreenSize(getScreenSizeOfDevice());
        mPresent = new InactivePresent(this);
        XSPSystem.startWifi(PjjApplication.application);
        reset = getIntent().getBooleanExtra(RESET, false);
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
        textView.setText(XSPSystem.getInstance().getOnlyCode() + "\n获取数据中。。。");
        if (reset) {//重置
            //mPresent.setActivateScreenStatue(ActivateStatue.createUnActivateBean());
            textView.setText(XSPSystem.getInstance().getOnlyCode() + ",等待激活。。。");
        } else {
            setJpushAlias();
        }
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
            startPlayActivity();
        }
    }

    @Override
    public void handlerActiveCode(String activeCode) {
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
                textView.setText(XSPSystem.getInstance().getOnlyCode() + "\n等待激活。。。");
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
        mainHandler.postDelayed(() -> mPresent.setActivateScreenStatue(ActivateStatue.createActivateBean()), DELAYED_TIME);
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

    private void startActivateNext() {
        if (ScreenInfManage.ADD_START_STATUE) {
            textView.setText(XSPSystem.getInstance().getOnlyCode() + ",等待开启");
        } else {
            startPlayActivity();
        }
    }

    private void startPlayActivity() {
        startActivity(new Intent(this, PlayActivity.class));
        finish();
    }

    @Override
    public void result(boolean tag) {
        runOnUiThread(() -> {
            if (tag) {
                updateJpushAliasStatueXml(STATUE_JPUSH_ALIAS);
                handlerActiveCode(SharedUtils.getActiveCode());
            } else {
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
