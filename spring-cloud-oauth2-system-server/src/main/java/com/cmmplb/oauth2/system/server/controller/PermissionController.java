package com.cmmplb.oauth2.system.server.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author penglibo
 * @date 2024-07-18 18:06:54
 * @since jdk 1.8
 */

@RestController
@RequestMapping("/permission")
public class PermissionController {

    /**
     * 需要有write编码权限
     */
    @PreAuthorize(value = "hasAuthority('write')")
    @GetMapping("/write")
    public String write() {
        return "ok";
    }

    /**
     * 需要有read编码权限
     */
    @PreAuthorize(value = "hasAuthority('read')")
    @GetMapping("/read")
    public String read() {
        return "ok";
    }

    /**
     * 需要有普通用户角色权限
     */
    @PreAuthorize(value = "hasRole('user')")
    @GetMapping("/user")
    public String user() {
        return "ok";
    }

    /**
     * 需要有管理员角色权限
     */
    @PreAuthorize(value = "hasRole('admin')")
    @GetMapping("/admin")
    public String admin() {
        return "ok";
    }
}