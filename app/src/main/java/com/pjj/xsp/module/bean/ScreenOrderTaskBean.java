package com.pjj.xsp.module.bean;

import com.pjj.xsp.utils.Log;

import com.google.gson.JsonIOException;
import com.pjj.xsp.db.DateTask;
import com.pjj.xsp.db.OrderTask;
import com.pjj.xsp.utils.TextJsonUtils;
import com.pjj.xsp.utils.TextViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by xinheng on 2018/11/17。
 * describe：广告屏根据订单号查询 结果
 */
public class ScreenOrderTaskBean extends ResultBean {

    /**
     * data : {"orderTime":[{"orderTime":"2018-11-03","timeSection":"17,18"},{"orderTime":"2018-11-04","timeSection":"17,18,19"}],"templetList":[{"templetName":"模板一","templet_id":"b5cc423aadf2483c8537b0db81c1dae5","templetType":"2","fileList":[{"fileName":"2ccce13e123b4983a5c61e28d7c322ca.mp4","file_id":"47c9c856ee104fb2836b6a9eb424adf1","fileUrl":"http://47.92.50.83:8080/video/2ccce13e123b4983a5c61e28d7c322ca.mp4","type":"2","filePlace":"1"}]}],"order":{"orderType":"2","orderId":"20181103164109154123446924993875","peopleInfo":"本店十一大酬宾，消费任一套餐均送价值100元餐具一套，买一送一，多买多送。\n消费满800元客户，可另外参与抽奖活动！！！"}}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * orderTime : [{"orderTime":"2018-11-03","timeSection":"17,18"},{"orderTime":"2018-11-04","timeSection":"17,18,19"}]
         * templetList : [{"templetName":"模板一","templet_id":"b5cc423aadf2483c8537b0db81c1dae5","templetType":"2","fileList":[{"fileName":"2ccce13e123b4983a5c61e28d7c322ca.mp4","file_id":"47c9c856ee104fb2836b6a9eb424adf1","fileUrl":"http://47.92.50.83:8080/video/2ccce13e123b4983a5c61e28d7c322ca.mp4","type":"2","filePlace":"1"}]}]
         * order : {"orderType":"2","orderId":"20181103164109154123446924993875","peopleInfo":"本店十一大酬宾，消费任一套餐均送价值100元餐具一套，买一送一，多买多送。\n消费满800元客户，可另外参与抽奖活动！！！"}
         */

        private ScreenTaskBean.DataBean order;
        private List<OrderTimeBean> orderTime;
        private List<ScreenTaskBean.DataBean.TempletListBean> templetList;

        public ScreenTaskBean.DataBean getOrder() {
            return order;
        }

        public void setOrder(ScreenTaskBean.DataBean order) {
            this.order = order;
        }

        public List<OrderTimeBean> getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(List<OrderTimeBean> orderTime) {
            this.orderTime = orderTime;
        }

        public List<ScreenTaskBean.DataBean.TempletListBean> getTempletList() {
            return templetList;
        }

        public void setTempletList(List<ScreenTaskBean.DataBean.TempletListBean> templetList) {
            this.templetList = templetList;
        }

        public static class OrderTimeBean {
            /**
             * orderTime : 2018-11-03
             * timeSection : 17,18
             */

            private String orderTime;
            private String timeSection;

            public String getOrderTime() {
                return orderTime;
            }

            public void setOrderTime(String orderTime) {
                this.orderTime = orderTime;
            }

            public String getTimeSection() {
                return timeSection;
            }

            public void setTimeSection(String timeSection) {
                this.timeSection = timeSection;
            }

            public DateTask change(String orderId) {
                DateTask dateTask = new DateTask();
                dateTask.setDate(orderTime);
                dateTask.setHour(timeSection);
                dateTask.setOrderId(orderId);
                return dateTask;
            }
        }
    }

    public static List<DateTask> changeDateTaskList(String orderId, List<DataBean.OrderTimeBean> orderTimeBeans) {
        ArrayList<DateTask> dateTasks = new ArrayList<>(orderTimeBeans.size());
        for (int i = 0; i < orderTimeBeans.size(); i++) {
            DataBean.OrderTimeBean orderTimeBean = orderTimeBeans.get(i);
            if (null != orderTimeBean && !TextViewUtils.isEmpty(orderTimeBean.getOrderTime())) {
                dateTasks.add(orderTimeBean.change(orderId));
            }
        }
        if (dateTasks.size() == 0) {
            return null;
        }
        return dateTasks;
    }

    public static OrderTask changeOrderTask(String orderId, String peopleInfo, List<ScreenTaskBean.DataBean.TempletListBean> templetListBeans) {
        if (null != peopleInfo || (null != templetListBeans && templetListBeans.size() > 0)) {
            OrderTask orderTask = new OrderTask();
            orderTask.setOrderId(orderId);
            orderTask.setPeopleInfo(peopleInfo);
            if (null != templetListBeans) {
                try {
                    String diyInfo = TextJsonUtils.toJsonString(templetListBeans);
                    Log.e("TAG", "changeOrderTask: " + diyInfo);
                    orderTask.setDiyInfo(diyInfo);
                } catch (JsonIOException e) {
                    e.printStackTrace();
                }
            }
            return orderTask;
        }
        return null;
    }
}
