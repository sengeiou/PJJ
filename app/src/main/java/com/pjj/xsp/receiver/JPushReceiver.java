package com.pjj.xsp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.pjj.xsp.module.ScreenInfManage;
import com.pjj.xsp.module.XspPlayUI;
import com.pjj.xsp.utils.ARSAUtils;
import com.pjj.xsp.utils.Log;
import com.pjj.xsp.utils.RSASubsectionUtile;
import com.pjj.xsp.utils.TextViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * Create by xinheng on 2018/10/15。
 * describe： 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * <p>
 * <p>
 * <p>
 * <p>
 * 2) 接收不到自定义消息
 */
public class JPushReceiver extends BroadcastReceiver {
    private static final String TAG = "TAG-JIGUANG-pjj";
    /**
     * 激活广告屏
     */
    private static final String ACTIVE_APP = "0001";
    /**
     * 正常工作的指令
     */
    private static final String APP_NORMAL_WORK = "0002";
    /**
     * app设置音量
     */
    private static final String APP_SET_VOLUME = "0003";
    /**
     * app设置为未激活状态
     */
    private static final String INACTIVE_APP = "0004";
    /**
     * 维保信息更换
     */
    private static final String WEI_BAO_INF_CHANGE = "0005";
    /**
     * 撤销订单
     */
    public static final String UNDO_ORDER = "0010";
    /**
     * 查询订单
     */
    public static final String SEARCH_TASK = "0011";
    /**
     * 重启
     */
    public static final String REBOOT_XSP = "0012";
    /**
     * app更新
     */
    public static final String UPDATE_APP = "0013";
    /**
     * 插播
     */
    public static final String INSERT_AD = "0015";
    /**
     * 更改默认便民
     */
    public static final String CHANGE_DEFAULT_BM = "0016";
    /**
     * 截屏
     */
    public static final String SCREENSHOTS = "0017";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        printBundle(bundle);
    }

    private static void printBundle(Bundle bundle) {
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_MESSAGE)) {
                String cipher = bundle.getString(JPushInterface.EXTRA_MESSAGE);
                if (TextUtils.isEmpty(cipher)) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }
                String string = RSASubsectionUtile.public_decipher(cipher);
                if (TextUtils.isEmpty(string)) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }
                Log.e(TAG, "JPush: " + string);
                try {
                    JSONObject json = new JSONObject(string);
                    String operateType = json.getString("operateType");
                    Log.e(TAG, "printBundle: " + operateType);
                    dealWithMessage(operateType, json);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Get message extra JSON error!");
                }
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                //2018年12月29日16:12:27 废弃
                /*String string = bundle.getString(JPushInterface.EXTRA_EXTRA);
                if (TextUtils.isEmpty(string)) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }
                Log.e(TAG, "JPush: " + string);
                try {
                    JSONObject json = new JSONObject(string);
                    String operateType = json.getString("operateType");
                    Log.e(TAG, "printBundle: " + operateType);
                    dealWithMessage(operateType, json);
                } catch (Exception e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }*/

            }
        }
    }

    /**
     * 信息处理
     *
     * @param code 指令
     * @param json json内容
     */
    private static void dealWithMessage(String code, JSONObject json) throws JSONException {
        if (null == code) {
            return;
        }
        switch (code) {
            case ACTIVE_APP://激活广告屏
                ScreenInfManage.getInstance().activateXsp();
                break;
            case APP_NORMAL_WORK://app开始正常工作
                ScreenInfManage.getInstance().startPlayView();
                break;
            case APP_SET_VOLUME://设置音量
                XspPlayUI.getInstall().setVolume(json.getString("setVoice"));
                break;
            case INACTIVE_APP://设置未激活
                XspPlayUI.getInstall().reset();
                break;
            case WEI_BAO_INF_CHANGE://更新维保信息
                XspPlayUI.getInstall().updateWeiBao();
                break;
            case UNDO_ORDER:
                XspPlayUI.getInstall().stopPlayOrder(json.getString("orderId"));
                break;
            case SEARCH_TASK:
                XspPlayUI.getInstall().searchTask(json.getString("orderId"));
                break;
            case REBOOT_XSP:
                XspPlayUI.getInstall().reboot();
                break;
            case UPDATE_APP:
                XspPlayUI.getInstall().updateApp(json.getString("updateAPKType"), json.getString("updateAPK"));
                break;
            case INSERT_AD:
                XspPlayUI.getInstall().insertAd(json.getString("fileType"), json.getString("drumbeatingName"));
                break;
            case CHANGE_DEFAULT_BM:
                String bmDefaultContent = json.getString("bmDefaultContent");
                if (!TextViewUtils.isEmpty(bmDefaultContent))
                    ScreenInfManage.default_bm = bmDefaultContent;
                XspPlayUI.getInstall().updateDefaultBm();
                break;
            case SCREENSHOTS:
                //String fileName = json.getString("fileName");
                String orderId = json.getString("orderId");
                XspPlayUI.getInstall().screenshots(orderId);
                break;
        }
    }

}
