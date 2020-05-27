package com.pjj.xsp.utils;

/**
 * Create by xinheng on 2018/10/16。
 * describe：检测参数
 */
public class ParameterUtils {
    public static void checkNull(Object object, String explain){
        if(null==object)
            throw new NullPointerException(explain);
    }

}
