package com.cmmplb.oauth2.auth.server.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author penglibo
 * @date 2024-07-25 14:02:02
 * @since jdk 1.8
 */
@RestController
@RequestMapping("/oauth")
public class UserController {

    /**
     * 提供user-info-uri端点
     * @return
     */
    @RequestMapping("/user")
    public Object user() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication;
    }
}
