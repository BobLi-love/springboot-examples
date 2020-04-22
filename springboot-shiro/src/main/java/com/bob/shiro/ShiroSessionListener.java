package com.bob.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * 配置session监听类
 */
public class ShiroSessionListener implements SessionListener {

    /**
     * 统计在线人数
     */
    private final AtomicInteger sessionCount = new AtomicInteger(0);

    /**
     * 会话创建时触发
     * @param session
     */
    @Override
    public void onStart(Session session) {
        sessionCount.incrementAndGet();
        System.out.println("登陆+1=="+ sessionCount.get());
    }

    /**
     * 退出会话时触发
     * @param session
     */
    @Override
    public void onStop(Session session) {
        sessionCount.decrementAndGet();
        System.out.println("登陆-1=="+ sessionCount.get());
    }

    @Override
    public void onExpiration(Session session) {
        sessionCount.decrementAndGet();
        System.out.println("登陆过期-1=="+sessionCount.get());
    }

    /**
     * 获取在线人数使用
     * @return
     */
    public AtomicInteger getSessionCount() {
        return sessionCount;
    }
}
