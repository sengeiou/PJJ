package com.pjj.xsp.contract;

import com.pjj.xsp.db.MinePlayTask;
import com.pjj.xsp.module.bean.WeatherBean;
import com.pjj.xsp.module.parameter.ScreenshotsBean;

import java.util.List;

/**
 * Create by xinheng on 2018/11/01。
 * describe：
 */
public interface MainContract {
    interface View extends BaseView {
        void updateWeatherView(List<WeatherBean.ResultsBean.WeatherDataBean> list);

        void updateXianXingNum(String[] arrays);

        void setUnActivateResult(boolean tag);

        void reStart(String cooperationMode);

        void checkOnLine();

        void updateLocalCityName(String cityName);
    }

    interface Present {
        void loadCityName();

        void loadCityWeather(String cityName);

        void loadXianXingNum(String localName);

        void loadXspNextTask();

        void loadWeiBao();

        void loadUnActivateStatue();

        void loadSearchTaskForOrderId(String orderId);

        void uploadScreenshotsName(ScreenshotsBean screenshotsBean);

        void loadLunBoTaskForOrderId(String order);

        void loadTakeScreenResult(String orderId, String error);

        void addMineTask(MinePlayTask task);

        void initHeartJson();
        void codeTaskFeedback(String json);

        void downloadFile(String path, String fileName, String code);
        void insertJson(String json);

        void uploadFile(String filePath, String msg);
    }
}
