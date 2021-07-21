package com.yzw.web.core.controller;

import com.yzw.web.common.BaseController;
import com.yzw.web.common.constants.MyConstants;
import com.yzw.web.common.entity.ImageCode;
import com.yzw.web.common.util.ImageCodeUtil;
import com.yzw.web.core.entity.Menu;
import com.yzw.web.core.entity.User;
import com.yzw.web.core.service.RmService;
import com.yzw.web.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: YaoZuoWei
 * @Date: 2020/04/23/12:29
 * @Description:
 */
@RequestMapping("/toLogin")
@Controller
public class LoginController extends BaseController {

    @Resource(name = "UserService")
    private UserService userService;

    @Autowired
    private SessionStrategy sessionStrategy;

    @Resource(name = "RmService")
    private RmService rmService;

    /**
     * 登录页面
     *
     * @return
     */
    @RequestMapping(value = {""})
    public String index(Model model) {
        //是否登陆过
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            return "login";
        } else {
            User user = userService.getUserByuserLabel(getLoginName());
            List<Menu> list = rmService.findAllMenuByUserNo(getLoginName());
            model.addAttribute("user", user);
            model.addAttribute("menus", list);
            return "index";
        }
    }

    /**
     * 图片验证码
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/images/code")
    @ResponseBody
    public void getCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ImageCode imageCode = ImageCodeUtil.generate(new ServletWebRequest(request));
        //由于session集群管理不能传image
        ImageCode imageCodePl = new ImageCode(imageCode.getCode(), imageCode.getExpireTime());
        sessionStrategy.setAttribute(new ServletWebRequest(request), MyConstants.SESSION_KEY, imageCodePl);
        ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
    }

    /**
     * session过期
     */
    @GetMapping("/session/invalid")
    public String sessionInvalid() {
        return "login";
    }

    /**
     * 登录下线界面
     */
    @GetMapping("/session/expired")
    public String sessionExpired() {
        return "error/ainlogin";
    }

    /**
     * 权限不足页面
     */
    /**
     * 登录下线界面
     */
    @GetMapping("/session/noauth")
    public String sessionNoauth() {
        return "error/noauth";
    }


    /**
     * 打开注册用户页面
     * @return
     */
    @GetMapping("/register")
    public String registerUser() {
        return "user/person/register";
    }
}
