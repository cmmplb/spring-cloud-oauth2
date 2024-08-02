
package com.cmmplb.oauth2.auth.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author penglibo
 * @date 2024-07-02 11:46:55
 * @since jdk 1.8
 */

@EnableFeignClients("com.cmmplb.oauth2.system.server.api")
@SpringBootApplication
public class AuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }

}
