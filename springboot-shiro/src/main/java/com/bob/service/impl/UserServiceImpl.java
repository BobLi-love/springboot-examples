package com.bob.service.impl;

import com.bob.entity.User;
import com.bob.entity.vo.UserVO;
import com.bob.mapper.UserMapper;
import com.bob.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Bob
 * @since 2020-04-07
 */
@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;

    @Override
    public UserVO getUserByName(String username) {
        log.info("从数据库中查询权限...");
        return userMapper.getUserByName(username);
    }
}
