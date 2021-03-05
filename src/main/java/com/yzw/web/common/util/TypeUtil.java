package com.yzw.web.common.util;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: YaoZuoWei
 * @Date: 2020/05/06/09:18
 * @Description:
 */

@Component
@Getter
@ConfigurationProperties(prefix = "typeutil")
public class TypeUtil {


    /**
     * security不过滤
     */
    public static String antMatchers;


    /**
     * rememberMe记住我设置token时间
     */
    public static int tokenValiditySeconds;

    /**
     * 同一用户在系统中只能登录一次
     */
    public static int maximumSession;

    public void setMaximumSession(int maximumSession) {
        TypeUtil.maximumSession = maximumSession;
    }



    public void setAntMatchers(String antMatchers) {
        TypeUtil.antMatchers = antMatchers;
    }


    public void setTokenValiditySeconds(int tokenValiditySeconds) {
        TypeUtil.tokenValiditySeconds = tokenValiditySeconds;
    }

}
