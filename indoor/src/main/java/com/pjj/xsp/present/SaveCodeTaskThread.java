package com.pjj.xsp.present;

import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import com.pjj.xsp.utils.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pjj.xsp.db.CodeJsonTask;
import com.pjj.xsp.db.MinePlayTask;
import com.pjj.xsp.intent.http.RetrofitService;
import com.pjj.xsp.manage.XSPSystem;
import com.pjj.xsp.module.ScreenInfManage;
import com.pjj.xsp.module.XspPlayUI;
import com.pjj.xsp.module.bean.ResultBean;
import com.pjj.xsp.receiver.JPushReceiver;
import com.pjj.xsp.utils.TextJsonUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SaveCodeTaskThread extends HandlerThread {
    private Handler handler;
    private JsonParser jsonParser;
    private static final int delayTime = 20 * 60 * 1000;//20min

    public SaveCodeTaskThread(String name) {
        super(name);
        jsonParser = new JsonParser();
    }

    public Handler getHandler() {
        if (null == handler) {
            handler = new Handler(getLooper());
        }
        return handler;
    }

    public void post(Runnable runnable) {
        handler.post(runnable);
    }

    public void postDelayed(Runnable runnable, int delayTime) {
        handler.postDelayed(runnable, delayTime);
    }

    public void post(String jsonCodeTask) {
        post(() -> post_(jsonCodeTask));
    }

    private boolean post_(String jsonCodeTask) {
        JsonElement parse = jsonParser.parse(jsonCodeTask);
        if (null != parse && parse.isJsonObject()) {
            JsonObject asJsonObject = parse.getAsJsonObject();
            String operateType = aGetString(asJsonObject, "operateType");
            if (JPushReceiver.SCREENSHOTS.equals(operateType)) {
                return false;
            }
            String commandId = asJsonObject.get("commandId").getAsString();
            if (TextUtils.isEmpty(commandId)) {
                return false;
            }
            if (CodeJsonTask.checkIsHas(commandId)) {//已存在
                return false;
            }
            CodeJsonTask codeJsonTask = new CodeJsonTask();
            codeJsonTask.setCommandId(commandId);
            codeJsonTask.setJson(jsonCodeTask);
            if (codeJsonTask.saveDb()) {
                setCodeTaskFeedback(commandId, codeJsonTask, operateType);
                //boolean downloadFileTag = isDownloadFile(operateType);
                dealWithJson(operateType, asJsonObject);
            }
        }
        return true;
    }

    private boolean isDownloadFile(String code) {
        final String[] array = JPushReceiver.downloadFileCode;
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(code)) {
                return true;
            }
        }
        return false;
    }

    private void dealWithJson(String code, JsonObject object) {
        if (TextUtils.isEmpty(code)) {
            return;
        }
        switch (code) {
            case JPushReceiver.ACTIVE_APP://激活广告屏
                //ScreenInfManage.getInstance().activateXsp();
                break;
            case JPushReceiver.APP_NORMAL_WORK://app开始正常工作
                ScreenInfManage.getInstance().startPlayView();
                break;
            case JPushReceiver.APP_SET_VOLUME://设置音量
                XspPlayUI.getInstall().setVolume(aGetString(object, "setVoice"));
                break;
            case JPushReceiver.INACTIVE_APP://设置未激活
                XspPlayUI.getInstall().reset();
                break;
            case JPushReceiver.WEI_BAO_INF_CHANGE://更新维保信息
                XspPlayUI.getInstall().updateWeiBao();
                break;
            case JPushReceiver.UNDO_ORDER:
                XspPlayUI.getInstall().stopPlayOrder(aGetString(object, "orderId"));
                break;
            case JPushReceiver.SEARCH_TASK:
                XspPlayUI.getInstall().searchTask(aGetString(object, "orderId"));
                break;
            case JPushReceiver.REBOOT_XSP:
                XspPlayUI.getInstall().reboot();
                break;
            case JPushReceiver.UPDATE_APP:
                String path = aGetString(object, "updateAPK");
                //String updateAPKType = aGetString(object, "updateAPKType");
                if (!TextUtils.isEmpty(path)) {
                    XspPlayUI.getInstall().downloadFile(path, path, code);
                }
                break;
            case JPushReceiver.INSERT_AD:
                XspPlayUI.getInstall().insertAd(aGetString(object, "fileType"), aGetString(object, "drumbeatingName"));
                break;
            case JPushReceiver.CHANGE_DEFAULT_BM:
                break;
            case JPushReceiver.SCREENSHOTS:
                //String fileName = json.getString("fileName");
                String orderId = aGetString(object, "orderId");
                XspPlayUI.getInstall().screenshots(orderId);
                break;
            case JPushReceiver.SEARCH_ROTATION_CHART_TASK:
                XspPlayUI.getInstall().searchTask(aGetString(object, "orderId"));
                break;
            case JPushReceiver.UPDATE_SYSTEM:
                String systemDownloadPath = aGetString(object, "systemDownloadPath");
                if (TextUtils.isEmpty(systemDownloadPath)) {
                    break;
                }
                String systemFileName = aGetString(object, "systemFileName");
                if (TextUtils.isEmpty(systemFileName)) {
                    systemFileName = XSPSystem.getInstance().getSystemUpdateFileName();
                }
                Log.e("TAG", "dealWithJson:UPDATE_SYSTEM " + systemDownloadPath);
                XspPlayUI.getInstall().downloadFile(systemDownloadPath, systemFileName, code);
                break;
            case JPushReceiver.TRADITIONAL_ORDER:
                XspPlayUI.getInstall().searchTask(aGetString(object, "orderId"));
                break;
            case JPushReceiver.USE_SELF:
                MinePlayTask taskText = getTaskText(object);
                XspPlayUI.getInstall().addMineTask(taskText);
                break;
            case JPushReceiver.TEMPORARY_STOP_USE: {//1 解除 0禁用
                boolean turnOff = "1".equals(aGetString(object, "flag"));
                XspPlayUI.getInstall().stopOrRecoverStatue(turnOff);
            }
            break;
            case JPushReceiver.CONTROLLER_HEAD_VIEW_SHOW_OR_HIDDEN: {
                boolean flag = "1".equals(aGetString(object, "showHeadInfo"));
                XspPlayUI.getInstall().controllerHeadViewShowOrHidden(flag);
            }
            break;
            case JPushReceiver.UPLOAD_LOG_TXT:
                XspPlayUI.getInstall().uploadFile();
                break;
        }
    }

    private void setCodeTaskFeedback(String commandId, CodeJsonTask codeJsonTask, String code) {
        final List<CodeJsonTask> codeJsonTasks = CodeJsonTask.filterFeedbackFail();
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(commandId);
        if (TextJsonUtils.isNotEmpty(codeJsonTasks)) {
            for (int i = 0; i < codeJsonTasks.size(); i++) {
                CodeJsonTask bean = codeJsonTasks.get(i);
                stringBuilder.append(",");
                stringBuilder.append(bean.getCommandId());
            }
        }
        Map<String, String> map = new HashMap<>(1);
        final String value = stringBuilder.toString();
        map.put("commandId", value);
        if (JPushReceiver.REBOOT_XSP.equals(code)) {
            codeJsonTask.setDeleteTag("1");
            codeJsonTask.update();
        }
        RetrofitService.getInstance().updateScreenCommandStatus(TextJsonUtils.toJsonString(map), new RetrofitService.CallbackClass<ResultBean>(ResultBean.class) {
            @Override
            protected void result(ResultBean resultBean) {
                //子线程更新数据库
                post(() -> feedbackSuccess(codeJsonTasks, codeJsonTask, value));
            }

            @Override
            protected void fail(String error) {
                Log.e("TAG", "updateScreenCommandStatus fail: " + error);
                failCodeTaskFeedback(codeJsonTask);
            }
        });
    }

    private void feedbackSuccess(List<CodeJsonTask> codeJsonTasks, CodeJsonTask codeJsonTask, String value) {
        if (TextJsonUtils.isNotEmpty(codeJsonTasks)) {
            for (CodeJsonTask bean : codeJsonTasks) {
                bean.setDeleteTag("1");
                bean.update();
            }
        }
        codeJsonTask.setDeleteTag("1");
        codeJsonTask.update();
        removeCodeTaskDelayed();
    }

    private void removeCodeTaskDelayed() {
        postDelayed(CodeJsonTask::deleteCommandId, delayTime);
    }

    private void failDownloadTaskFeedback(CodeJsonTask codeJsonTask) {
        codeJsonTask.setDownload("1");
        codeJsonTask.update();
    }

    private void failCodeTaskFeedback(CodeJsonTask codeJsonTask) {
        codeJsonTask.setFailFeedback("1");
        codeJsonTask.update();
    }

    private String aGetString(JsonObject object, String key) {
        JsonElement jsonElement = object.get(key);
        if (null != jsonElement) {
            return jsonElement.getAsString();
        }
        return null;
    }

    private MinePlayTask getTaskText(JsonObject jsonObject) {
        MinePlayTask taskText = null;
        try {
            taskText = new MinePlayTask();
            taskText.setOrderId(aGetString(jsonObject, "orderId"));
            taskText.setTemplateType(aGetString(jsonObject, "templetType"));
            //这个放最后，为了检验
            JsonElement fileList = jsonObject.get("fileList");
            taskText.setJson(TextJsonUtils.toJsonString(fileList));
            String showTime = aGetString(jsonObject, "showTime");
            if (null == showTime) {
                showTime = "15";
            }
            taskText.setShowTime(Integer.parseInt(showTime));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return taskText;
    }
}
