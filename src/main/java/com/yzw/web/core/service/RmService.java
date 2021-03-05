package com.yzw.web.core.service;

import com.yzw.web.common.BaseService;
import com.yzw.web.common.enums.EnumMenuType;
import com.yzw.web.core.entity.Menu;
import com.yzw.web.core.entity.MenuExample;
import com.yzw.web.core.mapper.MenuMapper;
import com.yzw.web.core.mapper.RmMapMapper;
import com.yzw.web.util.UserManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: YaoZuoWei
 * @Date: 2020/04/29/14:51
 * @Description:
 */
@Service("RmService")
public class RmService extends BaseService {
    @Resource
    private RmMapMapper rmMapMapper;

    @Resource
    private MenuMapper menuMapper;

    /**
     * 根据用户获取菜单(含有子菜单)
     *
     * @param userNo
     * @return
     */
    public List<Menu> findAllMenuByUserNo(String userNo) {
        //获取用户拥有的菜单(包括子菜单和父级菜单)
        List<Menu> menusAll;
        if (UserManager.isAdministrator(userNo)) {
            //管理员查看所有
            MenuExample menuExample=new MenuExample();
            menuExample.createCriteria().andMenuTypeNotEqualTo(EnumMenuType.MENU_BUTTON);
            menusAll = menuMapper.selectByExample(menuExample);
        } else {
            menusAll = findMenuByUserNotBt(userNo);
        }

        //创建一个集合用于保存所有的主菜单
        List<Menu> rootMeun = new ArrayList<>();

        //遍历所有菜单集合,如果是主菜单的话直接放入rootMeun集合
        for (Menu menu : menusAll) {
            if ("00000000".equals(menu.getParentId())) {
                rootMeun.add(menu);
            }
        }
        //这个是遍历所有主菜单,分别获取所有主菜单的所有子菜单
        for (Menu menu : rootMeun) {
            List<Menu> childrenList = getchildrenMeun(menu.getMenuGuid(), menusAll);
            menu.setChildren(childrenList);
        }

        return rootMeun;
    }

    private List<Menu> getchildrenMeun(String menuGuid, List<Menu> menusAll) {
        //用于保存子菜单
        List<Menu> childrenList = new ArrayList<>();

        //遍历所有菜单
        for (Menu menu : menusAll) {
            //判断当前的菜单父id是否匹配
            if (menu.getParentId().equals(menuGuid)) {
                childrenList.add(menu);
            }
        }

        //这里就是递归了，遍历所有的子菜单
        for (Menu menu : childrenList) {
            menu.setChildren(getchildrenMeun(menu.getMenuGuid(), menusAll));
        }

        return childrenList;
    }

    /**
     * 根据用户获取菜单(包括按钮)
     *
     * @param userNo
     * @return
     */
    public List<Menu> findMenuByUser(String userNo) {
        return rmMapMapper.findAllMenuByUserNo(userNo);
    }

    /**
     * 根据用户获取菜单(不包括按钮)
     *
     * @param userNo
     * @return
     */
    public List<Menu> findMenuByUserNotBt(String userNo) {
        return rmMapMapper.findMenuByUserNotBt(userNo);
    }
}
