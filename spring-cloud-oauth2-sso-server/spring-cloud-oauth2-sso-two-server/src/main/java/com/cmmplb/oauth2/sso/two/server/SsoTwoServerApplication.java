
package com.cmmplb.oauth2.sso.two.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;

/**
 * @author penglibo
 * @date 2024-07-02 16:56:28
 * @since jdk 1.8
 */

@EnableOAuth2Sso
@SpringBootApplication
public class SsoTwoServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsoTwoServerApplication.class, args);
    }

}
