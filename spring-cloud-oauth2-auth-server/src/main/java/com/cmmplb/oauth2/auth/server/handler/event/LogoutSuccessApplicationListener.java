package com.cmmplb.oauth2.auth.server.handler.event;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * @author penglibo
 * @date 2024-07-26 14:33:05
 * @since jdk 1.8
 * 退出登录事件监听处理
 */

@Slf4j
@Component
public class LogoutSuccessApplicationListener implements ApplicationListener<LogoutSuccessEvent> {

    @Override
    public void onApplicationEvent(LogoutSuccessEvent event) {
        Authentication authentication = (Authentication) event.getSource();
        log.info("退出登录处理,username:{}", authentication.getName());
        log.info("=============todo:保存退出登录处理日志等相关操作==================");
    }
}
