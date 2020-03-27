package com.one.wxpay.autoconfigure;


import com.one.wxpay.autoconfigure.properties.WxpayProperties;
import com.one.wxpay.autoconfigure.service.WxpayService;
import org.apache.http.impl.client.CloseableHttpClient;
import org.jdom2.input.SAXBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({ CloseableHttpClient.class, SAXBuilder.class })
@EnableConfigurationProperties(WxpayProperties.class)
public class WxpayAutoConfiguration {
    //注入相关服务
    @Bean
    public WxpayService wxpayService() {
        return new WxpayService();
    }

}
