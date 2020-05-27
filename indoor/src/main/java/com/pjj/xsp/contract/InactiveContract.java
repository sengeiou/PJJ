package com.pjj.xsp.contract;

import com.pjj.xsp.module.parameter.ActivateStatue;
import com.pjj.xsp.module.parameter.AppUpload;
import com.pjj.xsp.module.parameter.ScreenInfTag;

/**
 * Create by xinheng on 2018/10/16。
 * describe：激活页面协议
 */
public interface InactiveContract {
    interface View extends BaseView {
        /**
         * 更新显示内容
         *
         * @param text
         */
        void updateText(String text);

        void setActivateStatueResultSuccess();

        void setActivateStatueResultFail(String error);

        /**
         * 激活状态码
         *
         * @param activeCode
         */
        void handlerActiveCode(String activeCode);

        void sendAppUpdateSuccess();

        void sendSystemUpdateSuccess();

        void registerFail(String msg);
    }

    interface Present {
        /**
         * 发送唯一标识 注册信息
         *
         * @param screenId 标识
         */
        void sendOnlyTagForRegistered(ScreenInfTag screenId);

        /**
         * 获取广告屏信息
         * 包含激活、未激活、设备号
         *
         * @param screenId
         */
        void loadScreenInf(ScreenInfTag screenId);

        /**
         * 设置广告屏激活状态
         * 激活、未激活
         *
         * @param statue
         */
        void setActivateScreenStatue(ActivateStatue statue);

        void uploadUpdateAppSuccessInf(AppUpload appUpload);

        void uploadUpdateSystemSuccessInf(AppUpload appUpload);
    }
}
