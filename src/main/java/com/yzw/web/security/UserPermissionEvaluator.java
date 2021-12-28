package com.yzw.web.security;

import com.yzw.web.common.redis.CacheManager;
import com.yzw.web.common.util.StringUtil;
import com.yzw.web.core.entity.Menu;
import com.yzw.web.core.entity.User;
import com.yzw.web.core.service.RmService;
import com.yzw.web.core.service.UserService;
import com.yzw.web.util.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: YaoZuoWei
 * @Date: 2020/04/24/14:36
 * @Description: 自定义权限注解验证
 */
@Component
public class UserPermissionEvaluator implements PermissionEvaluator {
    @Autowired
    private UserService userService;

    @Autowired
    private RmService rmService;

    @Autowired
    private CacheManager cacheManager;

    /**
     * hasPermission鉴权方法
     * 这里仅仅判断PreAuthorize注解中的权限表达式
     * 实际中可以根据业务需求设计数据库通过targetUrl和permission做更复杂鉴权
     * 当然targetUrl不一定是URL可以是数据Id还可以是管理员标识等,这里根据需求自行设计
     *
     * @param authentication
     * @param targetUrl
     * @param permission
     * @return
     */
    @Override
    public boolean hasPermission(Authentication authentication, Object targetUrl, Object permission) {
        // 获取用户信息
        String userName;
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            userName = userDetails.getUsername();
        } else {
            userName = (String) authentication.getPrincipal();
        }
        User user = userService.getUserByuserLabel(userName);

        //管理员直接放行
        if (UserManager.isAdministrator(user.getUserNo())) {
            return true;
        }

        Set<String> permissions = new HashSet<>();

        // 查询用户权限(这里可以将权限放入缓存中提升效率)
        List<Menu> list = (List<Menu>) cacheManager.get(user.getUserNo() + "_permissions");
        if (list == null || list.size() == 0) {
            list = rmService.findMenuByUser(user.getUserNo());
            cacheManager.set(user.getUserNo() + "_permissions", list);
        }

        for (Menu menu : list) {
            if (!StringUtil.isEmpty(menu.getPerms())) {
                permissions.add(menu.getPerms());
            }
        }
        // 权限对比
        if (permissions.contains(permission.toString())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}