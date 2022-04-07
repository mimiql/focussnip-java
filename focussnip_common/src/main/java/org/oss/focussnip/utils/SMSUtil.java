package org.oss.focussnip.utils;


import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

public class SMSUtil {
    private static String accessKeyId = "LTAI4FckBwTXnPMvfPygrB5F";
    private static String regionld = "cn-hangzhou";
    private static String secret = "yb9NwGZCue7JktSOZoeQWKkeENhegF";

    public static boolean SendSMS(String phoneNumber , String code){
        // 1.创建接口API调用对象
        DefaultProfile profile = DefaultProfile.getProfile(regionld, accessKeyId, secret);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        // 2.发送post请求
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com"); //云服务(不能修改)
        request.setSysVersion("2017-05-25"); //版本号(不能修改)
        request.setSysAction("SendSms"); // 发送方式(不能修改)
        request.putQueryParameter("RegionId", "cn-hangzhou");
        // 发送的手机号（多个手机号可以用逗号隔开）
        request.putQueryParameter("PhoneNumbers", phoneNumber);
        request.putQueryParameter("SignName", "爱巢网站");
        // 3.请求模板(短信模板)
        request.putQueryParameter("TemplateCode", "SMS_174020820");
        // 4.请求模板参数（需要时json数组格式，验证码可以使用随机参数）
        request.putQueryParameter("TemplateParam","{\"code\":" + code + "}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            return true;
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;

    }

    public static void main(String[] args) {
        SendSMS("13078206516" , "1234");
    }
}
