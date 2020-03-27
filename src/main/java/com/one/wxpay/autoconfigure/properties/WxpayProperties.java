package com.one.wxpay.autoconfigure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
//配置文件
@ConfigurationProperties("spring.wxpay")
public class WxpayProperties {
    private String appId;

    private String mchId;

    private String mchKey;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getMchKey() {
        return mchKey;
    }

    public void setMchKey(String mchKey) {
        this.mchKey = mchKey;
    }
}
