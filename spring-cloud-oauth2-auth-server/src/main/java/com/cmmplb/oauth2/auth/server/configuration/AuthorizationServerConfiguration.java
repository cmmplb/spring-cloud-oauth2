package com.cmmplb.oauth2.auth.server.configuration;

import com.cmmplb.oauth2.resource.server.handler.GlobalWebResponseExceptionTranslator;
import com.cmmplb.oauth2.resource.server.mobile.MobileTokenGranter;
import com.cmmplb.oauth2.resource.server.service.impl.JdbcApprovalStoreImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.token.JdbcClientTokenServices;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author penglibo
 * @date 2024-07-04 10:28:32
 * @since jdk 1.8
 * oauth2授权服务器配置
 */

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private GlobalWebResponseExceptionTranslator globalWebResponseExceptionTranslator;

    @Autowired
    private DataSource dataSource;

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
                .userDetailsService(userDetailsService)
                // 配置认证管理器
                .authenticationManager(authenticationManager)
                // 配置grant_type模式
                .tokenGranter(tokenGranter(endpoints))
                // 配置token存储
                .tokenStore(tokenStore)
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
                .checkTokenAccess("permitAll()");
    }

    /**
     * 配置客户端详情
     * @param clients 客户端配置器
     * @throws Exception e
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 基于数据库配置
        jdbc(clients);
        // 基于内存配置
        // inMemory(clients);
    }

    /**
     * 基于数据库配置客户端信息
     */
    private void jdbc(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource);
    }

    /**
     * 基于内存配置客户端信息
     */
    private void inMemory(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                // 基于内存配置
                .inMemory()
                // 客户端id
                .withClient("web")
                // 客户端密钥
                .secret(passwordEncoder.encode("123456"))
                // 自动同意，为false登录后会跳转到授权页面
                .autoApprove(false)
                .scopes("username", "phone", "age")
                // 登录成功回调地址，这里如果配置了多个，则请求地址需要携带redirect_uri参数，并且值是配置的其中一个，如果只配置一个，则可以不带redirect_uri参数
                .redirectUris("http://localhost:10000/auth/actuator/health", "http://localhost:20000/actuator/health", "http://localhost:18080/auth")
                .authorizedGrantTypes("client_credentials", "password", "implicit", "authorization_code", "refresh_token");
    }

    /**
     * 创建grant_type列表，如果不配置则默认使用密码模式、简化模式、授权码模式、客户端模式以及刷新token模式
     * {@link AuthorizationServerEndpointsConfigurer#getDefaultTokenGranters()}
     * 如果配置了只使用配置中，默认配置失效
     * @param endpoints 端点配置器
     * @return TokenGranter
     */
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
    @Bean
    public ApprovalStore approvalStore() {
        return new JdbcApprovalStoreImpl(dataSource);
    }

    /**
     * 基于数据库存储授权码信息
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }
}