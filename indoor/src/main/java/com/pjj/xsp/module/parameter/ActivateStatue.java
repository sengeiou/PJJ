package com.pjj.xsp.module.parameter;

import com.pjj.xsp.intent.http.RetrofitService;
import com.pjj.xsp.manage.XSPSystem;
import com.pjj.xsp.module.bean.ResultBean;

/**
 * Create by xinheng on 2018/10/18。
 * describe：激活
 */
public class ActivateStatue {

    /**
     * screenId : 2121212121212#a
     * isActivate : 1
     */

    private String screenId;//设备码
    private String isActivate;//是否激活 0不激活 1激活

    public ActivateStatue() {
    }

    public ActivateStatue(String screenId, String isActivate) {
        this.screenId = screenId;
        this.isActivate = isActivate;
    }

    public String getScreenId() {
        return screenId;
    }

    public void setScreenId(String screenId) {
        this.screenId = screenId;
    }

    public String getIsActivate() {
        return isActivate;
    }

    public void setIsActivate(String isActivate) {
        this.isActivate = isActivate;
    }

    public static ActivateStatue createActivateBean() {
        return new ActivateStatue(XSPSystem.getInstance().getOnlyCode(), "1");
    }

    public static ActivateStatue createUnActivateBean() {
        return new ActivateStatue(XSPSystem.getInstance().getOnlyCode(), "0");
    }

    public static void setActivateScreenStatue(ActivateStatue activateStatue ,OnActivateScreenStatueListener statueListner) {
        RetrofitService.getInstance().activateScreen(activateStatue, new RetrofitService.CallbackClassResult<ResultBean>(ResultBean.class) {

            @Override
            protected void resultSuccess(ResultBean resultBean) {
                statueListner.success();
            }

            @Override
            protected void fail(String error) {
                statueListner.fail(error);
            }
        });
    }

    public interface OnActivateScreenStatueListener {
        void success();

        void fail(String error);
    }
}
