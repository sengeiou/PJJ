package com.pjj.xsp.db;


import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.pjj.xsp.intent.http.RetrofitService;
import com.pjj.xsp.module.XspTaskHandler;
import com.pjj.xsp.module.bean.ScreenPingTaskBean;
import com.pjj.xsp.module.bean.ScreenTaskBean;
import com.pjj.xsp.module.bean.SpeedScreenBean;
import com.pjj.xsp.utils.DateUtils;
import com.pjj.xsp.utils.Log;
import com.pjj.xsp.utils.TextJsonUtils;
import com.pjj.xsp.utils.TextViewUtils;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Create by xinheng on 2018/11/17。
 * describe：处理数据库操作
 */
public class HandleDaoDb {
    private static final String TAG = "flag";

    /**
     * 完成JavaBean对应记录的插入，如果表未创建，先创建fruit表
     *
     * @param bean
     * @return 返回long值 -1为失败
     */
    public static <T, K> boolean insertBean(T bean, AbstractDao<T, K> abstractDao) {
        boolean flag = abstractDao.insertOrReplace(bean) != -1;
        Log.i(TAG, "-------插入一条的结果为" + flag);
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

    public static void insertOrder(String orderId, ScreenTaskBean.DataBean bianmin, List<ScreenTaskBean.DataBean.TempletListBean> diy, String date, String hour) {
        if (hour.startsWith(",") && hour.endsWith(",")) {
            //hour = "%" + hour + "%";
        } else {
            hour = "," + hour + ",";
        }
        Log.e(TAG, "insertOrder: hour=" + hour + ", date=" + date);
        QueryBuilder<DateTask> queryBuilder = DaoManager.getInstance().getDaoSession().queryBuilder(DateTask.class);
        //queryBuilder
        queryBuilder.where(DateTaskDao.Properties.Date.eq(date))
                .where(DateTaskDao.Properties.Hour.like("%" + hour + "%"))
                .where(DateTaskDao.Properties.OrderId.like(orderId));
        List<DateTask> list = queryBuilder.list();
        DateTask unique = null;
        if (TextJsonUtils.isNotEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                DateTask dateTask = list.get(i);
                String date1 = dateTask.getDate();
                String hour1 = dateTask.getHour();
                String orderId1 = dateTask.getOrderId();
                Log.e("TAG", i + ",, date=" + date1 + ", hour=" + hour1);
                Log.e("TAG", ",,orderId=" + orderId1);
            }
            unique = list.get(0);
        }
        if (null != unique) {//有
            if (!orderId.equals(unique.getOrderId())) {//但订单不一致
                Log.e(TAG, "insertOrder: hour=" + hour + ", date=" + date);
                Log.e(TAG, "insertOrder: +服务器数据错误...");
            } else {
                //不用查了。。。
            }
        } else {//没有,插入
            DateTask dateTask = new DateTask();
            dateTask.setOrderId(orderId);
            dateTask.setHour(hour);
            dateTask.setDate(date);
            insertBean(dateTask, DaoManager.getInstance().getDaoSession().getDateTaskDao());

            QueryBuilder<OrderTask> orderTaskQueryBuilder = DaoManager.getInstance().getDaoSession().queryBuilder(OrderTask.class);
            List<OrderTask> list1 = orderTaskQueryBuilder.where(OrderTaskDao.Properties.OrderId.eq(orderId)).list();
            if (!TextJsonUtils.isNotEmpty(list1)) {
                OrderTask orderTask = new OrderTask();
                orderTask.setOrderId(orderId);
                if (null != bianmin) {
                    orderTask.setPeopleInfo(TextJsonUtils.toJsonString(bianmin));
                } else {
                    orderTask.setDiyInfo(TextJsonUtils.toJsonString(diy));
                }
                insertBean(orderTask, DaoManager.getInstance().getDaoSession().getOrderTaskDao());
            }
        }
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

    /**
     * 数据库条件查询
     *
     * @param date 日期 yyyy-MM-dd
     * @param hour 小时 整数 24小时制
     * @return map
     */
    public static HashMap<String, ScreenTaskBean.DataBean> queryEqLikeBeanByQueryBuilder(String date, String hour) {
        QueryBuilder<DateTask> queryBuilder = DaoManager.getInstance().getDaoSession().queryBuilder(DateTask.class);
        queryBuilder.where(DateTaskDao.Properties.Date.eq(date))
                .where(DateTaskDao.Properties.Hour.like("%," + hour + ",%"));
        //queryBuilder.and(DateTaskDao.Properties.Date.eq(date), DateTaskDao.Properties.Hour.like("%," + hour + ",%"));
        List<DateTask> list = null;
        try {
            list = queryBuilder.list();
            Log.e(TAG, "queryEqLikeBeanByQueryBuilder: " + list.size());
        } catch (DaoException e) {
            e.printStackTrace();
        }
        //Log.e(TAG, "queryEqLikeBeanByQueryBuilder: list size=");
        if (TextJsonUtils.isNotEmpty(list)) {
            HashMap stringDataBeanHashMap = new HashMap(2);
            insertMapData(list, stringDataBeanHashMap);
            return stringDataBeanHashMap;
        }
        return null;
    }

    private static void insertMapData(List<DateTask> list, HashMap stringDataBeanHashMap) {
        for (int i = 0; i < list.size(); i++) {
            DateTask dateTask = list.get(i);
            String orderId = dateTask.getOrderId();
            if (orderId.contains("{") && orderId.contains("}")) {
                if (orderId.contains("viewSizeBeanList")) {
                    Type type = new TypeToken<List<SpeedScreenBean.DataBean>>() {
                    }.getType();
                    List<SpeedScreenBean.DataBean> listPing = null;
                    try {
                        listPing = TextJsonUtils.gson.fromJson(orderId, type);
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                    stringDataBeanHashMap.put(XspTaskHandler.SCREEN_DIY_LIST, listPing);
                    return;
                }
                Type type = new TypeToken<List<ScreenPingTaskBean.DataBeanPin>>() {
                }.getType();
                List<ScreenPingTaskBean.DataBeanPin> listPing = null;
                try {
                    listPing = TextJsonUtils.gson.fromJson(orderId, type);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                stringDataBeanHashMap.put(XspTaskHandler.SCREEN_TITLE_10_LIST, listPing);
                return;
            }
            QueryBuilder<OrderTask> queryOrderTaskBuilder = DaoManager.getInstance().getDaoSession().queryBuilder(OrderTask.class);
            List<OrderTask> listOrder = queryOrderTaskBuilder.where(OrderTaskDao.Properties.OrderId.eq(orderId)).list();
            Log.e(TAG, "queryEqLikeBeanByQueryBuilder: listOrder size=" + listOrder.size());
            if (null != listOrder && listOrder.size() > 0) {
                OrderTask orderTask = listOrder.get(0);
                ScreenTaskBean.DataBean bianmin = null;
                ScreenTaskBean.DataBean diy = null;
                String peopleInfo = orderTask.getPeopleInfo();
                //Log.e(TAG, "insertMapData: json=" + peopleInfo);
                if (null != orderTask.getDiyInfo()) {
                    List<ScreenTaskBean.DataBean.TempletListBean> dies = null;
                    try {
                        Type type = new TypeToken<List<ScreenTaskBean.DataBean.TempletListBean>>() {
                        }.getType();
                        //dies = TextJsonUtils.parseList(orderTask.getDiyInfo());
                        dies = TextJsonUtils.gson.fromJson(orderTask.getDiyInfo(), type);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (TextJsonUtils.isNotEmpty(dies)) {
                        diy = new ScreenTaskBean.DataBean();
                        diy.setTempletList(dies);
                        diy.setOrderType("1");
                        diy.setOrderId(orderId);
                        Object data = stringDataBeanHashMap.get(XspTaskHandler.SCREEN_CONTENT);
                        ScreenTaskBean.DataBean dataBean = null;
                        if (data instanceof ScreenTaskBean.DataBean) {
                            dataBean = (ScreenTaskBean.DataBean) data;
                        }
                        if (dataBean != null && "1".equals(dataBean.getOrderType())) {
                            //break;
                        } else {
                            stringDataBeanHashMap.put(XspTaskHandler.SCREEN_CONTENT, diy);
                        }
                    }
                } else if (null != peopleInfo) {
                    try {
                        bianmin = TextJsonUtils.parse(peopleInfo, ScreenTaskBean.DataBean.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (null == bianmin) {
                        bianmin = new ScreenTaskBean.DataBean();
                        bianmin.setOrderId(orderId);
                        bianmin.setOrderType("2");
                        bianmin.setPeopleInfo(peopleInfo);
                    }
                    //Log.e(TAG, "insertMapData: peopleinf=" + bianmin.getPeopleInfo());
                    stringDataBeanHashMap.put(XspTaskHandler.SCREEN_TITLE, bianmin);
                }
                if (null != bianmin || null != diy) {//至少有一个有效
                } else {
                    //Long id_date = dateTask.getId_date();
                    //Long id = orderTask.getId();
                    //删除 无效信息
                    Log.e(TAG, "queryEqLikeBeanByQueryBuilder: 删除");
                    //deleteBean(dateTask);
                    //deleteBean(orderTask);
                }
            }
        }
    }

    /**
     * 删除数据库以及文件
     *
     * @param nowDate yyyy-MM-dd
     */
    public static void deleteSqlitAddFile(String nowDate) {
        List<DateTask> dateTasks = queryAllBean(DateTask.class);
        if (!TextJsonUtils.isNotEmpty(dateTasks)) {
            return;
        }
        List<DateTask> deleteDate = new ArrayList<>();
        List<String> saveOrderIds = new ArrayList<>();
        List<String> deleteOrderIds = new ArrayList<>();
        for (int i = 0; i < dateTasks.size(); i++) {
            DateTask dateTask = dateTasks.get(i);
            String date = dateTask.getDate();
            String orderId = dateTask.getOrderId();
            if (DateUtils.isDate1less2(date, nowDate)) {
                deleteDate.add(dateTask);
                deleteOrderIds.add(orderId);
            } else {
                saveOrderIds.add(orderId);
            }
        }
        delateListBean(deleteDate);
        Iterator<String> iterator = deleteOrderIds.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            if (saveOrderIds.contains(next)) {
                iterator.remove();
            }
        }
        List<String> deleteFiles = new ArrayList<>();
        for (int i = 0; i < deleteOrderIds.size(); i++) {
            String orderId = deleteOrderIds.get(i);
            List<OrderTask> orderTasks = HandleDaoDb.queryEqBeanByQueryBuilder(OrderTask.class, orderId, OrderTaskDao.Properties.OrderId);
            if (TextJsonUtils.isNotEmpty(orderTasks)) {
                for (int j = 0; j < orderTasks.size(); j++) {
                    OrderTask orderTask = orderTasks.get(j);
                    String diyInfo = orderTask.getDiyInfo();
                    List<ScreenTaskBean.DataBean.TempletListBean> dies = null;
                    try {
                        Type type = new TypeToken<List<ScreenTaskBean.DataBean.TempletListBean>>() {
                        }.getType();
                        dies = TextJsonUtils.gson.fromJson(diyInfo, type);
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                    if (TextJsonUtils.isNotEmpty(dies)) {
                        ScreenTaskBean.DataBean.TempletListBean templetListBean;
                        List<ScreenTaskBean.DataBean.TempletListBean.FileListBean> fileList;
                        ScreenTaskBean.DataBean.TempletListBean.FileListBean fileListBean;
                        for (int i1 = 0; i1 < dies.size(); i1++) {
                            templetListBean = dies.get(i1);
                            if (null != templetListBean) {
                                fileList = templetListBean.getFileList();
                                if (null != fileList && fileList.size() > 0) {
                                    for (int j1 = 0; j1 < fileList.size(); j1++) {
                                        fileListBean = fileList.get(j1);
                                        if (null != fileListBean) {
                                            String fileUrl = fileListBean.getFileUrl();
                                            deleteFiles.add(fileUrl);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                delateListBean(orderTasks);
            }
        }
        for (int i = 0; i < deleteFiles.size(); i++) {
            String filePath = deleteFiles.get(i);
            if (!TextViewUtils.isEmpty(filePath)) {
                File file = new File(filePath);
                if (file.exists()) {
                    file.getAbsoluteFile().delete();
                }
            }
        }
    }

}
