package com.pjj.xsp.module.parameter;

/**
 * Create by xinheng on 2018/10/29。
 * describe：
 */
public class VoiceNotify {
    /**
     * voiceNotify : {"caller":"","playTimes":"1","statusUrl":"","requestId":"","appId":"123c2e7f628442f2b918eeaa5502cabc","callee":"13798445566","billUrl":"http://106.29.38.228:80/voiceBack","type":"0","templateId":"","content":"测试内容"}
     */

    private VoiceNotifyBean voiceNotify;

    public VoiceNotifyBean getVoiceNotify() {
        return voiceNotify;
    }

    public void setVoiceNotify(VoiceNotifyBean voiceNotify) {
        this.voiceNotify = voiceNotify;
    }

    public static class VoiceNotifyBean {
        /**
         * caller :
         * playTimes : 1
         * statusUrl :
         * requestId :
         * appId : 123c2e7f628442f2b918eeaa5502cabc
         * callee : 13798445566
         * billUrl : http://106.29.38.228:80/voiceBack
         * type : 0
         * templateId :
         * content : 测试内容
         */

        private String caller;
        private String playTimes;
        private String statusUrl;
        private String requestId;
        private String appId;
        private String callee;
        private String billUrl;
        private String type;
        private String templateId;
        private String content;

        public String getCaller() {
            return caller;
        }

        public void setCaller(String caller) {
            this.caller = caller;
        }

        public String getPlayTimes() {
            return playTimes;
        }

        public void setPlayTimes(String playTimes) {
            this.playTimes = playTimes;
        }

        public String getStatusUrl() {
            return statusUrl;
        }

        public void setStatusUrl(String statusUrl) {
            this.statusUrl = statusUrl;
        }

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getCallee() {
            return callee;
        }

        public void setCallee(String callee) {
            this.callee = callee;
        }

        public String getBillUrl() {
            return billUrl;
        }

        public void setBillUrl(String billUrl) {
            this.billUrl = billUrl;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTemplateId() {
            return templateId;
        }

        public void setTemplateId(String templateId) {
            this.templateId = templateId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
