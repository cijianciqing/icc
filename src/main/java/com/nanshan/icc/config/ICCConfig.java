package com.nanshan.icc.config;

import com.dahuatech.icc.exception.ClientException;
import com.dahuatech.icc.oauth.http.DefaultClient;
import com.dahuatech.icc.oauth.http.IClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ICCConfig {
    @Bean
    public IClient iccDefaultClient() throws ClientException {
        return new DefaultClient();
    }
    //如果你项目没有办法增加iccSdk.properties配置文件，你也可以
//    @Bean
//    public IClient iccDefaultClient() throws ClientException {
//        return new DefaultClient("host", "username", "password", "clientId", "clientSecret");
//    }
}
