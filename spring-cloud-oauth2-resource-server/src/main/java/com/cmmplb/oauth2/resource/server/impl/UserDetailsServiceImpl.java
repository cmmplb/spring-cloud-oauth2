package com.cmmplb.oauth2.resource.server.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.cmmplb.oauth2.resource.server.bean.User;
import com.cmmplb.oauth2.resource.server.bean.UserInfoVO;
import com.cmmplb.oauth2.resource.server.constants.SecurityConstant;
import com.cmmplb.oauth2.resource.server.handler.exception.BusinessException;
import com.cmmplb.oauth2.resource.server.handler.exception.MobileNotFoundException;
import com.cmmplb.oauth2.resource.server.result.HttpCodeEnum;
import com.cmmplb.oauth2.resource.server.result.Result;
import com.cmmplb.oauth2.resource.server.service.UserDetailsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;

/**
 * @author penglibo
 * @date 2024-07-18 16:55:08
 * @since jdk 1.8
 */

@Slf4j
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final RestTemplate restTemplate;

    public static final String DEFAULT_ROLE_PREFIX = "ROLE_";

    public static final String SYSTEM_USER_INFO_URL = "http://spring-cloud-oauth2-system-server/user/info/";

    public static final String SYSTEM_USER_INFO_URL_MOBILE = "http://spring-cloud-oauth2-system-server/user/info/mobile/";

    @Override
    public UserDetails loadUserByUsername(String username) {
        Result<UserInfoVO> result = result(SYSTEM_USER_INFO_URL + username);
        if (result == null || result.getData() == null) {
            throw new UsernameNotFoundException(HttpCodeEnum.BAD_CREDENTIALS.getMessage());
        }
        return getUserDetails(username, result.getData());
    }

    @Override
    public UserDetails loadUserByMobile(String mobile) {
        Result<UserInfoVO> result = result(SYSTEM_USER_INFO_URL_MOBILE + mobile);
        if (result == null || result.getData() == null) {
            throw new MobileNotFoundException(HttpCodeEnum.MOBILE_NOT_FOUND.getMessage());
        }
        return getUserDetails(mobile, result.getData());
    }

    private Result<UserInfoVO> result(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstant.SOURCE, SecurityConstant.INNER);
        if (headers.getContentType() == null) {
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        }
        JSONObject res = restTemplate.exchange(url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                JSONObject.class).getBody();
        if (null == res) {
            log.error("服务调用失败");
            throw new BusinessException();
        }
        return res.toJavaObject(new TypeReference<Result<UserInfoVO>>() {
        });
    }

    private UserDetails getUserDetails(String username, UserInfoVO info) {
        Set<String> dbAuthsSet = new HashSet<>();
        if (!CollectionUtils.isEmpty(info.getRoles())) {
            // 获取角色
            for (String role : info.getRoles()) {
                // 角色编码添加前缀
                dbAuthsSet.add(DEFAULT_ROLE_PREFIX + role);
            }
            // 获取权限
            dbAuthsSet.addAll(info.getPermissions());
        }
        Set<SimpleGrantedAuthority> authorityArrayList = new HashSet<>();
        if (!CollectionUtils.isEmpty(dbAuthsSet)) {
            dbAuthsSet.forEach(auth -> authorityArrayList.add(new SimpleGrantedAuthority(auth)));
        }
        UserInfoVO.UserVO user = info.getUser();
        // org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator
        // redis的key生成规则是这个values里面的三个字段，client_id、scope、username，防止账号密码登录和手机号登录使用缓存同一个，手机号username为mobile
        // 扩展id字段
        return new User(user.getId(), username, user.getPassword(), true, true,
                true, true, authorityArrayList);
    }
}