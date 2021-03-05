package com.yzw.web.security;

import com.yzw.web.core.entity.Role;
import com.yzw.web.core.entity.User;
import com.yzw.web.core.service.RoleService;
import com.yzw.web.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: YaoZuoWei
 * @Date: 2020/04/24/14:06
 * @Description:
 */
@Component
public class MyUserDetailsService implements UserDetailsService {

    @Resource(name = "UserService")
    private UserService userService;

    @Resource(name = "RoleService")
    private RoleService roleService;


    /**
     * 表单登录
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByuserLabel(username);
        if (user == null) {
            return null;
        }

        MyUserDetail myUserDetail = new MyUserDetail();
        myUserDetail.setUsername(username);
        myUserDetail.setPassword(user.getUserPassword());

        List<Role> roleList = roleService.findRoleByUser(user.getUserGuid());

        // 角色集合
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : roleList) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleKey()));
        }
        myUserDetail.setAuthorities(authorities);
        return myUserDetail;
    }

}
