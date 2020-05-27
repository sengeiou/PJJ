package com.pjj.xsp.db;

import android.text.TextUtils;
import android.util.SparseIntArray;

import com.pjj.xsp.PjjApplication;
import com.pjj.xsp.module.MediaTaskCache;
import com.pjj.xsp.module.MediaTaskCacheHelp;
import com.pjj.xsp.module.PlayTaskParent;
import com.pjj.xsp.module.ScreenInfManage;
import com.pjj.xsp.module.XspPlayUI;
import com.pjj.xsp.module.bean.ScreenInfBean;
import com.pjj.xsp.utils.DateUtils;
import com.pjj.xsp.utils.Log;
import com.pjj.xsp.utils.SharedUtils;
import com.pjj.xsp.utils.TextJsonUtils;


import org.greenrobot.greendao.AbstractDao;

import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Create by xinheng on 2018/11/17。
 * describe：处理数据库操作
 */
public class HandleDaoDb {
    private static final String TAG = "HandleDaoDb_TAG";

    /**
     * 完成JavaBean对应记录的插入，如果表未创建，先创建fruit表
     *
     * @param bean
     * @return 返回long值 -1为失败
     */
    public static <T, K> Long insertBean(T bean, AbstractDao<T, K> abstractDao) {
        Long flag = abstractDao.insertOrReplace(bean);
        Log.i(TAG, "-------插入一条的结果为" + (flag != -1));
        //DaoManager.getInstance().getDaoSession().getXspTaskDao()
        return flag;
    }

    public static void insertDateTask(DateTask dateTask) {
        try {
            deleteBean(dateTask);
        } catch (Exception e) {
            e.printStackTrace();
        }
        insertBean(dateTask, DaoManager.getInstance().getDaoSession().getDateTaskDao());
    }

    /**
     * 插入多条数据，在子线程操作
     *
     * @return
     */
    public static <T> boolean insertListBean(final List<T> mList) {
        boolean flag = false;
        try {
            DaoManager.getInstance().getDaoSession().runInTx(() -> {
                for (T bean : mList) {
                    DaoManager.getInstance().getDaoSession().insertOrReplace(bean);
                }
            });
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static <T> boolean delateListBean(final List<T> mList) {
        boolean flag = false;
        try {
            DaoManager.getInstance().getDaoSession().runInTx(() -> {
                for (T bean : mList) {
                    DaoManager.getInstance().getDaoSession().delete(bean);
                }
            });
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 刷新指定数据
     */
    public static <T> boolean reFreshBean(T bean) {
        boolean flag = false;
        try {
            DaoManager.getInstance().getDaoSession().refresh(bean);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 修改一条数据
     *
     * @return
     */
    public static <T> boolean updateBean(T bean) {
        boolean flag = false;
        try {
            DaoManager.getInstance().getDaoSession().update(bean);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除单条记录
     *
     * @return
     */
    public static <T> boolean deleteBean(T bean) {
        if (null == bean) {
            return false;
        }
        boolean flag = false;
        try {
            DaoManager.getInstance().getDaoSession().delete(bean);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static void deleteDateTask(Long id) {
        List<DateTask> dateTasks = queryEqBeanByQueryBuilder(DateTask.class, id, DateTaskDao.Properties.Id);
        if (TextJsonUtils.isNotEmpty(dateTasks)) {
            for (int i = 0; i < dateTasks.size(); i++) {
                DateTask dateTask = dateTasks.get(i);
                if (null != dateTask) {
                    deleteBean(dateTask);
                }
            }
        }
    }

    /**
     * 删除所有记录
     *
     * @return
     */
    public static <T> boolean deleteAll(Class<T> cls) {
        boolean flag = false;
        try {
            DaoManager.getInstance().getDaoSession().deleteAll(cls);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static void deleteTask(String orderId) {
        ScreenInfBean.DataBean screenInfDataBean = ScreenInfManage.getInstance().getScreenInfDataBean();
        if (null != screenInfDataBean) {
            String cooperationMode = screenInfDataBean.getCooperationMode();
            if ("4".equals(cooperationMode)) {
                List<MinePlayTask> dateTasks = queryEqBeanByQueryBuilder(MinePlayTask.class, orderId, MinePlayTaskDao.Properties.OrderId);
                if (TextJsonUtils.isNotEmpty(dateTasks)) {
                    delateListBean(dateTasks);
                }
            } else {
                List<TaskText> dateTasks = queryEqBeanByQueryBuilder(TaskText.class, orderId, TaskTextDao.Properties.OrderId);
                if (TextJsonUtils.isNotEmpty(dateTasks)) {
                    for (int i = 0; i < dateTasks.size(); i++) {
                        TaskText bean = dateTasks.get(i);
                        Long id = bean.getId();
                        deleteDateTask(id);
                        deleteBean(bean);
                    }
                }
            }
        }
    }

    /**
     * 查询所有记录
     *
     * @return
     */
    public static <T> List<T> queryAllBean(Class<T> cls) {
        return DaoManager.getInstance().getDaoSession().loadAll(cls);
    }

    /**
     * 根据主键id查询记录
     *
     * @param key
     * @return
     */
    public static <T> T queryBeanById(Class<T> cls, long key) {
        return DaoManager.getInstance().getDaoSession().load(cls, key);
    }

    /**
     * 使用 sql语句进行查询操作
     * 参数一sql语句  参数二查询条件限定
     */
    public static <T> List<T> queryBeanBySql(Class<T> cls, String sql, String[] conditions) {
        return DaoManager.getInstance().getDaoSession().queryRaw(cls, sql, conditions);
    }

    /**
     * 使用queryBuilder进行查询
     *
     * @param cls      greeddao 对应实体类对象
     * @param id       greeddao 对应实体类中的一个属性
     * @param property cls-Dao类中与id对应的Property值
     * @return
     */
    public static <T> List<T> queryEqBeanByQueryBuilder(Class<T> cls, Object id, Property property) {
        QueryBuilder<T> queryBuilder = DaoManager.getInstance().getDaoSession().queryBuilder(cls);
        return queryBuilder.where(property.eq(id)).list();
    }

    public static <T> boolean checkIsHas(Class<T> cls, Object id, Property property) {
        List<T> ts = queryEqBeanByQueryBuilder(cls, id, property);
        if (TextJsonUtils.isNotEmpty(ts)) {
            return true;
        }
        return false;
    }

    public static void queryTaskAndPerform() {
        List<MinePlayTask> minePlayTasks = null;
        try {
            minePlayTasks = queryAllBean(MinePlayTask.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextJsonUtils.isNotEmpty(minePlayTasks)) {
            for (int i = 0; i < minePlayTasks.size(); i++) {
                MinePlayTask minePlayTask = minePlayTasks.get(i);
                PlayTaskParent playTaskParent = minePlayTask.getPlayTaskParent();
                if (null != playTaskParent) {
                    //XspPlayUI.getInstall().addLinkListTask(playTaskParent);
                    MediaTaskCacheHelp.getInstance().addTask(playTaskParent);
                } else {
                    Log.e(TAG, "queryTaskAndPerform: null");
                }
            }
        } else {
            Log.e(TAG, "minePlayTasks: size=0");
            //XspPlayUI.getInstall().startNextTask();
        }
        MediaTaskCacheHelp.getInstance().startTask(false);
    }

    public static boolean checkDateTask(String date, String raw) {
        QueryBuilder<DateTask> taskTextQueryBuilder = DaoManager.getInstance().getDaoSession().queryBuilder(DateTask.class);
        taskTextQueryBuilder.where(DateTaskDao.Properties.Date.eq(date))
                .where(DateTaskDao.Properties.Id.eq(raw));
        List<DateTask> list = taskTextQueryBuilder.list();
        return TextJsonUtils.isNotEmpty(list);
    }

    public static void queryTaskAndPerform(String date, String hour) {
        Log.e(TAG, "queryTaskAndPerform: date=" + date);
        QueryBuilder<DateTask> taskTextQueryBuilder = DaoManager.getInstance().getDaoSession().queryBuilder(DateTask.class);
        taskTextQueryBuilder.where(DateTaskDao.Properties.Date.eq(date));
        taskTextQueryBuilder.where(DateTaskDao.Properties.LimitTag.eq("0"));
        if (null != hour) {
            taskTextQueryBuilder.where(DateTaskDao.Properties.Hour.like("," + hour + ","));
        }
        List<DateTask> list = taskTextQueryBuilder.list();
        if (TextJsonUtils.isNotEmpty(list)) {
            //Log.e(TAG, "queryTaskAndPerform: " + list.size());
            int size = list.size();
            for (int i = 0; i < size; i++) {
                DateTask dateTask = list.get(i);
                Long id = dateTask.getId();
                TaskText taskText = queryBeanById(TaskText.class, id);
                if (null == taskText) {
                    deleteBean(dateTask);
                    continue;
                }
                PlayTaskParent playTaskParent = taskText.getPlayTaskParent();
                //TODO 下载判断，未加
                if (null != playTaskParent/*&&!playTaskParent.getTempletType().equals("1")*/) {
                    //XspPlayUI.getInstall().addLinkListTask(playTaskParent);
                    MediaTaskCacheHelp.getInstance().addTask(playTaskParent);
                } else {
                    Log.e(TAG, "queryTaskAndPerform: null");
                }
            }
        } else {
            Log.e(TAG, "queryTaskAndPerform: size=0");
            //XspPlayUI.getInstall().startNextTask();
        }
        MediaTaskCacheHelp.getInstance().startTask(true);
    }

    public static List<String> checkTask(List<String> list) {
        List<String> lists = new ArrayList<>();
        if ("4".equals(SharedUtils.getXmlForKey("cooperationMode"))) {
            for (int i = 0; i < list.size(); i++) {
                String orderId = list.get(i);
                List<MinePlayTask> minePlayTasks = queryEqBeanByQueryBuilder(MinePlayTask.class, orderId, MinePlayTaskDao.Properties.OrderId);
                if (!TextJsonUtils.isNotEmpty(minePlayTasks)) {
                    lists.add(orderId);
                }
            }
        } else {
            for (int i = 0; i < list.size(); i++) {
                String orderId = list.get(i);
                List<TaskText> taskTexts = queryEqBeanByQueryBuilder(TaskText.class, orderId, TaskTextDao.Properties.OrderId);
                if (!TextJsonUtils.isNotEmpty(taskTexts)) {
                    lists.add(orderId);
                } else {
                    Long id = taskTexts.get(0).getId();
                    QueryBuilder<DateTask> taskTextQueryBuilder = DaoManager.getInstance().getDaoSession().queryBuilder(DateTask.class);
                    taskTextQueryBuilder.where(DateTaskDao.Properties.Date.eq(DateUtils.getNowDate()));
                    taskTextQueryBuilder.where(DateTaskDao.Properties.Id.eq(id));
                    List<DateTask> dateList = taskTextQueryBuilder.list();
                    if (!TextJsonUtils.isNotEmpty(dateList)) {
                        lists.add(orderId);
                    }
                }
            }
        }
        return lists;
    }

    public static void cleanTaskDb() {
        if ("4".equals(SharedUtils.getXmlForKey("cooperationMode"))) {
            return;
        }
        List<DateTask> dateTasks = queryAllBean(DateTask.class);
       Log.e(TAG, "cleanTaskDb: " + dateTasks.size());
        if (!TextJsonUtils.isNotEmpty(dateTasks)) {
            return;
        }
        Collections.sort(dateTasks, (o1, o2) -> {
            String date1 = o1.getDate();
            String date2 = o2.getDate();
            if (null == date1) {
                return -1;
            }
            if (null == date2) {
                return 1;
            }
            if (DateUtils.isDate1less2(date1, date2)) {
                return -1;
            } else {
                return 1;
            }
        });
        String nowDate = DateUtils.getNowDate();
        int middle = dateTasks.size() / 2;
        boolean realTag = false;
        while (true) {
            String dateMiddle = dateTasks.get(middle).getDate();
            if (null == dateMiddle || DateUtils.isDate1less2(dateMiddle, nowDate)) {
                middle += 1;
                realTag = true;
            } else {
                break;
            }
            if (middle >= dateTasks.size()) {
                break;
            }
        }
        if (middle >= dateTasks.size()) {
            //全删
            middle = -100;
        } else {
            if (!realTag) {
                int middle_ = middle - 1;
                while (middle_ >= 0) {
                    String dateMiddle = dateTasks.get(middle_).getDate();
                    if (null == dateMiddle || DateUtils.isDate1less2(dateMiddle, nowDate)) {
                        break;
                    } else {
                        middle_ -= 1;
                    }
                    if (middle_ <= -1) {
                        break;
                    }
                }
                if (middle_ <= -1) {
                    //全部保留
                    middle = -101;
                } else {
                    middle = middle_ + 1;
                }
            }
        }
        if (middle == -101) {
            middle = 0;
        }
        if (middle == -100) {
            deleteAll(DateTask.class);
            deleteAll(TaskText.class);
            File file = new File(PjjApplication.App_Path);
            if (file.exists()) {
                File[] files = file.listFiles();
                for (File child : files) {
                    if (!child.isDirectory()) {
                        child.getAbsoluteFile().delete();
                    }
                }
            }
            return;
        }
        List<Long> canUseIds = new ArrayList<>();
        List<String> canUseFiles = new ArrayList<>();
        for (int i = middle; i < dateTasks.size(); i++) {
            DateTask dateTask = dateTasks.get(i);
            Long id = dateTask.getId();
            canUseIds.add(id);
            TaskText taskText = queryBeanById(TaskText.class, id);
            PlayTaskParent playTaskParent = taskText.getPlayTaskParent();
            if (null != playTaskParent) {
                canUseFiles.add(playTaskParent.getFilePath());
                if (!TextUtils.isEmpty(playTaskParent.getVideoPath())) {
                    canUseFiles.add(playTaskParent.getVideoPath());
                }
            }
        }
        for (int i = 0; i < middle; i++) {
            DateTask dateTask = dateTasks.get(i);
            Long id = dateTask.getId();
            if (!canUseIds.contains(id)) {
                TaskText taskText = queryBeanById(TaskText.class, id);
                if (null != taskText) {
                    deleteBean(taskText);
                }
            }
            deleteBean(dateTask);
        }
        File file = new File(PjjApplication.App_Path);
        if (file.exists()) {
            File[] files = file.listFiles();
            Log.e(TAG, "cleanTaskDb: file size=" + files.length);
            for (File child : files) {
                String name = child.getName();
                Log.e(TAG, "cleanTaskDb: " + name + ", ");
                if (!child.isDirectory() && !canUseFiles.contains(name)) {
                    child.getAbsoluteFile().delete();
                }
            }
        }
    }

}
