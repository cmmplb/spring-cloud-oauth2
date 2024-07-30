package com.cmmplb.oauth2.sso.two.server.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author penglibo
 * @date 2024-07-29 09:54:02
 * @since jdk 1.8
 */

@Controller
public class IndexController {

    @Value("${server.one-port}")
    private Integer onePort;

    @RequestMapping("/")
    public ModelAndView index() {
        // sso-one路径
        String ssoOneUrl = "http://localhost:" + onePort;
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        modelAndView.addObject("ssoOneUrl", ssoOneUrl);
        modelAndView.addObject("username", SecurityContextHolder.getContext().getAuthentication());
        return modelAndView;
    }
}
