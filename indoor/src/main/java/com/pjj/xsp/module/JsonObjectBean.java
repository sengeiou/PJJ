package com.pjj.xsp.module;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.pjj.xsp.db.DateTask;
import com.pjj.xsp.db.TaskText;
import com.pjj.xsp.utils.DateUtils;
import com.pjj.xsp.utils.Log;
import com.pjj.xsp.utils.TextJsonUtils;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;

import static com.pjj.xsp.module.bean.ResultBean.SUCCESS_CODE;

/**
 * Created by XinHeng on 2019/03/28.
 * describe：
 */
public class JsonObjectBean {
    private String flag;//结果码 String 0：失败 1：成功
    private String msg;//结果信息 String 返回结果信息描述
    private JsonObject jsonObject;
    private boolean isNowDay;
    private boolean limitTag;

    public boolean isSuccess() {
        return SUCCESS_CODE.equals(flag);
    }

    public JsonObjectBean(String json) {
        Log.e("TAG", json);
        try {
            JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
            msg = jsonObject.get("msg").getAsString();
            flag = jsonObject.get("flag").getAsString();
            this.jsonObject = jsonObject.get("data").getAsJsonObject();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    public String getMsg() {
        return msg;
    }

    public TaskText getTaskText() {
        if (!isSuccess()) {
            return null;
        }
        TaskText taskText = null;
        List<DateTask> dateList = null;
        String limit = "0";
        try {
            taskText = new TaskText();
            taskText.setOrderId(jsonObject.get("orderId").getAsString());
            taskText.setOrderType(jsonObject.get("orderType").getAsString());
            taskText.setTempletType(jsonObject.get("templetType").getAsString());
            JsonElement dateElement = jsonObject.get("orderTime");
            Type type = new TypeToken<List<DateTask>>() {
            }.getType();
            dateList = TextJsonUtils.gson.fromJson(dateElement, type);
            taskText.setDateTaskList(dateList);
            //这个放最后，为了检验
            taskText.setText(jsonObject.get("fileList").toString());
            taskText.setShowTime(jsonObject.get("showTime").getAsInt());
            JsonElement manageUser = jsonObject.get("manageUser");
            if (null != manageUser)
                limit = manageUser.getAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        limit = "manage".equals(limit) ? "1" : "0";
        limitTag = "1".equals(limit);
        if (TextJsonUtils.isNotEmpty(dateList)) {
            final boolean tag = !limitTag;
            Iterator<DateTask> it = dateList.iterator();
            while (it.hasNext()) {
                DateTask next = it.next();
                next.setLimitTag(limit);
                if (tag && !isNowDay && DateUtils.isNowDate(next.getDate())) {
                    isNowDay = true;
                }
            }
        }
        return taskText;
    }

    public boolean isNowDay() {
        return isNowDay;
    }

    public boolean isLimitTag() {
        return limitTag;
    }
}
