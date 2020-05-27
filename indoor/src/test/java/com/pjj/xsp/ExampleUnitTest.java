package com.pjj.xsp;

import android.content.Intent;

import com.google.gson.Gson;
import com.pjj.xsp.intent.http.RetrofitService;
import com.pjj.xsp.manage.XSPSystem;
import com.pjj.xsp.module.bean.ScreenInfBean;
import com.pjj.xsp.module.parameter.ActivateStatue;
import com.pjj.xsp.module.parameter.AppUpload;
import com.pjj.xsp.module.parameter.ScreenInfTag;
import com.pjj.xsp.module.parameter.ScreenOrderTask;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws InterruptedException {
        assertEquals(4, 2 + 2);
        List<Integer> list = new ArrayList<>();
        list.add(2);
        list.add(1);
        list.add(5);
        list.add(7);
        list.add(4);
        Collections.sort(list, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if (o1 > o2) {
                    return 1;
                } else if (o1 < o2) {
                    return -1;
                } else
                    return 0;
            }
        });
        System.out.println();
        for (int s : list) {
            System.out.print(s);
        }
        /*ScreenInfTag screenOrderTask = new ScreenInfTag();
        screenOrderTask.setScreenId("20_32_33_40_4e_a6");
        //screenOrderTask.setOrderId("20190410170017155488681761396754");
        ActivateStatue statue = new ActivateStatue("20_32_33_40_4e_a6", "1");
        ActivateStatue.setActivateScreenStatue(statue, new ActivateStatue.OnActivateScreenStatueListener() {
            @Override
            public void success() {
                System.out.println("成功");
            }

            @Override
            public void fail(String error) {
                System.out.println("失败");
            }
        });
       *//* RetrofitService.getInstance().getScreenInf(screenOrderTask, new RetrofitService.CallbackClassResult<ScreenInfBean>(ScreenInfBean.class) {
            @Override
            protected void resultSuccess(ScreenInfBean screenInfBean) {
                String s = new Gson().toJson(screenInfBean);
                System.out.println(s);
            }

            @Override
            protected void fail(String error) {
                System.out.println(error);
            }
        });*//*
        Thread.sleep(3000);*/
    }

    @Test
    public void readText() throws IOException {
        File file = new File("D:\\studioSpace\\company\\PJJ\\indoor\\src\\test\\java\\com\\pjj\\xsp\\aaa.txt");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String str;
        int num = -1;
        String out = "";
        while ((str = bufferedReader.readLine()) != null) {
            //System.out.println(str);
            String str1 = "nextIndex=";
            int index = str.indexOf(str1);
            if (index > -1) {
                int beginIndex = index + str1.length();
                String substring = str.substring(beginIndex, beginIndex + 1);
                int intent = Integer.parseInt(substring);
                if (num == -1) {
                    num = intent;
                    out = intent + ",";
                } else {
                    if (Math.abs(num - intent) == 1) {
                        out += (intent + ",");
                        num = intent;
                    }else{
                        System.out.println(out);
                        num = intent;
                        out = intent + ",";
                    }
                }
            }
        }
    }
}