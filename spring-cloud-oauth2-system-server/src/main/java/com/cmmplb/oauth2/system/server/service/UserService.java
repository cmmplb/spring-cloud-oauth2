package com.cmmplb.oauth2.system.server.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cmmplb.oauth2.resource.server.bean.UserInfoVO;
import com.cmmplb.oauth2.system.server.entity.User;

/**
 * @author penglibo
 * @date 2024-07-02 17:10:31
 * @since jdk 1.8
 */

public interface UserService extends IService<User> {

    /**
     * 根据用户名获取用户信息
     * @param username 用户名
     * @return 用户信息
     */
    UserInfoVO getByUsername(String username);

    /**
     * 根据手机号获取用户信息
     * @param mobile 手机号
     * @return 用户信息
     */
    UserInfoVO getByMobile(String mobile);
}

