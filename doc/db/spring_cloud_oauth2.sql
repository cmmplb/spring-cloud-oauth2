-- ----------------------------
-- Table structure for sys_oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `sys_oauth_client_details`;
CREATE TABLE `sys_oauth_client_details`
(
    `id`                      bigint   NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `client_id`               varchar(32) COLLATE utf8mb4_general_ci   DEFAULT NULL COMMENT '客户端ID',
    `client_secret`           varchar(256) COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '客户端密钥',
    `resource_ids`            varchar(256) COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '资源列表',
    `scope`                   varchar(256) COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '域(server,all)',
    `authorized_grant_types`  varchar(256) COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '认证类型(password,refresh_token,mobile,client_credentials)',
    `web_server_redirect_uri` varchar(256) COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '重定向地址',
    `authorities`             varchar(256) COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '角色列表',
    `access_token_validity`   int                                      DEFAULT NULL COMMENT 'token有效期',
    `refresh_token_validity`  int                                      DEFAULT NULL COMMENT '刷新令牌有效期',
    `additional_information`  varchar(4096) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '令牌扩展字段JSON',
    `auto_approve`            varchar(4) COLLATE utf8mb4_general_ci    DEFAULT NULL COMMENT '是否自动放行(true)',
    `create_time`             datetime NOT NULL COMMENT '创建时间',
    `create_by`               bigint   NOT NULL COMMENT '创建人',
    `update_time`             datetime NOT NULL COMMENT '更新时间',
    `update_by`               bigint   NOT NULL COMMENT '更新人',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `UQ_CLIENT_ID` (`client_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='系统认证客户端信息表';

-- ----------------------------
-- Records of sys_oauth_client_details
-- ----------------------------
BEGIN;
INSERT INTO `sys_oauth_client_details` (`id`, `client_id`, `client_secret`, `resource_ids`, `scope`,
                                        `authorized_grant_types`, `web_server_redirect_uri`, `authorities`,
                                        `access_token_validity`, `refresh_token_validity`, `additional_information`,
                                        `auto_approve`, `create_time`, `create_by`, `update_time`, `update_by`)
VALUES (1, 'spring_cloud_oauth2', 'e10adc3949ba59abbe56e057f20f883e', NULL, 'server,all',
        'password,refresh_token,mobile,third_party,client_credentials', NULL, NULL, NULL, NULL, NULL, 'true',
        '2022-11-03 10:09:08', -999, '2022-11-03 10:09:10', -999);
COMMIT;

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`
(
    `id`          bigint                                  NOT NULL AUTO_INCREMENT COMMENT '主键',
    `code`        varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码',
    `create_time` datetime                                NOT NULL COMMENT '创建时间',
    `update_time` datetime                                NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='权限表';

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`
(
    `id`          bigint                                 NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`        varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
    `code`        varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码',
    `create_time` datetime                               NOT NULL COMMENT '创建时间',
    `update_time` datetime                               NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='系统角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`
(
    `id`            bigint   NOT NULL AUTO_INCREMENT COMMENT '主键',
    `role_id`       bigint   NOT NULL COMMENT '角色id',
    `permission_id` bigint   NOT NULL COMMENT '权限id',
    `create_time`   datetime NOT NULL COMMENT '创建时间',
    `create_by`     bigint   NOT NULL COMMENT '创建人',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `UQ_MENU_ID_ROLE_ID` (`permission_id`, `role_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='系统角色跟菜单关联表';

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `id`          bigint                                 NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username`    varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
    `password`    varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码(MD5加密)',
    `mobile`      varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号',
    `create_time` datetime                               NOT NULL COMMENT '创建时间',
    `update_time` datetime                               NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='系统用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` (`id`, `username`, `password`, `mobile`, `create_time`, `update_time`)
VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', '18888888888', '2022-11-03 10:19:44', '2022-11-03 10:19:45');
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`
(
    `id`          bigint   NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`     bigint   NOT NULL COMMENT '用户id',
    `role_id`     bigint   NOT NULL COMMENT '角色id',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `create_by`   bigint   NOT NULL COMMENT '创建人',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `UQ_USER_ID_ROLE_ID` (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='系统用户角色关联表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
