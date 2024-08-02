package com.cmmplb.oauth2.resource.server.impl;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import javax.sql.DataSource;

/**
 * @author penglibo
 * @date 2024-08-01 11:11:06
 * @since jdk 1.8
 */

public class RedisClientDetailsServiceImpl extends JdbcClientDetailsService {

    /**
     * oauth 客户端信息
     */
    public static final String CLIENT_DETAILS_KEY = "oauth:client:details";

    public RedisClientDetailsServiceImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    @Cacheable(value = CLIENT_DETAILS_KEY, key = "#clientId", unless = "#result == null")
    public ClientDetails loadClientByClientId(String clientId) {
        return super.loadClientByClientId(clientId);
    }
}