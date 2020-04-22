package com.bob.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bob.common.R;
import com.bob.entity.Permission;
import com.bob.entity.RolePermission;
import com.bob.entity.User;
import com.bob.entity.vo.RoleVO;
import com.bob.entity.vo.UserVO;
import com.bob.service.PermissionService;
import com.bob.service.RolePermissionService;
import com.bob.service.UserService;
import com.bob.shiro.CustomRealm;
import com.bob.util.ClearCacheUtil;
import lombok.AllArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final PermissionService permissionService;
    private final RolePermissionService rolePermissionService;

    /**
     * 无权限访问的情况话，
     * 两种解决方案返回无权限信息
     *   1.通用异常处理(推荐使用，不然得不断加配置)
     *   2.过滤器配置类加符合条件的Filter
     */
    @RequiresPermissions("user:list")
    @GetMapping("/list")
    public R showUser() {
        return R.success("用户信息 list");
    }

    @RequiresRoles("admin")
    @RequiresPermissions("user:list")
    @GetMapping("/getByName")
    public R getUserByName(String username) {
        return R.success(userService.getUserByName(username));
    }

    // 给当前用户添加角色的权限
    @GetMapping("/addPermission")
    public R addPermission(Integer roleId, String permissionName) {
        // 插数据
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        UserVO user = userService.getUserByName(username);
        boolean contains = user.getRoles().stream().map(RoleVO::getId).collect(Collectors.toList()).contains(roleId);
        if (contains) {
            Permission permission = new Permission();
            permission.setPermissionName(permissionName);
            permissionService.save(permission);
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permission.getId());
            rolePermissionService.save(rolePermission);
        } else {
            return R.fail("当前用户没有此角色");
        }
        // 清除当前用户缓存
        ClearCacheUtil.clearAuthenAndAuthor();

        return R.success("添加权限成功!");
    }

    // 给当前用户删除角色的权限  注意权限 user:delete 是包含了user:delete:permission的
    @RequiresPermissions("user:delete:permission")
    @GetMapping("/deletePermission")
    public R deletePermission(Integer roleId, Integer permissionId) {
        // 删数据
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        UserVO user = userService.getUserByName(username);
        boolean contains = user.getRoles().stream().map(RoleVO::getId).collect(Collectors.toList()).contains(roleId);
        if (contains) {
            // mybatis plus的remove都是物理删除
            permissionService.removeById(permissionId);
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            QueryWrapper<RolePermission> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(RolePermission::getRoleId, roleId).eq(RolePermission::getPermissionId, permissionId);
            rolePermissionService.remove(queryWrapper);
        }
        // 清除当前用户缓存
        ClearCacheUtil.clearAuthenAndAuthor();

        return R.success("删除权限成功!");
    }

    @GetMapping("/clearCache")
    public R clearCache() {
        ClearCacheUtil.clearAuthenAndAuthor();
        return R.success("清除认证和授权缓存成功!");
    }

}
