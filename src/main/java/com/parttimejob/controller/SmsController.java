package com.parttimejob.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.parttimejob.entity.*;
import com.parttimejob.service.ManagerService;
import com.parttimejob.service.WorkerDataService;
import com.parttimejob.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: part-timeJob
 * @BelongsPackage: com.parttimejob.controller
 * @Author: Hinstein
 * @CreateTime: 2019-05-10 15:57
 * @Description:
 */
@Controller
public class SmsController {

    @Autowired
    WorkerService workerService;

    @Autowired
    WorkerDataService workerDataService;

    @Autowired
    ManagerService managerService;

    /**
     * Created on 17/6/7.
     * 短信API产品的DEMO程序,工程中包含了一个SmsDemo类，直接通过
     * 执行main函数即可体验短信产品API功能(只需要将AK替换成开通了云通信-短信产品功能的AK即可)
     * 工程依赖了2个jar包(存放在工程的libs目录下)
     * 1:aliyun-java-sdk-core.jar
     * 2:aliyun-java-sdk-dysmsapi.jar
     * <p>
     * 备注:Demo工程编码采用UTF-8
     * 国际短信发送请勿参照此DEMO
     */

    //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    static final String accessKeyId = "LTAITvuuY5kpApQG";
    static final String accessKeySecret = "0KorIhhZlWzKnCuBXG9VgrCAg0xiYJ";


    public static SendSmsResponse sendSms(String telephone, String code) throws ClientException {


        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(telephone);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName("校园兼职招聘管理系统");
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode("SMS_162521419");
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.setTemplateParam("{ \"code\":\"" + code + "\"}");

        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId("yourOutId");

        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            System.out.println("短信发送成功！");
        } else {
            System.out.println("短信发送失败！");
        }
        return sendSmsResponse;
    }


    public static QuerySendDetailsResponse querySendDetails(String bizId) throws ClientException {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        //必填-号码
        request.setPhoneNumber("18290022850");
        //可选-流水号
        request.setBizId(bizId);
        //必填-发送日期 支持30天内记录查询，格式yyyyMMdd
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
        request.setSendDate(ft.format(new Date()));
        //必填-页大小
        request.setPageSize(10L);
        //必填-当前页码从1开始计数
        request.setCurrentPage(1L);

        //hint 此处可能会抛出异常，注意catch
        QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);

        return querySendDetailsResponse;
    }

    //以下为测试代码，随机生成验证码
    private static int newcode;

    public static int getNewcode() {
        return newcode;
    }

    public static void setNewcode() {
        newcode = (int) (Math.random() * 9999) + 100;  //每次调用生成一次四位数的随机数
    }

    @ResponseBody
    @PostMapping("/worker/sms/put")
    public Map<String, String> workerPut(HttpServletRequest request, HttpSession session) throws ClientException, InterruptedException {

        HashMap<String, String> map = new HashMap<>();
        String rightCode = (String) request.getSession().getAttribute("rightCode");
        String tryCode = request.getParameter("tryCode");
        System.out.println(rightCode + "" + tryCode);
        if (tryCode.equals(rightCode)) {
            String username = request.getParameter("username");
            String phoneNumber = request.getParameter("phoneNumber");
            Worker worker = workerService.findByUserName(username);
            if (worker != null) {
                WorkerData workerData = workerDataService.findByWorkerId(worker.getId());
                if (workerData.getPhoneNumber().equals(phoneNumber)) {
                    //发短信
                    setNewcode();
                    String code = Integer.toString(getNewcode());
                    SendSmsResponse response = sendSms(phoneNumber, code);
                    System.out.println("短信接口返回的数据----------------");
                    System.out.println("验证码为：" + code);
                    System.out.println("Code=" + response.getCode());
                    System.out.println("Message=" + response.getMessage());
                    System.out.println("RequestId=" + response.getRequestId());
                    System.out.println("BizId=" + response.getBizId());
                    session.setAttribute("changeId", workerData.getWorkerId());
                    session.setAttribute("code", code);
                    map.put("success", "验证码发送成功！");
                    Thread.sleep(3000L);
                    return map;
                } else {
                    map.put("error", "用户名与手机号不匹配");
                    return map;
                }
            } else {
                map.put("error", "用户名不存在");
                return map;
            }
        } else {
            map.put("error", "验证码错误");
            return map;
        }
    }

    @ResponseBody
    @PostMapping("/manager/sms/put")
    public Map<String, String> managerPut(HttpServletRequest request,HttpSession session) throws ClientException, InterruptedException {

        HashMap<String, String> map = new HashMap<>();
        String rightCode = (String) request.getSession().getAttribute("rightCode");
        String tryCode = request.getParameter("tryCode");

        if (tryCode.equals(rightCode)) {
            String username = request.getParameter("username");
            String phoneNumber = request.getParameter("phoneNumber");
            Manager manager = managerService.findByUserName(username);
            if (manager != null) {
                if (manager.getPhoneNumber().equals(phoneNumber)) {
                    //发短信
                    setNewcode();
                    String code = Integer.toString(getNewcode());
                    SendSmsResponse response = sendSms(phoneNumber, code);
                    System.out.println("短信接口返回的数据----------------");
                    System.out.println("验证码为：" + code);
                    System.out.println("Code=" + response.getCode());
                    System.out.println("Message=" + response.getMessage());
                    System.out.println("RequestId=" + response.getRequestId());
                    System.out.println("BizId=" + response.getBizId());
                    session.setAttribute("changeId", manager.getId());
                    session.setAttribute("code", code);
                    map.put("success", "验证码发送成功！");
                    Thread.sleep(3000L);
                    return map;
                } else {
                    map.put("error", "用户名与手机号不匹配");
                    return map;
                }
            } else {
                map.put("error", "用户名不存在");
                return map;
            }
        } else {
            map.put("error", "验证码错误");
            return map;
        }
    }
}

