package com.yzw.web.security.handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description 暂无权限处理类
 * @author: YaoZuoWei
 * @CreateTime 2019/10/3 8:39
 */
@Component
public class UserAuthAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.sendRedirect("/toLogin/session/noauth");
    }
}