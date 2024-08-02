package com.cmmplb.oauth2.resource.server.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * @author penglibo
 * @date 2024-08-01 11:12:28
 * @since jdk 1.8
 * 授权码redis存储模式，参照内存存储模式，RedisTokenStore中redis操作
 * {@link org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices}
 * {@link org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore}
 */

@Slf4j
public class RedisAuthorizationCodeServicesImpl extends RandomValueAuthorizationCodeServices {

    private final RedisConnectionFactory connectionFactory;
    private final RedisTokenStoreSerializationStrategy serializationStrategy = new JdkSerializationStrategy();
    private static final boolean SPRING_DATA_REDIS_2_0 = ClassUtils.isPresent(
            "org.springframework.data.redis.connection.RedisStandaloneConfiguration",
            RedisTokenStore.class.getClassLoader());
    private Method redisConnectionSet20;

    /**
     * 授权码CODE缓存前缀
     */
    private static final String AUTHORIZATION_CODE_CACHE_PREFIX = "oauth:authorization:code:";

    /**
     * 授权码code缓存时效-5分钟
     */
    private static final long AUTHORIZATION_CODE_CACHE_TIME = 60 * 5;

    public RedisAuthorizationCodeServicesImpl(RedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
        if (SPRING_DATA_REDIS_2_0) {
            this.loadRedisConnectionMethods20();
        }
    }

    private void loadRedisConnectionMethods20() {
        this.redisConnectionSet20 = ReflectionUtils.findMethod(
                RedisConnection.class, "set", byte[].class, byte[].class);
    }

    @Override
    protected void store(String code, OAuth2Authentication authentication) {
        log.debug("store authorization code :{}", code);
        byte[] serializedAuth = serialize(authentication);
        byte[] serializedKey = serializeKey(get(code));
        try (RedisConnection conn = getConnection()) {
            conn.openPipeline();
            if (SPRING_DATA_REDIS_2_0) {
                try {
                    this.redisConnectionSet20.invoke(conn, serializedKey, serializedAuth);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                conn.set(serializedKey, serializedAuth);
                conn.expire(serializedKey, AUTHORIZATION_CODE_CACHE_TIME);
            }
            conn.closePipeline();
        }
    }

    @Override
    protected OAuth2Authentication remove(String code) {
        log.debug("remove authorization code :{}", code);
        byte[] serializedKey = serializeKey(get(code));
        try (RedisConnection conn = getConnection()) {
            byte[] bytes = conn.get(serializedKey);
            OAuth2Authentication oAuth2Authentication = deserializeAuthentication(bytes);
            if (null != oAuth2Authentication) {
                conn.del(serializeKey(get(code)));
                return oAuth2Authentication;
            }
        }
        return null;
    }

    private RedisConnection getConnection() {
        return connectionFactory.getConnection();
    }

    private byte[] serializeKey(String object) {
        return serialize(object);
    }

    private byte[] serialize(String string) {
        return serializationStrategy.serialize(string);
    }

    private byte[] serialize(Object object) {
        return serializationStrategy.serialize(object);
    }

    private OAuth2Authentication deserializeAuthentication(byte[] bytes) {
        return serializationStrategy.deserialize(bytes, OAuth2Authentication.class);
    }

    private String get(String code) {
        return AUTHORIZATION_CODE_CACHE_PREFIX + code;
    }
}
