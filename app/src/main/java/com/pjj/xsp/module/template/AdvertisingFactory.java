package com.pjj.xsp.module.template;

import com.pjj.xsp.utils.Log;

import com.pjj.xsp.R;

import java.util.Random;

/**
 * Create by xinheng on 2018/11/14。
 * describe：
 */
public class AdvertisingFactory {
    private static String[] ss = {"#011520", "#08856E", "#0874A3", "#951C14", "#903A08", "#130909", "#211A4D", "#4A4642"};
    private static int index = -1;

    public static AdvertisingBean getAdvertisingBeanLoop() {
        if (index == -1) {
            Random random = new Random();
            index = random.nextInt(ss.length);
        }
        index++;
        if (index == ss.length) {
            index = 0;
        }
        //index=3;
        return createAdvertising(ss[index]);
    }

    public static AdvertisingBean createAdvertising(String bottomMidBg) {
        Log.e("TAG", "createAdvertising: " + bottomMidBg);
        AdvertisingBean advertisingBean;
        switch (bottomMidBg) {
            case "#011520":
                advertisingBean = get011520();
                break;
            case "#08856E":
                advertisingBean = get08856E();
                break;
            case "#0874A3":
                advertisingBean = get0874A3();
                break;
            case "#951C14":
                advertisingBean = get951C14();
                break;
            case "#903A08":
                advertisingBean = get903A08();
                break;
            case "#130909":
                advertisingBean = get130909();
                break;
            case "#211A4D":
                advertisingBean = get211A4D();
                break;
            case "#4A4642":
                advertisingBean = get4A4642();
                break;
            default:
                advertisingBean = get011520();
        }
        return advertisingBean;
    }

    private static AdvertisingBean get011520() {
        AdvertisingBean ad = new AdvertisingBean();
        ad.setImageResource(R.mipmap.bg011520);
        ad.setTopTitleColor("#0DFFFF");
        ad.setTopTitleTextColor("#FFFFFF");
        ad.setTopTitleTextLineColor("#026C7C");
        ad.setTopAdContentColor("#0DFFFF");

        ad.setDiyBgColor("#011520");

        ad.setBottomElevatorBgColor("#012130");
        ad.setBottomElevatorTextColor("#FFFFFF");
        ad.setBottomImaBgColor("#013544");
        ad.setBottomImaColor("#0DFFFF");

        ad.setBottomMidBgColor("#011520");
        ad.setBottomDateTextColor("#026C7C");
        ad.setBottomWeekTextColor("#026C7C");
        ad.setBottomTimeTextColor("#FFFFFF");
        ad.setBottomCarTextColor("#0DFFFF");
        ad.setBottomCarNumColor("#EDA915");

        ad.setBottomOnlineCityBgColor("#012D3A");
        ad.setBottomOnlineTagColor("#04FD19");
        ad.setBottomOnlineTextColor("#0DFFFF");
        ad.setBottomCityColor("#0DFFFF");
        ad.setBottomDayTemperatureBgColor("#012130");
        ad.setBottomDayColor("#FFFFFF");
        ad.setBottomTemperatureColor("#0DFFFF");
        ad.setBottomVLineColor("#011520");
        return ad;
    }

    private static AdvertisingBean get08856E() {
        AdvertisingBean ad = new AdvertisingBean();
        ad.setImageResource(R.mipmap.bg08856e);
        ad.setTopTitleColor("#FFFFFF");
        ad.setTopTitleTextColor("#FFFFFF");
        ad.setTopTitleTextLineColor("#E6E6E6");
        ad.setTopAdContentColor("#FFFFFF");

        ad.setDiyBgColor("#08856E");

        ad.setBottomElevatorBgColor("#09A187");
        ad.setBottomElevatorTextColor("#FFFFFF");
        ad.setBottomImaBgColor("#2BBDA6");
        ad.setBottomImaColor("#FFFFFF");

        ad.setBottomMidBgColor("#08856E");
        ad.setBottomDateTextColor("#2BBDA6");
        ad.setBottomWeekTextColor("#2BBDA6");
        ad.setBottomTimeTextColor("#FFFFFF");
        ad.setBottomCarTextColor("#EAEAEA");
        ad.setBottomCarNumColor("#EDA915");

        ad.setBottomOnlineCityBgColor("#2BBDA6");
        ad.setBottomOnlineTagColor("#04FD19");
        ad.setBottomOnlineTextColor("#FFFFFF");
        ad.setBottomCityColor("#FFFFFF");
        ad.setBottomDayTemperatureBgColor("#09A187");
        ad.setBottomDayColor("#D5D5D5");
        ad.setBottomTemperatureColor("#FFFFFF");
        ad.setBottomVLineColor("#08856E");
        return ad;
    }

    private static AdvertisingBean get0874A3() {
        AdvertisingBean ad = new AdvertisingBean();
        ad.setImageResource(R.mipmap.bg0874a3);
        ad.setTopTitleColor("#FFFFFF");
        ad.setTopTitleTextColor("#FFFFFF");
        ad.setTopTitleTextLineColor("#B3E9FF");
        ad.setTopAdContentColor("#FFFFFF");

        ad.setDiyBgColor("#0874A3");

        ad.setBottomElevatorBgColor("#098CBF");
        ad.setBottomElevatorTextColor("#EEC748");
        ad.setBottomImaBgColor("#38AFDA");
        ad.setBottomImaColor("#FFFFFF");

        ad.setBottomMidBgColor("#0874A3");
        ad.setBottomDateTextColor("#5CD0FF");
        ad.setBottomWeekTextColor("#5CD0FF");
        ad.setBottomTimeTextColor("#FFFFFF");
        ad.setBottomCarTextColor("#B3E9FF");
        ad.setBottomCarNumColor("#EEC748");

        ad.setBottomOnlineCityBgColor("#38AFDA");
        ad.setBottomOnlineTagColor("#04FD19");
        ad.setBottomOnlineTextColor("#EEC748");
        ad.setBottomCityColor("#FFFFFF");
        ad.setBottomDayTemperatureBgColor("#098CBF");
        ad.setBottomDayColor("#FFFFFF");
        ad.setBottomTemperatureColor("#EEC748");
        ad.setBottomVLineColor("#0F89BB");
        return ad;
    }

    private static AdvertisingBean get951C14() {
        AdvertisingBean ad = new AdvertisingBean();
        ad.setImageResource(R.mipmap.bg951c14);
        //ad.setImageResource(R.mipmap.text_bg);
        ad.setTopTitleColor("#FFFFFF");
        ad.setTopTitleTextColor("#FFFFFF");
        ad.setTopTitleTextLineColor("#B3E9FF");
        ad.setTopAdContentColor("#FFFFFF");

        ad.setDiyBgColor("#951C14");

        ad.setBottomElevatorBgColor("#AC2D25");
        ad.setBottomElevatorTextColor("#EDA915");
        ad.setBottomImaBgColor("#C33C32");
        ad.setBottomImaColor("#FFFFFF");

        ad.setBottomMidBgColor("#951C14");
        ad.setBottomDateTextColor("#B9BCC1");
        ad.setBottomWeekTextColor("#B9BCC1");
        ad.setBottomTimeTextColor("#FFFFFF");
        ad.setBottomCarTextColor("#DEE1E5");
        ad.setBottomCarNumColor("#EDA915");

        ad.setBottomOnlineCityBgColor("#C33C32");
        ad.setBottomOnlineTagColor("#04FD19");
        ad.setBottomOnlineTextColor("#EDA915");
        ad.setBottomCityColor("#B9BCC1");
        ad.setBottomDayTemperatureBgColor("#AC2D25");
        ad.setBottomDayColor("#FFFFFF");
        ad.setBottomTemperatureColor("#DEE1E5");
        ad.setBottomVLineColor("#951C14");
        return ad;
    }

    private static AdvertisingBean get903A08() {
        AdvertisingBean ad = new AdvertisingBean();
        ad.setImageResource(R.mipmap.bg903a08);
        ad.setTopTitleColor("#FFFFFF");
        ad.setTopTitleTextColor("#FFFFFF");
        ad.setTopTitleTextLineColor("#B3E9FF");
        ad.setTopAdContentColor("#FFFFFF");

        ad.setDiyBgColor("#903A08");
        ad.setBottomMidBgColor("#903A08");

        ad.setBottomElevatorBgColor("#A9541C");
        ad.setBottomElevatorTextColor("#FFFFFE");
        ad.setBottomImaBgColor("#C36C2D");
        ad.setBottomImaColor("#FFFFFF");

        ad.setBottomDateTextColor("#E08D49");
        ad.setBottomWeekTextColor("#E08D49");
        ad.setBottomTimeTextColor("#FFFFFF");
        ad.setBottomCarTextColor("#FFFFFF");
        ad.setBottomCarNumColor("#FCC249");

        ad.setBottomOnlineCityBgColor("#C36C2D");
        ad.setBottomOnlineTagColor("#04FD19");
        ad.setBottomOnlineTextColor("#FFFFFF");
        ad.setBottomCityColor("#EEA31A");
        ad.setBottomDayTemperatureBgColor("#A9541C");
        ad.setBottomDayColor("#FFFFFF");
        ad.setBottomTemperatureColor("#FCC249");
        ad.setBottomVLineColor("#903A08");
        return ad;
    }

    private static AdvertisingBean get130909() {
        AdvertisingBean ad = new AdvertisingBean();
        ad.setImageResource(R.mipmap.bg130909);
        ad.setTopTitleColor("#FFFFFF");
        ad.setTopTitleTextColor("#FEA345");
        ad.setTopTitleTextLineColor("#AAAAAA");
        ad.setTopAdContentColor("#FFFFFF");

        ad.setDiyBgColor("#130909");
        ad.setBottomMidBgColor("#130909");

        ad.setBottomElevatorBgColor("#1B1111");
        ad.setBottomElevatorTextColor("#FFFFFF");
        ad.setBottomImaBgColor("#221717");
        ad.setBottomImaColor("#FEA345");

        ad.setBottomDateTextColor("#EAEAEA");
        ad.setBottomWeekTextColor("#EAEAEA");
        ad.setBottomTimeTextColor("#FFFFFF");
        ad.setBottomCarTextColor("#F88319");
        ad.setBottomCarNumColor("#FEA345");

        ad.setBottomOnlineCityBgColor("#221717");
        ad.setBottomOnlineTagColor("#04FD19");
        ad.setBottomOnlineTextColor("#FFFFFF");
        ad.setBottomCityColor("#B84F01");
        ad.setBottomDayTemperatureBgColor("#1B1111");
        ad.setBottomDayColor("#FFFFFF");
        ad.setBottomTemperatureColor("#FEA345");
        ad.setBottomVLineColor("#130909");
        return ad;
    }

    private static AdvertisingBean get211A4D() {
        AdvertisingBean ad = new AdvertisingBean();
        ad.setImageResource(R.mipmap.bg211a4d);
        ad.setTopTitleColor("#8781CC");
        ad.setTopTitleTextColor("#FFFFFF");
        ad.setTopTitleTextLineColor("#5B53A1");
        ad.setTopAdContentColor("#FFFFFF");

        ad.setDiyBgColor("#211A4D");
        ad.setBottomMidBgColor("#211A4D");

        ad.setBottomElevatorBgColor("#272057");
        ad.setBottomElevatorTextColor("#736DB2");
        ad.setBottomImaBgColor("#302A66");
        ad.setBottomImaColor("#FFFFFF");

        ad.setBottomDateTextColor("#8781CC");
        ad.setBottomWeekTextColor("#8781CC");
        ad.setBottomTimeTextColor("#FFFFFF");
        ad.setBottomCarTextColor("#EAEAEA");
        ad.setBottomCarNumColor("#FEA345");

        ad.setBottomOnlineCityBgColor("#302A66");
        ad.setBottomOnlineTagColor("#04FD19");
        ad.setBottomOnlineTextColor("#736DB2");
        ad.setBottomCityColor("#8781CC");
        ad.setBottomDayTemperatureBgColor("#272057");
        ad.setBottomDayColor("#FFFFFF");
        ad.setBottomTemperatureColor("#FEA345");
        ad.setBottomVLineColor("#130909");
        return ad;
    }

    private static AdvertisingBean get4A4642() {
        AdvertisingBean ad = new AdvertisingBean();
        ad.setImageResource(R.mipmap.bg4a4642);
        ad.setTopTitleColor("#BABABA");
        ad.setTopTitleTextColor("#FFFFFF");
        ad.setTopTitleTextLineColor("#A9A9A8");
        ad.setTopAdContentColor("#FFFFFF");

        ad.setDiyBgColor("#4A4642");
        ad.setBottomMidBgColor("#4A4642");

        ad.setBottomElevatorBgColor("#595551");
        ad.setBottomElevatorTextColor("#BABABA");
        ad.setBottomImaBgColor("#696560");
        ad.setBottomImaColor("#FFFFFF");

        ad.setBottomDateTextColor("#EAEAEA");
        ad.setBottomWeekTextColor("#EAEAEA");
        ad.setBottomTimeTextColor("#FFFFFF");
        ad.setBottomCarTextColor("#EAEAEA");
        ad.setBottomCarNumColor("#FEA345");

        ad.setBottomOnlineCityBgColor("#696560");
        ad.setBottomOnlineTagColor("#04FD19");
        ad.setBottomOnlineTextColor("#BABABA");
        ad.setBottomCityColor("#EAEAEA");
        ad.setBottomDayTemperatureBgColor("#595551");
        ad.setBottomDayColor("#FFFFFF");
        ad.setBottomTemperatureColor("#FEA345");
        ad.setBottomVLineColor("#130909");
        return ad;
    }
}
