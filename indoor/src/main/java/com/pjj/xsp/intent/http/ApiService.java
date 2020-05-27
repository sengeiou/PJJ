package com.pjj.xsp.intent.http;

import com.pjj.xsp.module.parameter.ActivateStatue;
import com.pjj.xsp.module.parameter.AppUpload;
import com.pjj.xsp.module.parameter.ScreenInfTag;
import com.pjj.xsp.module.parameter.ScreenOrderTask;
import com.pjj.xsp.module.parameter.ScreenshotsBean;
import com.pjj.xsp.module.parameter.UploadTakeScreen;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Create by xinheng on 2018/10/12 0012。
 * describe：retrofit网络接口
 */
public interface ApiService {

    /**
     * 任务获取
     *
     * @param screenId
     * @return
     */
    @POST("screen/queryScreenTask.action")
    Call<ResponseBody> queryScreenTask(@Body ScreenInfTag screenId);

    //    @POST("screen/getScreenList.action")
    @POST("screen/getNewMediaScreenList.action")
    Call<ResponseBody> getScreenList(@Body ScreenInfTag screenId);

    /**
     * 根据设备码注册广告屏
     *
     * @param screenId
     * @return
     */
//    @POST("screen/registerScreen.action")
    @POST("screen/registerNewMediaScreen.action")
    Call<ResponseBody> sendScreenTag(@Body ScreenInfTag screenId);

    /**
     * 激活广告屏
     *
     * @param activateStatue
     * @return
     */
//    @POST("screen/activateScreen.action")
    @POST("screen/activateNewMediaScreen.action")
    Call<ResponseBody> activateScreen(@Body ActivateStatue activateStatue);

    @GET
    Call<ResponseBody> downloadFile(@Url String url);

    @Streaming //添加这个注解用来下载大文件
    @GET
    Call<ResponseBody> downloadBigFile(@Url String fileUrl);

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
    @POST("screen/getNewMediaScreenOrderTask.action")
    Call<ResponseBody> getScreenOrderTask(@Body ScreenOrderTask screenOrderTask);

    @POST("screen/getAccessKey.action")
    Call<ResponseBody> getAccessKey(@Body RequestBody body);

    @POST("screen/getScreenshotsInfo.action")
    Call<ResponseBody> getScreenshotsInfo(@Body ScreenshotsBean body);

    @POST("screen/updateScreenVersion.action")
    Call<ResponseBody> updateScreenVersion(@Body AppUpload body);

    @POST("/screen/receiveScreenImgInfo.action")
    Call<ResponseBody> receiveScreenImgInfo(@Body UploadTakeScreen body);
    @POST("/screen/receiveScreenResponse.action")
    Call<ResponseBody> receiveScreenImgInfo(@Body RequestBody body);

    /**
     * 屏幕确认订单投放
     * @param body
     * @return
     */
    @POST("/screen/ackOrderScreen.action")
    Call<ResponseBody> ackOrderScreen(@Body RequestBody body);

    /**
     * 接收屏幕操作命令反馈
     * @param body
     * @return
     */
    @POST("/screen/updateScreenCommandStatus.action")
    Call<ResponseBody> updateScreenCommandStatus(@Body RequestBody body);

    /**
     * 接收广告屏日志信息
     * @param body
     * @return
     */
    @POST("/screen/insertScreenErrorFile.action")
    Call<ResponseBody> insertScreenErrorFile(@Body RequestBody body);

}
