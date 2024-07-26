package com.cmmplb.oauth2.resource.server.configuration;

import com.cmmplb.oauth2.resource.server.handler.AccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author penglibo
 * @date 2024-07-10 15:41:47
 * @since jdk 1.8
 * 资源服务器配置
 */

@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Autowired
    private RemoteTokenServices remoteTokenServices;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        // token存取
        resources.tokenStore(tokenStore);
        // 远程调用认证服务器校验token有效性
        resources.tokenServices(remoteTokenServices);
        // 权限不足处理
        resources.accessDeniedHandler(accessDeniedHandler);
    }

    /**
     * 配置资源接口安全
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();
        // 配置不需要安全拦截url
        registry.antMatchers("/user/info/*", "/user/info/mobile/*", "/client/login").permitAll();
        // 其余接口都需要认证
        registry.anyRequest().authenticated().and().csrf().disable();
    }
}