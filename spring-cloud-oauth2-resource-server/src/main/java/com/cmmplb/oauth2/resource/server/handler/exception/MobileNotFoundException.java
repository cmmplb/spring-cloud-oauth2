package com.cmmplb.oauth2.resource.server.handler.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author penglibo
 * @date 2024-07-22 14:26:42
 * @since jdk 1.8
 * {@link org.springframework.security.core.userdetails.UsernameNotFoundException}
 */
public class MobileNotFoundException extends AuthenticationException {

    /**
     * Constructs a <code>UsernameNotFoundException</code> with the specified message.
     * @param msg the detail message.
     */
    public MobileNotFoundException(String msg) {
        super(msg);
    }

    /**
     * Constructs a {@code UsernameNotFoundException} with the specified message and root
     * cause.
     * @param msg   the detail message.
     * @param cause root cause
     */
    public MobileNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
