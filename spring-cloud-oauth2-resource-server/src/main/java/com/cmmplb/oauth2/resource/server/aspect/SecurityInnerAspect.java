package com.cmmplb.oauth2.resource.server.aspect;

import com.cmmplb.oauth2.resource.server.annotation.WithoutLogin;
import com.cmmplb.oauth2.resource.server.constants.SecurityConstant;
import com.cmmplb.oauth2.resource.server.handler.exception.BusinessException;
import com.cmmplb.oauth2.resource.server.result.HttpCodeEnum;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author penglibo
 * @date 2024-07-31 15:24:58
 * @within自定义注解标注的类下所有的方法都会进入切面的方法
 * @annotation自定义注解标注的方法会进入切面
 * @since jdk 1.8
 */

@Aspect
public class SecurityInnerAspect {

    @Autowired
    private HttpServletRequest request;

    @SneakyThrows
    @Around("@within(com.cmmplb.oauth2.resource.server.annotation.WithoutLogin)")
    public Object around(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取方法上的注解
        WithoutLogin withoutLogin = signature.getMethod().getAnnotation(WithoutLogin.class);
        if (null == withoutLogin) {
            // 获取类上的注解
            withoutLogin = AnnotationUtils.findAnnotation(joinPoint.getTarget().getClass(), WithoutLogin.class);
        }
        if (null != withoutLogin && withoutLogin.value() && !SecurityConstant.INNER.equals(request.getHeader(SecurityConstant.SOURCE))) {
            throw new BusinessException(HttpCodeEnum.FORBIDDEN.getCode(), HttpCodeEnum.FORBIDDEN.getCode(), "内部服务,外部禁止访问-未授权");
        }
        return joinPoint.proceed();
    }
}