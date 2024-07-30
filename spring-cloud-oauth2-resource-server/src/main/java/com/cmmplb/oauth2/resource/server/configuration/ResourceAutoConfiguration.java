package com.cmmplb.oauth2.resource.server.configuration;

import com.cmmplb.oauth2.resource.server.configuration.properties.RestTemplateProperties;
import com.cmmplb.oauth2.resource.server.handler.AccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.JdbcClientTokenServices;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;

/**
 * @author penglibo
 * @date 2024-07-16 17:17:52
 * @since jdk 1.8
 */

@EnableGlobalAuthentication
@EnableConfigurationProperties(RestTemplateProperties.class)
public class ResourceAutoConfiguration {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private RestTemplateProperties restTemplateProperties;

    @Autowired
    private ResourceServerProperties resourceServerProperties;

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
    public ResourceServerTokenServices resourceServerTokenServices() {
        // 使用代码配置，会覆盖配置文件中实例，org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerTokenServicesConfiguration.RemoteTokenServicesConfiguration.UserInfoTokenServicesConfiguration.userInfoTokenServices
        if (resourceServerProperties.isPreferTokenInfo()) {
            RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
            remoteTokenServices.setRestTemplate(restTemplate());
            // 通过org.springframework.boot.autoconfigure.security.oauth2.OAuth2AutoConfiguration
            // 从OAuth2ClientProperties把clientId和clientSecret设置到ResourceServerProperties
            remoteTokenServices.setClientId(resourceServerProperties.getClientId());
            remoteTokenServices.setClientSecret(resourceServerProperties.getClientSecret());
            remoteTokenServices.setCheckTokenEndpointUrl(resourceServerProperties.getTokenInfoUri());
            return remoteTokenServices;
        } else {
            UserInfoTokenServices userInfoTokenServices = new UserInfoTokenServices(
                    resourceServerProperties.getUserInfoUri(), resourceServerProperties.getClientId());
            userInfoTokenServices.setRestTemplate(oAuth2RestTemplate());
            return userInfoTokenServices;
        }
    }

    /**
     * 服务调用
     */
    @Bean
    @LoadBalanced
    public OAuth2RestTemplate oAuth2RestTemplate() {
        BaseOAuth2ProtectedResourceDetails resource = new BaseOAuth2ProtectedResourceDetails();
        resource.setClientId(resourceServerProperties.getClientId());
        return new OAuth2RestTemplate(resource);
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
        restTemplate.setRequestFactory(getSimpleClientFactory());
        return restTemplate;
    }


    /**
     * 手动创建SimpleClientHttpRequestFactory，指定超时时间以及代理配置
     */
    private SimpleClientHttpRequestFactory getSimpleClientFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(restTemplateProperties.getReadTimeout());
        factory.setConnectTimeout(restTemplateProperties.getConnectionTimeout());
        RestTemplateProperties.Proxy proxy = restTemplateProperties.getProxy();
        if (proxy.getEnabled()) {
            SocketAddress address = new InetSocketAddress(proxy.getHost(), proxy.getPort());
            factory.setProxy(new Proxy(Proxy.Type.HTTP, address));
        }
        return factory;
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
