package com.pjj.xsp;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.pjj.xsp.intent.http.RetrofitService;
import com.pjj.xsp.module.bean.ScreenPingTaskBean;
import com.pjj.xsp.module.bean.ScreenTaskBean;
import com.pjj.xsp.module.parameter.ScreenInfTag;
import com.pjj.xsp.module.parameter.ScreenOrderTask;
import com.pjj.xsp.utils.RSASubsectionUtile;
import com.pjj.xsp.utils.TextJsonUtils;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private Map<String, String> map;

    @Test
    public void addition_isCorrect() throws InterruptedException {
        assertEquals(4, 2 + 2);
        //20190130143112154882987203821039
        String s1 = RSASubsectionUtile.public_decipher("OJfwtFW+mdt4dsgAOuxiegOZuzeM/jV3YTm7t0ELjJZVH6Wlma9OkinGY9mVCpcz6Xgndy9gezBJPE03ZRzUazWJMa2Hz9ivjKQ9RKuDVlHbS7VhhDwdoS1y1lYdAcruRMYQvZwSpfu+9mipbg70DP336OFYraKe2G6gH5lbF1f/ku3at1v03FBuREQ59gGrMsxeUFaAOhAqMOuKjkPmYBL5ewsKVpiqEXz7bPbQ5N+vY1S1Dc8j8TZlUtNGNs/xls//xgv2CquOJVu8zOUoQpmw+Mwm1MgIITFknqIsYbaYsFUPm4bn+nHzWPM7IgmihSpIMRGjQT/WsUZXU/cGdA==");
        System.out.println(s1);


        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("1", "adfa");
        stringStringHashMap.put("2", "adfa2");
        stringStringHashMap.put("3", "adfa3");
        stringStringHashMap.put("4", "adfa4");
        setMap(stringStringHashMap);
        dealWith(map);
        System.out.println(map);
        String s = "{\"Ret\":1,\"Data\":{\"Floor\":\"7\",\"DoorState\":\"0D\",\"UpDown\":\"FF\",\"FaultReportA\":\"06\",\"FaultReportB\":\"FF\"}}";
        System.out.println(s.length());
        String json = "{\"msg\": \"查询成功\",\"flag\": \"1\",\"data\": [{\"pieceType\": \"1\",\"pieceColour\": \"粉色\",\"orderType\": \"4\",\"orderId\": \"20190117140211154770493173654436\",\"peopleInfo\": \"现有9成新海尔冰箱1800元处理啦！！！联系人：赵先生   电话：13340012518\",\"companyName\": \"测试公司\",\"isShowName\": \"0\",\"phoneNumber\": \"18601931625\",\"isShowPhone\": \"1\",\"pieceNum\": 10,\"pieceTitle\": \"寻人启事\",\"name\": \"李宝华\",\"authType\": \"1\"}, {\"pieceType\": \"1\",\"pieceColour\": \"粉色\",\"orderType\": \"4\",\"orderId\": \"20190117140212154770493289198726\",\"peopleInfo\": \"现有9成新海尔冰箱1800元处理啦！！！联系人：赵先生   电话：13340012518\",\"companyName\": \"测试公司\",\"isShowName\": \"0\",\"phoneNumber\": \"18601931625\",\"isShowPhone\": \"1\",\"pieceNum\": 10,\"pieceTitle\": \"寻人启事\",\"name\": \"李宝华\",\"authType\": \"1\"}, {\"pieceType\": \"1\",\"pieceColour\": \"粉色\",\"orderType\": \"4\",\"orderId\": \"20190117140213154770493387715672\",\"peopleInfo\": \"现有9成新海尔冰箱1800元处理啦！！！联系人：赵先生   电话：13340012518\",\"companyName\": \"测试公司\",\"isShowName\": \"0\",\"phoneNumber\": \"18601931625\",\"isShowPhone\": \"1\",\"pieceNum\": 10,\"pieceTitle\": \"寻人启事\",\"name\": \"李宝华\",\"authType\": \"1\"}, {\"pieceType\": \"1\",\"pieceColour\": \"粉色\",\"orderType\": \"4\",\"orderId\": \"20190117140214154770493472106897\",\"peopleInfo\": \"现有9成新海尔冰箱1800元处理啦！！！联系人：赵先生   电话：13340012518\",\"companyName\": \"测试公司\",\"isShowName\": \"0\",\"phoneNumber\": \"18601931625\",\"isShowPhone\": \"1\",\"pieceNum\": 10,\"pieceTitle\": \"寻人启事\",\"name\": \"李宝华\",\"authType\": \"1\"}, {\"pieceType\": \"1\",\"pieceColour\": \"粉色\",\"orderType\": \"4\",\"orderId\": \"20190117140215154770493559652942\",\"peopleInfo\": \"现有9成新海尔冰箱1800元处理啦！！！联系人：赵先生   电话：13340012518\",\"companyName\": \"测试公司\",\"isShowName\": \"0\",\"phoneNumber\": \"18601931625\",\"isShowPhone\": \"1\",\"pieceNum\": 10,\"pieceTitle\": \"寻人启事\",\"name\": \"李宝华\",\"authType\": \"1\"}, {\"pieceType\": \"1\",\"pieceColour\": \"粉色\",\"orderType\": \"4\",\"orderId\": \"20190117140216154770493642253284\",\"peopleInfo\": \"现有9成新海尔冰箱1800元处理啦！！！联系人：赵先生   电话：13340012518\",\"companyName\": \"测试公司\",\"isShowName\": \"0\",\"phoneNumber\": \"18601931625\",\"isShowPhone\": \"1\",\"pieceNum\": 10,\"pieceTitle\": \"寻人启事\",\"name\": \"李宝华\",\"authType\": \"1\"}, {\"pieceType\": \"1\",\"pieceColour\": \"粉色\",\"orderType\": \"4\",\"orderId\": \"20190117140217154770493728047275\",\"peopleInfo\": \"现有9成新海尔冰箱1800元处理啦！！！联系人：赵先生   电话：13340012518\",\"companyName\": \"测试公司\",\"isShowName\": \"0\",\"phoneNumber\": \"18601931625\",\"isShowPhone\": \"1\",\"pieceNum\": 10,\"pieceTitle\": \"寻人启事\",\"name\": \"李宝华\",\"authType\": \"1\"}, {\"pieceType\": \"1\",\"pieceColour\": \"粉色\",\"orderType\": \"4\",\"orderId\": \"20190117140218154770493819192445\",\"peopleInfo\": \"现有9成新海尔冰箱1800元处理啦！！！联系人：赵先生   电话：13340012518\",\"companyName\": \"测试公司\",\"isShowName\": \"0\",\"phoneNumber\": \"18601931625\",\"isShowPhone\": \"1\",\"pieceNum\": 10,\"pieceTitle\": \"寻人启事\",\"name\": \"李宝华\",\"authType\": \"1\"}, {\"pieceType\": \"1\",\"pieceColour\": \"粉色\",\"orderType\": \"4\",\"orderId\": \"20190117140219154770493931269782\",\"peopleInfo\": \"现有9成新海尔冰箱1800元处理啦！！！联系人：赵先生   电话：13340012518\",\"companyName\": \"测试公司\",\"isShowName\": \"0\",\"phoneNumber\": \"18601931625\",\"isShowPhone\": \"1\",\"pieceNum\": 10,\"pieceTitle\": \"寻人启事\",\"name\": \"李宝华\",\"authType\": \"1\"}, {\"pieceType\": \"1\",\"pieceColour\": \"粉色\",\"orderType\": \"4\",\"orderId\": \"20190117140220154770494036714120\",\"peopleInfo\": \"现有9成新海尔冰箱1800元处理啦！！！联系人：赵先生   电话：13340012518\",\"companyName\": \"测试公司\",\"isShowName\": \"0\",\"phoneNumber\": \"18601931625\",\"isShowPhone\": \"1\",\"pieceNum\": 10,\"pieceTitle\": \"寻人启事\",\"name\": \"李宝华\",\"authType\": \"1\"}],\"hour\": \"15\",\"curDate\": \"2019-01-17\"}";
        try {
            ScreenPingTaskBean screenPingTaskBean = new Gson().fromJson(json, ScreenPingTaskBean.class);
            List<ScreenPingTaskBean.DataBeanPin> data = screenPingTaskBean.getData();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        ScreenInfTag screenInfTag = new ScreenInfTag();
        screenInfTag.setCurDate("2019-01-30");
        screenInfTag.setHour("16");
        screenInfTag.setScreenId("8c_f7_10_60_39_86");


        ScreenOrderTask screenOrderTask =new ScreenOrderTask();
        screenOrderTask.setOrderId("20190130143112154882987203821039");
        screenOrderTask.setScreenId("8c_f7_10_60_39_86");
        RetrofitService.getInstance().getScreenTaskInf(screenInfTag, new RetrofitService.MyCallback() {
            @Override
            protected void success(String s) {
                System.out.println(s);
            }

            @Override
            protected void fail(String error) {
                System.out.println(error);
            }
        });
        Thread.sleep(2000);
    }

    private void setMap(Map<String, String> map) {
        this.map = map;
    }

    private void dealWith(Map<String, String> map) {
        map.remove("3");
        map.remove("4");
        map.put("3", "我先滑腻");
    }
    @Test
    public void test() throws InterruptedException {
        String orderId="20190218090537155045193754124907";
        ScreenOrderTask screenOrderTask = new ScreenOrderTask();
        screenOrderTask.setOrderId(orderId);
        screenOrderTask.setScreenId("8c_f7_10_5f_fb_34");
        RetrofitService.getInstance().loadSearchTaskForOrderId(screenOrderTask, new RetrofitService.MyCallback() {
            @Override
            protected void success(String s) {
                System.out.println(s);
            }

            @Override
            protected void fail(String error) {

            }
        });
        Thread.sleep(2000);
    }
}