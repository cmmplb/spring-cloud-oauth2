package com.cmmplb.oauth2.auth.server.controller;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author penglibo
 * @date 2024-07-10 17:36:22
 * @since jdk 1.8
 */

@Controller
// 授权请求信息是存储在session
@SessionAttributes("authorizationRequest")
public class WhitelabelApprovalController {

    /**
     * 替换org.springframework.security.oauth2.provider.endpoint.WhitelabelApprovalEndpoint#getAccessConfirmation
     */
    @RequestMapping("/oauth/confirm/access")
    public ModelAndView getAccessConfirmation(Map<String, Object> model, HttpServletRequest request) throws Exception {
        AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("confirm-access");
        modelAndView.addObject("clientId", authorizationRequest.getClientId());
        modelAndView.addObject("scopes", getScope(authorizationRequest.getScope()));
        return modelAndView;
    }

    /**
     * 替换org.springframework.security.oauth2.provider.endpoint.WhitelabelErrorEndpoint
     */
    @RequestMapping("/oauth/error")
    public ModelAndView handleError(HttpServletRequest request) {
        Map<String, Object> model = new HashMap<String, Object>();
        Object error = request.getAttribute("error");
        String errorSummary;
        if (error instanceof OAuth2Exception) {
            OAuth2Exception oauthError = (OAuth2Exception) error;
            errorSummary = oauthError.getSummary();
        } else {
            errorSummary = "Unknown error";
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("oauth-error");
        modelAndView.addObject("errorSummary", errorSummary);
        return modelAndView;
    }

    // 对scope添加中文描述
    private static Map<String, Object> getScope(Set<String> scopes) {
        Map<String, Object> map = new HashMap<>();
        for (String scope : scopes) {
            // "username", "phone", "age"
            if ("username".equals(scope)) {
                map.put(scope, "用户名");
            }
            if ("phone".equals(scope)) {
                map.put(scope, "手机号");
            }
            if ("age".equals(scope)) {
                map.put(scope, "性别");
            }
        }
        return map;
    }
}
