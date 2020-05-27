package com.pjj.xsp.utils;

import android.graphics.Color;

import com.pjj.xsp.R;

/**
 * Created by XinHeng on 2019/01/19.
 * describe：
 */
public class ColorUtils {
    public static int getBgColor(String position) {
        switch (position) {
            case "1":
                return Color.parseColor("#EF3A3A");
            case "2":
                return Color.parseColor("#FB8B47");
            case "3":
                return Color.parseColor("#FBB805");
            case "4":
                return Color.parseColor("#CB1E5D");
            case "5":
                return Color.parseColor("#69B714");
            case "6":
                return Color.parseColor("#40BBF7");
            case "7":
                return Color.parseColor("#14C9BF");
            case "8":
                return Color.parseColor("#749DEC");
            case "9":
                return Color.parseColor("#7B78ED");
            case "10":
                return Color.parseColor("#1C9876");
            case "11":
                return Color.parseColor("#0172AE");
            default:
                return Color.parseColor("#6D43C9");
        }
    }
    public static int getLeftResource(String position) {
        switch (position) {
            case "1":
                return R.mipmap.left1;
            case "2":
                return R.mipmap.left2;
            case "3":
                return R.mipmap.left3;
            case "4":
                return R.mipmap.left4;
            case "5":
                return R.mipmap.left5;
            case "6":
                return R.mipmap.left6;
            case "7":
                return R.mipmap.left7;
            case "8":
                return R.mipmap.left8;
            case "9":
                return R.mipmap.left9;
            case "10":
                return R.mipmap.left10;
            case "11":
                return R.mipmap.left11;
            default:
                return R.mipmap.left12;
        }
    }
    public static int getRightResource(String position) {
        switch (position) {
            case "1":
                return R.mipmap.right1;
            case "2":
                return R.mipmap.right2;
            case "3":
                return R.mipmap.right3;
            case "4":
                return R.mipmap.right4;
            case "5":
                return R.mipmap.right5;
            case "6":
                return R.mipmap.right6;
            case "7":
                return R.mipmap.right7;
            case "8":
                return R.mipmap.right8;
            case "9":
                return R.mipmap.right9;
            case "10":
                return R.mipmap.right10;
            case "11":
                return R.mipmap.right11;
            default:
                return R.mipmap.right12;
        }
    }
}
