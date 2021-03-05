package com.yzw.web.core.service;

import com.yzw.web.common.BaseService;
import com.yzw.web.core.entity.Role;
import com.yzw.web.core.mapper.RoleMapper;
import com.yzw.web.core.mapper.UrMapMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: YaoZuoWei
 * @Date: 2020/04/26/14:50
 * @Description:
 */
@Service("RoleService")
public class RoleService extends BaseService {

    @Resource
    UrMapMapper urMapMapper;

    @Resource
    RoleMapper roleMapper;

    /**
     * 根据用户查询角色
     *
     * @param userGuid
     * @return
     */
    public List<Role> findRoleByUser(String userGuid) {
        return urMapMapper.findRoleByUser(userGuid);
    }
}
