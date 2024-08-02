package com.cmmplb.oauth2.auth.server.configuration;

import com.cmmplb.oauth2.resource.server.configuration.properties.Oauth2ConfigProperties;
import com.cmmplb.oauth2.resource.server.mobile.MobileAuthenticationProvider;
import com.cmmplb.oauth2.resource.server.service.UserDetailsService;
import com.cmmplb.oauth2.resource.server.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author penglibo
 * @date 2024-07-04 10:05:16
 * @since jdk 1.8
 * Security安全配置
 */

@Slf4j
@Order(1)
@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(Oauth2ConfigProperties.class)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    @Autowired
    private Oauth2ConfigProperties oauth2ConfigProperties;

    @Autowired(required = false)
    private UserDetailsService userDetailsService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                // 受保护的资源路径，其他路径则交给资源服务器处理
                .requestMatchers().antMatchers("/oauth/**", "/login/**", "/logout/**")
                .and()
                // 注册手机号验证码登录提供器
                .authenticationProvider(mobileAuthenticationProvider())
                // 表单登录
                .formLogin().permitAll()
                // 登录页面路径，默认/login，由于默认的登录页引用了bootstrapcdn，网络不通情况导致页面一直加载，直到bootstrap.min.css超时才响应，
                // 这里复制默认的页面，去掉bootstrap的引用，单独下载文件，实现登录功能
                .loginPage("/oauth/login")
                // 提交登录的接口路径，默认/login，如果通过网关的话，需要网关添加转发，也可以在路径前添加服务名/auth/login，不过后者单独请求认证服务的话就会404
                .loginProcessingUrl("/login")
                .and()
                .authorizeRequests()
                // 放行登录页面引用的css
                .antMatchers("/css/**").permitAll()
                .anyRequest().authenticated()
                .and()
                // 关闭跨域保护
                .csrf().disable();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 基于thymeleaf映射登录页面
        registry.addViewController("/oauth/login").setViewName("login-page");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(mobileAuthenticationProvider());
        if (oauth2ConfigProperties.getUserDetailsServiceType().equals(Oauth2ConfigProperties.UserDetailsServiceType.JDBC)) {
            auth.userDetailsService(userDetailsService);
        } else {
            List<Oauth2ConfigProperties.BaseUserDetails> users = oauth2ConfigProperties.getUsers();
            // 基于内存中的身份验证
            InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> configurer = auth.inMemoryAuthentication()
                    .passwordEncoder(passwordEncoder());
            for (Oauth2ConfigProperties.BaseUserDetails user : users) {
                UserDetailsManagerConfigurer<AuthenticationManagerBuilder, InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder>>.UserDetailsBuilder userDetailsBuilder = configurer
                        .withUser(user.getUsername());
                userDetailsBuilder.password(passwordEncoder().encode(user.getPassword()))
                        .accountExpired(user.isAccountNonExpired()).accountLocked(user.isAccountNonLock())
                        .credentialsExpired(user.isCredentialsNonExpired()).disabled(user.isDisable());
                if (!CollectionUtils.isEmpty(user.getRoles())) {
                    userDetailsBuilder.roles(user.getRoles().toArray(new String[0]));
                }
                if (!CollectionUtils.isEmpty(user.getAuthorities())) {
                    userDetailsBuilder.authorities(user.getAuthorities().toArray(new String[0]));
                }
            }
        }
    }

    /**
     * 使用数据库加载用户，注释掉userDetailsServiceBean，不然注入UserDetailsService会显示有多个实现的bean
     * 也可以不移除，上面注入的private UserDetailsService userDetailsService; 就需要改成具体实现类：private UserDetailsServiceImpl userDetailsServiceImpl
     * 或者自定义UserDetailsService
     */
    @Bean
    @Override
    public org.springframework.security.core.userdetails.UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }

    @Bean
    public MobileAuthenticationProvider mobileAuthenticationProvider() {
        MobileAuthenticationProvider mobileAuthenticationProvider = new MobileAuthenticationProvider();
        mobileAuthenticationProvider.setUserDetailsService(userDetailsService);
        return mobileAuthenticationProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 自定义MD5加密方式
        return new PasswordEncoder() {

            /**
             * MD5加密
             */
            @Override
            public String encode(CharSequence rawPassword) {
                log.info("加密：{}", rawPassword);
                return MD5Util.encode(String.valueOf(rawPassword));
            }

            /**
             * 匹配密码，rawPassword为输入的，encodedPassword数据库查出来的
             */
            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                log.info("解密：{},{}", rawPassword, encodedPassword);
                return encodedPassword.equals(MD5Util.encode(String.valueOf(rawPassword)));
            }
        };
        // return new BCryptPasswordEncoder(10);
    }
}