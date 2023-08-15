package com.nanshan.icc.org;

import com.dahuatech.hutool.http.Method;
import com.dahuatech.hutool.json.JSONUtil;
import com.dahuatech.icc.exception.ClientException;
import com.dahuatech.icc.oauth.http.DefaultClient;
import com.dahuatech.icc.oauth.http.IClient;
import com.dahuatech.icc.oauth.model.v202010.GeneralRequest;
import com.nanshan.icc.generated.entity.OrgEntity;
import com.nanshan.icc.generated.service.OrgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CJOrgService {

    private Integer initPageNum = 1;
    private Integer pageSize = 100;

    @Autowired
    private OrgService orgService;

    public void getOrgs() throws ClientException {
        //1.获取总数
        IClient iClient = new DefaultClient();
        GeneralRequest generalRequest =
                new GeneralRequest("/evo-apigw/evo-brm/1.2.0/organization/subsystem/page", Method.POST);
        Map<String, Object> map = new HashMap<>();

        map.put("includeSubOrgCodeFlag", true);
        map.put("pageNum", initPageNum);
        map.put("pageSize", pageSize);

        // 设置参数
        generalRequest.body(JSONUtil.toJsonStr(map));
        CJOrgPageResponse cjOrgPageResponse = iClient.doAction(generalRequest, CJOrgPageResponse.class);
        System.out.println(cjOrgPageResponse);
        Integer totalPage =cjOrgPageResponse.getData().getTotalPage();

        IClient iClientData = new DefaultClient();
        List<OrgEntity> orgEntities = new ArrayList<>();
        for (int i = 1; i <= totalPage; i++) {
            GeneralRequest generalRequestData =
                    new GeneralRequest("/evo-apigw/evo-brm/1.2.0/organization/subsystem/page", Method.POST);
            Map<String, Object> mapData = new HashMap<>();
            mapData.put("pageNum", i);
            mapData.put("pageSize", pageSize);
            mapData.put("includeSubOrgCodeFlag", true);
            generalRequestData.body(JSONUtil.toJsonStr(mapData));
            CJOrgPageResponse response = iClientData.doAction(generalRequestData, CJOrgPageResponse.class);
            response.getData().getPageData().forEach(a -> {
                OrgEntity orgEntity = new OrgEntity();
                orgEntity.setOrgCode(a.getOrgCode());
                orgEntity.setOrgName(a.getOrgName());
                orgEntity.setState(a.getStat());
                orgEntities.add(orgEntity);
            });
            orgService.remove(null);
            orgService.saveBatch(orgEntities);
        }


    }



}
