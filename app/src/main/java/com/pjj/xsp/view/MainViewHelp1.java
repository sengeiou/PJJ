package com.pjj.xsp.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pjj.xsp.R;
import com.pjj.xsp.PjjApplication;
import com.pjj.xsp.manage.BodyRunnable;
import com.pjj.xsp.manage.XSPSystem;
import com.pjj.xsp.module.ScreenInfManage;
import com.pjj.xsp.module.bean.BoardBean;
import com.pjj.xsp.module.bean.ScreenInfBean;
import com.pjj.xsp.module.bean.WeatherBean;
import com.pjj.xsp.module.template.AdvertisingBean;
import com.pjj.xsp.module.template.AdvertisingFactory;
import com.pjj.xsp.utils.Log;
import com.pjj.xsp.utils.TextViewUtils;
import com.pjj.xsp.view.custom.LineTextView;

import java.util.Calendar;
import java.util.List;

import static com.pjj.xsp.manage.ReceiveBuffOld3188.explain;


/**
 * Create by xinheng on 2018/10/30。
 * describe：主页面view管理
 */
public class MainViewHelp1 implements /*SocketConnectListener,*/ BodyRunnable.OnHasPeopleListener {
    private View head;
    private View footView;
    private Handler handler;
    private int weekOld = -1;
    private Calendar calendar;
    private ImageView iv_people_statue;
    private LinearLayout ll_car;
    private int colorElevator;
    private int unColorElevator = Color.parseColor("#FFFFFF");
    private boolean runChangeTag;
    private boolean isHasPeopleTag;
    private static MainViewHelp1 INSTANCE;
    private TextView tv_elevator_text;

    public static MainViewHelp1 getInstance() {
        if (null == INSTANCE) {
            synchronized (MainViewHelp1.class) {
                if (null == INSTANCE) {
                    INSTANCE = new MainViewHelp1();
                }
            }
        }
        return INSTANCE;
    }

    private MainViewHelp1() {
    }

    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setAdvertisingBean(AdvertisingBean advertisingBean) {
        if (advertisingBean == null) {
            advertisingBean = AdvertisingFactory.createAdvertising("");
        }
        runChangeTag = true;
        isHasPeopleTag = true;
        colorElevator = AdvertisingBean.getColor(advertisingBean.getBottomImaColor());
        //unColorElevator=AdvertisingBean.getColor(advertisingBean.getBottomImaBgColor());
        if (null != head) {
            advertisingBean.setImageDrawable(head);
            String topTitleColor = advertisingBean.getTopTitleColor();
            advertisingBean.setTextColor(head.findViewById(R.id.tv_name_no_explain), topTitleColor);
            advertisingBean.setTextColor(head.findViewById(R.id.tv_time_explain), topTitleColor);
            advertisingBean.setTextColor(head.findViewById(R.id.tv_help_phone_explain), topTitleColor);
            int topTitleTextColor = AdvertisingBean.getColor(advertisingBean.getTopTitleTextColor());
            int topTitleTextLineColor = AdvertisingBean.getColor(advertisingBean.getTopTitleTextLineColor());
            LineTextView tv_name_no = head.findViewById(R.id.tv_name_no);
            LineTextView tv_time = head.findViewById(R.id.tv_time);
            LineTextView tv_help_phone = head.findViewById(R.id.tv_help_phone);
            tv_name_no.setTextColor(topTitleTextColor);
            tv_time.setTextColor(topTitleTextColor);
            tv_help_phone.setTextColor(topTitleTextColor);

            tv_name_no.setLineColor(topTitleTextLineColor);
            tv_time.setLineColor(topTitleTextLineColor);
            tv_help_phone.setLineColor(topTitleTextLineColor);
            advertisingBean.setTextColor(head.findViewById(R.id.tv_content), topTitleColor);
        }
        if (null != footView) {
            int elevatorBgColor = AdvertisingBean.getColor(advertisingBean.getBottomImaBgColor());
            iv_people_statue.setBackground(new ColorDrawable(elevatorBgColor));
            ImageView iv_elevator_door_statue = footView.findViewById(R.id.iv_elevator_door_statue);
            ImageView iv_elevator_run_statue = footView.findViewById(R.id.iv_elevator_run_statue);
            TextView iv_elevator_floor = footView.findViewById(R.id.iv_elevator_floor);
            iv_elevator_door_statue.setBackground(new ColorDrawable(elevatorBgColor));
            iv_elevator_run_statue.setBackground(new ColorDrawable(elevatorBgColor));
            iv_elevator_floor.setBackground(new ColorDrawable(elevatorBgColor));
            iv_elevator_floor.setTextColor(AdvertisingBean.getColor(advertisingBean.getBottomImaColor()));
            advertisingBean.setTextColor(footView.findViewById(R.id.tv_elevator), advertisingBean.getBottomElevatorTextColor());
            advertisingBean.setBackground(footView.findViewById(R.id.fl_elevator), advertisingBean.getBottomElevatorBgColor());
            //时间
            advertisingBean.setTextColor(tv_now_time, advertisingBean.getBottomTimeTextColor());
            TextView tv_now_date = footView.findViewById(R.id.tv_now_date);
            TextView tv_now_week = footView.findViewById(R.id.tv_now_week);
            advertisingBean.setTextColor(tv_now_date, advertisingBean.getBottomDateTextColor());
            advertisingBean.setTextColor(tv_now_week, advertisingBean.getBottomWeekTextColor());
            //限号
            if (null != ll_car) {
                advertisingBean.setBackground(footView.findViewById(R.id.rl_middle), advertisingBean.getBottomMidBgColor());
                TextView tv_num1 = footView.findViewById(R.id.tv_num1);
                TextView tv_num2 = footView.findViewById(R.id.tv_num2);
                TextView tv_car = footView.findViewById(R.id.tv_car);
                TextView tv_car1 = footView.findViewById(R.id.tv_car1);
                int colorCar = AdvertisingBean.getColor(advertisingBean.getBottomCarTextColor());
                int colorNum = AdvertisingBean.getColor(advertisingBean.getBottomCarNumColor());
                tv_car.setTextColor(colorCar);
                tv_car1.setTextColor(colorCar);
                tv_num1.setTextColor(colorNum);
                tv_num2.setTextColor(colorNum);
            }
            //在线
            advertisingBean.setBackground(footView.findViewById(R.id.rl_online_city), advertisingBean.getBottomOnlineCityBgColor());
            TextView tv_online = footView.findViewById(R.id.tv_online);
            advertisingBean.setTextColor(tv_online, advertisingBean.getBottomOnlineTextColor());
            //城市
            advertisingBean.setTextColor(footView.findViewById(R.id.tv_local_city), advertisingBean.getBottomCityColor());
            //天气
            advertisingBean.setBackground(footView.findViewById(R.id.ll_tem), advertisingBean.getBottomDayTemperatureBgColor());
            int color_name = AdvertisingBean.getColor(advertisingBean.getBottomDayColor());
            int color_temperature = AdvertisingBean.getColor(advertisingBean.getBottomTemperatureColor());
            int color_line = AdvertisingBean.getColor(advertisingBean.getBottomVLineColor());
            TextView tv_today_name = footView.findViewById(R.id.tv_today_name);
            TextView tv_tomorrow_name = footView.findViewById(R.id.tv_tomorrow_name);
            TextView tv_after_tomorrow_name = footView.findViewById(R.id.tv_after_tomorrow_name);
            tv_today_name.setTextColor(color_name);
            tv_tomorrow_name.setTextColor(color_name);
            tv_after_tomorrow_name.setTextColor(color_name);

            TextView tv_today = footView.findViewById(R.id.tv_today);
            TextView tv_tomorrow = footView.findViewById(R.id.tv_tomorrow);
            TextView tv_after_tomorrow = footView.findViewById(R.id.tv_after_tomorrow);
            tv_today.setTextColor(color_temperature);
            tv_tomorrow.setTextColor(color_temperature);
            tv_after_tomorrow.setTextColor(color_temperature);

            View line1 = footView.findViewById(R.id.line1);
            View line2 = footView.findViewById(R.id.line2);
            line1.setBackgroundColor(color_line);
            line2.setBackgroundColor(color_line);
        }
    }

    private Drawable getCircleDrawable(String color) {
        //12px #FF0115 #04FD19
        int px12 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 12, footView.getResources().getDisplayMetrics());
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(AdvertisingBean.getColor(color));
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setBounds(0, 0, px12, px12);
        return drawable;
    }

    Handler.Callback callback = msg -> {
        updateNowTime();
        return false;
    };

    private TextView tv_now_time;

    /**
     * 初始化头部view
     *
     * @param context
     * @param fl_head 头部父类ViewGroup
     * @return
     */
    public TextView iniHeadView(Context context, ViewGroup fl_head) {
        handler = new Handler(Looper.getMainLooper(), callback);
        //headParents = fl_head;
        //head_inf = LayoutInflater.from(context).inflate(R.layout.layout_head, fl_head, false);
        //head_xsp_inf = LayoutInflater.from(context).inflate(R.layout.layout_head_xsp_inf, fl_head, false);
        head = LayoutInflater.from(context).inflate(R.layout.layout_head_weibao_ad, fl_head, false);
        TextView tv_content = head.findViewById(R.id.tv_content);//便民信息
        fl_head.addView(head);
        return tv_content;
    }

    public void updateWeiBao() {
        ScreenInfBean.DataBean screenId = ScreenInfManage.getInstance().getScreenInfDataBean();
        if (null != screenId) {
            TextView tv_name_no = head.findViewById(R.id.tv_name_no);//设备号
            tv_name_no.setText(TextViewUtils.clean(screenId.getScreenCode()));
            TextView tv_time = head.findViewById(R.id.tv_time);
            TextView tv_help_phone = head.findViewById(R.id.tv_help_phone);
            tv_time.setText(screenId.getLastTime());
            tv_help_phone.setText(screenId.getHelpPhone());
        }
    }

    public void initFootView(Context context, ViewGroup parents) {
        footView = LayoutInflater.from(context).inflate(R.layout.layout_foot, parents, false);
        tv_now_time = footView.findViewById(R.id.tv_now_time);
        iv_people_statue = footView.findViewById(R.id.iv_people_statue);
        ll_car = footView.findViewById(R.id.ll_car);
        ll_car.setVisibility(View.GONE);

        tv_elevator_text = footView.findViewById(R.id.tv_elevator_text);

        //tv_car.setText(Html.fromHtml("今日限行的尾号是：<font color='#EDA915'>5</font> 和 <font color='#EDA915'>0</font>"));
        parents.addView(footView);
        updateOnlineStatue(XSPSystem.isNetworkConnected(PjjApplication.application));
        oldHour = -1;
        weekOld = -1;
        updateNowTime();
//        BodyRunnable bodyRunnable = new BodyRunnable();
//        bodyRunnable.setOnHasPeopleListener(this);
//        bodyRunnable.run();
    }

    public void updateCarNum(String[] arrays) {
        if (arrays != null && arrays.length == 2 && weekOld != 7 && weekOld != 1) {//周末
            ll_car.setVisibility(View.VISIBLE);
            TextView tv_num1 = footView.findViewById(R.id.tv_num1);
            TextView tv_num2 = footView.findViewById(R.id.tv_num2);
            //tv_car.setText(Html.fromHtml("今日限行的尾号是：<font color='#EDA915'>" + arrays[0] + "</font> 和 <font color='#EDA915'>" + arrays[1] + "</font>"));
            tv_num1.setText(arrays[0]);
            tv_num2.setText(arrays[1]);
        } else {
            ll_car.setVisibility(View.GONE);
        }
    }

    private int oldHour = -1;

    private void updateNowTime() {
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
            TextView tv_now_date = footView.findViewById(R.id.tv_now_date);
            TextView tv_now_week = footView.findViewById(R.id.tv_now_week);
            tv_now_date.setText(year + "年" + month + "月" + date + "日");
            tv_now_week.setText(getWeek(week));
        } else {
            if (oldHour != hour) {
                oldHour = hour;
                onNewDayListener.newDay(week + "");
            }
        }
        tv_now_time.setText(formString(hour) + " : " + formString(min));
        handler.sendEmptyMessageDelayed(1, 1000 * 20);
    }

    private void updateElevatorStatue(BoardBean boardBean) {
        ImageView iv_elevator_door_statue = footView.findViewById(R.id.iv_elevator_door_statue);
        ImageView iv_elevator_run_statue = footView.findViewById(R.id.iv_elevator_run_statue);
        TextView iv_elevator_floor = footView.findViewById(R.id.iv_elevator_floor);

        int ret = boardBean.getRet();
        BoardBean.DataBean data = boardBean.getData();
        String floor = data.getFloor();
        iv_elevator_floor.setText(floor + "F");

        String upDown = data.getUpDown();
        if (upDown.equals("FF")) {//0F：电梯上行 10：电梯下行 FF：停梯
            setFilter(iv_elevator_run_statue, unColorElevator, 51);
            iv_elevator_run_statue.setImageResource(R.mipmap.tingzhi);
        } else if (upDown.equals("0F")) {
            //statue="上行状态";
            setFilter(iv_elevator_run_statue, colorElevator, 255);
            iv_elevator_run_statue.setImageResource(R.mipmap.xiangshang);
        } else {//"10" 电梯处于下行状态
            setFilter(iv_elevator_run_statue, colorElevator, 255);
            iv_elevator_run_statue.setImageResource(R.mipmap.xiangxia);
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
        iv.setColorFilter(color);
        iv.setAlpha(apha);
        //iv.setImageResource(resource);
    }

    public void updateCityName(String cityName) {
        TextView tv_local_city = footView.findViewById(R.id.tv_local_city);
        tv_local_city.setText("当前城市：" + cityName.replace("市", ""));
    }

    public void updateWeather(List<WeatherBean.ResultsBean.WeatherDataBean> list) {
        //Log.e("TAG", "updateWeather: size=" + list.size());
        if (list.size() > 2) {
            TextView tv_today = footView.findViewById(R.id.tv_today);
            TextView tv_tomorrow = footView.findViewById(R.id.tv_tomorrow);
            TextView tv_after_tomorrow = footView.findViewById(R.id.tv_after_tomorrow);

            ImageView iv_today = footView.findViewById(R.id.iv_today);
            ImageView iv_tomorrow = footView.findViewById(R.id.iv_tomorrow);
            ImageView iv_after_tomorrow = footView.findViewById(R.id.iv_after_tomorrow);
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
            Glide.with(footView).load(path).apply(requestOptions).into(iv);
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
            case "阴天":
            case "阴转多云":
                resource = R.mipmap.yintian;
                break;
            case "雨夹雪":
                resource = R.mipmap.yujiaxue;
                break;
            case "中雪":
                resource = R.mipmap.zhongxue;
                break;
            case "中雨":
                resource = R.mipmap.zhongyu;
                break;
        }
        return resource;
    }

    private OnNewDayListener onNewDayListener;

    public void setOnNewDayListener(OnNewDayListener onNewDayListener) {
        this.onNewDayListener = onNewDayListener;
    }

    private void sendMessage(BoardBean s) {
        if (s.isChange(boardBeanOld) || runChangeTag) {
            boardBeanRunnable.setBoardBean(s);
            boardBeanOld = s;
            handler.post(boardBeanRunnable);
        }
    }

    private BoardBeanRunnable boardBeanRunnable = new BoardBeanRunnable();
    private BoardBean boardBeanOld;
    private String body;

    @Override
    public void hasBody(String body) {
        if (null != body && (!body.equals(this.body) || isHasPeopleTag)) {
            this.body = body;
            //Log.e("TAG", "hasBody: " + this.body);
            handler.post(() -> {
                if (isHasPeopleTag) {
                    isHasPeopleTag = false;
                }
                if ("1".equals(this.body)) { // 判断是否有人
                    iv_people_statue.setColorFilter(unColorElevator);
                    iv_people_statue.setImageResource(R.mipmap.meiren);
                    iv_people_statue.setAlpha(51);
                } else if ("0".equals(this.body)) {//有人
                    iv_people_statue.setAlpha(225);
                    iv_people_statue.setColorFilter(colorElevator);
                    iv_people_statue.setImageResource(R.mipmap.youren);
                }
            });
        }
    }

    public void updateOnlineStatue(boolean tag) {
        if (null != footView) {
            footView.post(() -> {
                String color;
                if (tag) {
                    color = "#04FD19";
                } else {
                    color = "#FF0115";
                }
                TextView tv_online = footView.findViewById(R.id.tv_online);
                tv_online.setCompoundDrawables(getCircleDrawable(color), null, null, null);
            });
        }
    }


    public boolean getScreenTag() {
        return null != tv_elevator_text && tv_elevator_text.getVisibility() == View.GONE;
    }

    /**
     * 测试模式
     */
    public void setTest() {
        if (null != footView) {
            boolean tag = getScreenTag();
            Log.e("TAG", "setTest: tag=" + tag);
            if (null != onNewDayListener) {
                onNewDayListener.updateTheme(tag);
            }
            if (tag) {
                tv_elevator_text.setVisibility(View.VISIBLE);
            } else {
                tv_elevator_text.setVisibility(View.GONE);
            }
        }
    }

    public void setElevatorText(BoardBean bean) {
        /*sendMessage(bean);
        if (null != footView && tv_elevator_text.getVisibility() == View.VISIBLE) {
            String explain = explain(bean);
            //Log.e("TAG", "setElevatorText: " + explain);
            footView.post(() -> tv_elevator_text.setText(explain));
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

        void updateTheme(boolean tag);
    }

}
