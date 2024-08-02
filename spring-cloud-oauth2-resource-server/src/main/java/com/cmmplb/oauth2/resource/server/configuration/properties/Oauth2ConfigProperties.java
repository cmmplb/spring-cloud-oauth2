package com.cmmplb.oauth2.resource.server.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * @author penglibo
 * @date 2024-07-31 16:52:09
 * @since jdk 1.8
 * oauth2相关存储配置
 */

@Data
@ConfigurationProperties(prefix = "security.oauth2")
public class Oauth2ConfigProperties {

    /**
     * 令牌存储类型
     */
    private TokenStoreType tokenStoreType = TokenStoreType.IN_MEMORY;

    /**
     * Jwt密钥配置
     */
    private JwtKey jwtKey = new JwtKey();

    /**
     * 是否使用DefaultTokenServices
     */
    private boolean defaultTokenServices = false;

    /**
     * 用户信息类型
     */
    private UserDetailsServiceType userDetailsServiceType = UserDetailsServiceType.IN_MEMORY;

    /**
     * 客户端信息类型
     */
    private ClientDetailsServiceType clientDetailsServiceType = ClientDetailsServiceType.IN_MEMORY;

    /**
     * 授权码类型
     */
    private AuthorizationCodeServicesType authorizationCodeServicesType = AuthorizationCodeServicesType.IN_MEMORY;

    /**
     * 授权信息类型
     */
    private ApprovalStoreType approvalStoreType = ApprovalStoreType.IN_MEMORY;

    /**
     * 内存客户端配置
     */
    private List<BaseClientDetails> clients = new ArrayList<>();

    /**
     * 内存用户配置
     */
    private List<BaseUserDetails> users = new ArrayList<>();

    /**
     * Jwt密钥配置
     */
    @Data
    public static class JwtKey {

        /**
         * 是否为授权服务
         */
        private boolean authorized = false;

        /**
         * 认证服务配置
         */
        private Authorization authorization;

        /**
         * 客户端配置
         */
        private Client client;

        @Data
        public static class Authorization {

            /**
             * 是否使用对称加密方式
             */
            private boolean symmetric = true;

            /**
             * 对称加密签名密钥
             */
            private String signingKey;

            /**
             * 非对称加密密钥路径
             */
            private String keyPath;

            /**
             * 非对称加密密钥密码
             */
            private String pass;

            /**
             * 非对称加密密钥别名
             */
            private String alias;
        }

        @Data
        public static class Client {

            /**
             * 是否使用对称加密方式
             */
            private boolean symmetric = true;

            /**
             * 解密使用的对称加密签名密钥
             */
            private String signingKey;

            /**
             * 公钥路径
             */
            private String publicKeyFilePath;
        }
    }

    /**
     * 令牌存储类型
     */
    public enum TokenStoreType {

        IN_MEMORY,

        JDBC,

        JWT,

        REDIS
    }

    /**
     * 客户端信息类型
     */
    public enum ClientDetailsServiceType {

        IN_MEMORY,

        JDBC,

        /**
         * 经过jdbc查询之后缓存到redis
         */
        REDIS
    }

    /**
     * 授权码类型
     */
    public enum AuthorizationCodeServicesType {

        IN_MEMORY,

        JDBC,

        REDIS
    }

    /**
     * 授权信息类型
     */
    public enum ApprovalStoreType {

        IN_MEMORY,

        JDBC,

        Token
    }

    /**
     * 用户信息类型
     */
    public enum UserDetailsServiceType {

        IN_MEMORY,

        JDBC
    }

    /**
     * 内存用户配置
     */
    @Data
    public static class BaseUserDetails {

        /**
         * 用户名
         */
        private String username;

        /**
         * 密码
         */
        private String password;

        /**
         * 角色
         */
        private List<String> roles;

        /**
         * 权限
         */
        private List<String> authorities;

        /**
         * 账号是否过期
         */
        private boolean accountNonExpired = false;

        /**
         * 账号是否锁定
         */
        private boolean accountNonLock = false;

        /**
         * 凭证是否过期
         */
        private boolean credentialsNonExpired = false;

        /**
         * 账号是否禁用
         */
        private boolean disable = false;
    }
}