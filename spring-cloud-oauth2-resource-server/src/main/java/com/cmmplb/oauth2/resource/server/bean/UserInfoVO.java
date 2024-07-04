package com.cmmplb.oauth2.resource.server.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * @author penglibo
 * @date 2024-07-02 17:35:47
 * @since jdk 1.8
 */

@Data
public class UserInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户信息
     */
    private UserVO user;

    /**
     * 角色编码集合
     */
    private Set<String> roles;

    /**
     * 权限编码标识集合
     */
    private Set<String> permissions;

    @Data
    public static class UserVO implements Serializable {

        /**
         * 主键
         */
        private Long id;

        /**
         * 用户账号
         */
        private String username;

        /**
         * 密码
         */
        private String password;

        /**
         * 手机号
         */
        private String mobile;
    }
}
