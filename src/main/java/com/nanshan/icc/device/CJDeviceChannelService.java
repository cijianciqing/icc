package com.nanshan.icc.device;

import com.dahuatech.icc.brm.model.v202010.device.BrmDeviceChannelPageRequest;
import com.dahuatech.icc.brm.model.v202010.device.BrmDeviceChannelPageResponse;
import com.dahuatech.icc.exception.ClientException;
import com.dahuatech.icc.oauth.http.DefaultClient;
import com.dahuatech.icc.oauth.http.IClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 */
@Slf4j
@Service
public class CJDeviceChannelService {

  /**
   *分页获取设备通道信息
   */
  public BrmDeviceChannelPageResponse getDeviceChannel(BrmDeviceChannelPageRequest brmDeviceChannelPageRequest) throws ClientException {
    IClient iClient = new DefaultClient();

    BrmDeviceChannelPageResponse brmDeviceChannelPageResponse = iClient.doAction(brmDeviceChannelPageRequest,BrmDeviceChannelPageResponse.class);

    return brmDeviceChannelPageResponse;

  }



}
