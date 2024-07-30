package com.cmmplb.oauth2.resource.server.utils;

import com.cmmplb.oauth2.resource.server.bean.User;
import com.cmmplb.oauth2.resource.server.handler.exception.BusinessException;
import com.cmmplb.oauth2.resource.server.result.HttpCodeEnum;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author penglibo
 * @date 2024-07-29 10:22:01
 * @since jdk 1.8
 */
public class SecurityUtil {

    /**
     * 获取用户id
     */
    public static Long getUserId() {
        User user = getUser();
        if (user == null) {
            // 这里看业务情况是返回空、抛异常还是指定一个系统用户信息
            throw new BusinessException(HttpCodeEnum.UNAUTHORIZED);
        }
        return user.getId();
    }

    /**
     * 获取用户名
     */
    public static String getUsername() {
        User user = getUser();
        if (user == null) {
            // 这里看业务情况是返回空、抛异常还是指定一个系统用户信息
            // throw new BusinessException(HttpCodeEnum.UNAUTHORIZED);
            return null;
        }
        return user.getUsername();
    }

    /**
     * 获取用户
     */
    public static User getUser() {
        Authentication authentication = getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            return (User) principal;
        }
        return null;
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
