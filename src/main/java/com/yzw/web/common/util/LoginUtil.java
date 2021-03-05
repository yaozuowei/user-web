package com.yzw.web.common.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author YaoZuoWei
 * @date 2021/1/19 11:33
 * @Description:
 */
public class LoginUtil {
    /**
     * 获取登录人
     *
     * @return
     */
    public static String getLoginName() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            return userDetails.getUsername();
        } else {
            return (String) principal;
        }
    }
}
