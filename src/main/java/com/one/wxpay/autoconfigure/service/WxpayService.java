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
    //需要传递的参数 body out_trade_no total_fee spbill_create_ip notify_url trade_type
    public Map<String, String> sendPay(Map<String, String> data) {
        Map<String, String> map = new HashMap<>();

        map.put("appid",wxpayProperties.getAppId());

        map.put("mch_id",wxpayProperties.getMchId());

        map.put("key",wxpayProperties.getMchKey());

        map.put("nonce_str", WxpayUtil.getRandomString(32));

        map.put("sign_type","MD5");

        map.putAll(data);

        //顺序很重要
        map.put("sign",WxpayUtil.generateSign(map));

        map.remove("key");

        return ResultUtil.doXMLParse(WxpayUtil.doPost(WxpayUtil.generateXMLString(map)));
    }
}
