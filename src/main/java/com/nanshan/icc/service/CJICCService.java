package com.nanshan.icc.service;

import com.dahuatech.icc.brm.model.v202010.device.*;
import com.dahuatech.icc.exception.ClientException;
import com.nanshan.icc.device.CJDeviceChannelService;
import com.nanshan.icc.device.CJDeviceService;
import com.nanshan.icc.generated.entity.ChannelEntity;
import com.nanshan.icc.generated.entity.DeviceEntity;
import com.nanshan.icc.generated.service.ChannelService;
import com.nanshan.icc.generated.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CJICCService {

    private Integer initPageNum = 1;
    private Integer pageSize = 100;

    @Autowired
    private CJDeviceService cjDeviceService;
    @Autowired
    private CJDeviceChannelService cjDeviceChannelService;

    @Autowired
    DeviceService deviceService;
    @Autowired
    ChannelService channelService;

    public void getAllDeviceChannel() throws ClientException {
        List<BrmDevice> allDevices = new ArrayList<>();
        List<BrmDeviceChannelPageResponse.DeviceChannelPage> allDeviceChannels = new ArrayList<>();

        BrmDevicePageRequest brmDevicePageRequest = new BrmDevicePageRequest();
        brmDevicePageRequest.setPageNum(initPageNum);
        brmDevicePageRequest.setPageSize(pageSize);
        //1.1 获取device总数
        BrmDevicePageResponse.DevicePageData data = cjDeviceService.getDevice(brmDevicePageRequest).getData();
        Integer totalDevicePage = data.getTotalPage();

        //1.2 获取所有的device
        for (int i = 1; i <= totalDevicePage; i++) {
            BrmDevicePageRequest brmDevicePageRequestData = new BrmDevicePageRequest();
            brmDevicePageRequestData.setPageNum(i);
            brmDevicePageRequestData.setPageSize(pageSize);
            brmDevicePageRequestData.setPageNum(i);
            allDevices.addAll(cjDeviceService.getDevice(brmDevicePageRequestData).getData().getPageData());
        }

        List<DeviceEntity> deviceEntities = new ArrayList<>();

        //1.3 保存到数据库
        allDevices.forEach(a -> {
            DeviceEntity deviceEntity = new DeviceEntity();
            deviceEntity.setName(a.getDeviceName());
            deviceEntity.setCode(a.getDeviceCode());
            deviceEntities.add(deviceEntity);
        });

        deviceService.remove(null);
        deviceService.saveBatch(deviceEntities);
        //2.1 获取所有的channel
        List<DeviceEntity> list = deviceService.list();




        //2.2 获取对应的device channel
        List<ChannelEntity> channelEntities = new ArrayList<>();

        list.forEach(a -> {

            try {
                BrmDeviceChannelPageRequest brmDeviceChannelPageRequest = new BrmDeviceChannelPageRequest();
                brmDeviceChannelPageRequest.setPageNum(initPageNum);
                brmDeviceChannelPageRequest.setPageSize(pageSize);
                List<String> unitTypeList = new ArrayList<>();
                unitTypeList.add("1");//视频通道
                brmDeviceChannelPageRequest.setUnitTypeList(unitTypeList);
                //获取channel总数
                brmDeviceChannelPageRequest.setDeviceCodeList(Collections.singletonList(a.getCode()));
                BrmDeviceChannelPageResponse deviceChannelPageResponse = null;
                deviceChannelPageResponse = cjDeviceChannelService.getDeviceChannel(brmDeviceChannelPageRequest);

                Integer totalDeviceChannelPage = deviceChannelPageResponse.getData().getTotalPage();
                //获取所有的channel
                for (int i = 1; i <= totalDeviceChannelPage; i++) {
                    BrmDeviceChannelPageRequest brmDeviceChannelPageRequestData = new BrmDeviceChannelPageRequest();
                    brmDeviceChannelPageRequestData.setPageSize(pageSize);
                    brmDeviceChannelPageRequestData.setPageNum(i);
                    List<String> deviceCodeList = new ArrayList<>();
                    deviceCodeList.add(a.getCode());//获取特定设备的通道
                    brmDeviceChannelPageRequestData.setDeviceCodeList(deviceCodeList);
                    List<String> unitTypeListData = new ArrayList<>();
                    unitTypeListData.add("1");//视频通道
                    brmDeviceChannelPageRequestData.setUnitTypeList(unitTypeListData);
                    List<BrmDeviceChannelPageResponse.DeviceChannelPage> pageData = cjDeviceChannelService.getDeviceChannel(brmDeviceChannelPageRequestData).getData().getPageData();
                    pageData.forEach(b -> {
                        ChannelEntity channelEntity = new ChannelEntity();
                        channelEntity.setName(b.getChannelName());
                        channelEntity.setCode(b.getChannelCode());
                        channelEntity.setDeviceId(a.getId());
                        channelEntities.add(channelEntity);
                    });
                }
            } catch (ClientException e) {
                e.printStackTrace();
            }
        });
//        //获取channel总数
//        BrmDeviceChannelPageResponse deviceChannelPageResponse = cjDeviceChannelService.getDeviceChannel(brmDeviceChannelPageRequest);
//        Integer totalDeviceChannelPage = deviceChannelPageResponse.getData().getTotalPage();
//        //获取所有的device
//        for (int i = 1; i <= totalDeviceChannelPage; i++) {
//            brmDeviceChannelPageRequest.setPageNum(i);
//            allDeviceChannels.addAll(cjDeviceChannelService.getDeviceChannel(brmDeviceChannelPageRequest).getData().getPageData());
//        }
        channelService.remove(null);
        channelService.saveBatch(channelEntities);
//        System.out.println(allDeviceChannels.size());
    }

    public void getAllAlarm(){

    }
}
