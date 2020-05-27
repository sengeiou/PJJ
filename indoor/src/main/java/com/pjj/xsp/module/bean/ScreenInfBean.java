package com.pjj.xsp.module.bean;

import com.pjj.xsp.intent.http.RetrofitService;
import com.pjj.xsp.module.parameter.ScreenInfTag;

import java.util.List;

/**
 * Create by xinheng on 2018/10/18。
 * describe：广告屏相关信息
 */
public class ScreenInfBean extends ResultBean {

    /**
     * data : [{"lastTime":"2018-10-01","nextTime":"2018-11-01","discount":10,"screenName":"朗琴国际B座一号梯(a)","screenCode":"000001","screenId":"31110520100578531993#a","peoplePrice":1,"price":70,"helpPhone":"求救电话","company":"维保公司","mainUser":"维保人员","peopleDiscount":10,"isActivate":"0","register":"31110520100578531993"},{"screenId":"12345","createTime":1539671373000,"discount":10,"isActivate":"0"},{"screenId":"ff00658224794f85b33d74736b5b0c23","createTime":1539671889000,"discount":10,"isActivate":"0"}]
     * dataNum : 7
     */

    private int dataNum;
    private List<DataBean> data;

    public int getDataNum() {
        return dataNum;
    }

    public void setDataNum(int dataNum) {
        this.dataNum = dataNum;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * lastTime : 2018-10-01
         * nextTime : 2018-11-01
         * discount : 10
         * screenName : 朗琴国际B座一号梯(a)
         * screenCode : 000001
         * screenId : 31110520100578531993#a
         * peoplePrice : 1
         * price : 70
         * helpPhone : 求救电话
         * company : 维保公司
         * mainUser : 维保人员
         * peopleDiscount : 10
         * isActivate : 0
         * register : 31110520100578531993
         * createTime : 1539671373000
         */

        private String lastTime;
        private String nextTime;
        //private int discount;
        private String screenName;
        private String screenCode;
        private String screenId;
        //private int peoplePrice;
        //private int price;
        private String helpPhone;
        private String company;
        private String mainUser;
        //private float peopleDiscount;
        private String isActivate;//0未激活 1已激活
        private String register;
        //private long createTime;

        private String areaCode;//beijing
        private String areaName;//北京市

        private int openTime;
        private int closeTime;

        private String isShow;//是否显示物业信息   0不显示 1显示"
        private String propertyInfo;//物业信息
        private String propertyTitle;//物业标题
        private String cooperationMode;//合作运营模式 1地方自营 2联合运营 3 公司运营 4自用

        public String getPropertyTitle() {
            return propertyTitle;
        }

        public void setPropertyTitle(String propertyTitle) {
            this.propertyTitle = propertyTitle;
        }

        public int getOpenTime() {
            return openTime;
        }

        public void setOpenTime(int openTime) {
            this.openTime = openTime;
        }

        public int getCloseTime() {
            return closeTime;
        }

        public void setCloseTime(int closeTime) {
            this.closeTime = closeTime;
        }

        public String getLastTime() {
            return lastTime;
        }

        public void setLastTime(String lastTime) {
            this.lastTime = lastTime;
        }

        public String getNextTime() {
            return nextTime;
        }

        public void setNextTime(String nextTime) {
            this.nextTime = nextTime;
        }

//        public int getDiscount() {
//            return discount;
//        }
//
//        public void setDiscount(int discount) {
//            this.discount = discount;
//        }

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public String getScreenName() {
            return screenName;
        }

        public void setScreenName(String screenName) {
            this.screenName = screenName;
        }

        public String getScreenCode() {
            return screenCode;
        }

        public void setScreenCode(String screenCode) {
            this.screenCode = screenCode;
        }

        public String getScreenId() {
            return screenId;
        }

        public void setScreenId(String screenId) {
            this.screenId = screenId;
        }

//        public int getPeoplePrice() {
//            return peoplePrice;
//        }
//
//        public void setPeoplePrice(int peoplePrice) {
//            this.peoplePrice = peoplePrice;
//        }
//
//        public int getPrice() {
//            return price;
//        }
//
//        public void setPrice(int price) {
//            this.price = price;
//        }

        public String getHelpPhone() {
            return helpPhone;
        }

        public void setHelpPhone(String helpPhone) {
            this.helpPhone = helpPhone;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getMainUser() {
            return mainUser;
        }

        public void setMainUser(String mainUser) {
            this.mainUser = mainUser;
        }

//        public float getPeopleDiscount() {
//            return peopleDiscount;
//        }
//
//        public void setPeopleDiscount(float peopleDiscount) {
//            this.peopleDiscount = peopleDiscount;
//        }

        public String getIsActivate() {
            return isActivate;
        }

        public void setIsActivate(String isActivate) {
            this.isActivate = isActivate;
        }

        public String getRegister() {
            return register;
        }

        public void setRegister(String register) {
            this.register = register;
        }

//        public long getCreateTime() {
//            return createTime;
//        }
//
//        public void setCreateTime(long createTime) {
//            this.createTime = createTime;
//        }

        public String getIsShow() {
            return isShow;
        }

        public void setIsShow(String isShow) {
            this.isShow = isShow;
        }

        public String getPropertyInfo() {
            return propertyInfo;
        }

        public void setPropertyInfo(String propertyInfo) {
            this.propertyInfo = propertyInfo;
        }

        public String getCooperationMode() {
            return cooperationMode;
        }

        public void setCooperationMode(String cooperationMode) {
            this.cooperationMode = cooperationMode;
        }

        public boolean isActivate() {//0未激活 1已激活
            return "1".equals(isActivate);
        }
    }

    public static void loadScreenInf(ScreenInfTag screenInfTag, OnLoadScreenInfListener classResult) {
        RetrofitService.getInstance().getScreenInf(screenInfTag, new RetrofitService.CallbackClassResult<ScreenInfBean>(ScreenInfBean.class) {
            @Override
            protected void resultSuccess(ScreenInfBean screenInfBean) {
                List<DataBean> data = screenInfBean.getData();
                if (null != data && data.size() > 0) {
                    DataBean dataBean = data.get(0);
                    if (null != dataBean) {
                        classResult.success(dataBean);
                        return;
                    }
                }
                fail("无有效数据");
            }

            @Override
            protected void fail(String error) {
                classResult.fail(error);
            }
        });
    }

    public interface OnLoadScreenInfListener {
        void success(DataBean dataBean);

        void fail(String error);
    }

}
