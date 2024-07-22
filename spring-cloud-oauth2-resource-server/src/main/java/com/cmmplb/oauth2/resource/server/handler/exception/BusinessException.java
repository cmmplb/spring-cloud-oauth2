package com.cmmplb.oauth2.resource.server.handler.exception;

import com.cmmplb.oauth2.resource.server.result.HttpCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author penglibo
 * @date 2024-07-13 12:13:33
 * @since jdk 1.8
 * 业务异常
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = -7787200346109889949L;

    private int code;

    private int statusCode = 200;

    private String message;

    public BusinessException() {
        this.code = HttpCodeEnum.INTERNAL_SERVER_ERROR.getCode();
        this.message = HttpCodeEnum.INTERNAL_SERVER_ERROR.getMessage();
    }

    public BusinessException(String message) {
        this.code = HttpCodeEnum.INTERNAL_SERVER_ERROR.getCode();
        this.message = message;
    }

    public BusinessException(HttpCodeEnum httpCodeEnum) {
        this.code = httpCodeEnum.getCode();
        this.message = httpCodeEnum.getMessage();
    }

    public BusinessException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BusinessException(int code, int statusCode, String message) {
        this.code = code;
        this.statusCode = statusCode;
        this.message = message;
    }

    public BusinessException(int code) {
        this.code = code;
        this.message = HttpCodeEnum.INTERNAL_SERVER_ERROR.getMessage();
    }
}