package com.pjj.xsp.module.bean;

import android.text.TextUtils;

import com.pjj.xsp.module.ScreenInfManage;
import com.pjj.xsp.utils.TextJsonUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Create by xinheng on 2018/10/12 0012。
 * describe：任务信息
 */
public class ScreenTaskBean extends ScreenParentBean implements Serializable {
    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * pieceType : 0
         * orderType : 2
         * orderId : 20190126090539154846473955884568
         * peopleInfo : 本人丢失爱猫一只，全身白色，蓝眼睛，脖子上有粉色除跳蚤圈，比较胆小怕人，今年4岁；希望好心人看到此猫与我联系，谢谢！！！
         * companyName : 测试公司
         * isShowName : 0
         * phoneNumber : 13621265167
         * isShowPhone : 1
         * pieceNum : 1
         * name : 测试的账号
         * authType : 1
         */

        private String pieceType;
        private String orderType;
        private String orderId;
        private String peopleInfo;
        private String companyName;
        private String isShowName;
        private String phoneNumber;
        private String isShowPhone;
        private int pieceNum;
        private String name;
        private String authType;
        private List<TempletListBean> templetList;

        public String getPieceType() {
            return pieceType;
        }

        public String getBmInfJson() {
            DataBean dataBean = new DataBean();
            dataBean.setPeopleInfo(peopleInfo);
            dataBean.setPieceNum(pieceNum);
            dataBean.setPieceType(pieceType);
            dataBean.setOrderId(orderId);
            dataBean.setCompanyName(companyName);
            dataBean.setIsShowName(isShowName);
            dataBean.setPhoneNumber(phoneNumber);
            dataBean.setIsShowPhone(isShowPhone);
            dataBean.setAuthType(authType);
            dataBean.setName(name);
            return TextJsonUtils.toJsonString(dataBean);
        }

        public void setPieceType(String pieceType) {
            this.pieceType = pieceType;
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

        public int getPieceNum() {
            return pieceNum;
        }

        public void setPieceNum(int pieceNum) {
            this.pieceNum = pieceNum;
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

        public List<TempletListBean> getTempletList() {
            return templetList;
        }

        public void setTempletList(List<TempletListBean> templetList) {
            this.templetList = templetList;
        }

        public static class TempletListBean {
            /**
             * templetName : 模板一
             * templet_id : b5cc423aadf2483c8537b0db81c1dae5
             * templetType : 2
             * fileList : [{"fileName":"2ccce13e123b4983a5c61e28d7c322ca.mp4","fileUrl":"http://47.92.50.83:8080/video/2ccce13e123b4983a5c61e28d7c322ca.mp4","type":"2","filePlace":"1"}]
             */

            private String templetName;
            private String templet_id;
            private String templetType;
            private List<FileListBean> fileList;

            public String getTempletName() {
                return templetName;
            }

            public void setTempletName(String templetName) {
                this.templetName = templetName;
            }

            public String getTemplet_id() {
                return templet_id;
            }

            public void setTemplet_id(String templet_id) {
                this.templet_id = templet_id;
            }

            public String getTempletType() {
                return templetType;
            }

            public void setTempletType(String templetType) {
                this.templetType = templetType;
            }

            public List<FileListBean> getFileList() {
                return fileList;
            }

            public void setFileList(List<FileListBean> fileList) {
                this.fileList = fileList;
            }

            public static class FileListBean {
                /**
                 * fileName : 2ccce13e123b4983a5c61e28d7c322ca.mp4
                 * fileUrl : http://47.92.50.83:8080/video/2ccce13e123b4983a5c61e28d7c322ca.mp4
                 * type : 2
                 * filePlace : 1
                 */

                private String fileName;
                private String fileUrl;
                private String type;
                private String filePlace;

                public String getFileName() {
                    return fileName;
                }

                public void setFileName(String fileName) {
                    this.fileName = fileName;
                }

                public String getFileUrl() {
                    if (TextUtils.isEmpty(fileUrl)) {
                        return ScreenInfManage.filePathMedia + fileName;
                    } else {
                        return fileUrl;
                    }
                }

                private String getTypeName() {
                    if ("2".equals(type)) {
                        return "video/";
                    } else {
                        return "img/";
                    }
                }

                public void setFileUrl(String fileUrl) {
                    this.fileUrl = fileUrl;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getFilePlace() {
                    return filePlace;
                }

                public void setFilePlace(String filePlace) {
                    this.filePlace = filePlace;
                }
            }
        }
    }

}
