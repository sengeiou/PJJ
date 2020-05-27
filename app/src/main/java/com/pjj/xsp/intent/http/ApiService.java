package com.pjj.xsp.intent.http;

import com.pjj.xsp.module.parameter.ActivateStatue;
import com.pjj.xsp.module.parameter.ScreenInfTag;
import com.pjj.xsp.module.parameter.ScreenOrderTask;
import com.pjj.xsp.module.parameter.ScreenshotsBean;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Create by xinheng on 2018/10/12 0012。
 * describe：retrofit网络接口
 */
public interface ApiService {
    //http://47.92.50.83:8083/queryScreenTask.action

    /**
     * 任务获取
     *
     * @param screenId
     * @return
     */
    @POST("screen/queryScreenTask.action")
    Call<ResponseBody> queryScreenTask(@Body ScreenInfTag screenId);

    //@POST("queryScreenTask.action")
    //Call<ResponseBody> getScreenTaskInf(@Body("screenId") String screenId);
    @POST("screen/getScreenList.action")
    Call<ResponseBody> getScreenList(@Body ScreenInfTag screenId);

    /**
     * 根据设备码注册广告屏
     *
     * @param screenId
     * @return
     */
    @POST("screen/registerScreen.action")
    Call<ResponseBody> sendScreenTag(@Body ScreenInfTag screenId);

    /**
     * 激活广告屏
     *
     * @param activateStatue
     * @return
     */
    @POST("screen/activateScreen.action")
    Call<ResponseBody> activateScreen(@Body ActivateStatue activateStatue);

    @GET
    Call<ResponseBody> downloadFile(@Url String url);

    /**
     * 废弃
     *
     * @return
     */
    @GET("location/ip?ak=90c7c7fcf0337fa1fb8b8ac78172e79b&coor=bd09ll&qq-pf-to=pcqq.c2c")
    Call<ResponseBody> getCurrentCityName();

    //"http://api.map.baidu.com/telematics/v3/weather?location=" + encodeCityName + "&output=json&ak=90c7c7fcf0337fa1fb8b8ac78172e79b"
    @GET("telematics/v3/weather?output=json&ak=90c7c7fcf0337fa1fb8b8ac78172e79b")
    Call<ResponseBody> getDayWeather(@Query("location") String encodeCityName);

    @GET("{city}.html")
    Call<ResponseBody> getCarXianXingNum(@Path("city") String city);

    /**
     * 广告屏根据订单号查询任务
     *
     * @param screenOrderTask
     * @return
     */
    @POST("screen/getScreenOrderTask.action")
    Call<ResponseBody> getScreenOrderTask(@Body ScreenOrderTask screenOrderTask);
    @POST("screen/getAccessKey.action")
    Call<ResponseBody> getAccessKey(@Body RequestBody body);
    @POST("screen/getScreenshotsInfo.action")
    Call<ResponseBody> addScreenImg(@Body ScreenshotsBean body);
}
