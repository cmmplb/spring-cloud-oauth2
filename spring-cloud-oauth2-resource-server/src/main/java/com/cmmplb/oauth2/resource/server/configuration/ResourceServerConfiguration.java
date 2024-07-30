package com.cmmplb.oauth2.resource.server.configuration;

import com.cmmplb.oauth2.resource.server.configuration.properties.SecurityProperties;
import com.cmmplb.oauth2.resource.server.handler.AccessDeniedHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * @author penglibo
 * @date 2024-07-10 15:41:47
 * @since jdk 1.8
 * 资源服务器配置
 */

@Slf4j
@EnableResourceServer
@EnableConfigurationProperties(SecurityProperties.class)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Autowired
    private ResourceServerTokenServices resourceServerTokenServices;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        // token存取
        resources.tokenStore(tokenStore);
        // 校验token
        resources.tokenServices(resourceServerTokenServices);
        // 权限不足处理
        resources.accessDeniedHandler(accessDeniedHandler);
        // 异常端点处理
        resources.authenticationEntryPoint(authenticationEntryPoint);
    }

    /**
     * 配置资源接口安全
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();
        // 关闭资源验证
        if (!securityProperties.getEnabled()) {
            log.info("关闭资源验证");
            registry.anyRequest().permitAll();
        } else {
            // 配置不需要安全拦截url
            securityProperties.getWhiteList().values().forEach(url -> registry.antMatchers(url.split(",")).permitAll());
            // 所匹配的 URL 都不允许被访问。denyAll
            registry.anyRequest().authenticated().and().csrf().disable();
        }
    }
}