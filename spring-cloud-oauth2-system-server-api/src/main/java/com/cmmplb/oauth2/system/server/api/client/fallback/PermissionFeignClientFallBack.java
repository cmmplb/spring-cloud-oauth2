package com.cmmplb.oauth2.system.server.api.client.fallback;

import com.cmmplb.oauth2.system.server.api.client.PermissionFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * @author penglibo
 * @date 2024-07-31 11:30:02
 * @since jdk 1.8
 * 服务熔断降级处理
 */

@Slf4j
public class PermissionFeignClientFallBack implements FallbackFactory<PermissionFeignClient> {
    @Override
    public PermissionFeignClient create(Throwable cause) {
        log.error("服务调用失败:{}", cause.getMessage());
        return new PermissionFeignClient() {

            @Override
            public String write() {
                return "error";
            }

            @Override
            public String read() {
                return "error";
            }

            @Override
            public String user() {
                return "error";
            }

            @Override
            public String admin(String source) {
                return "error";
            }
        };
    }
}