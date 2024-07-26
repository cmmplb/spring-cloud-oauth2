package com.cmmplb.oauth2.resource.server.handler;

import com.cmmplb.oauth2.resource.server.handler.exception.MobileNotFoundException;
import com.cmmplb.oauth2.resource.server.result.HttpCodeEnum;
import com.cmmplb.oauth2.resource.server.result.Result;
import com.cmmplb.oauth2.resource.server.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.DefaultThrowableAnalyzer;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.util.ThrowableAnalyzer;

/**
 * @author penglibo
 * @date 2024-07-10 16:32:11
 * @since jdk 1.8
 * 认证服务器异常
 */

@Slf4j
public class GlobalWebResponseExceptionTranslator implements WebResponseExceptionTranslator<OAuth2Exception> {

    private final ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();

    // 这里添加SuppressWarnings忽略泛型提示，如果添加泛型，下面的Result就限制不能返回了
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public ResponseEntity translate(Exception e) {
        Throwable[] causeChain = throwableAnalyzer.determineCauseChain(e);
        Exception ase;

        Result<String> result = null;
        // 账号密码认证失败=>账号或密码错误
        ase = (UsernameNotFoundException) throwableAnalyzer.getFirstThrowableOfType(UsernameNotFoundException.class,
                causeChain);
        if (null != ase) {
            result = ResultUtil.custom(HttpCodeEnum.BAD_CREDENTIALS);
        }

        // 手机号认证失败=>手机号或验证码错误
        ase = (MobileNotFoundException) throwableAnalyzer.getFirstThrowableOfType(MobileNotFoundException.class,
                causeChain);
        if (ase != null) {
            result = ResultUtil.custom(HttpCodeEnum.MOBILE_NOT_FOUND);
        }

        // 无效授权=>账号或密码错误
        ase = (InvalidGrantException) throwableAnalyzer.getFirstThrowableOfType(InvalidGrantException.class,
                causeChain);
        if (null != ase) {
            result = ResultUtil.custom(HttpCodeEnum.BAD_CREDENTIALS);
        }

        // 无效token
        ase = (InvalidTokenException) throwableAnalyzer.getFirstThrowableOfType(InvalidTokenException.class,
                causeChain);
        if (null != ase) {
            result = ResultUtil.custom(HttpCodeEnum.UNAUTHORIZED);
        }

        // todo:还有一些权限异常后续使用到再添加

        if (null == result) {
            result = ResultUtil.custom(e.getMessage());
            // 不包含上述异常则服务器内部错误
            log.error("认证服务器异常:{}", e.getMessage());
        } else {
            log.info("认证服务器异常:{}", e.getMessage());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}