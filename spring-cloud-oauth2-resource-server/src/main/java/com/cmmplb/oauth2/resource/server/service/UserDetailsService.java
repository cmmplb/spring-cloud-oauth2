package com.cmmplb.oauth2.resource.server.service;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author penglibo
 * @date 2024-07-19 17:14:29
 * @since jdk 1.8
 */

public interface UserDetailsService extends org.springframework.security.core.userdetails.UserDetailsService {

    UserDetails loadUserByMobile(String username);
}