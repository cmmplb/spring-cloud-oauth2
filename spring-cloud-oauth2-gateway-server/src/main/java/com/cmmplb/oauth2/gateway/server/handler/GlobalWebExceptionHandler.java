package com.cmmplb.oauth2.gateway.server.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


/**
 * @author penglibo
 * @date 2024-07-02 11:42:12
 * @since jdk 1.8
 * 网关全局异常拦截
 */

@Slf4j
@Order(-1)
@Component
public class GlobalWebExceptionHandler implements ErrorWebExceptionHandler {

    @NonNull
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, @NonNull Throwable ex) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        // 参考AbstractErrorWebExceptionHandler
        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        // 按照异常类型进行处理
        HttpStatus httpStatus;
        String message;
        if (ex instanceof NotFoundException) {
            httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
            message = "服务不可用。";
        } else if (ex instanceof ResponseStatusException) {
            ResponseStatusException responseStatusException = (ResponseStatusException) ex;
            httpStatus = responseStatusException.getStatus();
            message = responseStatusException.getMessage();
        } else {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            message = "网络繁忙，请稍后再试。";
        }
        // 错误记录
        log.error("url:{},msg:{}", request.getPath(), ex.getMessage());

        // HttpStatus.OK
        response.setStatusCode(httpStatus);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        if (ex instanceof ResponseStatusException) {
            response.setStatusCode(((ResponseStatusException) ex).getStatus());
        }

        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            JSONObject json = new JSONObject();
            json.put("code", httpStatus.value());
            json.put("msg", message);
            return bufferFactory.wrap(JSON.toJSONBytes(json));
        }));
    }
}
