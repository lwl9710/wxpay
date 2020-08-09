package com.one.wxpay.autoconfigure.service;

import com.one.wxpay.autoconfigure.properties.WxpayProperties;
import com.one.wxpay.autoconfigure.utils.ResultUtil;
import com.one.wxpay.autoconfigure.utils.WxpayUtil;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.HashMap;
import java.util.Map;

public class WxpayService {
    @Autowired
    private WxpayProperties wxpayProperties;

    //发起微信支付
    public Map<String, String> sendPay(Map<String, String> data) {
        Map<String, String> map = new HashMap<>();
        //小程序ID 自动获取
        map.put("appid",wxpayProperties.getAppId());
        //商户号 自动获取
        map.put("mch_id",wxpayProperties.getMchId());
        //商户key 自动获取
        map.put("key",wxpayProperties.getMchKey());
        //添加随机字符串
        map.put("nonce_str", WxpayUtil.getRandomString(32));
        //添加签名类型 默认MD5
        map.put("sign_type","MD5");
        //添加用户定义的参数
        map.putAll(data);
        //添加签名
        map.put("sign",WxpayUtil.generateSign(map));
        //删除不必要参数key
        map.remove("key");
        Map<String, String> payData = ResultUtil.doXMLParse(WxpayUtil.doPost(WxpayUtil.generateXMLString(map)));
        payData.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
        payData = WxpayUtil.createPaySign(payData, wxpayProperties.getMchKey());
        return payData;
    }

}
