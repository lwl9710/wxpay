package com.one.wxpay.autoconfigure.utils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

public class WxpayUtil {
    public static final String URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    //调用统一下单接口
    public static String doPost(String params) {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(URL);

        CloseableHttpResponse response = null;

        String result = null;

        URI uri = null;

        try {
            uri = new URIBuilder().setScheme("https").setHost("api.mch.weixin.qq.com").setPort(443).setPath("/pay/unifiedorder").build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        httpPost.setHeader("Content-Type","text/xml;charset=UTF-8");
        if(!StringUtils.isEmpty(params)) {
            StringEntity stringEntity = new StringEntity(params,StandardCharsets.UTF_8);
            httpPost.setEntity(stringEntity);
        }

        try {
            response = httpclient.execute(httpPost);
            result = EntityUtils.toString(response.getEntity(),StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    httpclient.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return result;
    }

    //构建随机字符串
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = null;
        StringBuilder sb = new StringBuilder();

        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if(random != null) {
            for(int i = 0; i < length; i++) {
                sb.append(str.charAt(random.nextInt(62)));
            }
        }

        return sb.toString();
    }

    // 生成MD5加密字符串
    public static String toMD5String(String message) {
        MessageDigest m = null;
        StringBuilder sb = new StringBuilder();
        try {
            m = MessageDigest.getInstance("MD5");
            m.update(message.getBytes(StandardCharsets.UTF_8));
            byte[] s = m.digest();
            for (byte i : s) {
                sb.append(Integer.toHexString((0x000000FF & i) | 0xFFFFFF00).substring(6));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return sb.toString().toUpperCase();
    }

    //构建xml
    public static String generateXMLString(Map<String, String> data) {
        StringBuilder sb = new StringBuilder();
        Set<Map.Entry<String, String>> entries = data.entrySet();
        sb.append("<xml>");

        for (Map.Entry<String, String> entry : entries) {
            String label = entry.getKey();
            String value = entry.getValue();

            if(StringUtils.isEmpty(value))continue;
            sb
            .append("<")
            .append(label);

            if(label.equals("detail")) {
                sb
                .append("><![CDATA[")
                .append(value)
                .append("]]</")
                .append(label)
                .append(">");
            }else {
                sb
                .append(">")
                .append(value)
                .append("</");
            }
            sb
            .append(label)
            .append(">");
        }

        sb.append("</xml>");
        return sb.toString();
    }

    //构建签名
    public static String generateSign(Map<String, String> data) {
        StringBuilder sb = new StringBuilder();

        List<String> keys = new ArrayList<>(data.keySet());

        keys.sort(String::compareTo);

        for (String key : keys) {
            if(key.equals("key"))continue;
            sb.append(key).append("=").append(data.get(key)).append("&");
        }

        sb.append("key=").append(data.get("key"));

        return toMD5String(sb.toString());
    }
}
