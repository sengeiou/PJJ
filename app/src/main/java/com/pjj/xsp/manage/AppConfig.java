package com.pjj.xsp.manage;

import android.os.Environment;

import com.pjj.xsp.PjjApplication;

public class AppConfig {


    public static int SERIAL_PORT_BAUDRATE_T = 115200;
    public static int SERIAL_PORT_BAUDRATE = 9600;
    public static String SERIAL_PORT_DEVICE_S1 = "dev/ttyS1";
    public static String SERIAL_PORT_DEVICE_S3 = "dev/ttyS3";
    public static String SERIAL_PORT_DEVICE_S4 = "dev/ttyS4";
    public static int SERIAL_PORT_FLAGS = 0;

    //串行通讯
    public static boolean SERIALPORT_IS_ACTIVE = false;
    public static boolean BOARD_IS_ACTIVE = false;

    //    public static String SERIAL_PORT_DEVICE = "dev/ttyS3";
    public static String SERIAL_PORT_DEVICE = PjjApplication.OLD_3188 ? "dev/ttyS3" : "dev/ttyS4";

    public static final int MESSAGE_VIDEO_ABSOLUTE_PATH = 01;//视频绝对地址
    public final static String REMOTE_SERVICE_SERVER = "https://api.qiniudemo.com/upload";
    public final static String QUICK_START_VIDEO_DEMO_PATH = "/api/quick_start/simple_video_example_token.php";
    public static boolean NETWORK_STATE_IS_OK = false;//联网状态
    public static boolean isSuccessUpdateVideo = false;
    public static final String accessKey = "h-l4teG_1wgG7w9r1PNQzOnxzpiUbMG5LJ7VL-pI";
    public static final String secretKey = "39BG00oGNBHmbd__aDUzaqdMh3TyThc2yYywUuuq";
    public static final String bucket = "hlc-storage";
    public static final String CONFIG_FILE_VIDEO_RECORD = "hlc/VideoRecord.txt";

    /**
     * com.lzkj.aidlservice.receiver.RequestSyncPrmReceiver广播拦截的action
     */
    public static final String REQUEST_SYNC_PRM_RECEIVER_ACTION = "com.hlc.ad.receiver.REQUEST_SYNC_PRM_ACTION";

    public static boolean SERIALPORT_THREAD_FLAG = true;

    public static boolean recording = false;

    public static boolean isUpLoadVideo = false;
    public static boolean isRoomList = false;

    public static int APP_SLEEP_START_HOUR = 0;//静音开始时
    public static int APP_SLEEP_END_HOUR = 0;//静音结束时
    public static int APP_SLEEP_MINUTE = 0;//多久睡眠

    public static String FID = "";
    public static String CID = "";//ClientStatus.CLIENT_DEVICE_ID;

    //运行模式
    public static String RUN_MODEL = "UI"; // UI BG
    public static String BODY_MODEL = "OS"; // OS OB
    public static String DEVICE_ID = "";

    //VOIP 模式
    public static String VOIP_MODEL = "Y";
    // VOIP循环次数
    public static int VOIP_CALL_NUM = 1;
    public static int PACIFY_PLAY_NUM = 1;
    public static boolean PACIFY_IS_DOWNLOAD = false;

    //声音 按系统音量百分比
    public static float V_ADVERTISE_DEF_VOLUME = 1f;//广告页默认声音大小
    public static final float V_ADVERTISE_MIN_VOLUME = 0f;//广告页最小声音大小
    public static final float V_RESCUR_VOLUME = 1f;//救援页声音大小
    public static int SCREEN_BRIGHTNESS = 255;//屏幕亮度

    public static boolean BIND_DEVICE = false;
    public static String UI_PKG = "com.hlc.ad";
    public static String currentCityName = "";
    public static boolean callphone = false;
    public static String updateurl;
    public static String UNZIP = "com.hlc.ad.action.UNZIP"; //
    public static String HELP = "com.hlc.action.HELP"; //
    public static boolean isAlarm = false;

    public static String makeUrl(String remoteServer, String reqPath) {
        StringBuilder sb = new StringBuilder();
        sb.append(remoteServer);
        sb.append(reqPath);
        return sb.toString();
    }

    //运行模式
    public static final String CONFIG_FILE = "Hlc/hlc.prop";
    public static final String CONFIG_FILE_RECORD = "Hlc/devcie_video.txt";

    //系统SD卡目录
    public static final String BASE_PATH = "" + Environment.getExternalStorageDirectory();
    //广告资源下载目录
    public static final String AD_RESOURCE_DOWNLOAD_PATH = "Hlc/Download";
    //app 更新目录
    public static final String AD_RESOURCE_UPDATE_PATH = "Hlc/Update";
    //音频文件目录
    public static final String AUDIO_CACHE_PATH = "Hlc/Audio";
    //截图目录
    public static final String SCREENSHOT_PATH = "Hlc/screenshot";

    //广告界面更新HANDLER标识
    public static final int MESSAGE_WHAT_SERVER = 1;  //服务器状态
    public static final int MESSAGE_WHAT_BOARD = 2;  //板卡状态
    public static final int HANDLER_WHAT_VIDEO = 3;  //UDP的应急中心连接状态
    public static final int HANDLER_WHAT_CAMERA = 4;  //UDP的应急中心连接状态
    public static final int MESSAGE_WHAT_BD_DOWNLOAD = 5;  //主板更新
    public static final int MESSAGE_WHAT_AD_DOWNLOAD = 6;//广告下载
    public static final int MESSAGE_WHAT_RECORD = 7;  //电梯使用标识更新
    public static final int MESSAGE_WHAT_ADVERTISE = 8;  //广告内容更新
    public static final int MESSAGE_WHAT_VERSION = 9;  //版本文字更新
    public static final int MESSAGE_WHAT_WEATHER = 10;  //天气更新
    public static final int MESSAGE_WHAT_RESTART = 11;//重启APP
    public static final int MESSAGE_WHAT_RELOAD_SERIAL_PORT = 12;//重新加载  del
    public static final int MESSAGE_WHAT_UPDATE_ELEVATOR = 13;//开始更新电梯运行信息
    public static final int MESSAGE_WHAT_OPEN_CHINANIT = 14;//打开第 三方APP
    public static final int MESSAGE_WHAT_TIME = 15;//更新时间显示
    public static final int MESSAGE_WHAT_ACTIVITY_FRONT = 16;//界面置顶显示
    public static final int MESSAGE_WHAT_NEXT_ADVERTISE = 17;//运行下一个广告
    public static final int MESSAGE_WHAT_BODY = 18;  //人体感应状态
    public static final int MESSAGE_WHAT_START_SEND_VIDEO = 19;  //发送视频流 del
    public static final int MESSAGE_WHAT_STOP_SEND_VIDEO = 20;  //发送视频流 del
    public static final int MESSAGE_WHAT_CLOSE_ACTIVTY = 21;  //发送视频流 del
    public static final int MESSAGE_WHAT_QRCODE = 22;  //二维码更新
    public static final int MESSAGE_WHAT_INIT_ADVERTISE_ACTIVITY = 23;  //初始化广告页
    public static final int MESSAGE_WHAT_START_PMD = 25;  //启动跑马灯
    public static final int MESSAGE_WHAT_NETWORK_STATUS = 26;  //网络状态
    public static final int MESSAGE_WHAT_VIDEO_INTERCOM = 27;  //视频通话

    public static final String MAIN_VOIP_THREAD_NAME = "VoipThread";//VOIP线程名字






    public static String APP_NNUMBER_OF_ONLINE = "0x0000";//app通知在线人数
    public static String APP_UNREAD_MESSAGES = "0x0001";//app通知未读消息数
    public static String APP_SCREENSHOTS = "0x0002";//电梯app截屏
    public static String APP_RESTART_DEVICE = "0x0003";//电梯app重启设备
    public static String APP_RESTART_APP = "0x0004";//电梯app重启软件
    public static String APP_RELEASE_THEME = "0x0005";//电梯app发布主题
    public static String APP_DISABLE = "0x0006";//电梯app禁用
    public static String APP_START_USING = "0x0007";//电梯app启用
    public static String APP_TURN_OFF = "0x0008";//电梯app关机
    public static String APP_GET_BASIC_INFORMATION = "0x0009";//电梯app获取基本信息
    public static String APP_GET_LOG = "0x0010";//电梯app获取日志
    public static String APP_ONLINE = "0x0011";//上线通知
    public static String APP_OFFLINE = "0x0012";//离线通知
    public static String APP_UNBUNDLING_DEVICE = "0x0013";//解绑终端
    public static String APP_SET_VOLUME = "0x0014";//设置音量
    public static String APP_SET_FLOW_THRESHOLD = "0x0015";//设置流量阀值
    public static String APP_SET_WORKING_HOURS = "0x0016";//设置工作时间
    public static String APP_SYSTEM_MESSAGE = "0x0017";//系统消息
    public static String APP_ONLINE_UPGRADE = "0x0018";//在线升级
    public static String APP_RREAL_TIME_NEWS = "0x0019";//实时消息
    public static String APP_VIDEO_REQUEST = "0x0020";//视频请求（在终端未创建房间时，通过该命令创建）

    /**
     * 上传日志的action
     **/
    public static final String UPLOAD_LOG_ACTION = "com.hlc.mobile.downloadservice.receiver.UPLOAD_LOG_ACTION";
    /**
     * logPath
     **/
    public static final String LOG_PATH = "logPath";
    /***
     * 错误日志开头分割线
     */
    public static final String ERROR_SPLIT = "#&#";
    /***
     * 分割线
     */
    public static final String SPLIT = "|";

    /**
     * 图片区域
     */
    public static final int PIC_FRAGMENT = 1002;
    /**
     * 视频区域
     */
    public static final int VIDEO_FRAGMENT = 1001;
    /**微博区域*/
//	public static final int WEIBO_FRAGMENT = 3;
    /**时间区域*/
//    public static final int DATE_FRAGMENT = 3;
    /**天气区域*/
//    public static final int WEATHER_FRAGMENT = 4;
    /**跑马灯区域*/
//    public static final int TEXT_FRAGMENT = 5;


}

