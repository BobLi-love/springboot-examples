package com.bob.util;

import com.bob.shiro.CustomRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;

public class ClearCacheUtil {

    public static void clearAuthenAndAuthor() {
        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
        CustomRealm realm = (CustomRealm) securityManager.getRealms().iterator().next();
        realm.clearCache(SecurityUtils.getSubject().getPrincipals());
    }
}
