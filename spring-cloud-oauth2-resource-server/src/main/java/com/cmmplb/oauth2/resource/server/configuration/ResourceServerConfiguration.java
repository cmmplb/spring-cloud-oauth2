package com.cmmplb.oauth2.resource.server.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author penglibo
 * @date 2024-07-10 15:41:47
 * @since jdk 1.8
 * 资源服务器配置
 */

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Autowired
    private RemoteTokenServices remoteTokenServices;

    @Autowired
    private TokenStore tokenStore;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        // token存取
        resources.tokenStore(tokenStore);
        // 通过这个Bean，去远程调用认证服务器，验token
        resources.tokenServices(remoteTokenServices);
    }
}