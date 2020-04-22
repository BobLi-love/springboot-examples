package com.bob.service;

import com.bob.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bob.entity.vo.UserVO;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author Bob
 * @since 2020-04-07
 */
public interface UserService extends IService<User> {

    /**
     * 根据用户名获取用户及相关角色和权限
     * @param username
     * @return
     */
    UserVO getUserByName(String username);
}
