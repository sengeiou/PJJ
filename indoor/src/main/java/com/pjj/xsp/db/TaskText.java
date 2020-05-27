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
import org.greenrobot.greendao.annotation.ToMany;

import java.lang.reflect.Type;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * Created by XinHeng on 2019/03/28.
 * describe：
 */
@Entity
public class TaskText {
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String text;
    @NotNull
    private String orderType;
    @NotNull
    private String templetType;
    @NotNull
    private String orderId;
    @NotNull
    private int showTime = 15;
    @ToMany(referencedJoinProperty = "id")
    private List<DateTask> dateTaskList;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1359485572)
    private transient TaskTextDao myDao;

    @Generated(hash = 1995237364)
    public TaskText(Long id, @NotNull String text, @NotNull String orderType,
                    @NotNull String templetType, @NotNull String orderId, int showTime) {
        this.id = id;
        this.text = text;
        this.orderType = orderType;
        this.templetType = templetType;
        this.orderId = orderId;
        this.showTime = showTime;
    }

    @Generated(hash = 1627630791)
    public TaskText() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getTempletType() {
        return templetType;
    }

    public void setTempletType(String templetType) {
        this.templetType = templetType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getShowTime() {
        return showTime;
    }

    public void setShowTime(int showTime) {
        this.showTime = showTime;
    }

    public boolean isFullScreen() {
        //return "1".equals(templetType) || "2".equals(templetType);
        return true;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1742236040)
    public List<DateTask> getDateTaskList() {
        if (dateTaskList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DateTaskDao targetDao = daoSession.getDateTaskDao();
            List<DateTask> dateTaskListNew = targetDao
                    ._queryTaskText_DateTaskList(id);
            synchronized (this) {
                if (dateTaskList == null) {
                    dateTaskList = dateTaskListNew;
                }
            }
        }
        return dateTaskList;
    }

    public void setDateTaskList(List<DateTask> dateTaskList) {
        this.dateTaskList = dateTaskList;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 2063847972)
    public synchronized void resetDateTaskList() {
        dateTaskList = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1521364898)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTaskTextDao() : null;
    }

    public PlayTaskParent getPlayTaskParent() {
        if (isFullScreen()) {
            Type type = new TypeToken<List<FileBean>>() {
            }.getType();
            List<FileBean> fileBeans = null;
            try {
                fileBeans = TextJsonUtils.gson.fromJson(text, type);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
            if (TextJsonUtils.isNotEmpty(fileBeans)) {
                FileBean bean;
                String videoPath = null;
                if ("3".equals(templetType)) {
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
                return new TextTask(bean.getFileName(), bean.getType(), templetType) {
                    @Override
                    public String getTempletType() {
                        return templetType;
                    }

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
