package com.cmmplb.oauth2.resource.server.handler;

import com.alibaba.fastjson.JSON;
import com.cmmplb.oauth2.resource.server.result.HttpCodeEnum;
import com.cmmplb.oauth2.resource.server.result.Result;
import com.cmmplb.oauth2.resource.server.result.ResultUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.common.exceptions.InsufficientScopeException;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author penglibo
 * @date 2024-07-18 16:23:12
 * @since jdk 1.8
 * 权限不足处理器，覆盖默认的OAuth2AccessDeniedHandler包装失败信息
 */

@Slf4j
public class AccessDeniedHandler extends OAuth2AccessDeniedHandler {

    @Override
    @SneakyThrows
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException authException) {
        log.error(HttpCodeEnum.FORBIDDEN + " {}", request.getRequestURI());
        Throwable cause = authException.getCause();
        Result<?> custom = ResultUtil.custom(HttpCodeEnum.FORBIDDEN);
        if (cause instanceof InsufficientScopeException) {
            InsufficientScopeException e = (InsufficientScopeException) cause;
            custom.setMsg("资源权限范围不足:" + e.getAdditionalInformation());
        }
        response.setCharacterEncoding("utf8");
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpCodeEnum.FORBIDDEN.getCode());
        PrintWriter printWriter = response.getWriter();
        printWriter.append(JSON.toJSONString(custom));
    }

}
