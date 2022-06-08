package org.oss.focussnip.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import lombok.extern.slf4j.Slf4j;
import org.oss.focussnip.model.Orders;
import org.oss.focussnip.service.AlipayService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class AlipayServiceImpl implements AlipayService {

    /**支付宝请求地址*/
    private static String aliUrl = "https://openapi.alipaydev.com/gateway.do";
    /**支付宝应用ID*/
    private static String aliAppId = "2016101200670510";
    /**本地通过"支付宝开放平台开发助手"生成的私钥*/
    private static String aliAppPrivateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCMhUlQW23PewoTdGSa0DdY079jfjfg9AMF8afgGfXWMRTb6Fj5jPal0HrTj3pXXKaZJQK/X3wubmO1pY91tNuTA3O2uAoi58kPTEAwpFsYlcrwOOn30n0i6hqe5ZvOlIh7YP4zbGp6Q5KmWhn1BQ4NeVM0c4Yt3rCS4C041Yo+25Fi1VbswJC4xTMBu3l3A2cBF304joN31dIKHxppCx6P4KxkfpxRK6PyKARTR/rXFf6EUn4FT9y5MC3oXvOez2EPC3JdEqiaH8T6kDNgFhpEkpRmgrw3U6YahPTPlT42foL83D72fJkVtOV3uG6AriVPPnTKhXvSL50jvZxEghwNAgMBAAECggEAcom2D4zmvgbRWLg3xMTRSFMYnpVkILVkvXSTXKRscNGC6hjCZW7UIKYFZ+p8UzdPCGdVjP3cr9AE8Cvp+oyXqYZXzGjIwljh1tlVPRDKvAhk6VfKxdjJbSsa1cfcLw99C+wsZ2gIhm4L3BJ3M/OR18O74XYEueDZVm7qDXhpff+t+oP0x+Un/vXI+ddCk6pF6Gn6jjJrsranUaC1N/2jIki0WDcx0Dd7pw1RP3gknWYQBAxeiDl/pryil83xW1fX3W4CcNYS8IL980ZHAKssKUNVhdT3VtozxqC0iPWbxAh5+VkPe0Vh6jW98CkJNxZ7XmB/QxM5XGJc3NE/Yzyn9QKBgQDSEt+cBeyyoDUcBZ/sj/wMDKUbPjemjf2PrY0FdYMDW/NlNf5yI9AH+UHCU/LkcrPzMNxEDBSDlFXP80hvFkDgevX/5tW4tumT2/VMhRhrkpiMTar5NyCmZXGdr7aDa0L4MOZcWsf34eTMF8buPS8pI+ssuK/pU/0/rKMi1xgvjwKBgQCrPcJP4q68wV4yitXryR2rmLBrUnDsil7uro6lgMG3/jN96jejTjF7E0k3qBih1gAcjwEcASOUOOrAFRuI03HrgzhjbxcaWDOipxfzZuWGdPuQ8frNXg3J1USXDPV8Hfh0ZzPBL2P/kc+NVaeHsO4BkOiMkW6JIDnObbSG1XzsowKBgQCM19CJx8MZQ6Y34mGFdkeTQIsVx9IB3eLpiH8q8M1k3CpEaj3tAqvAHNKE2knX9jtFyjVzffHnAfXClSa0K+RVXLs/1eDmlePmmStOIVHtFX0nXA9MRTSjuxHAmgUw+SIOSqK3urmlLRN77lkWc3kCCS3sbuqp8gCxxyoc+r8hawKBgEj1DR6gA7aj5BBAmpfUnfGIIlH3U+lTotKqSFg3HVHY2fNhoMbQ/7/iHaW61pPM60s/mkdmLNRv04R2+b436CdhRtsGb59nyMgZzNiFFHFO0geVMTfiRJmjZY+9k7GHy108ww4htMckRnt+7NuhBCZiqNGK/L8rnbVXhDyWJJ1lAoGADelO8B9oRHKMlUSa1ZYm0ILlsreUxzgHI16KL8GG5LA+h02/GDwVwQiPICcWdLoe3RvUkt+nYXF8/z5EnlzvnWguiTUMEqApNKM6rXfSxqIbcjnThuKtmMs31Ki4bExbQtTlunSYDsVkN8F0frwqHT5QlAsjYuM+KtRQkNoRfls=";
    /**支付宝应用设置本地公钥后生成对应的支付宝公钥（非本地生成的公钥）*/
    private static String alipayPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAut4USwftX2FMjiYNAeciizu+9m3n/oaHDupc8qu3zy7Jv9K7+GA2lPn5Nv6eGeUPkCvsLs07SWF7M0OzwQEAzgESCRJm0myfisQ/X2Nbh42Ov8QYBxxTmeC5t0vmuiyxabE0lVA5uo6ZiPgV5Wd3utHPHEEzSUYOedNmYoyb4HzylGM93KVDZ9qUAPx0fXPN8bZTyiiFTHQ5cRV5wSufKJcmz/ViS8Nw5LqkkBQJVZVqXFwIokmIOXTCIBEUgWpLkt6vi9jJZYTpRWk/RjpAA3rx1uB1a6A9yE6uq62aelzrO7/JhRr8d+bG2fmVw4EQ+KREAnfOx1w1/9fDODVlKwIDAQAB";
    /**支付宝回调的接口地址*/
    private static String aliNotifyUrl = "http://in2.css518.cn:16077/alipay/alinotify";

    @Resource
    private OrderServiceImpl orderService;


    @Override
    public AlipayTradePrecreateResponse newAliOrder(Long orderId) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(aliUrl,aliAppId,aliAppPrivateKey,"json","utf-8",alipayPublicKey,"RSA2");
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        Orders orders = orderService.findOrderById(orderId);
        request.setNotifyUrl("focussnip");
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", orders.getOrderId());
        bizContent.put("total_amount", orders.getPrice());
        bizContent.put("subject", "focussnip");

        request.setBizContent(bizContent.toString());
        AlipayTradePrecreateResponse response = alipayClient.execute(request);
        if(response.isSuccess()){
            System.out.println("调用阿里沙箱支付成功,QRCode:" + response.getQrCode());
        } else {
            System.out.println("调用失败");
        }
        return response;
    }

    @Override
    public AlipayTradeQueryResponse queryOrder(String orderId) throws Exception {
        AlipayClient alipayClient = new DefaultAlipayClient(aliUrl, aliAppId, aliAppPrivateKey, "json", "utf-8", alipayPublicKey, "RSA2");
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setOutTradeNo(orderId);
        request.setBizModel(model);
        AlipayTradeQueryResponse response = alipayClient.execute(request);
        return response;
    }

}
