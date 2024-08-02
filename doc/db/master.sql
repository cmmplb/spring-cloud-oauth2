-- ----------------------------
-- Table structure for oauth_access_token
-- ----------------------------
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

-- ----------------------------
-- Records of oauth_access_token
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for oauth_approvals
-- ----------------------------
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

-- ----------------------------
-- Records of oauth_approvals
-- ----------------------------
BEGIN;
INSERT INTO `oauth_approvals` (`user_id`, `client_id`, `partner_key`, `scope`, `status`, `expires_at`,
                               `last_modified_at`)
VALUES ('admin', 'web', NULL, 'phone', 'APPROVED', '2024-08-26 10:55:19', '2024-07-26 10:55:19');
INSERT INTO `oauth_approvals` (`user_id`, `client_id`, `partner_key`, `scope`, `status`, `expires_at`,
                               `last_modified_at`)
VALUES ('admin', 'web', NULL, 'age', 'APPROVED', '2024-08-26 10:55:19', '2024-07-26 10:55:19');
INSERT INTO `oauth_approvals` (`user_id`, `client_id`, `partner_key`, `scope`, `status`, `expires_at`,
                               `last_modified_at`)
VALUES ('admin', 'web', NULL, 'username', 'APPROVED', '2024-08-26 10:55:19', '2024-07-26 10:55:19');
INSERT INTO `oauth_approvals` (`user_id`, `client_id`, `partner_key`, `scope`, `status`, `expires_at`,
                               `last_modified_at`)
VALUES ('admin', 'web', NULL, 'sex', 'APPROVED', '2024-08-26 10:55:19', '2024-07-26 10:55:19');
INSERT INTO `oauth_approvals` (`user_id`, `client_id`, `partner_key`, `scope`, `status`, `expires_at`,
                               `last_modified_at`)
VALUES ('admin', 'sso-one', NULL, 'username', 'APPROVED', '2024-08-29 11:37:08', '2024-07-29 11:37:08');
INSERT INTO `oauth_approvals` (`user_id`, `client_id`, `partner_key`, `scope`, `status`, `expires_at`,
                               `last_modified_at`)
VALUES ('admin', 'sso-one', NULL, 'phone', 'APPROVED', '2024-08-29 11:37:08', '2024-07-29 11:37:08');
INSERT INTO `oauth_approvals` (`user_id`, `client_id`, `partner_key`, `scope`, `status`, `expires_at`,
                               `last_modified_at`)
VALUES ('admin', 'sso-one', NULL, 'age', 'APPROVED', '2024-08-29 11:37:08', '2024-07-29 11:37:08');
INSERT INTO `oauth_approvals` (`user_id`, `client_id`, `partner_key`, `scope`, `status`, `expires_at`,
                               `last_modified_at`)
VALUES ('admin', 'sso-one', NULL, 'sex', 'APPROVED', '2024-08-29 11:37:08', '2024-07-29 11:37:08');
INSERT INTO `oauth_approvals` (`user_id`, `client_id`, `partner_key`, `scope`, `status`, `expires_at`,
                               `last_modified_at`)
VALUES ('admin', 'sso-two', NULL, 'sex', 'APPROVED', '2024-08-29 13:52:19', '2024-07-29 13:52:19');
INSERT INTO `oauth_approvals` (`user_id`, `client_id`, `partner_key`, `scope`, `status`, `expires_at`,
                               `last_modified_at`)
VALUES ('admin', 'sso-two', NULL, 'age', 'APPROVED', '2024-08-29 13:52:19', '2024-07-29 13:52:19');
INSERT INTO `oauth_approvals` (`user_id`, `client_id`, `partner_key`, `scope`, `status`, `expires_at`,
                               `last_modified_at`)
VALUES ('admin', 'sso-two', NULL, 'phone', 'APPROVED', '2024-08-29 13:52:19', '2024-07-29 13:52:19');
INSERT INTO `oauth_approvals` (`user_id`, `client_id`, `partner_key`, `scope`, `status`, `expires_at`,
                               `last_modified_at`)
VALUES ('admin', 'sso-two', NULL, 'username', 'APPROVED', '2024-08-29 13:52:19', '2024-07-29 13:52:19');
COMMIT;

-- ----------------------------
-- Table structure for oauth_client_details
-- ----------------------------
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

-- ----------------------------
-- Records of oauth_client_details
-- ----------------------------
BEGIN;
INSERT INTO `oauth_client_details` (`client_id`, `client_secret`, `resource_ids`, `scope`, `authorized_grant_types`,
                                    `web_server_redirect_uri`, `authorities`, `access_token_validity`,
                                    `refresh_token_validity`, `additional_information`, `autoapprove`, `create_time`)
VALUES ('sso-one', 'e10adc3949ba59abbe56e057f20f883e', NULL, 'username,phone,age,sex',
        'authorization_code,refresh_token', 'http://localhost:40000/login,http://127.0.0.1:40000/login', NULL, 43200,
        2592000, NULL, 'false', '2024-07-26 10:54:14');
INSERT INTO `oauth_client_details` (`client_id`, `client_secret`, `resource_ids`, `scope`, `authorized_grant_types`,
                                    `web_server_redirect_uri`, `authorities`, `access_token_validity`,
                                    `refresh_token_validity`, `additional_information`, `autoapprove`, `create_time`)
VALUES ('sso-two', 'e10adc3949ba59abbe56e057f20f883e', NULL, 'username,phone,age,sex',
        'authorization_code,refresh_token', 'http://localhost:50000/login,http://127.0.0.1:50000/login', NULL, 43200,
        2592000, NULL, 'false', '2024-07-26 10:54:14');
INSERT INTO `oauth_client_details` (`client_id`, `client_secret`, `resource_ids`, `scope`, `authorized_grant_types`,
                                    `web_server_redirect_uri`, `authorities`, `access_token_validity`,
                                    `refresh_token_validity`, `additional_information`, `autoapprove`, `create_time`)
VALUES ('system-server', 'e10adc3949ba59abbe56e057f20f883e', NULL, 'username,phone,age,sex', 'client_credentials', '',
        NULL, 43200, 2592000, NULL, 'false', '2024-07-26 10:54:14');
INSERT INTO `oauth_client_details` (`client_id`, `client_secret`, `resource_ids`, `scope`, `authorized_grant_types`,
                                    `web_server_redirect_uri`, `authorities`, `access_token_validity`,
                                    `refresh_token_validity`, `additional_information`, `autoapprove`, `create_time`)
VALUES ('web', 'e10adc3949ba59abbe56e057f20f883e', NULL, 'username,phone,age,sex',
        'client_credentials,password,implicit,authorization_code,refresh_token',
        'http://localhost:10000/auth/actuator/health,http://localhost:20000/actuator/health,http://localhost:18080/auth',
        NULL, 43200, 2592000, NULL, 'false', '2024-07-26 10:54:14');
COMMIT;

-- ----------------------------
-- Table structure for oauth_client_token
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_token`;
CREATE TABLE `oauth_client_token`
(
    `authentication_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键,根据当前的username(如果有),client_id与scope通过MD5加密',
    `token_id`          varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '从服务器端获取到的access_token的值',
    `token`             blob COMMENT 'OAuth2AccessToken对象序列化后的二进制数据',
    `user_name`         varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '登录时的用户名',
    `client_id`         varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '客户端id',
    `create_time`       timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`authentication_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Records of oauth_client_token
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for oauth_code
-- ----------------------------
DROP TABLE IF EXISTS `oauth_code`;
CREATE TABLE `oauth_code`
(
    `code`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '存储服务端系统生成的code的值(未加密)',
    `authentication` blob COMMENT 'AuthorizationRequestHolder对象序列化后的二进制数据',
    `create_time`    timestamp NOT NULL                                            DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='授权码信息表';

-- ----------------------------
-- Records of oauth_code
-- ----------------------------
BEGIN;
INSERT INTO `oauth_code` (`code`, `authentication`, `create_time`)
VALUES ('gQV2Ft',
        0xACED0005737200416F72672E737072696E676672616D65776F726B2E73656375726974792E6F61757468322E70726F76696465722E4F417574683241757468656E7469636174696F6EBD400B02166252130200024C000D73746F7265645265717565737474003C4C6F72672F737072696E676672616D65776F726B2F73656375726974792F6F61757468322F70726F76696465722F4F4175746832526571756573743B4C00127573657241757468656E7469636174696F6E7400324C6F72672F737072696E676672616D65776F726B2F73656375726974792F636F72652F41757468656E7469636174696F6E3B787200476F72672E737072696E676672616D65776F726B2E73656375726974792E61757468656E7469636174696F6E2E416273747261637441757468656E7469636174696F6E546F6B656ED3AA287E6E47640E0200035A000D61757468656E746963617465644C000B617574686F7269746965737400164C6A6176612F7574696C2F436F6C6C656374696F6E3B4C000764657461696C737400124C6A6176612F6C616E672F4F626A6563743B787000737200266A6176612E7574696C2E436F6C6C656374696F6E7324556E6D6F6469666961626C654C697374FC0F2531B5EC8E100200014C00046C6973747400104C6A6176612F7574696C2F4C6973743B7872002C6A6176612E7574696C2E436F6C6C656374696F6E7324556E6D6F6469666961626C65436F6C6C656374696F6E19420080CB5EF71E0200014C00016371007E00047870737200136A6176612E7574696C2E41727261794C6973747881D21D99C7619D03000149000473697A65787000000002770400000002737200426F72672E737072696E676672616D65776F726B2E73656375726974792E636F72652E617574686F726974792E53696D706C654772616E746564417574686F7269747900000000000002260200014C0004726F6C657400124C6A6176612F6C616E672F537472696E673B787074000A524F4C455F61646D696E7371007E000D740004726561647871007E000C707372003A6F72672E737072696E676672616D65776F726B2E73656375726974792E6F61757468322E70726F76696465722E4F41757468325265717565737400000000000000010200075A0008617070726F7665644C000B617574686F72697469657371007E00044C000A657874656E73696F6E7374000F4C6A6176612F7574696C2F4D61703B4C000B726564697265637455726971007E000E4C00077265667265736874003B4C6F72672F737072696E676672616D65776F726B2F73656375726974792F6F61757468322F70726F76696465722F546F6B656E526571756573743B4C000B7265736F7572636549647374000F4C6A6176612F7574696C2F5365743B4C000D726573706F6E7365547970657371007E0016787200386F72672E737072696E676672616D65776F726B2E73656375726974792E6F61757468322E70726F76696465722E426173655265717565737436287A3EA37169BD0200034C0008636C69656E74496471007E000E4C001172657175657374506172616D657465727371007E00144C000573636F706571007E00167870740003776562737200256A6176612E7574696C2E436F6C6C656374696F6E7324556E6D6F6469666961626C654D6170F1A5A8FE74F507420200014C00016D71007E00147870737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F400000000000067708000000080000000374000D726573706F6E73655F74797065740004636F646574000C72656469726563745F757269740025687474703A2F2F6C6F63616C686F73743A31383038302F617574683F747970653D636F6465740009636C69656E745F696471007E001978737200256A6176612E7574696C2E436F6C6C656374696F6E7324556E6D6F6469666961626C65536574801D92D18F9B80550200007871007E0009737200176A6176612E7574696C2E4C696E6B656448617368536574D86CD75A95DD2A1E020000787200116A6176612E7574696C2E48617368536574BA44859596B8B7340300007870770C000000103F4000000000000474000570686F6E65740003736578740003616765740008757365726E616D6578017371007E0026770C000000103F40000000000000787371007E001C3F40000000000000770800000010000000007871007E0021707371007E0026770C000000103F40000000000000787371007E0026770C000000103F4000000000000171007E001F787372004F6F72672E737072696E676672616D65776F726B2E73656375726974792E61757468656E7469636174696F6E2E557365726E616D6550617373776F726441757468656E7469636174696F6E546F6B656E00000000000002260200024C000B63726564656E7469616C7371007E00054C00097072696E636970616C71007E00057871007E0003017371007E00077371007E000B0000000277040000000271007E000F71007E00117871007E0033737200486F72672E737072696E676672616D65776F726B2E73656375726974792E7765622E61757468656E7469636174696F6E2E57656241757468656E7469636174696F6E44657461696C7300000000000002260200024C000D72656D6F74654164647265737371007E000E4C000973657373696F6E496471007E000E78707400093132372E302E302E317400203146373039353546424636464145453435304439373935373937343939343333707372002B636F6D2E636D6D706C622E6F61757468322E7265736F757263652E7365727665722E6265616E2E557365726F26775C7E966BA50200014C000269647400104C6A6176612F6C616E672F4C6F6E673B787200326F72672E737072696E676672616D65776F726B2E73656375726974792E636F72652E7573657264657461696C732E5573657200000000000002260200075A00116163636F756E744E6F6E457870697265645A00106163636F756E744E6F6E4C6F636B65645A001563726564656E7469616C734E6F6E457870697265645A0007656E61626C65644C000B617574686F72697469657371007E00164C000870617373776F726471007E000E4C0008757365726E616D6571007E000E7870010101017371007E0023737200116A6176612E7574696C2E54726565536574DD98509395ED875B0300007870737200466F72672E737072696E676672616D65776F726B2E73656375726974792E636F72652E7573657264657461696C732E5573657224417574686F72697479436F6D70617261746F720000000000000226020000787077040000000271007E000F71007E0011787074000561646D696E7372000E6A6176612E6C616E672E4C6F6E673B8BE490CC8F23DF0200014A000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B02000078700000000000000001,
        '2024-08-01 14:18:34');
COMMIT;

-- ----------------------------
-- Table structure for oauth_refresh_token
-- ----------------------------
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

-- ----------------------------
-- Records of oauth_refresh_token
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for permission
-- ----------------------------
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

-- ----------------------------
-- Records of permission
-- ----------------------------
BEGIN;
INSERT INTO `permission` (`id`, `code`, `create_time`, `create_by`, `update_time`, `update_by`)
VALUES (1, 'read', '2024-07-18 18:02:55', 1, '2024-07-18 18:08:07', 1);
INSERT INTO `permission` (`id`, `code`, `create_time`, `create_by`, `update_time`, `update_by`)
VALUES (2, 'write', '2024-07-18 18:08:30', 1, '2024-07-18 18:08:33', 1);
COMMIT;

-- ----------------------------
-- Table structure for role
-- ----------------------------
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

-- ----------------------------
-- Records of role
-- ----------------------------
BEGIN;
INSERT INTO `role` (`id`, `name`, `code`, `create_time`, `create_by`, `update_time`, `update_by`)
VALUES (1, '管理员', 'admin', '2024-07-18 18:02:38', 1, '2024-07-19 09:44:29', 1);
INSERT INTO `role` (`id`, `name`, `code`, `create_time`, `create_by`, `update_time`, `update_by`)
VALUES (2, '普通用户', 'user', '2024-07-18 18:09:41', 1, '2024-07-18 18:09:41', 1);
COMMIT;

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
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

-- ----------------------------
-- Records of role_permission
-- ----------------------------
BEGIN;
INSERT INTO `role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_by`)
VALUES (1, 1, 1, '2024-07-18 18:03:07', 1);
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
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

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` (`id`, `username`, `password`, `mobile`, `create_time`, `update_time`)
VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', '18888888888', '2022-11-03 10:19:44', '2022-11-03 10:19:45');
COMMIT;

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
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

-- ----------------------------
-- Records of user_role
-- ----------------------------
BEGIN;
INSERT INTO `user_role` (`id`, `user_id`, `role_id`, `create_time`, `create_by`)
VALUES (1, 1, 1, '2024-07-18 18:02:22', 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
