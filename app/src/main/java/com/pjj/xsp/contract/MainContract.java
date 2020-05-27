package com.pjj.xsp.contract;

import com.pjj.xsp.module.bean.WeatherBean;
import com.pjj.xsp.module.parameter.ScreenshotsBean;

import java.util.List;

/**
 * Create by xinheng on 2018/11/01。
 * describe：
 */
public interface MainContract {
    interface View extends BaseView {
        void updateLocalCityName(String cityName);
        void updateWeatherView(List<WeatherBean.ResultsBean.WeatherDataBean> list);
        void updateXianXingNum(String[] arrays);
        void setUnActivateResult(boolean tag);
        //void setSearchTask();
    }
    interface Present{
        void loadCityName();
        void loadCityWeather(String cityName);
        void loadXianXingNum(String localName);
        void loadXspNextTask();

        void loadWeiBao();
        void loadUnActivateStatue();

        void loadSearchTaskForOrderId(String orderId);
        void uploadScreenshotsName(ScreenshotsBean screenshotsBean);
    }
}
