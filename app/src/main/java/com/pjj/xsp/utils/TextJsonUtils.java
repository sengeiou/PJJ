package com.pjj.xsp.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Create by xinheng on 2018/11/19。
 * describe：建议尽量只是主线程调用
 */
public class TextJsonUtils {
    public static final Gson gson = new Gson();

    public static String toJsonString(Object o) {
        return gson.toJson(o);
    }

    public static <T> T parse(String s, Class<T> cls) {
        return gson.fromJson(s, cls);
    }

    public static <T> List<T> parseList(String s) {
        Type type = new TypeToken<List<T>>() {
        }.getType();
        return gson.fromJson(s, type);
    }

    public static boolean isNotEmpty(List list) {
        return null != list && list.size() > 0;
    }
}
