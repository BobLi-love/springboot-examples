package com.bob.service.impl;

import com.bob.entity.RolePermission;
import com.bob.mapper.RolePermissionMapper;
import com.bob.service.RolePermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 角色权限表 服务实现类
 * </p>
 *
 * @author Bob
 * @since 2020-04-07
 */
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {

}
