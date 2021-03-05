package com.yzw.web.core.controller;

import com.yzw.web.common.BaseController;
import com.yzw.web.common.constants.UserConstants;
import com.yzw.web.common.page.TableDataInfo;
import com.yzw.web.common.util.ResultUtil;
import com.yzw.web.common.util.SecurityEncodeUtil;
import com.yzw.web.core.entity.User;
import com.yzw.web.core.service.UserService;
import com.yzw.web.util.ftp.FtpProperties;
import com.yzw.web.util.ftp.FtpUtil;
import com.yzw.web.util.sms.SmsRedis;
import com.yzw.web.util.sms.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: YaoZuoWei
 * @Date: 2020/04/29/15:58
 * @Description:用户
 */

@RequestMapping("/user")
@Controller
public class UserController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Resource(name = "UserService")
    private UserService userService;

    @Autowired
    private FtpProperties ftpProperties;

    @Resource(name = "SmsService")
    private SmsService smsService;

    /**
     * 用户界面
     *
     * @return
     */
    @PreAuthorize("hasPermission('/findUser','system:user:view')")
    @RequestMapping(value = {"/findUser"})
    public String findUser() {
        return "user/user";
    }

    /**
     * 用户列表
     *
     * @return
     */
    @PreAuthorize("hasPermission('/findListUser','system:user:list')")
    @PostMapping(value = {"/findListUser"})
    @ResponseBody
    public TableDataInfo findListUser(User user) {
        startPage();
        List<User> list=userService.findListUser(user);
        return getDataTable(list);
    }


    /**
     * 个人信息
     */
    @RequestMapping(value = {"/profile"})
    public String profile(Model model) {
        User user = userService.getUserByuserLabel(getLoginName());
        model.addAttribute("user", user);
        return "user/person/profile";
    }

    /**
     * 修改头像界面
     */
    @RequestMapping(value = {"/profile/avatar"})
    public String profileAvatar(Model model) {
        User user = userService.getUserByuserLabel(getLoginName());
        model.addAttribute("user", user);
        return "user/person/avatar";
    }


    /**
     * 修改头像
     *
     * @param file
     * @return
     */
    @PostMapping("/profile/avatar/updateAvatar")
    @ResponseBody
    public ResultUtil updateAvatar(@RequestParam("avatarfile") MultipartFile file) {
        try {
            String avatar = FtpUtil.uploadFile(ftpProperties, file, getLoginName());
            User user = userService.getUserByuserLabel(getLoginName());
            user.setExtra1(avatar);
            userService.updateUser(user, getLoginName());
            return ResultUtil.success("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return ResultUtil.error(e.getMessage());
        }
    }

    /**
     * 校验手机号码
     */
    @PostMapping("/profile/checkPhoneUnique")
    @ResponseBody
    public String checkPhoneUnique(User user) {
        return userService.checkPhoneUnique(user);
    }

    /**
     * 校验email邮箱
     */
    @PostMapping("/profile/checkEmailUnique")
    @ResponseBody
    public String checkEmailUnique(User user) {
        return userService.checkEmailUnique(user);
    }

    /**
     * 验证密码是否正确
     *
     * @param oldPassword
     * @return
     */
    @GetMapping("/profile/checkPassword")
    @ResponseBody
    public boolean checkPassword(String oldPassword) {
        User user = userService.getUserByuserLabel(getLoginName());
        if (SecurityEncodeUtil.matches(oldPassword, user.getUserPassword())) {
            return true;
        }
        return false;
    }

    /**
     * 修改用户
     *
     * @param user
     * @return
     */
    @PostMapping("/profile/update")
    @ResponseBody
    public ResultUtil update(User user) {
        try {
            userService.updateUser(user, getLoginName());
            return ResultUtil.success("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(e.getMessage());
        }
    }

    /**
     * 注册用户
     *
     * @param user
     * @return
     */
    @PostMapping("/profile/save")
    @ResponseBody
    public ResultUtil save(User user) {
        try {
            userService.saveUser(user, getLoginName());
            return ResultUtil.success("注册成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(e.getMessage());
        }
    }

    /**
     * 短信发送
     *
     * @param phone
     * @return
     */
    @RequestMapping("/profile/sendSms")
    @ResponseBody
    public ResultUtil sendSms(@RequestParam("phone") String phone) {
        try {
            SmsRedis smsRedis = smsService.sendSmsCode(phone);
            LOG.info(smsRedis.toString());
            return ResultUtil.success("发送成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(e.getMessage());
        }
    }

    /**
     * 验证短信验证码
     *
     * @param directTel
     * @param verification
     * @return
     */
    @RequestMapping("/profile/verifyCode")
    @ResponseBody
    public String verifyCode(String directTel, String verification) {
        try {
            boolean flag = smsService.validSmsCode(directTel, verification);
            if (flag) {
                return UserConstants.USER_EMAIL_UNIQUE;
            } else {
                return UserConstants.USER_EMAIL_NOT_UNIQUE;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return UserConstants.USER_EMAIL_NOT_UNIQUE;
        }
    }

}
