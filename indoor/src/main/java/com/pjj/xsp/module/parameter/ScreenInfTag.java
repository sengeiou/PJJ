package com.pjj.xsp.module.parameter;

import java.util.Calendar;

/**
 * Create by xinheng on 2018/10/12 0012。
 * describe：广告屏唯一码
 */
public class ScreenInfTag {

    private String screenId;
    private String curDate;//播放日期 2018-10-15
    private String hour;//播放时间 13
    private String screenSize;
    public String getScreenId() {
        return screenId;
    }

    public String getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(String screenSize) {
        this.screenSize = screenSize;
    }

    public void setScreenId(String screenId) {
        this.screenId = screenId;
    }

    public String getCurDate() {
        return curDate;
    }

    public void setCurDate(String curDate) {
        this.curDate = curDate;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
    public void setLocalTime(){
        Calendar instance = Calendar.getInstance();
        int year = instance.get(Calendar.YEAR);
        int month = instance.get(Calendar.MONTH)+1;
        int date = instance.get(Calendar.DATE);
        int hour = instance.get(Calendar.HOUR_OF_DAY);
        setCurDate(year+"-"+getFormatString(month)+"-"+getFormatString(date));
        setHour(hour+"");
    }
    public void clearTime(){
        hour=null;
        curDate=null;
    }
    private String getFormatString(int mun){
        return String.format("%02d",mun);
    }
}
