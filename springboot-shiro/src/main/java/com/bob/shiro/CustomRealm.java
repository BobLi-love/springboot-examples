package com.bob.shiro;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bob.entity.Permission;
import com.bob.entity.vo.RoleVO;
import com.bob.entity.vo.UserVO;
import com.bob.service.UserService;
import com.bob.util.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    /**
     * 权限认证
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("权限认证中...");
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        // 根据用户名去查用户的角色和权限
        UserVO user = userService.getUserByName(username);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if (ObjectUtil.isNotEmpty(user)) {
            for (RoleVO role : user.getRoles()) {
                info.addRole(role.getRoleName());
                for (Permission permission : role.getPermissions()) {
                    info.addStringPermission(permission.getPermissionName());
                }
            }
        }

        return info;
    }

    /**
     * 身份认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("身份认证中...");
        String userName = (String) authenticationToken.getPrincipal();
        String userPwd = new String((char[]) authenticationToken.getCredentials());
        //根据用户名从数据库获取密码
        UserVO user = userService.getUserByName(userName);
        if (ObjectUtil.isNotEmpty(user)) {
            String password = user.getPassword();
            if (StrUtil.isBlank(userName)) {
                throw new AccountException("用户名为空");
            } else if (!MD5Utils.MD5Pwd(userName, userPwd).equals(password)) {
                throw new AccountException("密码不正确");
            }
            return new SimpleAuthenticationInfo(userName, password, ByteSource.Util.bytes(userName + "salt"), getName());
        }
        return null;
    }

    /**
     * 重写方法,清除当前用户的的 授权缓存
     * @param principals
     */
    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    /**
     * 重写方法，清除当前用户的 认证缓存
     * @param principals
     */
    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    /**
     * 自定义方法：清除所有授权缓存
     */
    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    /**
     * 自定义方法：清除所有认证缓存
     */
    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    /**
     * 自定义方法：清除所有的认证缓存和授权缓存
     */
    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }
}
