package com.pjj.xsp.module;

import com.pjj.xsp.db.DaoManager;
import com.pjj.xsp.db.DateTask;
import com.pjj.xsp.db.DateTaskDao;
import com.pjj.xsp.db.HandleDaoDb;
import com.pjj.xsp.db.TaskText;
import com.pjj.xsp.utils.DateUtils;
import com.pjj.xsp.utils.TextJsonUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MediaLimitTaskCache extends MediaTaskCache {
    private List<DateTask> listLimit = new ArrayList<>();

    MediaLimitTaskCache(String tag) {
        super(tag);
    }

    void initTodayAllLimitTask() {
        listLimit.clear();
        String date = DateUtils.getNowDate();
        QueryBuilder<DateTask> dateTaskQueryBuilder = DaoManager.getInstance().getDaoSession().queryBuilder(DateTask.class);
        dateTaskQueryBuilder.where(DateTaskDao.Properties.Date.eq(date));
        dateTaskQueryBuilder.where(DateTaskDao.Properties.LimitTag.eq("1"));
        List<DateTask> list = dateTaskQueryBuilder.list();
        if (TextJsonUtils.isNotEmpty(list)) {
            listLimit.addAll(list);
        }
    }

    boolean addLimitListTask(DateTask dateTask) {
        String nowDate = DateUtils.getNowDate();
        if (!nowDate.equals(dateTask.getDate())) {
            //不是当天任务
            return false;
        }
        int nowHour = DateUtils.getNowHour();
        String[] split = dateTask.getHour().split(",");
        if (contains(split, String.valueOf(nowHour))) {
            //此小时任务
            listLimit.add(dateTask);
            findAndAdd(dateTask.getId());
            return true;
        }
        return false;
    }


    boolean hasHourTask(int hour) {
        Iterator<DateTask> it = listLimit.iterator();
        String hourS = String.valueOf(hour);
        boolean hasTag = false;
        while (it.hasNext()) {
            DateTask next = it.next();
            String hour1 = next.getHour();
            String[] split = hour1.split(",");
            if (contains(split, hourS)) {
                hasTag = true;
                Long textTaskId = next.getId();
                findAndAdd(textTaskId);
            }
        }
        return hasTag;
    }

    private void findAndAdd(Long textTaskId) {
        TaskText taskText = HandleDaoDb.queryBeanById(TaskText.class, textTaskId);
        if (null != taskText) {
            PlayTaskParent playTaskParent = taskText.getPlayTaskParent();
            if (null != playTaskParent) {
                addTask(playTaskParent);
            }
        }
    }

    private boolean contains(String[] arrays, String hour) {
        if (null == arrays || arrays.length == 0) {
            return false;
        }
        for (int i = 0; i < arrays.length; i++) {
            if (hour.equals(arrays[i])) {
                return true;
            }
        }
        return false;
    }

    void clearAll() {
        clearTask();
        listLimit.clear();
    }
}
