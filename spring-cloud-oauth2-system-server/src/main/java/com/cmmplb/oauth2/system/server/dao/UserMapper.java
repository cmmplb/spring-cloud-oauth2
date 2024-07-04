package com.cmmplb.oauth2.system.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmmplb.oauth2.system.server.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author penglibo
 * @date 2024-07-02 17:40:52
 * @since jdk 1.8
 */

@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据id获取关联的角色编码
     * @param id 用户id
     * @return 角色编码集合
     */
    List<String> selectRoleCodesById(@Param("id") Long id);

    /**
     * 根据角色编码集合获取关联的权限编码集合
     * @param roleCodes 角色编码集合
     * @return 权限编码集合
     */
    List<String> selectPermissionCodesByRoleCodes(@Param("roleCodes") List<String> roleCodes);
}
