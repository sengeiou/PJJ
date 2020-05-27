package com.pjj.xsp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.pjj.xsp.PjjApplication;

/**
 * Create by xinheng on 2018/10/16。
 * describe：
 */
public class SharedUtils {
    private static final String XML_NAME = "tlw_xsp";
    private static final String TAG_CODE = "tag_code";
    public static final String ACTIVE_CODE = "active_code";
    public static final String ALIAS_CODE = "alias_code";
    public static final String VOLUME_NUM = "volume_num";

    /**
     * 保存
     *
     * @param key
     * @return
     */
    public static boolean saveForXml(String key, String value) {
        SharedPreferences tagCode = PjjApplication.application.getSharedPreferences(XML_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = tagCode.edit();
        edit.putString(key, value);
        return edit.commit();
    }

    public static boolean saveForXml(String key, int value) {
        SharedPreferences tagCode = PjjApplication.application.getSharedPreferences(XML_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = tagCode.edit();
        edit.putInt(key, value);
        return edit.commit();
    }

    public static String getXmlForKey(String key) {
        SharedPreferences tagCode = PjjApplication.application.getSharedPreferences(XML_NAME, Context.MODE_PRIVATE);
        return tagCode.getString(key, null);
    }
    public static int getXmlForKeyInt(String key,int defaultValue) {
        SharedPreferences tagCode = PjjApplication.application.getSharedPreferences(XML_NAME, Context.MODE_PRIVATE);
        return tagCode.getInt(key, defaultValue);
    }

    /**
     * 保存唯一码
     *
     * @param tag 唯一码
     * @return
     */
    public static boolean saveTagCode(String tag) {
        SharedPreferences tagCode = PjjApplication.application.getSharedPreferences(XML_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = tagCode.edit();
        edit.putString(TAG_CODE, tag);
        return edit.commit();
    }

    /**
     * @return 获取激活状态
     */
    public static String getActiveCode() {
        SharedPreferences tagCode = PjjApplication.application.getSharedPreferences(XML_NAME, Context.MODE_PRIVATE);
        return tagCode.getString(ACTIVE_CODE, null);
    }

    /**
     * 清除唯一码
     *
     * @return true-->成功
     */
    public static boolean cleanTagCode() {
        return saveTagCode("");
    }

    /**
     * 清除xml存储
     *
     * @return true-->成功
     */
    public static boolean cleanXml() {
        SharedPreferences tagCode = PjjApplication.application.getSharedPreferences(XML_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = tagCode.edit();
        edit.clear();
        return edit.commit();
    }
}
