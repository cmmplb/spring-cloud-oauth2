package com.cmmplb.oauth2.gateway.server.filters;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author penglibo
 * @date 2024-07-03 16:11:13
 * @since jdk 1.8
 * 通过网关转发到认证服务器 oauth/authorize 时未登录重定向跳转问题。
 * <a href="https://ask.csdn.net/questions/1061712">...</a>
 */

@Slf4j
@Configuration
public class ResponseGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        if (path.contains("/login") || path.contains("/oauth/authorize")) {
            return chain.filter(exchange.mutate().response(new ServerHttpResponseDecorator(exchange.getResponse()) {
                @NonNull
                @Override
                public Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
                    HttpStatus status = this.getStatusCode();
                    if (null != status && status.equals(HttpStatus.FOUND)) {
                        ServerHttpRequest request = exchange.getRequest();
                        String replacement = request.getURI().getScheme() + "://" + request.getURI().getAuthority() + "/auth";
                        String location = getHeaders().getFirst("Location");
                        if (StringUtils.isNotBlank(location)) {
                            String replaceLocation = location.replaceAll("^((ht|f)tps?)://(\\d{1,3}.){3}\\d{1,3}(:\\d+)?", replacement);
                            log.info("原路径：{}，重写响应路径：{}", location, replaceLocation);
                            getHeaders().set("Location", replaceLocation);
                        }
                    }
                    return super.writeWith(body);
                }
            }).build());
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 1;
    }
}