package com.pjj.xsp.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pjj.xsp.PjjApplication;
import com.pjj.xsp.R;
import com.pjj.xsp.manage.BodyRunnable;
import com.pjj.xsp.manage.BootManage;
import com.pjj.xsp.manage.XSPSystem;
import com.pjj.xsp.module.MediaTaskCacheHelp;
import com.pjj.xsp.module.ScreenInfManage;
import com.pjj.xsp.module.bean.BoardBean;
import com.pjj.xsp.module.bean.ScreenInfBean;
import com.pjj.xsp.module.bean.WeatherBean;
import com.pjj.xsp.utils.Log;
import com.pjj.xsp.utils.TextViewUtils;

import java.util.Calendar;
import java.util.List;

/**
 * Create by xinheng on 2018/10/30。
 * describe：主页面view管理
 */
public class MainViewHelp implements /*SocketConnectListener,*/ BodyRunnable.OnHasPeopleListener {
    private View head;
    private View headView;
    private ViewGroup footParentView;
    private Handler handler;
    private int weekOld = -1;
    private Calendar calendar;
    private ImageView iv_people_statue;
    private View ll_car;
    private int colorElevator;
    private int unColorElevator = Color.parseColor("#FFFFFF");
    private boolean runChangeTag;
    private boolean isHasPeopleTag;
    private static com.pjj.xsp.view.MainViewHelp INSTANCE;
    //private TextView tv_elevator_text;

    public static com.pjj.xsp.view.MainViewHelp getInstance() {
        if (null == INSTANCE) {
            synchronized (com.pjj.xsp.view.MainViewHelp.class) {
                if (null == INSTANCE) {
                    INSTANCE = new com.pjj.xsp.view.MainViewHelp();
                }
            }
        }
        return INSTANCE;
    }


    private MainViewHelp() {
        handler = new Handler(Looper.getMainLooper(), callback);
    }

    public static int getColor(String color) {
        int i = Color.BLACK;
        try {
            i = Color.parseColor(color);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    private Drawable getCircleDrawable(String color) {
        //12px #FF0115 #04FD19
        int px12 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 12, headView.getResources().getDisplayMetrics());
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(getColor(color));
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setBounds(0, 0, px12, px12);
        return drawable;
    }

    Handler.Callback callback = msg -> {
        updateNowTime();
        return true;
    };

    private TextView tv_now_time;

    /*public void updateWeiBao() {
        ScreenInfBean.DataBean screenId = ScreenInfManage.getInstance().getScreenInfDataBean();
        if (null != screenId) {
            TextView tv_name_no = headView.findViewById(R.id.tv_no);//设备号
            tv_name_no.setText(TextViewUtils.clean(screenId.getScreenCode()));

            TextView tv_time = headView.findViewById(R.id.tv_weibao_date);
            TextView tv_help_phone = headView.findViewById(R.id.tv_help_phone);
            tv_time.setText(screenId.getLastTime());
            tv_help_phone.setText(screenId.getHelpPhone());

        }
    }*/
    public void updateWeiBao() {
        ScreenInfBean.DataBean screenId = ScreenInfManage.getInstance().getScreenInfDataBean();
        if (null != screenId) {
            TextView tv_name_no = headView.findViewById(R.id.tv_no);//设备号
            tv_name_no.setText(TextViewUtils.clean(screenId.getScreenCode()));
            if ("1".equals(screenId.getIsShow())) {
                Log.e("TAG", "updateWeiBao: show");
                if (footParentView.getChildCount() == 0) {
                    footParentView.addView(LayoutInflater.from(footParentView.getContext()).inflate(R.layout.layout_play_head_ad, footParentView, false));
                }
                TextView tv_bianmin = footParentView.findViewById(R.id.tv_bianmin);
                tv_bianmin.setText(TextViewUtils.clean(screenId.getPropertyInfo()));
                String title = screenId.getPropertyTitle();
                if (TextUtils.isEmpty(title)) {
                    return;
                }
                TextView tv_bianmin_name = footParentView.findViewById(R.id.tv_bianmin_name);
                TextView tv_bianmin_name1 = footParentView.findViewById(R.id.tv_bianmin_name1);
                int index = title.indexOf("\n");
                if (index > -1 && index < 4) {
                    String first = title.substring(0, index);
                    tv_bianmin_name.setText(first);
                    if (index + 1 < title.length()) {
                        String second = title.substring(index + 1).replace("\n", "");
                        tv_bianmin_name1.setVisibility(View.VISIBLE);
                        tv_bianmin_name1.setText(second);
                    }
                } else {
                    int length = title.length();
                    if (length > 4) {
                        tv_bianmin_name1.setVisibility(View.VISIBLE);
                        title = title.replace("\n", "");
                        String first = title.substring(0, 4);
                        String second = title.substring(4, length);
                        tv_bianmin_name.setText(first);
                        tv_bianmin_name1.setText(second);
                    } else {
                        tv_bianmin_name1.setVisibility(View.GONE);
                        tv_bianmin_name.setText(title);
                    }
                }
            } else {
                if (null != footParentView)
                    footParentView.removeAllViews();
            }
        }
    }

    public void initFootView(ViewGroup parents) {
        footParentView = parents;
        Log.e("TAG", "initFootView: " + (parents == null));
    }

    public void initView(Context context, ViewGroup parents) {
        headView = LayoutInflater.from(context).inflate(R.layout.layout_foot_weibao_elevator, parents, false);
        tv_now_time = headView.findViewById(R.id.tv_now_time);
        tv_now_time.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/DIGIFAW.TTF"));
        iv_people_statue = headView.findViewById(R.id.iv_people_statue);
        ll_car = headView.findViewById(R.id.ll_car);
        ll_car.setVisibility(View.GONE);
        Typeface fontFace = Typeface.createFromAsset(context.getAssets(), "fonts/IMPACT.TTF");
        TextView tv_name_no = headView.findViewById(R.id.tv_no);//设备号
        TextView tv_time = headView.findViewById(R.id.tv_weibao_date);
        TextView tv_help_phone = headView.findViewById(R.id.tv_help_phone);
        tv_name_no.setTypeface(fontFace);
        tv_time.setTypeface(fontFace);
        tv_help_phone.setTypeface(fontFace);
        //tv_car.setText(Html.fromHtml("今日限行的尾号是：<font color='#EDA915'>5</font> 和 <font color='#EDA915'>0</font>"));
        //updateOnlineStatue(XSPSystem.isNetworkConnected(PjjApplication.application));
        updateOnlineStatue(false);
        parents.addView(headView);
        oldHour = -1;
        weekOld = -1;
        firstSetTime = true;
        updateNowTime();
        //2018年12月20日10:57:52
        if (PjjApplication.OLD_3188) {
            BodyRunnable bodyRunnable = new BodyRunnable();
            bodyRunnable.setOnHasPeopleListener(this);
            bodyRunnable.run();
        }
        headView.post(() -> {
            if (BootManage.getInstance().openSerialPort()) {
                //   SocketManage.getInstance().createSocket("ws://47.92.50.83:8083/websocket/" + XSPSystem.getInstance().getOnlyCode() + "/1/1");
                BootManage.getInstance().statReadThread();
            }
        });
    }

    public void updateCarNum(String[] arrays) {
        if (arrays != null && arrays.length == 2 && weekOld != 7 && weekOld != 1) {//周末
            ll_car.setVisibility(View.VISIBLE);
            TextView tv_num1 = headView.findViewById(R.id.tv_car_no);
            TextView tv_num2 = headView.findViewById(R.id.tv_car_no1);
            //tv_car.setText(Html.fromHtml("今日限行的尾号是：<font color='#EDA915'>" + arrays[0] + "</font> 和 <font color='#EDA915'>" + arrays[1] + "</font>"));
            tv_num1.setText(arrays[0]);
            tv_num2.setText(arrays[1]);
        } else {
            ll_car.setVisibility(View.GONE);
        }
    }

    private int oldHour = -1;

    public int getWeekOld() {
        return weekOld;
    }

    private boolean reStartTag = false;
    /**
     * 第一次设置时间标志
     */
    private boolean firstSetTime = true;

    public void updateNowTime_(boolean tag) {
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int date = calendar.get(Calendar.DATE);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        //Log.e("TAG", "updateNowTime: " + year + "年" + month + "月" + date + "日 " + ", "+week);
        if (oldHour == -1) {
            oldHour = hour;
        }
        if (week != this.weekOld && null != onNewDayListener) {
            if (weekOld != -1) {
                onNewDayListener.newDay(week + "");
            }
            weekOld = week;

            TextView tv_now_date = headView.findViewById(R.id.tv_now_date);
            TextView tv_now_week = headView.findViewById(R.id.tv_now_week);
            tv_now_date.setText(year + "年" + month + "月" + date + "日");
            tv_now_week.setText(getWeek(week));

            needUpdateWeatherTag = true;
            reStartTag = true;
        } else {
            if (hour == 8 && min == 0 && needUpdateWeatherTag) {
                oldHour = hour;
                needUpdateWeatherTag = false;
                onNewDayListener.newDay(week + "");
            } else if (tag && hour == 2 && min == 0 && reStartTag && !firstSetTime) {
                reStartTag = false;
                XSPSystem.getInstance().reboot();
            } else {
                //Log.e("TAG", "updateNowTime_: hour=" + hour + ", min=" + min);
            }
        }
        tv_now_time.setText(formString(hour) + " : " + formString(min));
        MediaTaskCacheHelp.getInstance().checkNowHour(hour);
//        if (MediaTaskCacheHelp.getInstance().mHour != hour && MediaTaskCacheHelp.getInstance().checkHourLimitTask(hour)) {
//            MediaTaskCacheHelp.getInstance().startLimitTask();
//        } else {
//            MediaTaskCacheHelp.getInstance().clearLimitTask();
//        }
        if (firstSetTime && hour != 2 && min != 0) {
            firstSetTime = false;
        }
    }

    private void updateNowTime() {
        updateNowTime_(true);
        handler.sendEmptyMessageDelayed(1, 30 * 1000);
    }

    private boolean needUpdateWeatherTag = true;
    private boolean firstStart;

    private void updateElevatorStatue(BoardBean boardBean) {
        firstStart = true;
        //Log.e("TAG", "updateElevatorStatue: 电梯状态更新");
        ImageView iv_elevator_door_statue = headView.findViewById(R.id.iv_elevator_door_statue);
        ImageView iv_elevator_run_statue = headView.findViewById(R.id.iv_elevator_run_statue);
        TextView iv_elevator_floor = headView.findViewById(R.id.iv_elevator_floor);

        int ret = boardBean.getRet();
        BoardBean.DataBean data = boardBean.getData();
        String floor = data.getFloor();
        iv_elevator_floor.setText(floor + "F");

        String upDown = data.getUpDown();
        if (null != upDown) {
            if ("FF".equals(upDown)) {//0F：电梯上行 10：电梯下行 FF：停梯
                setFilter(iv_elevator_run_statue, unColorElevator, 51);
                iv_elevator_run_statue.setImageResource(R.mipmap.tingzhi);
            } else if ("0F".equals(upDown)) {
                //statue="上行状态";
                setFilter(iv_elevator_run_statue, colorElevator, 255);
                iv_elevator_run_statue.setImageResource(R.mipmap.xiangshang);
            } else {//"10" 电梯处于下行状态
                setFilter(iv_elevator_run_statue, colorElevator, 255);
                iv_elevator_run_statue.setImageResource(R.mipmap.xiangxia);
            }
        }
        String faultReportA = data.getFaultReportA();
        if (!"FF".equals(faultReportA)) {
            faultReportA = "A故障：" + faultReportA;
        }
        String doorState = data.getDoorState();
        if ("0D".equals(doorState)) {//0D ：开门状态 0E：关门状态 FF：等待进入工作状态
            //doorStatue="开门";
            setFilter(iv_elevator_door_statue, colorElevator, 255);
            iv_elevator_door_statue.setImageResource(R.mipmap.kaimen);
        } else if ("FF".equals(doorState)) {
            //doorStatue="门FF";//电梯处于等待进入工作状态
            setFilter(iv_elevator_door_statue, unColorElevator, 51);
            iv_elevator_door_statue.setImageResource(R.mipmap.mentingzhi);
        } else {
            //doorStatue="关门";
            setFilter(iv_elevator_door_statue, colorElevator, 255);
            iv_elevator_door_statue.setImageResource(R.mipmap.guanmen);
        }

    }

    private void setFilter(ImageView iv, int color, int apha) {
//        iv.setColorFilter(color);
//        iv.setAlpha(apha);
        //iv.setImageResource(resource);
    }

    public void updateCityName(String cityName) {
        TextView tv_local_city = headView.findViewById(R.id.tv_local_city);
        tv_local_city.setText("当前城市：" + cityName.replace("市", ""));
    }

    public void updateWeather(List<WeatherBean.ResultsBean.WeatherDataBean> list) {
        //Log.e("TAG", "updateWeather: size=" + list.size());
        if (list.size() > 2) {
            TextView tv_today = headView.findViewById(R.id.tv_today);
            TextView tv_tomorrow = headView.findViewById(R.id.tv_tomorrow);
            TextView tv_after_tomorrow = headView.findViewById(R.id.tv_after_tomorrow);

            ImageView iv_today = headView.findViewById(R.id.iv_today);
            ImageView iv_tomorrow = headView.findViewById(R.id.iv_tomorrow);
            ImageView iv_after_tomorrow = headView.findViewById(R.id.iv_after_tomorrow);
            //今日
            WeatherBean.ResultsBean.WeatherDataBean today = list.get(0);
            tv_today.setText(today.getTemperature());
            updateImage(iv_today, getImageResource(today.getWeather()), today.getDayPictureUrl());

            WeatherBean.ResultsBean.WeatherDataBean tomorrow = list.get(1);
            tv_tomorrow.setText(tomorrow.getTemperature());
            updateImage(iv_tomorrow, getImageResource(tomorrow.getWeather()), tomorrow.getDayPictureUrl());

            WeatherBean.ResultsBean.WeatherDataBean after_tomorrow = list.get(2);
            tv_after_tomorrow.setText(after_tomorrow.getTemperature());
            updateImage(iv_after_tomorrow, getImageResource(after_tomorrow.getWeather()), after_tomorrow.getDayPictureUrl());
        }

    }

    private String formString(int i) {
        return String.format("%02d", i);
    }

    private String getWeek(int week) {
        switch (week) {
            case 2:
                return "星期一";
            case 3:
                return "星期二";
            case 4:
                return "星期三";
            case 5:
                return "星期四";
            case 6:
                return "星期五";
            case 7:
                return "星期六";
            default:
                return "星期日";
        }
    }

    RequestOptions requestOptions = new RequestOptions().error(R.mipmap.yintian);

    private void updateImage(ImageView iv, int resource, String path) {
        if (resource == -1) {
            Glide.with(headView).load(path).apply(requestOptions).into(iv);
        } else {
            iv.setImageResource(resource);
        }
    }

    private int getImageResource(String s) {
        Log.e("TAG", "getImageResource: 天气=" + s);
        if (null == s) {
            return -1;
        }
        int resource = -1;
        switch (s) {
            case "暴雨":
                resource = R.mipmap.baoyu;
                break;
            case "冰雹":
                resource = R.mipmap.bingbao;
                break;
            case "大雪":
                resource = R.mipmap.daxue;
                break;
            case "大雨":
                resource = R.mipmap.dayu;
                break;
            case "多云":
            case "多云转阴":
                resource = R.mipmap.duoyun;
                break;
            case "雷雨":
                resource = R.mipmap.leiyu;
                break;
            case "晴":
                resource = R.mipmap.qing;
                break;
            case "沙尘":
                resource = R.mipmap.shachen;
                break;
            case "霜":
                resource = R.mipmap.shuang;
                break;
            case "雾霾":
                resource = R.mipmap.wumai;
                break;
            case "小雪":
                resource = R.mipmap.xiaoxue;
                break;
            case "小雨转晴":
                resource = R.mipmap.xiaoyuzhuanqing;
                break;
            case "雪转晴":
                resource = R.mipmap.xuezhuanqing;
                break;
            case "夜":
                resource = R.mipmap.ye;
                break;
            case "夜多云":
                resource = R.mipmap.yeduoyun;
                break;
           /* case "阴天":
            case "阴转多云":
                resource = R.mipmap.yintian;
                break;*/
            case "雨夹雪":
                resource = R.mipmap.yujiaxue;
                break;
            case "中雪":
                resource = R.mipmap.zhongxue;
                break;
            case "中雨":
                resource = R.mipmap.zhongyu;
                break;
            case "雾":
                resource = R.mipmap.wu;
        }
        return resource;
    }

    private OnNewDayListener onNewDayListener;

    public void setOnNewDayListener(OnNewDayListener onNewDayListener) {
        this.onNewDayListener = onNewDayListener;
    }

    public void initFirstStart() {
        firstStart = false;
    }

    private void sendMessage(BoardBean s) {
        if (null != s) {
            hasBody(s.getData().getManCheck());
            if (s.isChange(boardBeanOld) || runChangeTag || !firstStart) {
                boardBeanRunnable.setBoardBean(s);
                boardBeanOld = s;
                handler.post(boardBeanRunnable);
            }
        }
    }

    private BoardBeanRunnable boardBeanRunnable = new BoardBeanRunnable();
    private BoardBean boardBeanOld;
    private String body;
    private final String tag = PjjApplication.OLD_3188 ? "1" : "12";

    @Override
    public void hasBody(String body) {
        if (null != body && (!body.equals(this.body) || isHasPeopleTag)) {
            this.body = body;
            //Log.e("TAG", "hasBody: " + this.body);
            handler.post(() -> {
                if (isHasPeopleTag) {
                    isHasPeopleTag = false;
                }
                //Log.e("TAG", "hasBody: " + body + ", " + tag);
                if (tag.equals(this.body)) { // 判断是否有人 1
                    //iv_people_statue.setColorFilter(unColorElevator);
                    iv_people_statue.setImageResource(R.mipmap.meiren);
                    //iv_people_statue.setAlpha(51);
                } else /*("11".equals(this.body)) */ {//有人0
                    //iv_people_statue.setAlpha(225);
                    //iv_people_statue.setColorFilter(colorElevator);
                    iv_people_statue.setImageResource(R.mipmap.youren);
                }
            });
        }
    }

    public void updateOnlineStatue(boolean tag) {
        if (null != headView) {
            handler.post(() -> {
                String color;
                if (tag) {
                    color = "#009B4C";
                } else {
                    color = "#FF0115";
                }
                TextView tv_online = headView.findViewById(R.id.tv_online);
                tv_online.setCompoundDrawables(getCircleDrawable(color), null, null, null);
            });
        }
    }


    public boolean getScreenTag() {
        return true;
        //return null != tv_elevator_text && tv_elevator_text.getVisibility() == View.GONE;
    }

    public void setElevatorText(BoardBean bean) {
        sendMessage(bean);
        /*if (null != headView && tv_elevator_text.getVisibility() == View.VISIBLE) {
            String explain = explain(bean);
            //Log.e("TAG", "setElevatorText: " + explain);
            headView.post(() -> tv_elevator_text.setText(explain));
        }*/
    }

    class BoardBeanRunnable implements Runnable {
        private BoardBean boardBean;

        public void setBoardBean(BoardBean boardBean) {
            this.boardBean = boardBean;
        }

        @Override
        public void run() {
            if (runChangeTag) {
                runChangeTag = false;
            }
            updateElevatorStatue(boardBean);
        }
    }

    public interface OnNewDayListener {
        void newDay(String time);
    }

}
