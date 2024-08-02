package com.cmmplb.oauth2.resource.server.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;

/**
 * @author penglibo
 * @date 2024-07-31 14:05:02
 * @since jdk 1.8
 * feign请求拦截，添加认证请求头
 */

public class FeignRequestInterceptor implements RequestInterceptor {

    @Autowired
    private HttpServletRequest request;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 添加请求头Authorization
        requestTemplate.header(HttpHeaders.AUTHORIZATION, request.getHeader(HttpHeaders.AUTHORIZATION));
    }
}
