package com.pjj.xsp.intent.http;

import com.pjj.xsp.BuildConfig;
import com.pjj.xsp.utils.Log;
import com.pjj.xsp.utils.RSASubsectionUtile;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by XinHeng on 2018/12/03.
 * describe：
 */
public class AppendUrlParamInterceptor implements Interceptor {
    public static final boolean elevatorTag = BuildConfig.USE_TYPE.contains("elevator_data");

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
        String s = bodyToString(oldRequest.body());
        if (BuildConfig.DEBUG) {
            Log.e("TAG", "参数json：" + s);
        }
        Request.Builder builder = null;
        //if (!TextUtils.isEmpty(s)) {
        if (!(s == null || s.length() == 0)) {
            //关键部分，设置requestBody的编码格式为json
            try {
                //boolean jaimi = oldRequest.url().toString().contains("addScreenImg");
                String content = RSASubsectionUtile.public_encipher(s);
                Log.e("TAG", "intercept: 密文："+content );
                builder = oldRequest.newBuilder().post(RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), content));
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            builder = oldRequest.newBuilder();
        }
        HttpUrl url = oldRequest.url();
        if (elevatorTag) {
            String path = url.encodedPath();
            Log.e("TAG", "intercept11: " + path);
            HttpUrl.Builder urlBuilder = url.newBuilder();
            List<String> strings = url.encodedPathSegments();
            if (strings.size() > 1) {
                if (path.contains("registerNewMediaScreen")) {
                    urlBuilder.setPathSegment(1, "registerScreen.action");
                } else if (path.contains("activateNewMediaScreen")) {
                    urlBuilder.setPathSegment(1, "activateScreen.action");
                } else if (path.contains("getNewMediaScreenList")) {
                    urlBuilder.setPathSegment(1, "getScreenList.action");
                }
            }
            HttpUrl httpUrl = urlBuilder.build();
            builder.url(httpUrl);
        }
        Request build = builder.build();
        Log.e("TAG", "intercept11: " + build.url());
        return chain.proceed(build);
    }

    private static String bodyToString(RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}
