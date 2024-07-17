package com.cmmplb.oauth2.auth.server.configuration;

import com.cmmplb.oauth2.resource.server.handler.GlobalWebResponseExceptionTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

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
                // 配置token存储
                .tokenStore(tokenStore)
                // 自定义异常处理
                .exceptionTranslator(globalWebResponseExceptionTranslator)
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
        clients
                // 基于内存配置
                .inMemory()
                // 客户端id
                .withClient("web")
                // 客户端密钥
                .secret(passwordEncoder.encode("123456"))
                // 自动同意，为false登录后会跳转到授权页面
                .autoApprove(false)
                .scopes("all")
                // 登录成功回调地址，这里如果配置了多个，则请求地址需要携带redirect_uri参数，并且值是配置的其中一个，如果只配置一个，则可以不带redirect_uri参数
                .redirectUris("http://localhost:10000/auth/actuator/health", "http://localhost:20000/actuator/health", "http://localhost:18080/auth")
                .authorizedGrantTypes("client_credentials", "password", "implicit", "authorization_code", "refresh_token")
        ;
    }
}