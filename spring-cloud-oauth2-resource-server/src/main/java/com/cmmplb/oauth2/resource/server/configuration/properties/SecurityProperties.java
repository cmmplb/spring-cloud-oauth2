package com.cmmplb.oauth2.resource.server.configuration.properties;

import com.cmmplb.oauth2.resource.server.annotation.WithoutLogin;
import io.netty.util.internal.StringUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;

/**
 * @author penglibo
 * @date 2024-07-30 15:36:52
 * @since jdk 1.8
 * <a href="https://blog.csdn.net/weixin_42214548/article/details/112936957">接口忽略认证授权</a>
 */

@Slf4j
@Setter
@Getter
@ConfigurationProperties(prefix = "security")
public class SecurityProperties implements InitializingBean, ApplicationContextAware {

    /**
     * 是否开启资源拦截, 默认开启
     */
    private Boolean enabled = true;

    /**
     * 白名单, 以map存储, key为资源分组名称, value为资源路径数组, 按','分割, 例:
     * white-list:
     * --system: /user/info/*,/user/info/mobile/*,/client/login
     * 前面的--是为了防止格式化代码, 往前缩进了，实际配置前面需要两个空格。0.0
     */
    private Map<String, String> whiteList;

    public static final String PREFIX = "security";

    public static final String ENABLED = "enabled";

    private ApplicationContext applicationContext;

    // 资源分组名称
    public static final String AUTH_IGNORE = "without_login";

    @Override
    public void afterPropertiesSet() {
        // 获取所有被注解的类或者方法
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        map.keySet().forEach(mappingInfo -> {
            HandlerMethod handlerMethod = map.get(mappingInfo);
            build(mappingInfo, AnnotationUtils.findAnnotation(handlerMethod.getMethod(), WithoutLogin.class));
            build(mappingInfo, AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), WithoutLogin.class));
        });
        log.info("资源白名单：{}", whiteList);
    }

    private void build(RequestMappingInfo mappingInfo, Object ignore) {
        if (null != ignore && null != mappingInfo.getPatternsCondition()) {
            mappingInfo.getPatternsCondition().getPatterns().forEach(url -> {
                String white = whiteList.get(AUTH_IGNORE);
                // 匹配PathVariable的路径正则, 例/user/info/{username}替换为/user/info/*
                String path = url.replaceAll("\\{(.*?)}", "*");
                if (StringUtil.isNullOrEmpty(white)) {
                    white = path;
                } else {
                    white = white + "," + path;
                }
                whiteList.put(AUTH_IGNORE, white);
            });
        }
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext context) {
        this.applicationContext = context;
    }
}