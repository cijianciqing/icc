package com.nanshan.icc.alarm;

import com.dahuatech.hutool.http.Method;
import com.dahuatech.hutool.json.JSONUtil;
import com.dahuatech.icc.exception.ClientException;
import com.dahuatech.icc.oauth.http.DefaultClient;
import com.dahuatech.icc.oauth.http.IClient;
import com.dahuatech.icc.oauth.model.v202010.GeneralRequest;
import com.dahuatech.icc.oauth.model.v202010.GeneralResponse;
import com.nanshan.icc.generated.entity.AlarmEntity;
import com.nanshan.icc.generated.service.AlarmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CJAlarmService {

    private Integer initPageNum = 1;
    private Integer pageSize = 100;

    @Autowired
    private  AlarmService alarmService;

    public void getAlarms(String alarmStartDateString, String alarmEndDateString) throws ClientException {
        //1.获取总数
        IClient iClient = new DefaultClient();
        GeneralRequest generalRequest =
                new GeneralRequest("/evo-apigw/evo-event/1.0.0/alarm-record/count-num", Method.POST);
        Map<String, Object> map = new HashMap<>();

        map.put("alarmStartDateString", alarmStartDateString);
        map.put("alarmEndDateString", alarmEndDateString);
        //只获取设置的报警事件
        map.put("isEvent", "0");
        // 设置参数
        generalRequest.body(JSONUtil.toJsonStr(map));
        CJAlarmPageResponse cjAlarmPageResponse = iClient.doAction(generalRequest, CJAlarmPageResponse.class);
        Integer totalAlarmNum = cjAlarmPageResponse.getData().getValue();
        Integer totalAlarmPage = new BigDecimal(totalAlarmNum).divide(new BigDecimal(pageSize),BigDecimal.ROUND_UP).intValue();

        //2.获取相应的报警
        IClient iClientData = new DefaultClient();
        List<AlarmEntity> alarmEntities = new ArrayList<>();
        for (int i = 1; i <= totalAlarmPage; i++) {
            GeneralRequest generalRequestData =
                    new GeneralRequest("/evo-apigw/evo-event/1.2.0/alarm-record/page", Method.POST);
            Map<String, Object> mapData = new HashMap<>();
            mapData.put("pageNum", i);
            mapData.put("pageSize", pageSize);
            mapData.put("alarmStartDateString", alarmStartDateString);
            mapData.put("alarmEndDateString", alarmEndDateString);
//            map.put("alarmType", "13");//通道断线
            mapData.put("isEvent", "0");//报警预案标识,配置报警预案后是0，空则查询报警与事件
            // 设置参数
            generalRequestData.body(JSONUtil.toJsonStr(mapData));
            CJAlarmPageResponse response = iClientData.doAction(generalRequestData, CJAlarmPageResponse.class);
            response.getData().getPageData().forEach(a -> {
                AlarmEntity alarmEntity = new AlarmEntity();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                alarmEntity.setAlarmDate( LocalDateTime.parse(a.getAlarmDate(), formatter));
                alarmEntity.setNodeCode(a.getNodeCode());
                alarmEntity.setAlarmPosition(a.getAlarmPosition());
                alarmEntities.add(alarmEntity);
            });
            alarmService.remove(null);
            alarmService.saveBatch(alarmEntities);
        }


    }


    /*
     * 获取告警统计
     * */
    public void alarm_statistics(GeneralRequest generalRequest) throws ClientException {
        IClient iClient = new DefaultClient();
//        GeneralRequest generalRequest =
//                new GeneralRequest("/evo-apigw/evo-event/1.0.0/alarm-record/count-num", Method.POST);
//        Map<String, Object> map = new HashMap<>();
//
//        map.put("alarmStartDateString", "2023-07-09 00:00:00");
//        map.put("alarmEndDateString", "2023-07-09 23:59:59");
//        //只获取设置的报警事件
//        map.put("isEvent", "0");
////        List<String> deviceCodeList = new ArrayList<>();
////        deviceCodeList.add("1004361$1$0$0");
////        map.put("nodeCodeList", deviceCodeList);
//        // 设置参数
//        generalRequest.body(JSONUtil.toJsonStr(map));
        GeneralResponse response = iClient.doAction(generalRequest, generalRequest.getResponseClass());
        String result = response.getResult();

        System.out.println(JSONUtil.toJsonStr(response));
    }
}
