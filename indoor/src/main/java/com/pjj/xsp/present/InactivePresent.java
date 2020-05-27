package com.pjj.xsp.present;

import android.os.Handler;
import android.widget.Toast;

import com.pjj.xsp.PjjApplication;
import com.pjj.xsp.contract.InactiveContract;
import com.pjj.xsp.intent.http.RetrofitService;
import com.pjj.xsp.manage.XSPSystem;
import com.pjj.xsp.module.ScreenInfManage;
import com.pjj.xsp.module.bean.ResultBean;
import com.pjj.xsp.module.bean.ScreenInfBean;
import com.pjj.xsp.module.parameter.ActivateStatue;
import com.pjj.xsp.module.parameter.AppUpload;
import com.pjj.xsp.module.parameter.ScreenInfTag;
import com.pjj.xsp.utils.Log;
import com.pjj.xsp.utils.SharedUtils;
import com.pjj.xsp.view.activity.UnPlayActivity;

/**
 * Create by xinheng on 2018/10/16。
 * describe：
 */
public class InactivePresent extends BasePresent<InactiveContract.View> implements InactiveContract.Present {

    public InactivePresent(InactiveContract.View view) {
        super(view);
    }

    private Handler mainHandler = new Handler();

    @Override
    public void sendOnlyTagForRegistered(ScreenInfTag screenId) {
        RetrofitService.getInstance().sendScreenTag(screenId, new RetrofitService.CallbackClass<ResultBean>(ResultBean.class) {

            @Override
            protected void result(ResultBean registerResultBean) {
                String flag = registerResultBean.getFlag();
                if (!isViewNotNull()) {
                    return;
                }
                if ("1".equals(flag)) {//注册成功
                    mView.updateText(XSPSystem.getInstance().getOnlyCode() + "注册成功，请稍等。。。");
                    //注册成功 下一步激活
                    SharedUtils.saveForXml(SharedUtils.ACTIVE_CODE, UnPlayActivity.STATUE_REGISTER + "");
                    mView.handlerActiveCode(UnPlayActivity.STATUE_REGISTER + "");
                } else {
                    String msg = registerResultBean.getMsg();
                    if (null != msg && msg.contains("已经被注册")) {//{"msg":"该设备号已经被注册","flag":"0"}
                        SharedUtils.saveForXml(SharedUtils.ACTIVE_CODE, UnPlayActivity.STATUE_REGISTER + "");
                        mView.updateText(XSPSystem.getInstance().getOnlyCode() + "已经注册，请稍等。。。");
                        //查询是否激活
                        loadScreenInf(ScreenInfManage.getInstance().getScreenInfTag());
                    } else {
                        mView.registerFail(XSPSystem.getInstance().getOnlyCode() + "\n注册失败\n" + msg);
                    }
                }
            }

            @Override
            protected void fail(String error) {
                mView.registerFail(XSPSystem.getInstance().getOnlyCode() + "\n注册失败\n请检查网络，正在重试");
                mainHandler.postDelayed(() -> sendOnlyTagForRegistered(screenId), 2000);
            }
        });
    }

    @Override
    public void loadScreenInf(ScreenInfTag screenInfTag) {
        ScreenInfBean.loadScreenInf(screenInfTag, new ScreenInfBean.OnLoadScreenInfListener() {
            @Override
            public void success(ScreenInfBean.DataBean dataBean) {
                if (isViewNotNull()) {
                    if (dataBean.isActivate()) {
                        //ScreenInfManage.getInstance().setScreenInfDataBean(dataBean);
                        mView.setActivateStatueResultSuccess();
                    } else {//未激活,
                        mView.handlerActiveCode(UnPlayActivity.STATUE_REGISTER + "");
                    }
                }
            }

            @Override
            public void fail(String error) {
                Log.e("TAG", "fail: " + error);
                Toast.makeText(PjjApplication.application, "广告屏信息-" + error, Toast.LENGTH_SHORT).show();
                //mView.handlerActiveCode(UnPlayActivity.STATUE_REGISTER + "");
            }
        });
    }

    @Override
    public void setActivateScreenStatue(ActivateStatue statue) {
        ActivateStatue.setActivateScreenStatue(statue, new ActivateStatue.OnActivateScreenStatueListener() {
            @Override
            public void success() {
                if (isViewNotNull()) {
                    mView.setActivateStatueResultSuccess();
                }
            }

            @Override
            public void fail(String error) {
                if (isViewNotNull()) {
                    mView.setActivateStatueResultFail(error);
                }
            }
        });
    }

    @Override
    public void uploadUpdateAppSuccessInf(AppUpload appUpload) {
        RetrofitService.MyCallback myCallback = new RetrofitService.MyCallback() {
            @Override
            protected void success(String s) {
                mView.sendAppUpdateSuccess();
            }

            @Override
            protected void fail(String error) {
                mView.failStatue(error);
                mainHandler.postDelayed(() -> uploadUpdateAppSuccessInf(appUpload), 3000);
            }
        };
        getRetrofitService().updateScreenVersion(appUpload, myCallback);
    }

    @Override
    public void uploadUpdateSystemSuccessInf(AppUpload appUpload) {
        RetrofitService.MyCallback myCallback = new RetrofitService.MyCallback() {
            @Override
            protected void success(String s) {
                Log.e("TAG", "success111: " + s);
                mView.sendSystemUpdateSuccess();
            }

            @Override
            protected void fail(String error) {
                mView.failStatue(error);
                mainHandler.postDelayed(() -> uploadUpdateSystemSuccessInf(appUpload), 3000);
            }
        };
        getRetrofitService().updateScreenVersion(appUpload, myCallback);
    }
}
