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
    public static final String STOP_RECOVER = "stop_recover";
    public static final String HEAD_VIEW_SHOW_TAG = "head_view_show_tag";
    /**
     * 当前升级记录
     */
    public static final String VERSION_UPDATE = "version_update";
    /**
     * 新app本地路径
     */
    public static final String NEW_APP_PATH = "new_app_path";
    /**
     * 当前系统版本
     */
    public static final String SYSTEM_API_VERSION = "system_api_version";

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

    public static int getXmlForKeyInt(String key, int defaultValue) {
        SharedPreferences tagCode = PjjApplication.application.getSharedPreferences(XML_NAME, Context.MODE_PRIVATE);
        try {
            return tagCode.getInt(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
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
