-- 每张表添加了扩展字段create_time

-- ------------------------------------------------
-- 客户端令牌信息表 ----------------------------------
-- ------------------------------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details`
(
    `client_id`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键,客户端id',
    `client_secret`           varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         DEFAULT NULL COMMENT '客户端密钥',
    `resource_ids`            varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         DEFAULT NULL COMMENT '客户端所能访问的资源id集合',
    `scope`                   varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         DEFAULT NULL COMMENT '指定客户端申请的权限范围',
    `authorized_grant_types`  varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         DEFAULT NULL COMMENT '指定客户端支持的grant_type(authorization_code,password,refresh_token,implicit,client_credentials)',
    `web_server_redirect_uri` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         DEFAULT NULL COMMENT '客户端的重定向URI,多个用逗号(,)分隔',
    `authorities`             varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         DEFAULT NULL COMMENT '指定客户端所拥有的SpringSecurity的权限值',
    `access_token_validity`   int                                                                   DEFAULT NULL COMMENT '设定客户端的access_token的有效时间值',
    `refresh_token_validity`  int                                                                   DEFAULT NULL COMMENT '设定客户端的refresh_token的有效时间值',
    `additional_information`  varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        DEFAULT NULL COMMENT '令牌扩展字段JSON',
    `autoapprove`             varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci           DEFAULT NULL COMMENT '设置用户是否自动Approval操作,默认值为‘false’',
    `create_time`             datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`client_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='认证客户端信息表';

INSERT INTO `oauth_client_details` (`client_id`, `client_secret`, `resource_ids`, `scope`,
                                    `authorized_grant_types`, `web_server_redirect_uri`, `authorities`,
                                    `access_token_validity`, `refresh_token_validity`, `additional_information`,
                                    `autoapprove`)
VALUES ('web', 'e10adc3949ba59abbe56e057f20f883e', NULL, 'username,phone,age,sex',
        'client_credentials,password,implicit,authorization_code,refresh_token',
        'http://localhost:10000/auth/actuator/health,http://localhost:20000/actuator/health,http://localhost:18080/auth',
        NULL, 36000, 43200, NULL, 'false');

-- ------------------------------------------------
-- 客户端令牌信息表 ----------------------------------
-- ------------------------------------------------
DROP TABLE IF EXISTS `oauth_client_token`;
CREATE TABLE `oauth_client_token`
(
    `authentication_id` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键,根据当前的username(如果有),client_id与scope通过MD5加密',
    `token_id`          varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '从服务器端获取到的access_token的值',
    `token`             blob COMMENT 'OAuth2AccessToken对象序列化后的二进制数据',
    `user_name`         varchar(128) COLLATE utf8mb4_general_ci                       DEFAULT NULL COMMENT '登录时的用户名',
    `client_id`         varchar(128) COLLATE utf8mb4_general_ci                       DEFAULT NULL COMMENT '客户端id',
    `create_time`       timestamp                               NOT NULL              DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`authentication_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- ------------------------------------------------
-- 授权令牌信息表 ----------------------------------
-- ------------------------------------------------
DROP TABLE IF EXISTS `oauth_access_token`;
CREATE TABLE `oauth_access_token`
(
    `token_id`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'access_token的值MD5加密',
    `token`             blob COMMENT 'OAuth2AccessToken对象序列化后的二进制数据',
    `authentication_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '其值是根据当前的username(如果有),client_id与scope通过MD5加密生成',
    `user_name`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户名',
    `client_id`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '客户端id',
    `authentication`    blob COMMENT '存储将OAuth2Authentication对象序列化后的二进制数据',
    `refresh_token`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'refresh_token的值MD5加密',
    `create_time`       timestamp NOT NULL                                            DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY `authentication_id` (`authentication_id`),
    KEY `token_id_index` (`token_id`),
    KEY `authentication_id_index` (`authentication_id`),
    KEY `user_name_index` (`user_name`),
    KEY `client_id_index` (`client_id`),
    KEY `refresh_token_index` (`refresh_token`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='授权令牌信息表';

-- ------------------------------------------------
-- 刷新令牌信息表 ----------------------------------
-- ------------------------------------------------
DROP TABLE IF EXISTS `oauth_refresh_token`;
CREATE TABLE `oauth_refresh_token`
(
    `token_id`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'refresh_token的值MD5加密',
    `token`          blob COMMENT 'OAuth2RefreshToken对象序列化后的二进制数据',
    `authentication` blob COMMENT 'OAuth2Authentication对象序列化后的二进制数据',
    `create_time`    timestamp NOT NULL                                            DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY `token_id_index` (`token_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='刷新令牌信息表';

-- ------------------------------------------------
-- 授权码信息表 ----------------------------------
-- ------------------------------------------------
DROP TABLE IF EXISTS `oauth_code`;
CREATE TABLE `oauth_code`
(
    `code`           varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '存储服务端系统生成的code的值(未加密)',
    `authentication` blob COMMENT 'AuthorizationRequestHolder对象序列化后的二进制数据',
    `create_time`    timestamp NOT NULL                      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='授权码信息表';

-- ------------------------------------------------
-- 授权信息表 ----------------------------------
-- ------------------------------------------------
DROP TABLE IF EXISTS `oauth_approvals`;
CREATE TABLE `oauth_approvals`
(
    `user_id`          varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户账号-对应username字段',
    `client_id`        varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '客户端id',
    `partner_key`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '翻译是合作伙伴密钥,不知道有什么用0.0',
    `scope`            varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '作用域范围',
    `status`           varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '状态:APPROVED-同意;DENIED-拒绝;',
    `expires_at`       datetime                                                      DEFAULT NULL COMMENT '有效期至',
    `last_modified_at` datetime                                                      DEFAULT NULL COMMENT '最后修改时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='授权信息表';