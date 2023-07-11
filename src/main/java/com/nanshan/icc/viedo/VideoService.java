package com.nanshan.icc.viedo;

import com.dahuatech.hutool.http.Method;
import com.dahuatech.icc.brm.model.v202010.device.BrmDeviceChannelPageRequest;
import com.dahuatech.icc.brm.model.v202010.device.BrmDeviceChannelPageResponse;
import com.dahuatech.icc.exception.ClientException;
import com.dahuatech.icc.oauth.http.DefaultClient;
import com.dahuatech.icc.oauth.http.IClient;
import com.dahuatech.icc.oauth.http.IccHttpHttpRequest;
import com.dahuatech.icc.oauth.profile.IccProfile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Slf4j
public class VideoService {

    private String host;
    private String username;
    private String password;
    private String clientId;
    private String clientSecret;

    public VideoService(String host, String username, String password, String clientId, String clientSecret) throws ClientException {
        this.host = host;
        this.username = username;
        this.password = password;
        this.clientId = clientId;
        this.clientSecret = clientSecret;

        iClient = new DefaultClient(host, username, password, clientId, clientSecret);
    }

    private final ObjectMapper mapper = new ObjectMapper();

    private IClient iClient;

    /**
     * 获取设备通道列表
     *
     * @param pageNum   页号
     * @param pageSize  页大小
     * @param unitTypeList  单元类型列表
     * @param channelTypeList   通道类型列表
     * @param isOnline  是否在线
     * @return  设备通道列表
     * @throws ClientException  客户端异常
     */
    public List<BrmDeviceChannelPageResponse.DeviceChannelPage> getDeviceChannelPage(int pageNum, int pageSize,
                                                                               List<String> unitTypeList, List<String> channelTypeList, List<String> deviceCodeList,
                                                                               int isOnline) throws ClientException {
        BrmDeviceChannelPageRequest brmDeviceChannelPageRequest = new BrmDeviceChannelPageRequest();
        brmDeviceChannelPageRequest.setPageNum(pageNum);
        brmDeviceChannelPageRequest.setPageSize(pageSize);
        brmDeviceChannelPageRequest.setUnitTypeList(unitTypeList);
        brmDeviceChannelPageRequest.setChannelTypeList(channelTypeList);
        brmDeviceChannelPageRequest.setIncludeSubOwnerCodeFlag(true);
        brmDeviceChannelPageRequest.setDeviceCodeList(deviceCodeList);
        brmDeviceChannelPageRequest.setIsOnline(isOnline);
        BrmDeviceChannelPageResponse brmDeviceChannelPageResponse = iClient.doAction(brmDeviceChannelPageRequest, BrmDeviceChannelPageResponse.class);
        if (!brmDeviceChannelPageResponse.isSuccess()) {
            log.error("获取设备通道列表失败");
            throw new RuntimeException("fetch channel error");
        }
        BrmDeviceChannelPageResponse.DeviceChannelPageData brmDeviceChannelPageResponseData = brmDeviceChannelPageResponse.getData();
        List<BrmDeviceChannelPageResponse.DeviceChannelPage> channelList = brmDeviceChannelPageResponseData.getPageData();
        if (CollectionUtils.isEmpty(channelList)) {
            log.error("设备通道列表为空");
            channelList = Collections.emptyList();
        }
        return channelList;
    }

    /**
     * 获取实时预览rtsp流地址
     *
     * @param channelCode   通道编码
     * @param streamType    码流类型：1=主码流，2=辅码流
     * @return  rtsp流地址
     * @throws ClientException  客户端异常
     */
    public String startVideo(String channelCode, String streamType) throws ClientException {
        IccHttpHttpRequest startVideoRequest = new IccHttpHttpRequest(IccProfile.URL_SCHEME + "/evo-apigw/admin/API/MTS/Video/StartVideo", Method.POST);
        // 参数注释：
        //  channelId 视频通道编码
        //  dataType 视频类型：1=视频
        //  streamType 码流类型：1=主码流，2=辅码流
        String startVideoBody = "{\"data\":{\"channelId\":\"%s\",\"dataType\":\"1\",\"streamType\":\"%s\"}}";
        startVideoBody = String.format(startVideoBody, channelCode, streamType);
        log.info("请求参数：{}", startVideoBody);
        startVideoRequest.body(startVideoBody);
        String startVideoResponse = iClient.doAction(startVideoRequest);
        String rtspUrl;
        try {
            JsonNode data = mapper.readValue(startVideoResponse, JsonNode.class).get("data");
            rtspUrl = data.get("url").asText() + "?token=" + data.get("token").asText();
        } catch (JsonProcessingException e) {
            log.error("startVideoResponse[{}] format error", startVideoResponse, e);
            throw new RuntimeException("response format error");
        }
        return rtspUrl;
    }

    /**
     * 实时预览 “HLS、FLV、RTMP实时拉流”
     *
     * @param channelCode   通道编码
     * @param streamType    码流类型：1=主码流，2=辅码流
     * @param type  拉流方式：hls,flv,rtmp
     * @return  流地址
     * @throws ClientException  客户端异常
     */
    public String realtime(String channelCode, String streamType, String type) throws ClientException {
        IccHttpHttpRequest realtimeRequest = new IccHttpHttpRequest(IccProfile.URL_SCHEME + "/evo-apigw/admin/API/video/stream/realtime", Method.POST);
        // 参数注释：
        //  channelId 视频通道编码
        //  streamType 码流类型：1=主码流，2=辅码流
        //  type 拉流方式：hls,flv,rtmp
        String realtimeBody = "{\"data\":{\"channelId\":\"%s\",\"streamType\":\"%s\",\"type\":\"%s\"}}";
        realtimeBody = String.format(realtimeBody, channelCode, streamType, type);
        log.info("请求参数：{}", realtimeBody);
        realtimeRequest.body(realtimeBody);
        String realtimeResponse = iClient.doAction(realtimeRequest);
        String url;
        try {
            JsonNode data = mapper.readValue(realtimeResponse, JsonNode.class).get("data");
            url = data.get("url").asText();
        } catch (JsonProcessingException e) {
            log.error("realtimeResponse[{}] format error", realtimeResponse, e);
            throw new RuntimeException("response format error");
        }
        return url;
    }

    /**
     * 查询通道的录像列表
     *
     * @param channelCode   通道编码
     * @param streamType    码流类型：0=全部，1=主码流，2=辅码流
     * @param recordSource  录像来源：2=设备，3=中心
     * @param startTime     开始时间(时间戳：单位秒)
     * @param endTime       结束时间(时间戳：单位秒)
     * @return  录像列表
     * @throws ClientException  客户端异常
     */
    public JsonNode getRecords(String channelCode, String streamType, String recordSource, String startTime, String endTime) throws ClientException {
        IccHttpHttpRequest queryRecordRequest = new IccHttpHttpRequest(IccProfile.URL_SCHEME + "/evo-apigw/admin/API/SS/Record/QueryRecords", Method.POST);
        // 参数注释：
        //  channelId 视频通道编码
        //  streamType 码流类型：0=全部，1=主码流，2=辅码流
        //  recordSource 录像来源：2=设备，3=中心
        //  recordType 录像类型：0=全部，1=手动录像，2=报警录像，3=动态监测，4=视频丢失，5=视频遮挡，6=定时录像，7=全天候录像，8=文件录像转换（平台录像计划配的录像是定时录像）
        //  startTime 开始时间(时间戳：单位秒)
        //  endTime 结束时间(时间戳：单位秒)
        String queryRecordBody = "{\"data\":{\"channelId\":\"%s\",\"streamType\":\"%s\",\"recordSource\":\"%s\",\"recordType\":\"0\",\"startTime\":\"%s\",\"endTime\":\"%s\"}}";
        queryRecordBody = String.format(queryRecordBody, channelCode, streamType, recordSource, startTime, endTime);
        log.info("请求参数：{}", queryRecordBody);
        queryRecordRequest.body(queryRecordBody);
        String queryRecordResponse = iClient.doAction(queryRecordRequest);
        JsonNode videoRecordList;
        try {
            JsonNode queryRecordResponseData = mapper.readValue(queryRecordResponse, JsonNode.class).get("data");
            videoRecordList = queryRecordResponseData.get("records");
        } catch (JsonProcessingException e) {
            log.error("json format error", e);
            throw new RuntimeException("response format error");
        }
        return videoRecordList;
    }

    /**
     * 以文件形式回放
     *
     * @param channelCode   通道编码
     * @param streamType    码流类型：1=主码流，2=辅码流
     * @param recordSource  录像来源：2=设备，3=中心
     * @param recordType    录像类型：0=全部，1=手动录像，2=报警录像，3=动态监测，4=视频丢失，5=视频遮挡，6=定时录像，7=全天候录像，8=文件录像转换
     * @param startTime     开始时间(时间戳：单位秒)
     * @param endTime       结束时间(时间戳：单位秒)
     * @param fileName      录像文件信息
     * @param ssId          SS服务ID
     * @param streamId      码流处理
     * @param diskId        磁盘ID
     * @return  rtsp流地址
     * @throws ClientException  客户端异常
     */
    public String replayByFile(String channelCode, String streamType,
                               String recordSource, String recordType,
                               String startTime, String endTime,
                               String fileName, String ssId,
                               String streamId, String diskId) throws ClientException {
        IccHttpHttpRequest replayByFileRequest = new IccHttpHttpRequest(IccProfile.URL_SCHEME + "/evo-apigw/admin/API/SS/Playback/StartPlaybackByFile", Method.POST);
        // 参数注释：
        //  channelId 视频通道编码
        //  streamType 码流类型：1=主码流，2=辅码流
        //  recordSource 录像来源：2=设备，3=中心
        //  recordType 录像类型：0=全部，1=手动录像，2=报警录像，3=动态监测，4=视频丢失，5=视频遮挡，6=定时录像，7=全天候录像，8=文件录像转换
        //  startTime 开始时间(时间戳：单位秒)
        //  endTime 结束时间(时间戳：单位秒)
        //  fileName 录像文件信息
        //  ssId SS服务ID
        //  streamId 码流处理
        //  diskId 磁盘ID
        String replayByFileBody = "{\"data\":{\"channelId\":\"%s\",\"streamType\":\"%s\",\"recordSource\":\"%s\",\"recordType\":\"%s\",\"startTime\":\"%s\",\"endTime\":\"%s\",\"fileName\":\"%s\",\"ssId\":\"%s\",\"streamId\":\"%s\",\"diskId\":\"%s\"}}";
        replayByFileBody = String.format(replayByFileBody, channelCode, streamType, recordSource, recordType, startTime, endTime, fileName, ssId, streamId, diskId);
        log.info("请求参数：{}", replayByFileBody);
        replayByFileRequest.body(replayByFileBody);
        String replayByFileResponse = iClient.doAction(replayByFileRequest);
        String url;
        try {
            JsonNode replayByFileResponseData = mapper.readValue(replayByFileResponse, JsonNode.class).get("data");
            url = replayByFileResponseData.get("url").asText() + "?token=" + replayByFileResponseData.get("token").asText();
        } catch (JsonProcessingException e) {
            log.error("json format error", e);
            throw new RuntimeException("response format error");
        }
        return url;
    }

    /**
     * 以时间形式回放
     *
     * @param channelCode   通道编码
     * @param streamType    码流类型：1=主码流，2=辅码流
     * @param recordSource  录像来源：2=设备，3=中心
     * @param recordType    录像类型：默认传1即可
     * @param startTime     开始时间(时间戳：单位秒)
     * @param endTime       结束时间(时间戳：单位秒)
     * @return  rtsp流地址
     * @throws ClientException  客户端异常
     */
    public String replayByTime(String channelCode, String streamType,
                               String recordSource, String recordType,
                               String startTime, String endTime) throws ClientException {
        IccHttpHttpRequest replayByTimeRequest = new IccHttpHttpRequest(IccProfile.URL_SCHEME + "/evo-apigw/admin/API/SS/Playback/StartPlaybackByTime", Method.POST);
        // 参数注释：
        //  channelId 视频通道编码
        //  streamType 码流类型：1=主码流，2=辅码流
        //  recordSource 录像来源：2=设备，3=中心
        //  recordType 录像类型：默认传1即可
        //  startTime 开始时间(时间戳：单位秒)
        //  endTime 结束时间(时间戳：单位秒)
        String replayByTimeBody = "{\"data\":{\"channelId\":\"%s\",\"streamType\":\"%s\",\"recordSource\":\"%s\",\"recordType\":\"%s\",\"startTime\":\"%s\",\"endTime\":\"%s\"}}";
        replayByTimeBody = String.format(replayByTimeBody, channelCode, streamType, recordSource, recordType, startTime, endTime);
        log.info("请求参数：{}", replayByTimeBody);
        replayByTimeRequest.body(replayByTimeBody);
        String replayByTimeResponse = iClient.doAction(replayByTimeRequest);
        String url;
        try {
            JsonNode replayByTimeResponseData = mapper.readValue(replayByTimeResponse, JsonNode.class).get("data");
            url = replayByTimeResponseData.get("url").asText() + "?token=" + replayByTimeResponseData.get("token").asText();
        } catch (JsonProcessingException e) {
            log.error("json format error", e);
            throw new RuntimeException("response format error");
        }
        return url;
    }

    /**
     * 以HLS、RTMP方式进行录像回放
     *
     * @param channelCode   通道编码
     * @param streamType    码流类型：1=主码流，2=辅码流
     * @param type          拉流方式：支持hls/rtmp格式， flv录像类型不支持
     * @param recordSource  录像来源：2=设备录像，3=中心录像
     * @param recordType    录像类型：1 表示普通录像 默认1
     * @param startTime     开始时间,格式:"2020-11-12 11:10:11"
     * @param endTime       结束时间,格式:"2020-11-12 23:10:11"
     * @return  流地址
     * @throws ClientException  客户端异常
     */
    public String replay(String channelCode, String streamType, String type, String recordSource, String recordType, String startTime, String endTime) throws ClientException {
        IccHttpHttpRequest replayRequest = new IccHttpHttpRequest(IccProfile.URL_SCHEME + "/evo-apigw/admin/API/video/stream/record", Method.POST);
        // 参数注释：
        //  channelId 视频通道编码
        //  streamType 码流类型：1=主码流，2=辅码流
        //  type 拉流方式：支持hls/rtmp格式， flv录像类型不支持
        //  recordSource 录像来源：2=设备录像，3=中心录像
        //  recordType 录像类型：1 表示普通录像 默认1
        //  beginTime 开始时间,格式:"2020-11-12 11:10:11"
        //  endTime 结束时间,格式:"2020-11-12 23:10:11"
        String replayBody = "{\"data\":{\"channelId\":\"%s\",\"streamType\":\"%s\",\"type\":\"%s\",\"recordSource\":\"%s\",\"recordType\":\"%s\",\"beginTime\":\"%s\",\"endTime\":\"%s\"}}";
        replayBody = String.format(replayBody, channelCode, streamType, type, recordSource, recordType, startTime, endTime);
        log.info("请求参数：{}", replayBody);
        replayRequest.body(replayBody);
        String replayResponse = iClient.doAction(replayRequest);
        String url;
        try {
            JsonNode replayResponseData = mapper.readValue(replayResponse, JsonNode.class).get("data");
            url = replayResponseData.get("url").asText();
        } catch (JsonProcessingException e) {
            log.error("json format error", e);
            throw new RuntimeException("response format error");
        }
        return url;
    }

    public void cameraOperate(String channelCode, String operateType,
                              String direct, String step,
                              String command, String extend) throws ClientException {
        IccHttpHttpRequest operateCameraRequest = new IccHttpHttpRequest(IccProfile.URL_SCHEME +"/evo-apigw/admin/API/DMS/Ptz/OperateCamera", Method.POST);
        // 参数注释：
        //  channelCode 视频通道编码
        //  operateType 操作类型：1=变倍，2=变焦，3=光圈
        //  direct 方向：1=增加，2=减小
        //  step 步长
        //  command 命令：0=停止动作，1=开启动作
        //  extend 扩展数据
        String operateCameraBody = "{\"data\":{\"channelId\":\"%s\",\"operateType\":\"%s\",\"direct\":\"%s\",\"step\":\"%s\",\"command\":\"%s\",\"extend\":\"%s\"}}";
        operateCameraBody = String.format(operateCameraBody, channelCode, operateType, direct, step, command, extend);
        log.info("请求参数：{}", operateCameraBody);
        operateCameraRequest.body(operateCameraBody);
        String operateCameraResponse = iClient.doAction(operateCameraRequest);
        try {
            JsonNode code = mapper.readValue(operateCameraResponse, JsonNode.class).get("code");
            if (code.asInt() != 1000) {
                log.error("请求失败，{}", operateCameraResponse);
                throw new RuntimeException("return error");
            }
        } catch (JsonProcessingException e) {
            log.error("operateCameraResponse[{}] format error", operateCameraResponse, e);
            throw new RuntimeException("response format error");
        }
    }

    public void directOperate(String channelCode,
                              String direct, String stepX, String stepY,
                              String command, String extend) throws ClientException {
        IccHttpHttpRequest operateDirectRequest = new IccHttpHttpRequest(IccProfile.URL_SCHEME +"/evo-apigw/admin/API/DMS/Ptz/OperateDirect", Method.POST);
        // 参数注释：
        //  channelId 视频通道编码
        //  direct 方向：1=上，2=下，3=左，4=右，5=左上，6=左下，7=右上，8=右下
        //  stepX 水平方向步长
        //  stepY 垂直方向步长
        //  command 命令：0=停止动作，1=开启动作
        //  extend 扩展数据
        String operateDirectBody = "{\"data\":{\"channelId\":\"%s\",\"direct\":\"%s\",\"stepX\":\"%s\",\"stepY\":\"%s\",\"command\":\"%s\",\"extend\":\"%s\"}}";
        operateDirectBody = String.format(operateDirectBody, channelCode, direct, stepX, stepY, command, extend);
        log.info("请求参数：{}", operateDirectBody);
        operateDirectRequest.body(operateDirectBody);
        String operateDirectResponse = iClient.doAction(operateDirectRequest);
        try {
            JsonNode code = mapper.readValue(operateDirectResponse, JsonNode.class).get("code");
            if (code.asInt() != 1000) {
                log.error("请求失败，{}", operateDirectResponse);
                throw new RuntimeException("return error");
            }
        } catch (JsonProcessingException e) {
            log.error("operateDirectResponse[{}] format error", operateDirectResponse, e);
            throw new RuntimeException("response format error");
        }
    }

    /**
     * 云台功能控制
     *
     * @param channelCode   通道编码
     * @param operateType   参见开放平台：operateType操作类型
     * @param target        水平方向步长
     * @param command       命令：0=停止动作，1=开启动作，仅当operateType=[11,12,13,14,15,18,19,20,21]时有意义，其他情况下为0
     * @param extend        扩展数据
     * @throws ClientException  客户端异常
     */
    public void functionOperate(String channelCode, String operateType,
                                String target, String command,
                                String extend) throws ClientException {
        IccHttpHttpRequest operateFunctionRequest = new IccHttpHttpRequest(IccProfile.URL_SCHEME +"/evo-apigw/admin/API/DMS/Ptz/OperateFunction", Method.POST);
        // 参数注释：
        //  channelId 视频通道编码
        //  operateType 参见开放平台：operateType操作类型
        //  target 水平方向步长
        //  command 命令：0=停止动作，1=开启动作，仅当operateType=[11,12,13,14,15,18,19,20,21]时有意义，其他情况下为0
        //  extend 扩展数据
        String operateFunctionBody = "{\"data\":{\"channelId\":\"%s\",\"operateType\":\"%s\",\"target\":\"%s\",\"command\":\"%s\",\"extend\":\"\"}}";
        operateFunctionBody = String.format(operateFunctionBody, channelCode, operateType, target, command, extend);
        log.info("请求参数：{}", operateFunctionBody);
        operateFunctionRequest.body(operateFunctionBody);
        String operateFunctionResponse = iClient.doAction(operateFunctionRequest);
        try {
            JsonNode code = mapper.readValue(operateFunctionResponse, JsonNode.class).get("code");
            if (code.asInt() != 1000) {
                log.error("请求失败，{}", operateFunctionResponse);
                throw new RuntimeException("return error");
            }
        } catch (JsonProcessingException e) {
            log.error("operateFunctionResponse[{}] format error", operateFunctionResponse, e);
            throw new RuntimeException("response format error");
        }
    }
}
