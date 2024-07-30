INSERT INTO `spring_cloud_oauth2`.`oauth_client_details` (`client_id`, `client_secret`, `resource_ids`, `scope`,
                                                          `authorized_grant_types`, `web_server_redirect_uri`,
                                                          `authorities`, `access_token_validity`,
                                                          `refresh_token_validity`, `additional_information`,
                                                          `autoapprove`, `create_time`)
VALUES ('sso-one', 'e10adc3949ba59abbe56e057f20f883e', NULL, 'username,phone,age,sex',
        'authorization_code,refresh_token', 'http://localhost:40000/login,http://127.0.0.1:40000/login', NULL, 36000,
        43200, NULL, 'false', '2024-07-26 10:54:14');
INSERT INTO `spring_cloud_oauth2`.`oauth_client_details` (`client_id`, `client_secret`, `resource_ids`, `scope`,
                                                          `authorized_grant_types`, `web_server_redirect_uri`,
                                                          `authorities`, `access_token_validity`,
                                                          `refresh_token_validity`, `additional_information`,
                                                          `autoapprove`, `create_time`)
VALUES ('sso-two', 'e10adc3949ba59abbe56e057f20f883e', NULL, 'username,phone,age,sex',
        'authorization_code,refresh_token', 'http://localhost:50000/login,http://127.0.0.1:50000/login', NULL, 36000,
        43200, NULL, 'false', '2024-07-26 10:54:14');
INSERT INTO `spring_cloud_oauth2`.`oauth_client_details` (`client_id`, `client_secret`, `resource_ids`, `scope`,
                                                          `authorized_grant_types`, `web_server_redirect_uri`,
                                                          `authorities`, `access_token_validity`,
                                                          `refresh_token_validity`, `additional_information`,
                                                          `autoapprove`, `create_time`)
VALUES ('system-server', 'e10adc3949ba59abbe56e057f20f883e', NULL, 'username,phone,age,sex', 'client_credentials', '',
        NULL, 36000, 43200, NULL, 'false', '2024-07-26 10:54:14');