package com.cmmplb.oauth2.auth.server.handler.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * @author penglibo
 * @date 2024-07-26 16:11:21
 * @since jdk 1.8
 * 登录失败处理
 */

@Slf4j
@Component
public class LoginFailApplicationListener implements ApplicationListener<AbstractAuthenticationFailureEvent> {

    @Override
    public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
        Authentication authentication = (Authentication) event.getSource();
        log.info("登录失败处理,username:{}", authentication.getName());
        log.info("=============保存登录失败处理日志等相关操作==================");
    }
}
