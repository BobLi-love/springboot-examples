package com.bob.mapper;

import com.bob.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bob.entity.vo.UserVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author Bob
 * @since 2020-04-07
 */
public interface UserMapper extends BaseMapper<User> {

    UserVO getUserByName(@Param("username") String username);
}
