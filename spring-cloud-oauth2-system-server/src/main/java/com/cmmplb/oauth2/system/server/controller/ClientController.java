package com.cmmplb.oauth2.system.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.ClientTokenServices;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * @author penglibo
 * @date 2024-07-25 17:53:16
 * @since jdk 1.8
 */

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientTokenServices clientTokenServices;

    @PostMapping("/login")
    public OAuth2AccessToken login() {
        // 创建 ClientCredentialsResourceDetails 对象
        ClientCredentialsResourceDetails details = new ClientCredentialsResourceDetails();
        details.setClientId("web");
        details.setClientSecret("123456");
        details.setAccessTokenUri("http://localhost:20000/oauth/token");
        // details.setAccessTokenUri("http://SPRING-CLOUD-OAUTH2-AUTH-SERVER/oauth/token");
        // 创建 OAuth2RestTemplate 对象
        AccessTokenProviderChain accessTokenProviderChain = new AccessTokenProviderChain(Arrays.<AccessTokenProvider>asList(
                // AccessTokenProviderChain源码初始化有这四个，这里用的是ClientCredentialsResourceDetails，所以用ClientCredentialsAccessTokenProvider
                // new AuthorizationCodeAccessTokenProvider(),
                // new ImplicitAccessTokenProvider(),
                // new ResourceOwnerPasswordAccessTokenProvider(),
                new ClientCredentialsAccessTokenProvider()));
        accessTokenProviderChain.setClientTokenServices(clientTokenServices);
        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(details);
        restTemplate.setAccessTokenProvider(accessTokenProviderChain);
        return restTemplate.getAccessToken();
    }
}