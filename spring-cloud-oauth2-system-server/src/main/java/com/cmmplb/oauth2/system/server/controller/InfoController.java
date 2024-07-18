package com.cmmplb.oauth2.system.server.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author penglibo
 * @date 2024-07-15 13:51:27
 * @since jdk 1.8
 */

@RestController
@RequestMapping("/info")
public class InfoController {

    @PreAuthorize("#oauth2.hasScope('username')")
    @GetMapping("/username")
    public String username() {
        return "ok";
    }

    @PreAuthorize("#oauth2.hasScope('phone')")
    @GetMapping("/phone")
    public String phone() {
        return "ok";
    }

    @PreAuthorize("#oauth2.hasScope('age')")
    @GetMapping("/age")
    public String age() {
        return "ok";
    }

    @PreAuthorize("#oauth2.hasAnyScope('phone','age')")
    @GetMapping("/phone/age")
    public String phoneAndAge() {
        return "ok";
    }
}