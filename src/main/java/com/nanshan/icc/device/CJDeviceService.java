package com.nanshan.icc.device;

import com.dahuatech.icc.brm.model.v202010.device.BrmDevicePageRequest;
import com.dahuatech.icc.brm.model.v202010.device.BrmDevicePageResponse;
import com.dahuatech.icc.exception.ClientException;
import com.dahuatech.icc.oauth.http.DefaultClient;
import com.dahuatech.icc.oauth.http.IClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * 基础资源角色相关接口调用demo演示
 *
 * @author icc-developer
 * @since 2022/01/24
 */
@Slf4j
@Service
public class CJDeviceService {

  /**
   *分页获取设备信息
   * @throws ClientException
   */
  public BrmDevicePageResponse getDevice(BrmDevicePageRequest brmDevicePageRequest) throws ClientException {
    IClient iClient = new DefaultClient();
//    BrmDevicePageRequest brmDevicePageRequest = new BrmDevicePageRequest();
//    brmDevicePageRequest.setPageNum(1);
//    brmDevicePageRequest.setPageSize(10);

    BrmDevicePageResponse brmDevicePageResponse = iClient.doAction(brmDevicePageRequest,BrmDevicePageResponse.class);

    return brmDevicePageResponse;

  }


}
