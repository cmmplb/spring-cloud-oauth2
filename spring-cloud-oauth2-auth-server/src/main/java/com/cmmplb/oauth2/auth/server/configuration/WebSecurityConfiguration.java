package com.cmmplb.oauth2.auth.server.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author penglibo
 * @date 2024-07-04 10:05:16
 * @since jdk 1.8
 * Security安全配置
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
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
                // 关闭跨域保护
                .and().csrf().disable();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 基于thymeleaf映射登录页面
        registry.addViewController("/oauth/login").setViewName("login-page");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                // 基于内存中的身份验证
                .inMemoryAuthentication()
                .passwordEncoder(passwordEncoder())

                // 模拟管理员用户
                .withUser("admin")
                .password(passwordEncoder().encode("123456"))
                .roles("ADMIN")
                .and()

                // 模拟普通用户
                .withUser("user")
                .password(passwordEncoder().encode("123456"))
                .roles("USER");
    }

    @Bean
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}