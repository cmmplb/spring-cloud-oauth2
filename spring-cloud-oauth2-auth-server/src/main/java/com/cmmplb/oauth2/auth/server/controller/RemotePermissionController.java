package com.cmmplb.oauth2.auth.server.controller;

import com.cmmplb.oauth2.resource.server.constants.SecurityConstant;
import com.cmmplb.oauth2.system.server.api.client.PermissionFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author penglibo
 * @date 2024-07-18 18:06:54
 * @since jdk 1.8
 */

@RestController
@RequestMapping("/remote/permission")
public class RemotePermissionController {

    @Autowired
    private PermissionFeignClient permissionFeignClient;

    /**
     * 需要有write编码权限
     */
    @GetMapping("/write")
    public String write() {
        return permissionFeignClient.write();
    }

    /**
     * 需要有read编码权限
     */
    @GetMapping("/read")
    public String read() {
        return permissionFeignClient.read();
    }

    /**
     * 需要有普通用户角色权限
     */
    @GetMapping("/user")
    public String user() {
        return permissionFeignClient.user();
    }

    /**
     * 需要有管理员角色权限
     */
    @GetMapping("/admin")
    public String admin() {
        return permissionFeignClient.admin(SecurityConstant.INNER);
    }
}