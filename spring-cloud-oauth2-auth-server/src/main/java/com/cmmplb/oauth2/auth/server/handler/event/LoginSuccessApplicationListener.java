package com.cmmplb.oauth2.auth.server.handler.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * @author penglibo
 * @date 2024-07-26 16:11:54
 * @since jdk 1.8
 * 登录成功的处理
 */

@Slf4j
@Component
public class LoginSuccessApplicationListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        Authentication authentication = (Authentication) event.getSource();
        log.info("登录成功处理,username:{}", authentication.getName());
        log.info("=============保存登录成功处理日志等相关操作==================");
    }
}
