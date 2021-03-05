package com.yzw.web.security.handler;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: YaoZuoWei
 * @Date: 2020/04/27/13:45
 * @Description:登录成功处理类
 */
@Slf4j
@Component
public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info(authentication.getPrincipal() + "-----登录成功");
        JSONObject res = new JSONObject();
        res.put("success", true);
        res.put("status", 200);
        res.put("msg", "登录成功");

        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().append(res.toString());

    }
}
