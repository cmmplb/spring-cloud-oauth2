package com.cmmplb.oauth2.resource.server.configuration;

import com.cmmplb.oauth2.resource.server.handler.AccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.JdbcClientTokenServices;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.implicit.ImplicitAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.util.Arrays;

/**
 * @author penglibo
 * @date 2024-07-16 17:17:52
 * @since jdk 1.8
 */

@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceAutoConfiguration {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private DataSource dataSource;

    /**
     * 令牌存储
     */
    @Bean
    public TokenStore redisTokenStore() {
        // 基于redis缓存存储token
        // return new RedisTokenStore(redisConnectionFactory);
        // 基于数据库存储
        return new JdbcTokenStore(dataSource);
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
        // 这个抽筋有时候会报错，No instances available for localhost，然后有时候下面那个又说UnNonHostException
        // 后面打断的时候反应过来，这是重启的时候认证服务还没在Eureka注册好，多等一会儿就行了。0.0
        // remoteTokenServices.setCheckTokenEndpointUrl("http://localhost:20000/oauth/check_token");
        remoteTokenServices.setCheckTokenEndpointUrl("http://SPRING-CLOUD-OAUTH2-AUTH-SERVER/oauth/check_token");
        return remoteTokenServices;
    }

    /**
     * 通过令牌获取用户信息来验证有效性
     * todo:后续处理
     */
    // @Bean
    // public UserInfoTokenServices userInfoTokenServices() {
    //     return new UserInfoTokenServices("http://localhost:20000/oauth/user", "web");
    // }

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

    /**
     * oauth2.scope权限表达式解析
     */
    @Bean
    public OAuth2MethodSecurityExpressionHandler oAuth2MethodSecurityExpressionHandler() {
        return new OAuth2MethodSecurityExpressionHandler();
    }

    /**
     * 权限不足处理器
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandler();
    }

    /**
     * 基于数据库存储客户端令牌
     */
    @Bean
    public JdbcClientTokenServices clientTokenServices() {
        return new JdbcClientTokenServices(dataSource);
    }
}
