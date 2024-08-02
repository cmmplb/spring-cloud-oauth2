package com.cmmplb.oauth2.system.server.api.client;

import com.cmmplb.oauth2.system.server.api.client.fallback.PermissionFeignClientFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author penglibo
 * @date 2024-07-31 11:29:37
 * @since jdk 1.8
 */

@FeignClient(contextId = "permission-feign", name = "spring-cloud-oauth2-system-server", path = "/permission", fallbackFactory = PermissionFeignClientFallBack.class)
public interface PermissionFeignClient {

    /**
     * 需要有write编码权限
     */
    @GetMapping("/write")
    String write();

    /**
     * 需要有read编码权限
     */
    @GetMapping("/read")
    String read();

    /**
     * 需要有普通用户角色权限
     */
    @GetMapping("/user")
    String user();

    /**
     * 需要有管理员角色权限
     */
    @GetMapping("/admin")
    String admin(@RequestHeader("Source") String source);

}
