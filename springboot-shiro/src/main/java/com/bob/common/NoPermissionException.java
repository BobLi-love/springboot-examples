package com.bob.common;

import com.bob.common.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
@Slf4j
public class NoPermissionException {
    @ResponseBody
    @ExceptionHandler(UnauthorizedException.class)
    public ModelAndView handleShiroException(Exception ex) {
        log.error("您暂无此权限", ex);
        ModelAndView view = new ModelAndView();
        view.setViewName("403");
        return view;
    }
    @ResponseBody
    @ExceptionHandler(AuthorizationException.class)
    public ModelAndView AuthorizationException(Exception ex) {
        log.error("权限认证失败", ex);
        ModelAndView view = new ModelAndView();
        view.setViewName("403");
        return view;
    }
}
