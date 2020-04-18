<h1 style = "text-align: center">微信支付小工具使用教程</h1>

1. 下载相关的(预)发行release版本(com.one.wxpay.zip)

2. 将zip解压到本地maven仓库中

3. 添加maven坐标如下

   ```xml
   <dependency>
       <groupId>com.one.wxpay</groupId>
       <artifactId>wxpay</artifactId>
       <version>{下载的版本号}</version>
   </dependency>
   ```

4. application.yml配置必要参数

   ```yml
   //示例子
   spring:
     wxpay:
       //微信提供的appId
       app-id: ******
       //微信商户平台ID
       mch-id: ******
       //微信商户平台Key
       mch-key: ******
   ```

5. 注入服务

   ```java
   @Autowired
   private WxpayService wxpayService;
   ```
   
6. 调用方法并传入相关参数发起支付

   ```java
   /* 
   * sendPay方法参数介绍
   * Map必填参数介绍(其他参数参照官方文档):
   * body：商品描述
   * out_trade_no：商户订单号
   * total_fee：价格(单位:分)
   * spbill_create_ip：发起支付的IP地址
   * notify_url trade_type：微信支付回调地址
   * trade_type：交易类型(由于一个后台系统可能提供多端app支付使用,故此参数并没加入配置文件中)
   * openid：当trade_type为JSAPI时为必传
   */
   ```
   ```java
   // 应用实例
   @RestController
   public class test() {
      @Autowired
      WxpayService wxpayService;
   
      @GetMapping("/pay") 
      public Map<String,String> pay{
         Map<String,String> params = new HashMap<>();


        params.put("body","测试-商品");

        params.put("out_trade_no", UUID.randomUUID().toString().replace("-","").toUpperCase());

        params.put("total_fee","100");

        params.put("spbill_create_ip",request.getRemoteAddr());

        params.put("notify_url","你的回调地址 请输入全域名");

        params.put("trade_type","NATIVE");

        return wxpayService.sendPay(params);
      }
   }
   ```
   
   <span style = "color: red">注：</span>[微信支付详细参数链接](https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=9_1)
