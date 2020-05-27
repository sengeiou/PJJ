package com.pjj.xsp.module.bean;

import com.pjj.xsp.db.DateTask;
import com.pjj.xsp.utils.TextJsonUtils;

import java.util.List;

/**
 * Created by XinHeng on 2019/03/18.
 * describe：diy 拼屏
 */
public class SpeedScreenBean extends ScreenParentBean {
    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }
    public DateTask changeDateTask() {
        DateTask dateTask = new DateTask();
        dateTask.setDate(getCurDate());
        dateTask.setHour(getHour());
        dateTask.setOrderId(TextJsonUtils.toJsonString(data));
        return dateTask;
    }
    public static class DataBean {
        /**
         * viewSizeBeanList : [{"randomFlag":"1","fileName":"d939386464e0c15f3d91ac630632e96a.mp4","orderId":"20190316112423155270666328605029","x":0,"width":1,"y":0,"type":2,"height":1},{"fileName":"d939386464e0c15f3d91ac630632e96a.mp4","orderId":"20190316113607155270736787687940","x":0,"width":1,"y":1,"height":1}]
         * IdentificationId : 658448e350b646c7a45edca8a1dc6910
         * size : 2
         * proportionX : 1
         * proportionY : 2
         */

        private String IdentificationId;
        private int size;
        private int proportionX;
        private int proportionY;
        private List<ViewSizeBeanListBean> viewSizeBeanList;

        public String getIdentificationId() {
            return IdentificationId;
        }

        public void setIdentificationId(String IdentificationId) {
            this.IdentificationId = IdentificationId;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getProportionX() {
            return proportionX;
        }

        public void setProportionX(int proportionX) {
            this.proportionX = proportionX;
        }

        public int getProportionY() {
            return proportionY;
        }

        public void setProportionY(int proportionY) {
            this.proportionY = proportionY;
        }

        public List<ViewSizeBeanListBean> getViewSizeBeanList() {
            return viewSizeBeanList;
        }

        public void setViewSizeBeanList(List<ViewSizeBeanListBean> viewSizeBeanList) {
            this.viewSizeBeanList = viewSizeBeanList;
        }

        public static class ViewSizeBeanListBean {
            /**
             * randomFlag : 1
             * fileName : d939386464e0c15f3d91ac630632e96a.mp4
             * orderId : 20190316112423155270666328605029
             * x : 0
             * width : 1
             * y : 0
             * type : 2
             * height : 1
             */

            private String randomFlag;
            private String fileName;
            private String orderId;
            private int x;
            private int width;
            private int y;
            private int type;
            private int height;

            public String getRandomFlag() {
                return randomFlag;
            }

            public void setRandomFlag(String randomFlag) {
                this.randomFlag = randomFlag;
            }

            public String getFileName() {
                return fileName;
            }

            public void setFileName(String fileName) {
                this.fileName = fileName;
            }

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public int getX() {
                return x;
            }

            public void setX(int x) {
                this.x = x;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getY() {
                return y;
            }

            public void setY(int y) {
                this.y = y;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }
        }
    }
}
