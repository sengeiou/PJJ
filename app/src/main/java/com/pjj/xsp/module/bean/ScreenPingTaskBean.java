package com.pjj.xsp.module.bean;

import com.pjj.xsp.db.DateTask;
import com.pjj.xsp.utils.TextJsonUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by xinheng on 2018/10/12 0012。
 * describe：拼屏任务信息
 */
public class ScreenPingTaskBean extends ScreenParentBean implements Serializable {
    private List<DataBeanPin> data;

    public List<DataBeanPin> getData() {
        return data;
    }

    public void setData(List<DataBeanPin> data) {
        this.data = data;
    }

    public DateTask changeDateTask() {
        DateTask dateTask = new DateTask();
        dateTask.setDate(getCurDate());
        dateTask.setHour(getHour());
        dateTask.setOrderId(TextJsonUtils.toJsonString(data));
        return dateTask;
    }

    /**
     * 拼屏
     */
    public static class DataBeanPin {
        /**
         * 拼屏类型
         * 0 不拼  1 拼屏幕  2拼时间段
         */
        private String pieceType;
        /**
         * 颜色标识 1-12
         */
        private String pieceColour;
        private String orderType;
        private String orderId;
        private String peopleInfo;
        private String companyName;
        private String isShowName;
        private String phoneNumber;
        private String isShowPhone;
        private String pieceNum;
        /**
         * 便民名称
         */
        private String pieceTitle;
        private String name;
        /**
         * 类型 商家 个人
         */
        private String authType;

        public String getPieceType() {
            return pieceType;
        }

        public void setPieceType(String pieceType) {
            this.pieceType = pieceType;
        }

        public String getPieceColour() {
            return pieceColour;
        }

        public void setPieceColour(String pieceColour) {
            this.pieceColour = pieceColour;
        }

        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getPeopleInfo() {
            return peopleInfo;
        }

        public void setPeopleInfo(String peopleInfo) {
            this.peopleInfo = peopleInfo;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getIsShowName() {
            return isShowName;
        }

        public void setIsShowName(String isShowName) {
            this.isShowName = isShowName;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getIsShowPhone() {
            return isShowPhone;
        }

        public void setIsShowPhone(String isShowPhone) {
            this.isShowPhone = isShowPhone;
        }

        public String getPieceNum() {
            return pieceNum;
        }

        public void setPieceNum(String pieceNum) {
            this.pieceNum = pieceNum;
        }

        public String getPieceTitle() {
            return pieceTitle;
        }

        public void setPieceTitle(String pieceTitle) {
            this.pieceTitle = pieceTitle;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAuthType() {
            return authType;
        }

        public void setAuthType(String authType) {
            this.authType = authType;
        }
    }
}
