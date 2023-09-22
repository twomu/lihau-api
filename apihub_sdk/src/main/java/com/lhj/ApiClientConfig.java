package com.lhj;

import com.lhj.apiclient.NameApiClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lihua
 */
@Configuration
@ConfigurationProperties("apihub.client")
public class ApiClientConfig {
    String accessKey;
    String secretKey;
    @Bean
    public NameApiClient getApiClien(){
        return new NameApiClient(accessKey,secretKey);
    }
}
