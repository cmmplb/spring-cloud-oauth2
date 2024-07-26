package com.cmmplb.oauth2.resource.server.bean;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

/**
 * @author penglibo
 * @date 2024-07-26 17:01:36
 * @since jdk 1.8
 * 扩展用户信息
 */

@Getter
public class User extends org.springframework.security.core.userdetails.User {

    /**
     * 主键
     */
    private final Long id;

    // 用户其他扩展字段..

    public User(Long id, String username, String password, boolean enabled,
                boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
                Collection<? extends SimpleGrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
    }

    public static final String COLUMN_USER_ID = "user_id";
}
