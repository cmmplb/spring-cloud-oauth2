package com.cmmplb.oauth2.system.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmmplb.oauth2.resource.server.bean.UserInfoVO;
import com.cmmplb.oauth2.system.server.dao.UserMapper;
import com.cmmplb.oauth2.system.server.entity.User;
import com.cmmplb.oauth2.system.server.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;

/**
 * @author penglibo
 * @date 2024-07-02 17:42:13
 * @since jdk 1.8
 */

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public UserInfoVO getByUsername(String username) {
        User user = baseMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (null == user) {
            throw new RuntimeException("用户信息不存在");
        }
        return getUserInfoVO(user);
    }

    @Override
    public UserInfoVO getByMobile(String mobile) {
        User user = baseMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, mobile));
        if (null == user) {
            throw new RuntimeException("用户信息不存在");
        }
        return getUserInfoVO(user);
    }

    private UserInfoVO getUserInfoVO(User user) {
        UserInfoVO userInfoVO = new UserInfoVO();
        UserInfoVO.UserVO userVO = new UserInfoVO.UserVO();
        BeanUtils.copyProperties(user, userVO);
        userInfoVO.setUser(userVO);
        List<String> roleCodes = baseMapper.selectRoleCodesById(user.getId());
        if (!CollectionUtils.isEmpty(roleCodes)) {
            List<String> permissionCodes = baseMapper.selectPermissionCodesByRoleCodes(roleCodes);
            // 设置角色编码
            userInfoVO.setRoles(new HashSet<>(roleCodes));
            // 设置菜单按钮权限编码
            userInfoVO.setPermissions(new HashSet<>(permissionCodes));
        }
        return userInfoVO;
    }
}

