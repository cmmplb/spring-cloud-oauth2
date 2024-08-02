package com.cmmplb.oauth2.resource.server.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author penglibo
 * @date 2024-07-30 17:04:50
 * @since jdk 1.8
 * 标记此注解的资源不需要登录
 */

@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface WithoutLogin {

    /**
     * 开启内部服务aop权限拦截
     */
    boolean value() default false;
}