package com.cmmplb.oauth2.system.server.controller;

import com.cmmplb.oauth2.resource.server.bean.UserInfoVO;
import com.cmmplb.oauth2.system.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author penglibo
 * @date 2024-07-02 17:40:32
 * @since jdk 1.8
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/health")
    public String health() {
        return "ok";
    }

    // @AuthIgnore
    // @WithoutLogin
    @GetMapping("/info/{username}")
    public UserInfoVO getByUsername(@PathVariable String username) {
        return userService.getByUsername(username);
    }

    // @AuthIgnore
    @GetMapping("/info/mobile/{mobile}")
    public UserInfoVO getByMobile(@PathVariable String mobile) {
        return userService.getByMobile(mobile);
    }

    @GetMapping("/info")
    public UserInfoVO getInfo() {
        return userService.getByUsername("SecurityUtil.getUsername()");
    }
}
