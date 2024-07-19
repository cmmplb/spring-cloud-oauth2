package com.cmmplb.oauth2.resource.server.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.cmmplb.oauth2.resource.server.bean.UserInfoVO;
import com.cmmplb.oauth2.resource.server.handler.exception.BusinessException;
import com.cmmplb.oauth2.resource.server.result.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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

    private static final String BAD_CREDENTIALS = "用户名或密码错误";

    @Override
    public UserDetails loadUserByUsername(String username) {
        HttpHeaders headers = new HttpHeaders();
        if (headers.getContentType() == null) {
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        }
        JSONObject res = restTemplate.exchange(SYSTEM_USER_INFO_URL + username,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                JSONObject.class).getBody();
        if (null == res) {
            log.error("服务调用失败");
            // 用户不存在一般是用户名或密码错误，提示用户信息不存在的话不安全。
            throw new BusinessException(BAD_CREDENTIALS);
        }
        Result<UserInfoVO> result = res.toJavaObject(new TypeReference<Result<UserInfoVO>>() {
        });
        if (result == null || result.getCode() != HttpStatus.OK.value() || result.getData() == null) {
            throw new BusinessException(BAD_CREDENTIALS);
        }
        return getUserDetails(result.getData());
    }

    private UserDetails getUserDetails(UserInfoVO info) {
        Set<String> dbAuthsSet = new HashSet<>();
        if (!CollectionUtils.isEmpty(info.getRoles())) {
            // 获取角色
            for (String role : info.getRoles()) {
                // 角色编码添加前缀
                dbAuthsSet.add(this.DEFAULT_ROLE_PREFIX + role);
            }
            // 获取权限
            dbAuthsSet.addAll(info.getPermissions());
        }
        Set<SimpleGrantedAuthority> authorityArrayList = new HashSet<>();
        if (!CollectionUtils.isEmpty(dbAuthsSet)) {
            dbAuthsSet.forEach(auth -> authorityArrayList.add(new SimpleGrantedAuthority(auth)));
        }
        UserInfoVO.UserVO user = info.getUser();
        return new User(user.getUsername(), user.getPassword(), true, true,
                true, true, authorityArrayList);
    }
}