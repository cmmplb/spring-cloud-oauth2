package com.cmmplb.oauth2.resource.server.impl;

import com.cmmplb.oauth2.resource.server.bean.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author penglibo
 * @date 2024-07-26 17:33:04
 * @since jdk 1.8
 * 拓展token信息
 */
public class TokenEnhancerImpl implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        final Map<String, Object> additionalInfo = new HashMap<>(2);
        if (null != authentication.getUserAuthentication()) {
            User user = (User) authentication.getUserAuthentication().getPrincipal();
            // 添加用户id字段
            additionalInfo.put(User.COLUMN_USER_ID, user.getId());
        }
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
