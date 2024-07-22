package com.cmmplb.oauth2.resource.server.mobile;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author penglibo
 * @date 2024-07-19 15:59:48
 * @since jdk 1.8
 * {@link org.springframework.security.authentication.UsernamePasswordAuthenticationToken}
 */
public class MobileAuthenticationToken extends AbstractAuthenticationToken {

    // 认证信息，org.springframework.security.core.userdetails.UserDetails
    private final Object principal;

    // 验证码
    private Object credentials;

    // 未认证
    public MobileAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    // 已认证
    public MobileAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
