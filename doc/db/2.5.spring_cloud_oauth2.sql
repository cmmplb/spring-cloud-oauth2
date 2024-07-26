DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details`
(
    `id`                      bigint   NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `client_id`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   DEFAULT NULL COMMENT '客户端ID',
    `client_secret`           varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '客户端密钥',
    `resource_ids`            varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '资源列表',
    `scope`                   varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '域(server,all)',
    `authorized_grant_types`  varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '认证类型(password,refresh_token,mobile,client_credentials)',
    `web_server_redirect_uri` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '重定向地址',
    `authorities`             varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '角色列表',
    `access_token_validity`   int                                                            DEFAULT NULL COMMENT 'token有效期',
    `refresh_token_validity`  int                                                            DEFAULT NULL COMMENT '刷新令牌有效期',
    `additional_information`  varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '令牌扩展字段JSON',
    `autoapprove`             varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci    DEFAULT NULL COMMENT '是否自动放行(true)',
    `create_time`             datetime NOT NULL                                              DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`               bigint   NOT NULL COMMENT '创建人',
    `update_time`             datetime NOT NULL                                              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_by`               bigint   NOT NULL COMMENT '更新人',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `UQ_CLIENT_ID` (`client_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='认证客户端信息表';

BEGIN;
INSERT INTO `oauth_client_details` (`id`, `client_id`, `client_secret`, `resource_ids`, `scope`,
                                    `authorized_grant_types`, `web_server_redirect_uri`, `authorities`,
                                    `access_token_validity`, `refresh_token_validity`, `additional_information`,
                                    `autoapprove`, `create_time`, `create_by`, `update_time`, `update_by`)
VALUES (1, 'web', 'e10adc3949ba59abbe56e057f20f883e', NULL, 'username,phone,age,sex',
        'client_credentials,password,implicit,authorization_code,refresh_token',
        'http://localhost:10000/auth/actuator/health,http://localhost:20000/actuator/health,http://localhost:18080/auth',
        NULL, 36000, 43200, NULL, 'false', '2024-07-18 16:27:40', 1, '2024-07-18 16:49:36', 1);
COMMIT;

DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`
(
    `id`          bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '主键',
    `code`        varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码',
    `create_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`   bigint                                                        NOT NULL COMMENT '创建人',
    `update_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_by`   bigint                                                        NOT NULL COMMENT '更新人',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='权限表';

BEGIN;
INSERT INTO `permission` (`id`, `code`, `create_time`, `create_by`, `update_time`, `update_by`)
VALUES (1, 'read', '2024-07-18 18:02:55', 1, '2024-07-18 18:08:07', 1);
INSERT INTO `permission` (`id`, `code`, `create_time`, `create_by`, `update_time`, `update_by`)
VALUES (2, 'write', '2024-07-18 18:08:30', 1, '2024-07-18 18:08:33', 1);
COMMIT;

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`
(
    `id`          bigint                                                       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
    `code`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码',
    `create_time` datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`   bigint                                                       NOT NULL COMMENT '创建人',
    `update_time` datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_by`   bigint                                                       NOT NULL COMMENT '更新人',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='角色表';

BEGIN;
INSERT INTO `role` (`id`, `name`, `code`, `create_time`, `create_by`, `update_time`, `update_by`)
VALUES (1, '管理员', 'admin', '2024-07-18 18:02:38', 1, '2024-07-19 09:44:29', 1);
INSERT INTO `role` (`id`, `name`, `code`, `create_time`, `create_by`, `update_time`, `update_by`)
VALUES (2, '普通用户', 'user', '2024-07-18 18:09:41', 1, '2024-07-18 18:09:41', 1);
COMMIT;

DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission`
(
    `id`            bigint   NOT NULL AUTO_INCREMENT COMMENT '主键',
    `role_id`       bigint   NOT NULL COMMENT '角色id',
    `permission_id` bigint   NOT NULL COMMENT '权限id',
    `create_time`   datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`     bigint   NOT NULL COMMENT '创建人',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `UQ_MENU_ID_ROLE_ID` (`permission_id`, `role_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='角色菜单关联表';

BEGIN;
INSERT INTO `role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_by`)
VALUES (1, 1, 1, '2024-07-18 18:03:07', 1);
COMMIT;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`          bigint                                                       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
    `password`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码(MD5加密)',
    `mobile`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '手机号',
    `create_time` datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='用户表';

BEGIN;
INSERT INTO `user` (`id`, `username`, `password`, `mobile`, `create_time`, `update_time`)
VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', '18888888888', '2022-11-03 10:19:44', '2022-11-03 10:19:45');
COMMIT;

DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`
(
    `id`          bigint   NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`     bigint   NOT NULL COMMENT '用户id',
    `role_id`     bigint   NOT NULL COMMENT '角色id',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`   bigint   NOT NULL COMMENT '创建人',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `UQ_USER_ID_ROLE_ID` (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='用户角色关联表';

BEGIN;
INSERT INTO `user_role` (`id`, `user_id`, `role_id`, `create_time`, `create_by`)
VALUES (1, 1, 1, '2024-07-18 18:02:22', 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
