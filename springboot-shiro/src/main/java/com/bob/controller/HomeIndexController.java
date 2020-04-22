package com.bob.controller;


import com.bob.common.R;
import com.bob.entity.User;
import com.bob.entity.vo.UserVO;
import com.bob.service.UserService;
import com.bob.util.MD5Utils;
import lombok.AllArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.ExecutionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class HomeIndexController {

    private final UserService userService;

    @GetMapping("/addCookie")
    public R testCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("bob", "liyong");
        // 这样配置只有localhost:8080/aa/bb 会携带Cookie,会忽略项目名
        // cookie.setPath("/aa/bb");
        // 指定Cookie绑定的路径，注意指定的路径必须带上项目名称
        cookie.setPath(request.getContextPath() + "/cookie/test"); // localhost:8080/springboot-shiro/aa/bb 会携带
        // 设置Cookie的有效期，单位秒
        // 值>0,表示会存放在客户端的硬盘上
        // 值<0,与不设置一样，会存放在客户端浏览器的缓存中
        // 值=0，表示Cookie一生成就马上生效
        cookie.setMaxAge(120);
        response.addCookie(cookie);
        return R.success("添加Cookie成功");
    }

    @GetMapping("/cookie/test")
    public R test(HttpServletRequest request, HttpServletResponse response) {
        // 解析Cookie
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            System.out.println("key:" + cookie.getName() + "====value:" + cookie.getValue());
        }
        return R.success("解析Cookie成功");
    }

    @GetMapping("/addSession")
    public R addSession(HttpServletRequest request, String key, String value) {
        HttpSession session = request.getSession();
        session.setAttribute(key, value);
        System.out.println("session=="+ session);
        return R.success("session添加成功!");
    }

    @GetMapping("/getSession")
    public R getSession(HttpServletRequest request, String key) {
        HttpSession session = request.getSession(false);
        String value = null;
        if (session != null) {
            value = (String) session.getAttribute(key);
        }
        System.out.println("session=="+ session);
        return R.success(key + "===" + value);
    }

    @GetMapping("/login")
    public ModelAndView defaultLogin() {
        ModelAndView view = new ModelAndView();
        view.setViewName("login");
        return view;
    }

    @GetMapping("/test")
    public ModelAndView test() {
        ModelAndView view = new ModelAndView();
        view.setViewName("test");
        return view;
    }

    @PostMapping("/login")
    public ModelAndView login(@RequestParam("username") String username, @RequestParam("password") String password, boolean rememberMe, HttpSession session) {
        ModelAndView view = new ModelAndView();
        // 从SecurityUtils里边创建一个 subject
        Subject subject = SecurityUtils.getSubject();
        // 在认证提交前准备 token（令牌）
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        // 实现Remember me功能，也可以在构造器中添加此参数
        token.setRememberMe(rememberMe);
        // 执行认证登陆
        try {
            subject.login(token);
            // 判断当前用户是否被认证，即是否已经登录
            if (subject.isAuthenticated()) {
                view.setViewName("test");
            } else {
                token.clear();
                view.addObject("msg", "登录失败");
            }
        } catch (AuthenticationException  ae) {
            view.addObject("msg", "用户名或密码不正确");
        } catch (Exception e) {
            view.addObject("msg", e.getMessage());
        }
        return view;
    }

    @PostMapping("/registered")
    public R registered(@Valid @RequestBody User user) {
        String pwd = MD5Utils.MD5Pwd(user.getUsername(), user.getPassword());
        user.setPassword(pwd);
        userService.save(user);
        return R.success("注册成功!");
    }

    @GetMapping("/unauthorized")
    public R unauthorized() {
        return R.fail("您没有该权限!");
    }
}
