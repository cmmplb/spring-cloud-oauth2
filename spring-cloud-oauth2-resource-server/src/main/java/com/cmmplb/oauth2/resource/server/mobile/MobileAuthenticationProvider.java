package com.cmmplb.oauth2.resource.server.mobile;


import com.cmmplb.oauth2.resource.server.service.UserDetailsService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author penglibo
 * @date 2024-07-19 16:11:21
 * @since jdk 1.8
 * {@link org.springframework.security.authentication.dao.DaoAuthenticationProvider}
 */

@Slf4j
@Setter
public class MobileAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    /**
     * 认证逻辑
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String mobile = authentication.getPrincipal().toString();
        String code = authentication.getCredentials().toString();
        log.info("mobile:{},code:{}", mobile, code);
        UserDetails userDetails = userDetailsService.loadUserByMobile(mobile);
        // 这里是验证逻辑，先什么都不做，后面再完善
        // todo:...
        // 认证成功后返回认证对象
        MobileAuthenticationToken authenticationToken = new MobileAuthenticationToken(userDetails, userDetails.getAuthorities());
        authenticationToken.setDetails(authenticationToken.getDetails());
        return authenticationToken;
    }

    /**
     * 指定AuthenticationProvider的认证对象
     * 对应MobileTokenGranter里面的getOAuth2Authentication()：authenticationManager.authenticate(userAuth)
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(MobileAuthenticationToken.class);
    }

}