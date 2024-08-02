package com.cmmplb.oauth2.auth.server.configuration;

import com.cmmplb.oauth2.resource.server.configuration.properties.Oauth2ConfigProperties;
import com.cmmplb.oauth2.resource.server.handler.GlobalWebResponseExceptionTranslator;
import com.cmmplb.oauth2.resource.server.impl.RedisAuthorizationCodeServicesImpl;
import com.cmmplb.oauth2.resource.server.impl.RedisClientDetailsServiceImpl;
import com.cmmplb.oauth2.resource.server.mobile.MobileTokenGranter;
import com.cmmplb.oauth2.resource.server.service.UserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.InMemoryApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author penglibo
 * @date 2024-07-04 10:28:32
 * @since jdk 1.8
 * oauth2授权服务器配置
 */

@Slf4j
@Configuration
@EnableAuthorizationServer
@EnableConfigurationProperties(Oauth2ConfigProperties.class)
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private TokenEnhancer tokenEnhancer;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired(required = false)
    private UserDetailsService userDetailsService;

    @Autowired
    private AccessTokenConverter accessTokenConverter;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private Oauth2ConfigProperties oauth2ConfigProperties;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired(required = false)
    private InMemoryUserDetailsManager inMemoryUserDetailsManager;

    @Autowired
    private GlobalWebResponseExceptionTranslator globalWebResponseExceptionTranslator;

    /**
     * 配置授权服务器端点的非安全功能，如令牌存储、令牌自定义、用户批准和授权类型。
     * 默认情况下你不需要做任何事情，除非你需要密码授权，在这种情况下你需要提供一个 {@link AuthenticationManager}。 *
     * @param endpoints 端点配置器
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                // 重复使用reuseRefreshToken
                .reuseRefreshTokens(false)
                // 用户信息服务
                .userDetailsService(userDetailsService())
                // 配置认证管理器
                .authenticationManager(authenticationManager)
                // 配置grant_type模式
                .tokenGranter(tokenGranter(endpoints))
                // 拓展token信息
                .tokenEnhancer(tokenEnhancer)
                // 配置token存储
                .tokenStore(tokenStore)
                // 认证服务器的token服务
                .tokenServices(tokenServices())
                // 配置token转换器
                .accessTokenConverter(accessTokenConverter)
                // 配置授权存储
                .approvalStore(approvalStore())
                // 配置授权码存储
                .authorizationCodeServices(authorizationCodeServices())
                // 自定义异常处理
                .exceptionTranslator(globalWebResponseExceptionTranslator)
                // 替换默认的授权页面地址，参数1是默认地址，参数2是自定义地址
                .pathMapping("/oauth/confirm_access", "/oauth/confirm/access")
                // 替换默认的错误页面地址
                .pathMapping("/oauth/error", "/oauth/error")
        ;
    }

    /**
     * 配置令牌端点(Token Endpoint)的安全约束
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
                // 允许客户端表单验证，默认false
                .allowFormAuthenticationForClients()
                // 允许校验token请求，默认denyAll()
                .checkTokenAccess("permitAll()")
                // 允许获取公钥请求，默认denyAll()
                .tokenKeyAccess("isAuthenticated()")
        ;
    }

    /**
     * 配置客户端详情
     * @param clients 客户端配置器
     * @throws Exception e
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        if (oauth2ConfigProperties.getClientDetailsServiceType().equals(Oauth2ConfigProperties.ClientDetailsServiceType.JDBC)) {
            clients.jdbc(dataSource);
        } else if (oauth2ConfigProperties.getClientDetailsServiceType().equals(Oauth2ConfigProperties.ClientDetailsServiceType.REDIS)) {
            clients.withClientDetails(new RedisClientDetailsServiceImpl(dataSource));
        } else {
            InMemoryClientDetailsServiceBuilder serviceBuilder = clients.inMemory();
            for (BaseClientDetails client : oauth2ConfigProperties.getClients()) {
                ClientDetailsServiceBuilder<InMemoryClientDetailsServiceBuilder>.ClientBuilder clientBuilder =
                        serviceBuilder.withClient(client.getClientId());
                clientBuilder.secret(passwordEncoder.encode(client.getClientSecret()));
                if (!CollectionUtils.isEmpty(client.getAutoApproveScopes())) {
                    clientBuilder.autoApprove(client.getAutoApproveScopes().toArray(new String[0]));
                }
                if (!CollectionUtils.isEmpty(client.getScope())) {
                    clientBuilder.scopes(client.getScope().toArray(new String[0]));
                }
                if (!CollectionUtils.isEmpty(client.getRegisteredRedirectUri())) {
                    clientBuilder.redirectUris(client.getRegisteredRedirectUri().toArray(new String[0]));
                }
                if (!CollectionUtils.isEmpty(client.getAuthorizedGrantTypes())) {
                    clientBuilder.authorizedGrantTypes(client.getAuthorizedGrantTypes().toArray(new String[0]));
                }
                if (null != client.getAccessTokenValiditySeconds()) {
                    clientBuilder.accessTokenValiditySeconds(client.getAccessTokenValiditySeconds());
                }
                if (null != client.getRefreshTokenValiditySeconds()) {
                    clientBuilder.refreshTokenValiditySeconds(client.getRefreshTokenValiditySeconds());
                }
            }
        }
    }

    /**
     * 认证服务器的token服务，使用@Primary覆盖ResourceServerTokenServices
     * 不定义这个bean的话，当携带token请求认证服务，会经过ResourceServerTokenServices调用checkToken或者userInfo方法重复调用，
     * 本身就是auth获取自身的资源，不需要再调用checkToken接口验证了，直接使用DefaultTokenServices通过tokenStore验签
     */
    @Bean
    @Primary
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore);
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setReuseRefreshToken(true);
        tokenServices.setTokenEnhancer(tokenEnhancer);
        tokenServices.setClientDetailsService(clientDetailsService);
        return tokenServices;
    }

    /**
     * 创建grant_type列表，如果不配置则默认使用密码模式、简化模式、授权码模式、客户端模式以及刷新token模式
     * {@link AuthorizationServerEndpointsConfigurer#getDefaultTokenGranters()}
     * 如果配置了只使用配置中，默认配置失效
     * @param endpoints 端点配置器
     * @return TokenGranter
     */
    @SuppressWarnings("JavadocReference")
    private TokenGranter tokenGranter(AuthorizationServerEndpointsConfigurer endpoints) {
        // 在原有配置下添加手机号验证码模式
        TokenGranter tokenGranter = endpoints.getTokenGranter();
        ArrayList<TokenGranter> tokenGranters = new ArrayList<>(Collections.singletonList(tokenGranter));
        // 添加一个自定义手机号验证码模式
        tokenGranters.add(new MobileTokenGranter(endpoints.getTokenServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory(), authenticationManager));
        return new CompositeTokenGranter(tokenGranters);
    }

    /**
     * 基于数据库获取授权信息
     */
    public ApprovalStore approvalStore() {
        ApprovalStore approvalStore;
        if (oauth2ConfigProperties.getApprovalStoreType().equals(Oauth2ConfigProperties.ApprovalStoreType.JDBC)) {
            approvalStore = new JdbcApprovalStore(dataSource);
        } else if (oauth2ConfigProperties.getApprovalStoreType().equals(Oauth2ConfigProperties.ApprovalStoreType.Token)) {
            approvalStore = new TokenApprovalStore();
        } else {
            approvalStore = new InMemoryApprovalStore();
        }
        return approvalStore;
    }

    /**
     * 授权码信息
     */
    public AuthorizationCodeServices authorizationCodeServices() {
        AuthorizationCodeServices authorizationCodeServices;
        if (oauth2ConfigProperties.getAuthorizationCodeServicesType().equals(Oauth2ConfigProperties.AuthorizationCodeServicesType.JDBC)) {
            authorizationCodeServices = new JdbcAuthorizationCodeServices(dataSource);
        } else if (oauth2ConfigProperties.getAuthorizationCodeServicesType().equals(Oauth2ConfigProperties.AuthorizationCodeServicesType.REDIS)) {
            authorizationCodeServices = new RedisAuthorizationCodeServicesImpl(redisConnectionFactory);
        } else {
            authorizationCodeServices = new InMemoryAuthorizationCodeServices();
        }
        return authorizationCodeServices;
    }

    /**
     * 用户信息
     */
    public org.springframework.security.core.userdetails.UserDetailsService userDetailsService() {
        if (oauth2ConfigProperties.getUserDetailsServiceType().equals(Oauth2ConfigProperties.UserDetailsServiceType.JDBC)) {
            return userDetailsService;
        } else {
            return inMemoryUserDetailsManager;
        }
    }
}