package com.pjj.xsp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pjj.xsp.PjjApplication;
import com.pjj.xsp.db.DateTask;
import com.pjj.xsp.db.MinePlayTask;
import com.pjj.xsp.db.TaskText;
import com.pjj.xsp.intent.http.RetrofitService;
import com.pjj.xsp.manage.XSPSystem;
import com.pjj.xsp.module.ScreenInfManage;
import com.pjj.xsp.module.XspPlayUI;
import com.pjj.xsp.utils.Base64Utils;
import com.pjj.xsp.utils.DateUtils;
import com.pjj.xsp.utils.FileUtils;
import com.pjj.xsp.utils.Log;
import com.pjj.xsp.utils.RSASubsectionUtile;
import com.pjj.xsp.utils.TextJsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

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
    public static final String ACTIVE_APP = "0001";
    /**
     * 正常工作的指令
     */
    public static final String APP_NORMAL_WORK = "0002";
    /**
     * app设置音量
     */
    public static final String APP_SET_VOLUME = "0003";
    /**
     * app设置为未激活状态
     */
    public static final String INACTIVE_APP = "0004";
    /**
     * 维保信息更换
     */
    public static final String WEI_BAO_INF_CHANGE = "0005";
    /**
     * 撤销订单
     */
    public static final String UNDO_ORDER = "0010";
    /**
     * 查询订单，获取任务
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
    /**
     * 查询新闻媒体订单
     * 如：轮播图
     */
    public static final String SEARCH_ROTATION_CHART_TASK = "0018";
    /**
     * 系统升级
     */
    public static final String UPDATE_SYSTEM = "0019";
    /**
     * 自用广告屏获取信息
     */
    public static final String USE_SELF = "0020";
    /**
     * 传统信息订单
     */
    public static final String TRADITIONAL_ORDER = "0021";
    /**
     * 临时停止使用或解除限制
     */
    public static final String TEMPORARY_STOP_USE = "0022";
    /**
     * 控制头部view显示或隐藏
     */
    public static final String CONTROLLER_HEAD_VIEW_SHOW_OR_HIDDEN = "0023";
    /**
     * 上传相关记录
     */
    public static final String UPLOAD_LOG_TXT = "0024";
    /**
     * 需要下载文件的命令数组
     */
    public static final String[] downloadFileCode = {SEARCH_TASK, UPDATE_APP, INSERT_AD, SCREENSHOTS, SEARCH_ROTATION_CHART_TASK, UPDATE_SYSTEM, USE_SELF, TRADITIONAL_ORDER};

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
                Log.i(TAG, cipher);
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
                    if (SCREENSHOTS.equals(operateType)) {
                        String orderId = json.getString("orderId");
                        XspPlayUI.getInstall().screenshots(orderId);
                    } else if (ACTIVE_APP.equals(operateType)) {
                        ScreenInfManage.getInstance().activateXsp();
                    } else {
                        //dealWithMessage(operateType, json);
                        XspPlayUI.getInstall().insertJson(string);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Get message extra JSON error!");
                }
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                //2018年12月29日16:12:27 废弃
                String string = bundle.getString(JPushInterface.EXTRA_EXTRA);

                Log.e(TAG, "JPush: " + string);

                /*try {
                    JSONObject json = new JSONObject(new String(Base64Utils.decode(string), "utf-8"));
                    String operateType = json.getString("operateType");
                    Log.e(TAG, "printBundle: " + operateType);
                    dealWithMessage(operateType, json);
                } catch (Exception e) {
                    e.printStackTrace();
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
    public static void dealWithMessage(String code, JSONObject json) throws JSONException {
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
                String updateAPKType = null;
                try {
                    updateAPKType = json.getString("updateAPKType");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                XspPlayUI.getInstall().updateApp(updateAPKType, json.getString("updateAPK"));
                break;
            case INSERT_AD:
                XspPlayUI.getInstall().insertAd(json.getString("fileType"), json.getString("drumbeatingName"));
                break;
            case CHANGE_DEFAULT_BM:
                break;
            case SCREENSHOTS:
                //String fileName = json.getString("fileName");
                String orderId = json.getString("orderId");
                XspPlayUI.getInstall().screenshots(orderId);
                break;
            case SEARCH_ROTATION_CHART_TASK:
                XspPlayUI.getInstall().searchTask(json.getString("orderId"));
                break;
            case UPDATE_SYSTEM:
                String systemDownloadPath = json.getString("systemDownloadPath");
                String systemFileName = null;
                if (json.has("systemFileName")) {
                    try {
                        systemFileName = json.getString("systemFileName");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (TextUtils.isEmpty(systemDownloadPath)) {
                    return;
                }
                downLoadFile(systemDownloadPath, systemFileName);
                //XspPlayUI.getInstall().searchTask(systemDownloadPath);
                break;
            case TRADITIONAL_ORDER:
                XspPlayUI.getInstall().searchTask(json.getString("orderId"));
                break;
            case USE_SELF:
                MinePlayTask taskText = getTaskText(json);
                XspPlayUI.getInstall().addMineTask(taskText);
                break;
            case TEMPORARY_STOP_USE: {//1 解除 0禁用
                boolean turnOff = "1".equals(json.getString("flag"));
                XspPlayUI.getInstall().stopOrRecoverStatue(turnOff);
            }
            break;
            case CONTROLLER_HEAD_VIEW_SHOW_OR_HIDDEN: {
                boolean flag = "1".equals(json.getString("showHeadInfo"));
                XspPlayUI.getInstall().controllerHeadViewShowOrHidden(flag);
            }
            break;
        }
    }

    public static MinePlayTask getTaskText(JSONObject jsonObject) {
        MinePlayTask taskText = null;
        List<DateTask> dateList = null;
        try {
            taskText = new MinePlayTask();
            taskText.setOrderId(jsonObject.getString("orderId"));
            taskText.setTemplateType(jsonObject.getString("templetType"));
            //这个放最后，为了检验
            taskText.setJson(jsonObject.getJSONArray("fileList").toString());
            taskText.setShowTime(Integer.parseInt(jsonObject.getString("showTime")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return taskText;
    }

    public static void downLoadFile(String filePath, String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            fileName = "update.zip";
        }
        String fileSdcardPath = PjjApplication.App_Path + fileName;
        File file = new File(fileSdcardPath);
        if (file.exists()) {
            file.getAbsoluteFile().delete();
        }
        RetrofitService.getInstance().downloadBigFile(filePath, fileName, new FileUtils.OnDownloadListener() {
            @Override
            public void success() {
                XSPSystem.getInstance().systemUpdate(fileSdcardPath);
            }

            @Override
            public void fail() {

            }
        });
    }
}
