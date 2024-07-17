package com.cmmplb.oauth2.resource.server.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

/**
 * @author penglibo
 * @date 2024-07-10 16:32:11
 * @since jdk 1.8
 * 认证服务器异常
 */

@Slf4j
public class GlobalWebResponseExceptionTranslator implements WebResponseExceptionTranslator<OAuth2Exception> {

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) {
        // 不包含上述异常则服务器内部错误
        log.error("认证服务器异常:{}", e.getMessage());
        return new ResponseEntity<>(new OAuth2Exception(e.getMessage(), e), HttpStatus.OK);
    }
}
