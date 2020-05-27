package com.pjj.xsp.db;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.pjj.xsp.module.PlayTaskParent;
import com.pjj.xsp.module.TextTask;
import com.pjj.xsp.module.bean.FileBean;
import com.pjj.xsp.utils.TextJsonUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

import java.lang.reflect.Type;
import java.util.List;

@Entity
public class MinePlayTask {
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String orderId;
    private String templateType;
    @NotNull
    private int showTime = 15;
    @NotNull
    private String json;

    @Generated(hash = 1023366983)
    public MinePlayTask(Long id, @NotNull String orderId, String templateType,
                        int showTime, @NotNull String json) {
        this.id = id;
        this.orderId = orderId;
        this.templateType = templateType;
        this.showTime = showTime;
        this.json = json;
    }

    @Generated(hash = 23168385)
    public MinePlayTask() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return this.orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTemplateType() {
        return this.templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public int getShowTime() {
        return this.showTime;
    }

    public void setShowTime(int showTime) {
        this.showTime = showTime;
    }

    public String getJson() {
        return this.json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    private boolean isFullScreen() {
        return true;
    }

    public PlayTaskParent getPlayTaskParent() {
        if (isFullScreen()) {
            Type type = new TypeToken<List<FileBean>>() {
            }.getType();
            List<FileBean> fileBeans = null;
            try {
                fileBeans = TextJsonUtils.gson.fromJson(json, type);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
            if (TextJsonUtils.isNotEmpty(fileBeans)) {
                FileBean bean;
                String videoPath = null;
                if ("3".equals(templateType) || "5".equals(templateType)) {
                    if (fileBeans.size() != 2) {
                        return null;
                    } else {
                        bean = fileBeans.get(0);
                        if ("1".equals(bean.getType())) {
                            videoPath = fileBeans.get(1).getFileName();
                        } else {
                            videoPath = bean.getFileName();
                            bean = fileBeans.get(1);
                        }
                    }
                } else {
                    bean = fileBeans.get(0);
                }
                //下载
                final String finalVideoPath = videoPath;
                return new TextTask(bean.getFileName(), bean.getType(), templateType) {
                    @Override
                    public String getVideoPath() {
                        return finalVideoPath;
                    }

                    @Override
                    public int getPlayTime() {
                        return getShowTime();
                    }

                    @Override
                    public String getTaskTag() {
                        return getOrderId();
                    }
                };
            }
        }
        return null;
    }
}
