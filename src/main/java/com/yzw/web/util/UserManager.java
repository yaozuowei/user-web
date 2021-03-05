package com.yzw.web.util;

/**
 * @author YaoZuoWei
 * @date 2021/3/4 14:57
 * @Description:
 */
public class UserManager {
    /**
     * 用户是否是管理员
     * @param userLabel
     * @return
     */
    public static boolean isAdministrator(String userLabel) {
        return "admin".equals(userLabel);
    }
}
