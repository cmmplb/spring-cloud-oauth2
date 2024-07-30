package com.cmmplb.oauth2.system.server.controller;

import com.cmmplb.oauth2.resource.server.annotation.WithoutLogin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author penglibo
 * @date 2024-07-10 10:41:16
 * @since jdk 1.8
 */

@WithoutLogin
@RestController
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "ok";
    }
}