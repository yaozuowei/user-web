package com.yzw.web.core.service;

import com.yzw.web.common.BaseService;
import com.yzw.web.common.constants.UserConstants;
import com.yzw.web.common.util.BizIdUtil;
import com.yzw.web.common.util.SecurityEncodeUtil;
import com.yzw.web.common.util.StringUtil;
import com.yzw.web.core.entity.User;
import com.yzw.web.core.entity.UserExample;
import com.yzw.web.core.mapper.UserMapper;
import com.yzw.web.exception.PlatException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: YaoZuoWei
 * @Date: 2020/04/26/14:31
 * @Description:
 */
@Service("UserService")
public class UserService extends BaseService {

    @Resource
    UserMapper userMapper;

    /**
     * 根据userLabel查询一条记录
     *
     * @param userLabel
     * @return
     */
    public User getUserByuserLabel(String userLabel) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserNoEqualTo(userLabel);
        List<User> list = userMapper.selectByExample(userExample);
        if (list.size() == 1) {
            return list.get(0);
        } else {
            return null;
        }

    }

    /***
     * 是否存在用户
     * @param userLabel
     * @return
     */
    public boolean isExistsUser(String userLabel){
        return getUserByuserLabel(userLabel) == null;
    }

    /**
     *查询所有用户集合 分页
     * @param user
     * @return
     */
    public List<User> findListUser(User user){
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if (StringUtils.isNotEmpty(user.getUserNo())){
            criteria.andUserNoLike(user.getUserNo()+"%");
        }
        if (StringUtils.isNotEmpty(user.getDirectTel())){
            criteria.andDirectTelLike(user.getDirectTel()+"%");
        }
        if (StringUtils.isNotEmpty(user.getUserSystem())){
            criteria.andUserSystemEqualTo(user.getUserSystem());
        }
        if (StringUtils.isNotEmpty((String)user.getParams().get("beginTime"))){
            try {
                criteria.andCreateDateGreaterThanOrEqualTo(new SimpleDateFormat("yyyy-MM-dd").parse((String)user.getParams().get("beginTime")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.isNotEmpty((String)user.getParams().get("endTime"))){
            try {
                criteria.andCreateDateLessThanOrEqualTo(new SimpleDateFormat("yyyy-MM-dd").parse((String)user.getParams().get("endTime")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    return userMapper.selectByExample(userExample);

    }

    /**
     * 修改用户
     */
    public int updateUser(User user,String loginName) {
        User NewUser=getUserByuserLabel(user.getUserNo());
        user.setUserGuid(NewUser.getUserGuid());
        user.initUpdate(loginName);
        if (!StringUtils.isEmpty(user.getUserPassword())){
            user.setUserPassword(SecurityEncodeUtil.encode(user.getUserPassword()));
        }
        return userMapper.updateByPrimaryKeySelective(user);
    }


    /**
     * 新增用户
     */
    public User saveUser(User user,String loginName)throws PlatException {
        if (StringUtil.isEmpty(user.getUserNo())){
            throw new PlatException("登录账号不能为空!");
        }
        if (!isExistsUser(user.getUserNo())){
            throw new PlatException("账号已存在!");
        }

        initModel(loginName,user);
        if (StringUtil.isEmpty(user.getUserPassword())){
            user.setUserPassword("123456");
        }
        user.setUserPassword(SecurityEncodeUtil.encode(user.getUserPassword()));
        user.setUserGuid(BizIdUtil.INSTANCE.nextId());
        userMapper.insertSelective(user);
        return user;
    }

    /**
     * 校验手机号码
     * @param user
     * @return
     */
    public String checkPhoneUnique(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andDirectTelEqualTo(user.getDirectTel());
        List<User> list = userMapper.selectByExample(userExample);

        if (StringUtil.isEmpty(user.getUserGuid())){
            //用户注册
            if (list.isEmpty()){
                return UserConstants.USER_EMAIL_UNIQUE;
            }else {
                return UserConstants.USER_EMAIL_NOT_UNIQUE;
            }
            //登录后
        }else {
            if (!list.isEmpty()&&!user.getUserGuid().equals(list.get(0).getUserGuid())){
                return UserConstants.USER_EMAIL_NOT_UNIQUE;
            }else {
                return UserConstants.USER_EMAIL_UNIQUE;
            }
        }


    }


    /**
     * 校验email邮箱
     * @param user
     * @return
     */
    public String checkEmailUnique(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andEmailEqualTo(user.getEmail());
        List<User> list = userMapper.selectByExample(userExample);

        if (StringUtil.isEmpty(user.getUserGuid())){
            //用户注册
            if (list.isEmpty()){
                return UserConstants.USER_EMAIL_UNIQUE;
            }else {
                return UserConstants.USER_EMAIL_NOT_UNIQUE;
            }
            //登录后
        }else {
            if (!list.isEmpty()&&!user.getUserGuid().equals(list.get(0).getUserGuid())){
                return UserConstants.USER_EMAIL_NOT_UNIQUE;
            }else {
                return UserConstants.USER_EMAIL_UNIQUE;
            }
        }
    }

    /**
     * 停用或启用用户
     * @param loginName
     * @param user
     */
    public void changeStatus(String loginName, User user) throws PlatException{
        User userRX=getUserByuserLabel(user.getUserNo());
        if (userRX==null){
            throw new PlatException("用户不存在");
        }
        user.initUpdate(loginName);
        user.setUserGuid(userRX.getUserGuid());
        userMapper.updateByPrimaryKeySelective(user);

    }
}
