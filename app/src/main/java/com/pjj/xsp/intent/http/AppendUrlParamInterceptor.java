package com.pjj.xsp.intent.http;

import android.text.TextUtils;

import com.pjj.xsp.utils.RSASubsectionUtile;

import java.io.IOException;

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

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
        String s = bodyToString(oldRequest.body());
        Request build;
        //if (!TextUtils.isEmpty(s)) {
        if (!(s == null || s.length() == 0)) {
            //关键部分，设置requestBody的编码格式为json
            Request.Builder builder = null;
            try {
                //boolean jaimi = oldRequest.url().toString().contains("addScreenImg");
                builder = oldRequest.newBuilder().post(RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), RSASubsectionUtile.public_encipher(s)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            build = builder.build();
        } else {
            build = oldRequest.newBuilder().build();
        }
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
