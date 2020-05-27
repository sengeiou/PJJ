package com.pjj.xsp.intent.http;

import android.os.Handler;
import android.os.Looper;

import com.pjj.xsp.BuildConfig;
import com.pjj.xsp.module.parameter.ScreenshotsBean;
import com.pjj.xsp.utils.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.pjj.xsp.PjjApplication;
import com.pjj.xsp.module.bean.ResultBean;
import com.pjj.xsp.module.bean.ScreenInfBean;
import com.pjj.xsp.module.bean.ScreenOrderTaskBean;
import com.pjj.xsp.module.parameter.ActivateStatue;
import com.pjj.xsp.module.parameter.ScreenInfTag;
import com.pjj.xsp.module.parameter.ScreenOrderTask;
import com.pjj.xsp.utils.FileUtils;
import com.pjj.xsp.utils.RSASubsectionUtile;
import com.pjj.xsp.utils.TextViewUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Create by xinheng on 2018/10/12 0012。
 * describe：网络接口实现
 */
public class RetrofitService {
    private static RetrofitService RETROFIT_SERVICE;

    private RetrofitService() {
        initRetrofit();
        handler = new Handler(Looper.getMainLooper());
    }

    private Retrofit retrofit;
    private static Gson gson;
    private Handler handler;

    public static RetrofitService getInstance() {
        if (null == RETROFIT_SERVICE) {
            synchronized (RetrofitService.class) {
                if (null == RETROFIT_SERVICE) {
                    RETROFIT_SERVICE = new RetrofitService();
                }
            }
        }
        return RETROFIT_SERVICE;
    }

    private void initRetrofit() {
        gson = new Gson();
        retrofit = getRetrofit(BuildConfig.APP_TYPE ? "http://39.98.75.37:8083/" : "http://47.92.50.83:8083/");
//        retrofit = getRetrofit("http://192.168.1.124:8083/");
    }

    public Retrofit getRetrofit(String url) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(new OkHttpClient.Builder()
                        .addInterceptor(new AppendUrlParamInterceptor())
                        .build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    public Call<ResponseBody> getCurrentCityName(Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = getRetrofit("http://api.map.baidu.com/").create(ApiService.class).getCurrentCityName();
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> getDayWeather(String encodeCityName, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = getRetrofit("http://api.map.baidu.com/").create(ApiService.class).getDayWeather(encodeCityName);
        responseBodyCall.enqueue(callback);
        return responseBodyCall;

    }

    /**
     * 发送设备唯一码
     *
     * @param screenId 唯一码
     * @param callBack 回调
     * @return 网络体
     */
    public Call<ResponseBody> sendScreenTag(ScreenInfTag screenId, Callback<ResponseBody> callBack) {
        Call<ResponseBody> responseBodyCall = retrofit.create(ApiService.class).sendScreenTag(screenId);
        responseBodyCall.enqueue(callBack);
        return responseBodyCall;
    }

    /**
     * 获取任务信息
     *
     * @param screenId 参数
     * @param callBack 回调
     * @return
     */
    public Call<ResponseBody> getScreenTaskInf(ScreenInfTag screenId, Callback callBack) {
        Call<ResponseBody> responseBodyCall = retrofit.create(ApiService.class).queryScreenTask(screenId);
        responseBodyCall.enqueue(callBack);
        return responseBodyCall;
    }

    /**
     * 获取屏幕信息
     *
     * @param screenId 参数
     * @param callBack 回调
     * @return
     */
    public Call<ResponseBody> getScreenInf(ScreenInfTag screenId, CallbackClassResult<ScreenInfBean> callBack) {
        Call<ResponseBody> responseBodyCall = retrofit.create(ApiService.class).getScreenList(screenId);
        responseBodyCall.enqueue(callBack);
        return responseBodyCall;
    }

    /**
     * 激活广告屏
     *
     * @param activateStatue 参数
     * @param callBack       回调
     * @return
     */
    public Call<ResponseBody> activateScreen(ActivateStatue activateStatue, CallbackClassResult<ResultBean> callBack) {
        Call<ResponseBody> responseBodyCall = retrofit.create(ApiService.class).activateScreen(activateStatue);
        responseBodyCall.enqueue(callBack);
        return responseBodyCall;
    }

    public Call<ResponseBody> getCarXianXingNum(String local, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = getRetrofit("https://xianxing.911cha.com/").create(ApiService.class).getCarXianXingNum(local);
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> loadAccessKeyTask(String userId, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = retrofit.create(ApiService.class).getAccessKey(getRequestBody("{\"userId\":\"" + userId + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }
    public Call<ResponseBody> uploadScreenshotsName(ScreenshotsBean screenshotsBean, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = retrofit.create(ApiService.class).addScreenImg(screenshotsBean);
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    private RequestBody getRequestBody(String string) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), string);
        return body;
    }

    public void downloadFile(String url) {
        FileUtils.OnDownloadListener onDownloadListener = new FileUtils.OnDownloadListener() {
            @Override
            public void success() {

            }

            @Override
            public void fail() {
                if (less59min())
                    handler.postDelayed(() -> downloadFile(url), 2000);
            }
        };
        downloadFile(url, onDownloadListener);
    }

    public void downloadFileRe(String url, FileUtils.OnDownloadListener onDownloadListener1) {
        FileUtils.OnDownloadListener onDownloadListener = new FileUtils.OnDownloadListener() {
            @Override
            public void success() {
                onDownloadListener1.success();
            }

            @Override
            public void fail() {
                if (less59min())
                    handler.postDelayed(() -> downloadFile(url), 2000);
                else
                    onDownloadListener1.fail();
            }
        };
        downloadFile(url, onDownloadListener);
    }

    /**
     * 文件下载
     *
     * @param url 下载链接
     */
    public void downloadFile(String url, FileUtils.OnDownloadListener onDownloadListener) {
        if (TextViewUtils.isEmpty(url)) {
            return;
        }
        final String fileName = getFileName(url);
        Log.e("TAG", "downloadFile: " + url + ", " + fileName);
        File fileComplete = new File(PjjApplication.App_Path + fileName);
        if (fileComplete.exists()) {
            Log.e("TAG", "downloadFile: 已存在");
            onDownloadListener.success();
            return;
            //fileComplete.getAbsoluteFile().delete();
        }

        Call<ResponseBody> responseBodyCall = retrofit.create(ApiService.class).downloadFile(url);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    if (null != body) {
                        InputStream inputStream = body.byteStream();
                        saveFile(fileName, inputStream, onDownloadListener);
                    } else {
                        if (less59min())
                            handler.postDelayed(() -> downloadFile(url), 2000);
                    }
                } else {
                    ResponseBody responseBody = response.errorBody();
                    if (null != responseBody) {
                        String string = null;
                        try {
                            string = responseBody.string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.e("TAG", "onResponse: error=" + string);
                    }
                    if (less59min())
                        handler.postDelayed(() -> downloadFile(url), 2000);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (null != t) {
                    t.printStackTrace();
                }
                if (less59min())
                    handler.postDelayed(() -> downloadFile(url), 2000);
            }
        });
    }

    public String getFileName(String url) {
        // 从路径中获取
        return url.substring(url.lastIndexOf("/") + 1);
    }

    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private boolean less59min() {
        Calendar calendar = Calendar.getInstance();
        int i = calendar.get(Calendar.MINUTE);
        boolean b = i < 58;
        if (!b) {
            FileUtils.saveStringFile(PjjApplication.App_Path + "error.txt", sf.format(new Date()) + "下载任务失败");
        }
        return b;
    }

    private void saveFile(final String url, final InputStream stream, FileUtils.OnDownloadListener onDownloadListener) {
        new Thread() {
            @Override
            public void run() {
                FileUtils.saveFile(PjjApplication.App_Path + url, stream, onDownloadListener);
            }
        }.start();
    }

    public Call<ResponseBody> loadSearchTaskForOrderId(ScreenOrderTask screenOrderTask, MyCallback callBack) {
        Call<ResponseBody> responseBodyCall = retrofit.create(ApiService.class).getScreenOrderTask(screenOrderTask);
        responseBodyCall.enqueue(callBack);
        return responseBodyCall;
    }


    public static abstract class MyCallback implements Callback<ResponseBody> {

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            Request request = call.request();
            //RequestBody body1 = call.request().body();
            //telematics .html
            String url = request.url().toString();
            boolean cipher = !(url.contains("telematics") || url.contains(".html")|| url.contains("addScreenImg"));

            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                if (null == body) {
                    fail("service is wrong");
                    return;
                }
                try {
                    String string = body.string();
                    if (cipher) {
                        //TODO 解密
                        string = RSASubsectionUtile.public_decipher(string);
                    }
                    success(string);
                } catch (Exception e) {
                    e.printStackTrace();
                    fail("解码错误");
                }
            } else {
                try {
                    String string = response.errorBody().string();
                    fail(string);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call call, Throwable t) {
            if (null != t) {
                t.printStackTrace();
            }
            fail("异常错误");
        }

        protected abstract void success(String s);

        protected abstract void fail(String error);
    }

    public static abstract class CallbackClass<T> extends MyCallback {
        private Class<T> tClass;

        public CallbackClass(Class<T> tClass) {
            this.tClass = tClass;
        }

        @Override
        protected void success(String s) {
            if (null == s) {
                fail("暂无数据");
            } else {
                Log.e("TAG", "success: " + s);
                T t = null;
                try {
                    t = gson.fromJson(s, tClass);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    fail("pars-error:" + s);
                    return;
                }
                if (null == t) {
                    fail("暂无数据");
                    return;
                }
                result(t);
            }
        }

        protected abstract void result(T t);
    }

    public static abstract class CallbackClassResult<T extends ResultBean> extends CallbackClass<T> {

        public CallbackClassResult(Class<T> tClass) {
            super(tClass);
        }

        @Override
        protected void result(T t) {
            if (t.isSuccess()) {
                resultSuccess(t);
            } else {
                fail(t.getMsg());
            }
        }

        protected abstract void resultSuccess(T t);
    }
}
