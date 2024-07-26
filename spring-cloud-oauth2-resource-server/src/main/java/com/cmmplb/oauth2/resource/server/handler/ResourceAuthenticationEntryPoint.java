package com.cmmplb.oauth2.resource.server.handler;

import com.alibaba.fastjson.JSON;
import com.cmmplb.oauth2.resource.server.result.HttpCodeEnum;
import com.cmmplb.oauth2.resource.server.result.Result;
import com.cmmplb.oauth2.resource.server.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author penglibo
 * @date 2024-07-26 15:01:34
 * @since jdk 1.8
 * 资源异常细节处理，也可以继承OAuth2AuthenticationEntryPoint 重写commence
 */

@Slf4j
public class ResourceAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setCharacterEncoding("utf8");
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpCodeEnum.UNAUTHORIZED.getCode());
        Result<String> result = ResultUtil.custom(HttpCodeEnum.UNAUTHORIZED);
        if (authException != null) {
            // 设置一个data显示无效的token
            result.setData(authException.getMessage());
        }
        // 针对令牌过期
        if (authException instanceof InsufficientAuthenticationException) {
            result.setMsg(HttpCodeEnum.UNAUTHORIZED.getMessage());
        }
        // 针对凭证错误过期
        if (authException instanceof BadCredentialsException) {
            result.setMsg(HttpCodeEnum.BAD_CREDENTIALS.getMessage());
        }
        PrintWriter printWriter = response.getWriter();
        printWriter.append(JSON.toJSONString(result));
        log.info("{},{}", request.getRequestURI(), result.getMsg());
    }
}
