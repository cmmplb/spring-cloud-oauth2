package com.cmmplb.oauth2.resource.server.feign;

import com.alibaba.fastjson.JSONObject;
import com.cmmplb.oauth2.resource.server.handler.exception.BusinessException;
import com.cmmplb.oauth2.resource.server.result.HttpCodeEnum;
import com.cmmplb.oauth2.resource.server.result.Result;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author penglibo
 * @date 2024-07-31 13:56:27
 * @since jdk 1.8
 * feign异常消息解码
 */

@Slf4j
public class FeignClientErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        log.error("methodKey:{},status:{}", methodKey, response.status());
        try {
            if (response.body() != null) {
                // 获取原始的返回内容
                String message = Util.toString(response.body().asReader(Util.UTF_8));
                // 将返回内容反序列化为Result，这里应根据自身项目作修改
                Result<?> result = JSONObject.parseObject(message, Result.class);
                throw new BusinessException(result.getCode(), result.getMsg());
            }
        } catch (Exception ignored) {
            // 部分异常响应
            if (response.status() == 401) {
                throw new BusinessException(HttpCodeEnum.UNAUTHORIZED);
            }
            if (response.status() == 403) {
                throw new BusinessException(HttpCodeEnum.FORBIDDEN);
            }
            if (response.status() == 404) {
                throw new BusinessException(HttpCodeEnum.NOT_FOUND);
            }
        }
        throw new BusinessException(response.status(), response.status(), methodKey);
    }
}