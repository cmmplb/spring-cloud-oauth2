package com.cmmplb.oauth2.resource.server.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

/**
 * @author penglibo
 * @date 2024-07-16 17:17:52
 * @since jdk 1.8
 */

public class ResourceAutoConfiguration {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * 令牌存储
     */
    @Bean
    public TokenStore redisTokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    /**
     * 远程令牌服务
     */
    @Bean
    @Primary
    public RemoteTokenServices remoteTokenServices() {
        RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
        remoteTokenServices.setRestTemplate(restTemplate());
        // 这里到时候可以通过配置文件获取，现在先固定
        remoteTokenServices.setClientId("web");
        remoteTokenServices.setClientSecret("123456");
        remoteTokenServices.setCheckTokenEndpointUrl("http://localhost:20000/oauth/check_token");
        return remoteTokenServices;
    }

    /**
     * 服务调用
     */
    @Bean
    @Primary
    @LoadBalanced
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
        return restTemplate;
    }

}