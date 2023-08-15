package com.nanshan.icc;

import com.dahuatech.hutool.http.Method;
import com.dahuatech.hutool.json.JSONUtil;
import com.dahuatech.icc.brm.model.v202010.device.BrmDeviceChannelPageResponse;
import com.dahuatech.icc.exception.ClientException;
import com.dahuatech.icc.oauth.http.DefaultClient;
import com.dahuatech.icc.oauth.http.IClient;
import com.dahuatech.icc.oauth.model.v202010.GeneralRequest;
import com.dahuatech.icc.oauth.model.v202010.GeneralResponse;
import com.nanshan.icc.alarm.CJAlarmPageResponse;
import com.nanshan.icc.alarm.CJAlarmService;
import com.nanshan.icc.device.CJDeviceChannelService;
import com.nanshan.icc.org.CJOrgService;
import com.nanshan.icc.service.CJICCService;
import com.nanshan.icc.viedo.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@SpringBootTest
@Slf4j
class IccApplicationTests {

    @Autowired
    private CJICCService cjiccService;
    @Autowired
    private CJDeviceChannelService cjDeviceChannelService;

    @Autowired
    CJAlarmService cjAlarmService;

    @Autowired
    CJOrgService cjOrgService;
    /*
    * 重新获取设备及通道信息
    * */
    @Test
    public void cjTest1() throws ClientException {
        cjiccService.getAllDeviceChannel();
    }

    /*
    * 获取最近7天的自定义告警信息
    * */
    @Test
    public void cjTest2() throws ClientException {
        // 获取当前时间
        LocalDateTime currentDateTime = LocalDateTime.now();
        String now = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("当前的精确时间是：" + now);
        // 获取7天前的精确时间
        LocalDateTime sevenDaysAgo = currentDateTime.minusDays(7);
        String dayBefore = sevenDaysAgo.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        // 输出7天前的精确时间
        System.out.println("7天前的精确时间是：" + dayBefore);

        cjAlarmService.getAlarms(dayBefore,now);
    }


    /*
     * 获取所有组织信息
     * */
    @Test
    public void cjTest3() throws ClientException {
        cjOrgService.getOrgs();
    }


    private final String host = "172.15.3.100";
    private final String username = "OpenAPI";
    private final String password = "abCD1234";
    private final String clientId = "icc_client_01";
    private final String clientSecret = "5473f0c6-07bb-4cec-84ad-0ac8a9a76d03";



    private VideoService videoService;

    {
        try {
            videoService = new VideoService(host, username, password, clientId, clientSecret);
        } catch (ClientException e) {
            log.error("初始化客户端失败", e);
            videoService = null;
        }
    }

    @Test
    void contextLoads() {
    }

    @GetMapping("/test")
    public String test() {
        log.info("ttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt");
        return "hello world!";
    }

    @Test
    public void page() throws ClientException {
        IClient iClient = new DefaultClient();
        GeneralRequest generalRequest =
                new GeneralRequest("/evo-apigw/evo-event/1.2.0/alarm-record/page", Method.POST);
        Map<String, String> map = new HashMap<>();
        map.put("pageNum", "1");
        map.put("pageSize", "10");
        map.put("alarmType", "13");
        // 设置参数
        generalRequest.body(JSONUtil.toJsonStr(map));
        GeneralResponse response = iClient.doAction(generalRequest, generalRequest.getResponseClass());
        String result = response.getResult();
        System.out.println(JSONUtil.toJsonStr(response));
    }

    /*
     * 获取设备对应的channel
     * */
    @Test
    public void device() throws ClientException {
        log.info("----步骤一----{}------", "获取在线设备通道列表");
        try {
            // 单元类型：1 编码单元
            // 通道类型：编码单元下的通道类型 1 视频通道
            List<BrmDeviceChannelPageResponse.DeviceChannelPage> channelList = videoService.getDeviceChannelPage(1, 100, Arrays.asList("1"), Arrays.asList("1"), Arrays.asList("1004361"), 1);
            if (CollectionUtils.isEmpty(channelList)) {
                return;
            }
            for (BrmDeviceChannelPageResponse.DeviceChannelPage deviceChannelPage : channelList) {
                System.out.println(deviceChannelPage.getChannelCode() + "-->" + deviceChannelPage.getChannelName());
            }

        } catch (ClientException e) {
            log.error("客户端异常", e);
            throw e;
        } catch (Exception e) {
            log.error("请求异常", e);
            throw e;
        }
    }

    /*
     * 获取告警
     * */
    @Test
    public void alarm() throws ClientException {
        IClient iClient = new DefaultClient();
        GeneralRequest generalRequest =
                new GeneralRequest("/evo-apigw/evo-event/1.2.0/alarm-record/page", Method.POST);
        Map<String, Object> map = new HashMap<>();
        map.put("pageNum", "1");
        map.put("pageSize", "10");
        map.put("alarmType", "13");
        List<String> deviceCodeList = new ArrayList<>();
        deviceCodeList.add("1004361$1$0$0");
        map.put("nodeCodeList", deviceCodeList);
        // 设置参数
        generalRequest.body(JSONUtil.toJsonStr(map));
        GeneralResponse response = iClient.doAction(generalRequest, generalRequest.getResponseClass());
        String result = response.getResult();
        System.out.println(JSONUtil.toJsonStr(response));
    }
    @Test
    public void alarm01() throws ClientException {
        IClient iClient = new DefaultClient();
        GeneralRequest generalRequest =
                new GeneralRequest("/evo-apigw/evo-event/1.2.0/alarm-record/page", Method.POST);
        Map<String, Object> map = new HashMap<>();
        map.put("pageNum", "1");
        map.put("pageSize", "10");
        map.put("alarmType", "13");
        List<String> deviceCodeList = new ArrayList<>();
        deviceCodeList.add("1004361$1$0$0");
        map.put("nodeCodeList", deviceCodeList);
        // 设置参数
        generalRequest.body(JSONUtil.toJsonStr(map));
        CJAlarmPageResponse response = iClient.doAction(generalRequest, CJAlarmPageResponse.class);
        System.out.println(response);
        System.out.println(response.getData());
        for (CJAlarmPageResponse.CJAlarmlPage cjAlarmlPage : response.getData().getPageData()) {
            System.out.println(cjAlarmlPage);
        }
    }


    /*
     * 获取告警统计
     * */
    @Test
    public void alarm_statistics() throws ClientException {
        IClient iClient = new DefaultClient();
        GeneralRequest generalRequest =
                new GeneralRequest("/evo-apigw/evo-event/1.0.0/alarm-record/count-num", Method.POST);
        Map<String, Object> map = new HashMap<>();

        map.put("alarmStartDateString", "2023-07-09 00:00:00");
        map.put("alarmEndDateString", "2023-07-09 23:59:59");
        //只获取设置的报警事件
        map.put("isEvent", "0");
//        List<String> deviceCodeList = new ArrayList<>();
//        deviceCodeList.add("1004361$1$0$0");
//        map.put("nodeCodeList", deviceCodeList);
        // 设置参数
        generalRequest.body(JSONUtil.toJsonStr(map));
        GeneralResponse response = iClient.doAction(generalRequest, generalRequest.getResponseClass());
        String result = response.getResult();

        System.out.println(JSONUtil.toJsonStr(response));
    }
}
