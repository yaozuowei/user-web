package com.yzw.web.common.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: YaoZuoWei
 * @Date: 2020/05/07/10:55
 * @Description: springSecurity加密
 */
public class SecurityEncodeUtil {

    /**
     * security加密
     *
     * @param password
     * @return
     */
    public static String encode(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    /**
     * 验证密码
     * @param password 原密码
     * @param newPassword 加密后密码
     * @return
     */
    public static boolean matches(String password, String newPassword){
        return new BCryptPasswordEncoder().matches(password,newPassword);
    }

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("123456"));
    }
}
